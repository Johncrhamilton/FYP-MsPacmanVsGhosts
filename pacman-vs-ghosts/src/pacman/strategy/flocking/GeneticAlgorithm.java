package pacman.strategy.flocking;

import java.util.ArrayList;
import java.util.Random;

import pacman.Executor;
import pacman.controllers.Controller;
import pacman.controllers.examples.NearestPillPacMan;
import pacman.controllers.examples.RandomNonRevPacMan;
import pacman.controllers.examples.RandomPacMan;
import pacman.controllers.examples.StarterPacMan;
import pacman.entries.ghosts.FlockingStrategyGhosts;
import pacman.entries.pacman.InfluenceMapPacMan;

import pacman.game.Constants.MOVE;
import pacman.game.Constants.GHOST;
import pacman.strategy.flocking.FSConstants.ACTOR;
import pacman.strategy.flocking.FSConstants.GHOST_STATE;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;

/**
 * @author John
 * A simple genetic algorithm with distributive initialisation (Using both a uniform real and normal distribution) for flocking strategies,
 * uniform crossover and tournament selection for parents.
 */
public class GeneticAlgorithm {

	//Pacman Controllers
	private ArrayList<Controller<MOVE>> pacmanControllers;

	//Number distributions
	private UniformRealDistribution uniformRealDistribution;
	private NormalDistribution gaussianDistribution;

	//Genetic Algorithm parameters
	private ArrayList<FlockingStrategy> fittestIndividual = null;
	private double fittestIndividualScore = Double.MAX_VALUE;

	//Number of games run when calculating flocking strategy scores = 5
	private int NUM_SCORE_EXPERIMENT_RUNS = 5;

	private int individualFlockingStrategySize;

	private Executor exec;
	private Random random;

	public GeneticAlgorithm(Executor exec)
	{
		this.exec = exec;

		uniformRealDistribution = new UniformRealDistribution(0.0, FSConstants.LARGEST_NEIGHBOURHOOD_RADIUS);
		gaussianDistribution = new NormalDistribution(0.0, ((double) 1/3));

		if(FSConstants.HOMOGENEOUS_GHOSTS)
		{
			individualFlockingStrategySize = 1;
		}
		else
		{
			//Number of different ghosts (Blinky, Pinky, Inky and Sue)
			individualFlockingStrategySize = GHOST.values().length;
		}
	}

	public String bestFlockingStrategy()
	{
		random = new Random();

		//Initialisation of population
		ArrayList<ArrayList<FlockingStrategy>> population = initialisation();

		//Evaluate new Individuals and save their scores
		ArrayList<Double> populationScores = evaluatePopulation(population);

		//Starting fittest individual
		String result = "STARTING FITTEST INDIVIDUAL: ";

		//for(FlockingStrategy flockingStrategy : fittestIndividual)
		//{
		//	result += flockingStrategy.toString();
		//}

		result += "\nSCORE: " + fittestIndividualScore + "\n";

		int generationCount = 0;
		int fittestIndividualStreak = 0;
		ArrayList<FlockingStrategy> previousFittestIndividual = fittestIndividual;

		while(generationCount < FSConstants.NUMBER_OF_GENERATIONS && fittestIndividualStreak < FSConstants.MAXIMUM_FITTEST_INDIVIDUAL_STREAK)
		{
			//Produce offspring through recombination of parents and mutation
			ArrayList<ArrayList<FlockingStrategy>> offspringPopulation = produceOffspring(population, populationScores);

			//Evaluate new Individuals and update the saved scores
			populationScores = evaluatePopulation(offspringPopulation);

			//Survivor Selection Generational
			population = offspringPopulation;

			if(FSConstants.ELITISM)
			{
				int eliteIndex = random.nextInt(population.size());
				population.set(eliteIndex, fittestIndividual);
				populationScores.set(eliteIndex, fittestIndividualScore);
			}

			//If the fittest individual hasn't changed this generation
			if(fittestIndividual.equals(previousFittestIndividual))
			{
				fittestIndividualStreak++;
			}
			else
			{
				fittestIndividualStreak = 0;
				previousFittestIndividual = fittestIndividual;
			}

			generationCount++;
		}

		//The fittest individual
		result += "\nFITTEST INDIVIDUAL: ";

		for(FlockingStrategy flockingStrategy : fittestIndividual)
		{
			result += flockingStrategy.toString();
		}

		result += "\nSCORE: " + fittestIndividualScore;

		result += "\nTotal number of Generations: " + generationCount;

		return result;
		//return fittestIndividual;
	}

	/**
	 * Initialise the population
	 * @return population
	 */
	private ArrayList<ArrayList<FlockingStrategy>> initialisation()
	{
		ArrayList<ArrayList<FlockingStrategy>> initialPopulation = new ArrayList<ArrayList<FlockingStrategy>>(FSConstants.POPULATION_SIZE);

		while(initialPopulation.size() != FSConstants.POPULATION_SIZE)
		{
			ArrayList<FlockingStrategy> individual = new ArrayList<FlockingStrategy>(individualFlockingStrategySize);

			//An individual can have 1 or 4 flocking strategies
			for(int i = 0; i < individualFlockingStrategySize; i++)
			{
				//Determine Neighbourhoods using a Uniform Real Distribution for the number generation
				ArrayList<Double> neighbourhoods = new ArrayList<Double>();

				for(int n = 0; n < (FSConstants.NUMBER_OF_NEIGHBOURHOODS - 1); n++)
				{
					neighbourhoods.add(uniformRealDistribution.sample());
				}
				//The fourth neighbourhood is always between the (NUMBER_OF_NEIGHBOURHOODS - 1)th neighbourhood and the largest neighbourhood
				neighbourhoods.add(FSConstants.LARGEST_NEIGHBOURHOOD_RADIUS);

				//Determine the Actor Context Matrix Magnitudes using a truncated [-1, 1] Normal Distribution with 0 mean and 1/3 standard deviation
				double[][][] actorContextMatrixMagnitudes = new double[GHOST_STATE.values().length][ACTOR.values().length][FSConstants.NUMBER_OF_NEIGHBOURHOODS];

				for(int x = 0; x < GHOST_STATE.values().length; x++)
				{
					for(int y = 0; y < ACTOR.values().length; y++)
					{
						for(int z = 0; z < FSConstants.NUMBER_OF_NEIGHBOURHOODS; z++)
						{
							double magnitude = gaussianDistribution.sample();

							//Truncate magnitude between 1 and -1
							if(magnitude > 1)
							{
								magnitude = 1;
							}
							else if(magnitude < -1)
							{
								magnitude = -1;
							}

							actorContextMatrixMagnitudes[x][y][z] = magnitude;
						}
					}
				}
				individual.add(new FlockingStrategy(neighbourhoods, actorContextMatrixMagnitudes));
			}
			initialPopulation.add(individual);
		}
		return initialPopulation;
	}

	/**
	 * Create a new population of children from the parents
	 * @param population
	 * @return offspringPopulation
	 */
	private ArrayList<ArrayList<FlockingStrategy>> produceOffspring(ArrayList<ArrayList<FlockingStrategy>> population, ArrayList<Double> populationScores)
	{
		ArrayList<ArrayList<FlockingStrategy>> offspringPopulation = new ArrayList<ArrayList<FlockingStrategy>>(population.size());

		while(offspringPopulation.size() != population.size())
		{
			//Select two parents
			ArrayList<FlockingStrategy> parentOne = tournamentParentSelection(population, populationScores);
			ArrayList<FlockingStrategy> parentTwo = tournamentParentSelection(population, populationScores);

			ArrayList<FlockingStrategy> childOne = new ArrayList<FlockingStrategy>();
			ArrayList<FlockingStrategy> childTwo = new ArrayList<FlockingStrategy>();

			//An individual can have 1 or 4 flocking strategies
			for(int i = 0; i < individualFlockingStrategySize; i++)
			{
				//Homogeneous ghosts will all crossover within the same strategy
				//Heterogeneous ghosts will only crossover with their respective flocking strategy

				//Recombination (Uniform Crossover)
				if(random.nextDouble() < FSConstants.RECOMBINATION_PROBABILITY)
				{
					//Neighbourhoods
					ArrayList<Double> childOneNeighbourhoods = new ArrayList<Double>();
					ArrayList<Double> childTwoNeighbourhoods = new ArrayList<Double>();

					for(int n = 0; n < FSConstants.NUMBER_OF_NEIGHBOURHOODS; n++)
					{
						//Add neighbourhood from the appropriate parent's flocking strategy to the appropriate child's strategy
						if(random.nextDouble() < FSConstants.RECOMBINATION_MIXING_PROBABILITY)
						{
							childOneNeighbourhoods.add(parentOne.get(i).getNeighbourhoods().get(n));
							childTwoNeighbourhoods.add(parentTwo.get(i).getNeighbourhoods().get(n));
						}
						else
						{
							childOneNeighbourhoods.add(parentTwo.get(i).getNeighbourhoods().get(n));
							childTwoNeighbourhoods.add(parentOne.get(i).getNeighbourhoods().get(n));
						}
					}

					//Actor Context Matrix Magnitudes
					double[][][] childOneACMM = new double[GHOST_STATE.values().length][ACTOR.values().length][FSConstants.NUMBER_OF_NEIGHBOURHOODS];
					double[][][] childTwoACMM = new double[GHOST_STATE.values().length][ACTOR.values().length][FSConstants.NUMBER_OF_NEIGHBOURHOODS];

					for(int x = 0; x < GHOST_STATE.values().length; x++)
					{
						for(int y = 0; y < ACTOR.values().length; y++)
						{
							for(int z = 0; z < FSConstants.NUMBER_OF_NEIGHBOURHOODS; z++)
							{
								if(random.nextDouble() < FSConstants.RECOMBINATION_MIXING_PROBABILITY)
								{
									childOneACMM[x][y][z] = parentOne.get(i).getActorContextMatrixMagnitudes()[x][y][z];
									childTwoACMM[x][y][z] = parentTwo.get(i).getActorContextMatrixMagnitudes()[x][y][z];
								}
								else
								{
									childOneACMM[x][y][z] = parentTwo.get(i).getActorContextMatrixMagnitudes()[x][y][z];
									childTwoACMM[x][y][z] = parentOne.get(i).getActorContextMatrixMagnitudes()[x][y][z];
								}
							}
						}
					}

					//Create the new children
					childOne.add(new FlockingStrategy(childOneNeighbourhoods, childOneACMM));
					childTwo.add(new FlockingStrategy(childTwoNeighbourhoods, childTwoACMM));
				}
				else
				{
					//Set children as clones of their parents
					//Have to be clones since the same parents can be used again
					childOne.add(parentOne.get(i).clone());
					childTwo.add(parentTwo.get(i).clone());
				}

				//Mutation
				mutation(childOne.get(i));
				mutation(childTwo.get(i));
			}

			//Add children
			offspringPopulation.add(childOne);

			//In the event that the population size is odd, don't add the second child at the end
			if(offspringPopulation.size() < population.size())
			{
				offspringPopulation.add(childTwo);
			}
		}

		return offspringPopulation;
	}

	/**
	 * Tournament Parent Selection
	 * @param population
	 * @param populationScores
	 * @return selectedParent
	 */
	private ArrayList<FlockingStrategy> tournamentParentSelection(ArrayList<ArrayList<FlockingStrategy>> population, ArrayList<Double> populationScores)
	{
		ArrayList<FlockingStrategy> selectedParent = null;
		double selectedParentScore = Double.MAX_VALUE;

		if(population.size() == populationScores.size())
		{
			for(int i = 0; i < FSConstants.TOURNAMENT_SELECTION_SIZE; i++)
			{
				int randomIndex = random.nextInt(population.size());
				if(populationScores.get(randomIndex) < selectedParentScore)
				{
					selectedParent = population.get(randomIndex);
					break;
				}
			}
		}
		else
		{
			throw new IllegalStateException("Population passed and population scores passed have different sizes: " + population.size() + " " + populationScores.size());
		}

		return selectedParent;
	}

	/**
	 * Perform mutation on the flockingStrategy
	 * @param flockingStrategy
	 */
	private void mutation(FlockingStrategy flockingStrategy)
	{
		if(random.nextDouble() < FSConstants.MUTATION_PROBABILITY)
		{
			//Reinitialise a random Neighbourhood using a Uniform Real Distribution for the number generation
			if(random.nextDouble() < FSConstants.MUTATION_SWITCH_PROBABILITY)
			{
				//The fourth neighbourhood is always between the (NUMBER_OF_NEIGHBOURHOODS - 1)th neighbourhood and the largest neighbourhood
				int randomIndex = random.nextInt((FSConstants.NUMBER_OF_NEIGHBOURHOODS - 1));
				flockingStrategy.setNeighbourhood(randomIndex, uniformRealDistribution.sample());
			}
			//Reinitialise a random Actor Context Matrix Magnitude using a truncated [-1, 1] Normal Distribution with 0 mean and 1/3 standard deviation
			else
			{
				int indexGhost = random.nextInt(GHOST_STATE.values().length);
				int indexActor = random.nextInt(ACTOR.values().length);
				int indexNeighbourhood = random.nextInt(FSConstants.NUMBER_OF_NEIGHBOURHOODS);

				double magnitude = gaussianDistribution.sample();

				//Truncate magnitude between 1 and -1
				if(magnitude > 1)
				{
					magnitude = 1;
				}
				else if(magnitude < -1)
				{
					magnitude = -1;
				}

				flockingStrategy.getActorContextMatrixMagnitudes()[indexGhost][indexActor][indexNeighbourhood] = magnitude;
			}
		}
	}

	/**
	 * Evaluate the fitness of each member in the population
	 * @param population
	 */
	private ArrayList<Double> evaluatePopulation(ArrayList<ArrayList<FlockingStrategy>> population)
	{
		ArrayList<Double> populationScores = new ArrayList<Double>(population.size());

		for(int i = 0; i < population.size(); i++)
		{
			double currentIndividualScore = calculateScore(population.get(i));
			populationScores.add(currentIndividualScore);

			//The fitter Individual has the lower score
			if(currentIndividualScore < fittestIndividualScore)
			{
				fittestIndividual = population.get(i);
				fittestIndividualScore = currentIndividualScore;
			}
		}

		return populationScores;
	}
	
	/**
	 * An individual's objective score is the sum of squared average performances against the set of pacman controllers
	 * @param Individual
	 * @return score
	 */
	@SuppressWarnings("serial")
	private double calculateScore(ArrayList<FlockingStrategy> Individual)
	{
		double score = 0;
		
		//Setup new Ms. Pacman controllers to calculate the individual's score
		pacmanControllers = new ArrayList<Controller<MOVE>>()
		{{
			add(new NearestPillPacMan());
			add(new RandomNonRevPacMan());
			add(new RandomPacMan());
			add(new StarterPacMan());
			//add(new InfluenceMapPacMan());
		}};
		
		FlockingStrategyGhosts flockingStrategyGhosts = new FlockingStrategyGhosts(Individual);

		for(Controller<MOVE> pacManController : pacmanControllers)
		{
			double pacManScore = Math.pow(exec.runExperiment(pacManController, flockingStrategyGhosts, NUM_SCORE_EXPERIMENT_RUNS).getAverageScore(), 2);
			score += pacManScore;
		}

		return Math.sqrt(score);
	}
}

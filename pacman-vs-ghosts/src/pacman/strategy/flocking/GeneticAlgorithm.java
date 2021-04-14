package pacman.strategy.flocking;

import java.util.ArrayList;
import java.util.Collections;
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
import pacman.strategy.flocking.FSConstants.ACTOR;
import pacman.strategy.flocking.FSConstants.GHOST_STATE;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;

public class GeneticAlgorithm {

	//Number of games run when calculating flocking strategy scores
	private static final int NUM_EXPERIMENT_RUNS = 1;

	//Pacman Controllers
	private ArrayList<Controller<MOVE>> pacmanControllers;

	//Number distributions
	private UniformRealDistribution uniformRealDistribution;
	private NormalDistribution gaussianDistribution;

	//Genetic Algorithm parameters
	private FlockingStrategy fittestIndividual = null;
	private double fittestIndividualScore = Double.MAX_VALUE;

	private Executor exec;
	private Random random;

	@SuppressWarnings("serial")
	public GeneticAlgorithm(Executor exec) 
	{
		this.exec = exec;

		uniformRealDistribution = new UniformRealDistribution(0.0, FSConstants.LARGEST_NEIGHBOURHOOD_RADIUS);
		gaussianDistribution = new NormalDistribution(0.0, ((double) 1/3));

		pacmanControllers = new ArrayList<Controller<MOVE>>()
		{{
			add(new NearestPillPacMan());
			add(new RandomNonRevPacMan());
			add(new RandomPacMan());
			add(new StarterPacMan());
			//add(new InfluenceMapPacMan());
		}};
	}

	public FlockingStrategy bestFlockingStrategy() 
	{
		random = new Random();

		//Initialisation of population
		ArrayList<FlockingStrategy> population = initialisation();

		//Evaluate new Individuals and save their scores
		ArrayList<Double> populationScores = evaluatePopulation(population);		

		//Print the Starting fittest individual
		System.out.print("Starting fittest individual: ");
		System.out.println(fittestIndividual.toString());		
		System.out.println("Score: " + fittestIndividualScore + "\n");

		int generationCount = 0;
		int fittestIndividualStreak = 0;
		FlockingStrategy previousFittestIndividual = fittestIndividual;

		while(generationCount < FSConstants.NUMBER_OF_GENERATIONS && fittestIndividualStreak < 10) 
		{
			//Produce offspring through recombination of parents and mutation
			ArrayList<FlockingStrategy> offspringPopulation = produceOffspring(population, populationScores);

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
		
		System.out.println("Total number of Generations: " + generationCount);

		//Print the fittest individual
		System.out.println("Fittest individual: ");
		System.out.println(fittestIndividual.toString());		
		System.out.println("Score: " + fittestIndividualScore);

		return fittestIndividual;
	}

	/**
	 * Initialise the population
	 * @return population
	 */
	private ArrayList<FlockingStrategy> initialisation()
	{
		ArrayList<FlockingStrategy> initialPopulation = new ArrayList<FlockingStrategy>(FSConstants.POPULATION_SIZE);

		while(initialPopulation.size() != FSConstants.POPULATION_SIZE) 
		{
			//Determine Neighbourhoods using a Uniform Real Distribution for the number generation
			ArrayList<Double> neighbourhoods = new ArrayList<Double>();

			for(int n = 0; n < (FSConstants.NUMBER_OF_NEIGHBOURHOODS - 1); n++)
			{
				neighbourhoods.add(uniformRealDistribution.sample());				
			}
			//The fourth neighbourhood is always between the (NUMBER_OF_NEIGHBOURHOODS - 1)th neighbourhood and the largest neighbourhood
			neighbourhoods.add(FSConstants.LARGEST_NEIGHBOURHOOD_RADIUS);

			//Sort in ascending order
			Collections.sort(neighbourhoods);

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

			initialPopulation.add(new FlockingStrategy(neighbourhoods, actorContextMatrixMagnitudes));
		}

		return initialPopulation;
	}

	/**
	 * Create a new population of children from the parents
	 * @param population 
	 * @return offspringPopulation
	 */
	private ArrayList<FlockingStrategy> produceOffspring(ArrayList<FlockingStrategy> population, ArrayList<Double> populationScores)
	{
		ArrayList<FlockingStrategy> offspringPopulation = new ArrayList<FlockingStrategy>(population.size());

		while(offspringPopulation.size() != population.size()) 
		{
			//Select two parents
			FlockingStrategy parentOne = tournamentParentSelection(population, populationScores);
			FlockingStrategy parentTwo = tournamentParentSelection(population, populationScores);

			FlockingStrategy childOne = null;
			FlockingStrategy childTwo = null;

			//Recombination (Uniform Crossover)
			if(random.nextDouble() < FSConstants.RECOMBINATION_PROBABILITY) 
			{
				//Neighbourhoods
				ArrayList<Double> childOneNeighbourhoods = new ArrayList<Double>();
				ArrayList<Double> childTwoNeighbourhoods = new ArrayList<Double>();

				for(int n = 0; n < FSConstants.NUMBER_OF_NEIGHBOURHOODS; n++)
				{
					if(random.nextDouble() < FSConstants.RECOMBINATION_MIXING_PROBABILITY) 
					{
						childOneNeighbourhoods.add(parentOne.getNeighbourhoods().get(n));	
						childTwoNeighbourhoods.add(parentTwo.getNeighbourhoods().get(n));															
					} 
					else
					{
						childOneNeighbourhoods.add(parentTwo.getNeighbourhoods().get(n));	
						childTwoNeighbourhoods.add(parentOne.getNeighbourhoods().get(n));							
					}
				}

				//Sort in ascending order
				Collections.sort(childOneNeighbourhoods);
				Collections.sort(childTwoNeighbourhoods);

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
								childOneACMM[x][y][z] = parentOne.getActorContextMatrixMagnitudes()[x][y][z];
								childTwoACMM[x][y][z] = parentTwo.getActorContextMatrixMagnitudes()[x][y][z];
							}
							else
							{
								childOneACMM[x][y][z] = parentTwo.getActorContextMatrixMagnitudes()[x][y][z];
								childTwoACMM[x][y][z] = parentOne.getActorContextMatrixMagnitudes()[x][y][z];
							}
						}
					}
				}

				//Create the new children
				childOne = new FlockingStrategy(childOneNeighbourhoods, childOneACMM);
				childTwo = new FlockingStrategy(childTwoNeighbourhoods, childTwoACMM);
			}
			else
			{
				//Set children as clones of their parents
				//Have to be clones since the same parents can be used again
				childOne = parentOne.clone();
				childTwo = parentTwo.clone();
			}

			//Mutation
			mutation(childOne);
			mutation(childTwo);

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
	private FlockingStrategy tournamentParentSelection(ArrayList<FlockingStrategy> population, ArrayList<Double> populationScores) 
	{
		FlockingStrategy selectedParent = null;
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
				flockingStrategy.getNeighbourhoods().set(randomIndex, uniformRealDistribution.sample());

				//Sort in ascending order
				Collections.sort(flockingStrategy.getNeighbourhoods());
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
	private ArrayList<Double> evaluatePopulation(ArrayList<FlockingStrategy> population) 
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
	private double calculateScore(FlockingStrategy Individual) 
	{
		double score = 0;
		FlockingStrategyGhosts flockingStrategyGhosts = new FlockingStrategyGhosts(Individual);

		for(Controller<MOVE> pacManController : pacmanControllers) 
		{
			double pacManScore = exec.runExperiment(pacManController, flockingStrategyGhosts, NUM_EXPERIMENT_RUNS).getAverageScore();
			score += pacManScore;
		}

		return score;
	}
}
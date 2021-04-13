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
		//Initialisation of population
		ArrayList<FlockingStrategy> population = initialisation();

		//Evaluate new Individuals
		evaluatePopulation(population);

		System.out.print("Starting fittest individual: ");
		System.out.println(fittestIndividual.toString() + "\nScore: " + fittestIndividualScore);

		random = new Random();

		int generationCount = 0;		

		while(generationCount < FSConstants.NUMBER_OF_GENERATIONS) 
		{
			//Perform parent selection using tournament method to get a population of parents.
			//ArrayList<FlockingStrategy> parentPopulation = parentSelection(population);

			//Produce offspring through recombination of parents and mutation
			ArrayList<FlockingStrategy> offspringPopulation = produceOffspring(population);

			//Evaluate new Individuals
			evaluatePopulation(offspringPopulation);

			//Survivor Selection Generational
			population = offspringPopulation;
			
			if(FSConstants.ELITISM) 
			{
				population.remove(random.nextInt(FSConstants.POPULATION_SIZE));
				population.add(fittestIndividual);
			}

			generationCount++;
		}

		System.out.print("Fittest individual: " + fittestIndividual.toString());
		System.out.println(fittestIndividualScore);
		return fittestIndividual;
	}



	/**
	 * Initialise the population
	 * @return ArrayList<FlockingStrategy> population
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


	private ArrayList<FlockingStrategy> produceOffspring(ArrayList<FlockingStrategy> population)
	{
		ArrayList<FlockingStrategy> offspringPopulation = new ArrayList<FlockingStrategy>(FSConstants.POPULATION_SIZE);

		while(offspringPopulation.size() != FSConstants.POPULATION_SIZE) 
		{
			//Select two parents
			FlockingStrategy parentOne = population.get(random.nextInt(FSConstants.POPULATION_SIZE));
			FlockingStrategy parentTwo = population.get(random.nextInt(FSConstants.POPULATION_SIZE));

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
								childOneACMM[x][y][z] = parentOne.getActorContextMatrixMagnitudes()[x][y][z];
								childTwoACMM[x][y][z] = parentTwo.getActorContextMatrixMagnitudes()[x][y][z];
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
				childOne = parentOne;
				childTwo = parentTwo;
			}

			//Mutation
			mutation(childOne);
			mutation(childTwo);

			//Add children
			offspringPopulation.add(childOne);
			offspringPopulation.add(childTwo);
		}

		return offspringPopulation;
	}

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

				flockingStrategy.getActorContextMatrixMagnitudes()[indexGhost][indexActor][indexNeighbourhood] = gaussianDistribution.sample();
			}
		}
	}

	/**
	 * Evaluate the fitness of each member in the population
	 * @param population
	 */
	private void evaluatePopulation(ArrayList<FlockingStrategy> population) 
	{		
		for(int i = 0; i < population.size(); i++) 
		{
			double currentIndividualScore = calculateScore(population.get(i));

			//The fitter Individual has the lower score
			if(currentIndividualScore < fittestIndividualScore) 
			{
				fittestIndividual = population.get(i);
				fittestIndividualScore = currentIndividualScore;
			}
		}
	}

	/**
	 * An individual's objective score is the sum of squared average performances against the set of pacman controllers
	 * @param Individual
	 * @return double score
	 */
	private double calculateScore(FlockingStrategy Individual) 
	{
		double score = 0;
		FlockingStrategyGhosts flockingStrategyGhosts = new FlockingStrategyGhosts(Individual);

		for(Controller<MOVE> pacManController : pacmanControllers) 
		{
			//System.out.println(pacManController.toString());
			double pacManScore = exec.runExperiment(pacManController, flockingStrategyGhosts, NUM_EXPERIMENT_RUNS).getAverageScore();
			//System.out.println(pacManScore);
			score += pacManScore;
		}

		//System.out.println("------");

		return score;
	}
}
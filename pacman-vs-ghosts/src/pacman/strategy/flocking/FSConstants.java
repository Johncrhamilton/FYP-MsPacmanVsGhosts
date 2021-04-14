package pacman.strategy.flocking;

import java.util.ArrayList;

public class FSConstants {

	public static final boolean HOMOGENEOUS_GHOSTS = false;
	
	/*Genetic Algorithm Constants*/
	public static final int NUMBER_OF_GENERATIONS = 100;
	public static final int POPULATION_SIZE = 50;
	public static final int TOURNAMENT_SELECTION_SIZE = 5;
	public static final double RECOMBINATION_PROBABILITY = 0.95;
	public static final double RECOMBINATION_MIXING_PROBABILITY = 0.6;
	public static final double MUTATION_PROBABILITY = 0.15;
	public static final double MUTATION_SWITCH_PROBABILITY = 0.25;
	public static final boolean ELITISM = true;

	/*Flocking Strategy Constants and ENUMS*/
	//Euclidean distance from top left power pill to bottom right is about 141 hence the largest neighbourhood radius should be greater than this distance
	public final static double LARGEST_NEIGHBOURHOOD_RADIUS = 300.0;
	
	public final static ArrayList<Double> NEIGHBOURHOODS = new ArrayList<Double>() 
	{{
		add(50.0);
		add(100.0);
		add(150.0);
		add(LARGEST_NEIGHBOURHOOD_RADIUS);
	}};
	
	public final static int NUMBER_OF_NEIGHBOURHOODS = 4;

	public final static double[][][] ACTOR_CONTEXT_MATRIX_MAGNITUDES =
		{
				/*HUNTER*/
				{
					/*PACMAN*/{1,0,0,0},/*POWERPILL*/{0,0,0,0},/*HUNTER*/{0,0,0,0},/*HUNTED*/{0,0,0,0},/*FLASH*/{0,0,0,0}
				},
				//HUNTED
				{
					/*PACMAN*/{-1,0,0,0},/*POWERPILL*/{0,0,0,0},/*HUNTER*/{0,0,0,0},/*HUNTED*/{0,0,0,0},/*FLASH*/{0,0,0,0}
				},
				//FLASH
				{
					/*PACMAN*/{-1,0,0,0},/*POWERPILL*/{0,0,0,0},/*HUNTER*/{0,0,0,0},/*HUNTED*/{0,0,0,0},/*FLASH*/{0,0,0,0}
				},
		};

	public enum GHOST_STATE
	{
		HUNTER,
		HUNTED,
		FLASH
	}

	public enum ACTOR
	{
		PACMAN,
		POWERPILL,
		HUNTER,
		HUNTED,
		FLASH
	}
}

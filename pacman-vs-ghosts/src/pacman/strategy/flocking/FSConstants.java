package pacman.strategy.flocking;

import java.util.ArrayList;

public class FSConstants {

	public static final boolean HOMOGENEOUS_GHOSTS = true;

	/*Genetic Algorithm Constants*/
	public static final int NUMBER_OF_GENERATIONS = 100;
	public static final int MAXIMUM_FITTEST_INDIVIDUAL_STREAK = 10;
	public static final int POPULATION_SIZE = 50;
	public static final int TOURNAMENT_SELECTION_SIZE = 5;
	public static final double RECOMBINATION_PROBABILITY = 0.95;
	public static final double RECOMBINATION_MIXING_PROBABILITY = 0.5;
	public static final double MUTATION_PROBABILITY = 0.25;
	public static final double MUTATION_SWITCH_PROBABILITY = 0.15;
	public static final boolean ELITISM = true;

	/*Flocking Strategy Constants and ENUMS*/
	//Euclidean distance from top left power pill to bottom right is about 141 hence the largest neighbourhood radius should be at least double this distance
	public final static double LARGEST_NEIGHBOURHOOD_RADIUS = 300;

	public final static int NUMBER_OF_NEIGHBOURHOODS = 4;
	
	/*
	 * Flocking Ghosts
	 * 1 strategy for Homogeneous ghosts and 4 strategies for Heterogeneous Ghosts
	 */
	@SuppressWarnings("serial")
	public final static ArrayList<FlockingStrategy> FLOCKING_STRATEGIES = new ArrayList<FlockingStrategy>() 
	{{
		add(new FlockingStrategy(NEIGHBOURHOODS_0, ACTOR_CONTEXT_MATRIX_MAGNITUDES_0));
		//add(new FlockingStrategy(NEIGHBOURHOODS_1, ACTOR_CONTEXT_MATRIX_MAGNITUDES_1));
		//add(new FlockingStrategy(NEIGHBOURHOODS_2, ACTOR_CONTEXT_MATRIX_MAGNITUDES_2));
		//add(new FlockingStrategy(NEIGHBOURHOODS_3, ACTOR_CONTEXT_MATRIX_MAGNITUDES_3));
	}};

	@SuppressWarnings("serial")
	private final static ArrayList<Double> NEIGHBOURHOODS_0 = new ArrayList<Double>()
	{{
		add(75.0);
		add(150.0);
		add(225.0);
		add(LARGEST_NEIGHBOURHOOD_RADIUS);
	}};
	
	@SuppressWarnings("serial")
	private final static ArrayList<Double> NEIGHBOURHOODS_1 = new ArrayList<Double>()
	{{
		add(75.0);
		add(150.0);
		add(225.0);
		add(LARGEST_NEIGHBOURHOOD_RADIUS);
	}};
	
	@SuppressWarnings("serial")
	private final static ArrayList<Double> NEIGHBOURHOODS_2 = new ArrayList<Double>()
	{{
		add(75.0);
		add(150.0);
		add(225.0);
		add(LARGEST_NEIGHBOURHOOD_RADIUS);
	}};

	@SuppressWarnings("serial")
	private final static ArrayList<Double> NEIGHBOURHOODS_3 = new ArrayList<Double>()
	{{
		add(75.0);
		add(150.0);
		add(225.0);
		add(LARGEST_NEIGHBOURHOOD_RADIUS);
	}};

	private final static double[][][] ACTOR_CONTEXT_MATRIX_MAGNITUDES_0 =
		{
				/*HUNTER*/
				{
					/*PACMAN*/		{1,0,0,0},
					/*POWERPILL*/	{0,0,0,0},
					/*HUNTER*/		{0,0,0,0},
					/*HUNTED*/		{0,0,0,0},
					/*FLASH*/		{0,0,0,0}
				},
				//HUNTED
				{
					/*PACMAN*/		{-1,0,0,0},
					/*POWERPILL*/	{0,0,0,0},
					/*HUNTER*/		{0,0,0,0},
					/*HUNTED*/		{0,0,0,0},
					/*FLASH*/		{0,0,0,0}
				},
				//FLASH
				{
					/*PACMAN*/		{-1,0,0,0},
					/*POWERPILL*/	{0,0,0,0},
					/*HUNTER*/		{0,0,0,0},
					/*HUNTED*/		{0,0,0,0},
					/*FLASH*/		{0,0,0,0}
				},
		};
	
	private final static double[][][] ACTOR_CONTEXT_MATRIX_MAGNITUDES_1 =
		{
				/*HUNTER*/
				{
					/*PACMAN*/		{1,0,0,0},
					/*POWERPILL*/	{0,0,0,0},
					/*HUNTER*/		{0,0,0,0},
					/*HUNTED*/		{0,0,0,0},
					/*FLASH*/		{0,0,0,0}
				},
				//HUNTED
				{
					/*PACMAN*/		{-1,0,0,0},
					/*POWERPILL*/	{0,0,0,0},
					/*HUNTER*/		{0,0,0,0},
					/*HUNTED*/		{0,0,0,0},
					/*FLASH*/		{0,0,0,0}
				},
				//FLASH
				{
					/*PACMAN*/		{-1,0,0,0},
					/*POWERPILL*/	{0,0,0,0},
					/*HUNTER*/		{0,0,0,0},
					/*HUNTED*/		{0,0,0,0},
					/*FLASH*/		{0,0,0,0}
				},
		};
	
	private final static double[][][] ACTOR_CONTEXT_MATRIX_MAGNITUDES_2 =
		{
				/*HUNTER*/
				{
					/*PACMAN*/		{1,0,0,0},
					/*POWERPILL*/	{0,0,0,0},
					/*HUNTER*/		{0,0,0,0},
					/*HUNTED*/		{0,0,0,0},
					/*FLASH*/		{0,0,0,0}
				},
				//HUNTED
				{
					/*PACMAN*/		{-1,0,0,0},
					/*POWERPILL*/	{0,0,0,0},
					/*HUNTER*/		{0,0,0,0},
					/*HUNTED*/		{0,0,0,0},
					/*FLASH*/		{0,0,0,0}
				},
				//FLASH
				{
					/*PACMAN*/		{-1,0,0,0},
					/*POWERPILL*/	{0,0,0,0},
					/*HUNTER*/		{0,0,0,0},
					/*HUNTED*/		{0,0,0,0},
					/*FLASH*/		{0,0,0,0}
				},
		};
	
	private final static double[][][] ACTOR_CONTEXT_MATRIX_MAGNITUDES_3 =
		{
				/*HUNTER*/
				{
					/*PACMAN*/		{1,0,0,0},
					/*POWERPILL*/	{0,0,0,0},
					/*HUNTER*/		{0,0,0,0},
					/*HUNTED*/		{0,0,0,0},
					/*FLASH*/		{0,0,0,0}
				},
				//HUNTED
				{
					/*PACMAN*/		{-1,0,0,0},
					/*POWERPILL*/	{0,0,0,0},
					/*HUNTER*/		{0,0,0,0},
					/*HUNTED*/		{0,0,0,0},
					/*FLASH*/		{0,0,0,0}
				},
				//FLASH
				{
					/*PACMAN*/		{-1,0,0,0},
					/*POWERPILL*/	{0,0,0,0},
					/*HUNTER*/		{0,0,0,0},
					/*HUNTED*/		{0,0,0,0},
					/*FLASH*/		{0,0,0,0}
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

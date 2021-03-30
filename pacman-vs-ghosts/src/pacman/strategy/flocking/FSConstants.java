package pacman.strategy.flocking;

public class FSConstants {

	/*
	* Euclidean distance from top left power pill to bottom right is about 141
	* Hence the greatest neighbourhood radius should be roughly about the distance
	*/
	public static double[] NEIGHBOURHOODS = new double[] {200};
	public static int NUMBER_OF_NEIGHBOURHOODS = NEIGHBOURHOODS.length;
	
	public static double[][][] ACTOR_CONTEXT_MATRIX_MAGNITUDES =
		{
			/*HUNTER*/
			{
				/*PACMAN*/{1,0},/*POWERPILL*/{0,0},/*HUNTER*/{0,0},/*HUNTED*/{0,0},/*FLASH*/{0,0}
			},
			//HUNTED
			{
				/*PACMAN*/{-1,0},/*POWERPILL*/{0,0},/*HUNTER*/{0,0},/*HUNTED*/{0,0},/*FLASH*/{0,0}
			},
	        //FLASH
			{
				/*PACMAN*/{-1,0},/*POWERPILL*/{0,0},/*HUNTER*/{0,0},/*HUNTED*/{0,0},/*FLASH*/{0,0}
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

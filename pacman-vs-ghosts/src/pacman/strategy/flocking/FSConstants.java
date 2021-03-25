package pacman.strategy.flocking;

public class FSConstants {

	public static double NUMBER_OF_NEIGHBOURHOODS = 3;
	public static double NEIGHBOURHOOD_INTERVAL = 15;

	public enum GhostState 
	{
		HUNTER,
		HUNTED,
		FLASH
	}
	
	public enum Actor
	{
		PACMAN,
		POWERPILL,
		HUNTER,
		HUNTED,
		FLASH
	}
}

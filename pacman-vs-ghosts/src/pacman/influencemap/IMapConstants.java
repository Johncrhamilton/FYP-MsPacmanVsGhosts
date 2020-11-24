package pacman.influencemap;

import java.util.Random;

public class IMapConstants {
	
	public static final int INFLUENCE_OF_PILL = 4;
	public static final int INFLUENCE_OF_GHOST = -90;
	public static final int INFLUENCE_OF_EDIBLE_GHOST = 60;
	public static final int INFLUENCE_OF_POWERPILL = 40;
	public static final int INFLUENCE_OF_FREEDOM_OF_CHOICE = 90;
	
	public static final double INFLUENCE_FACTOR = 0.95;
	
	public static final int POWERPILL_DISTANCE_THRESHOLD = 150;
	public static final Random random = new Random();
	
}

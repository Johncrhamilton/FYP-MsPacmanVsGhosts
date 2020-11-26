package pacman.influencemap;

import java.util.Random;

public class IMConstants {
	
	//Constants for Ms. Pacman
	public static final int INFLUENCE_OF_PILL = 4;
	public static final double INFLUENCE_OF_PILL_LIMIT = 0.04;
	
	public static final int INFLUENCE_OF_GHOST = -90;
	public static final double INFLUENCE_OF_GHOST_LIMIT = -9;
	
	public static final int INFLUENCE_OF_EDIBLE_GHOST = 60;
	public static final double INFLUENCE_OF_EDIBLE_GHOST_LIMIT = 0.6;
	
	public static final int INFLUENCE_OF_POWERPILL = 40;
	public static final double INFLUENCE_OF_POWERPILL_POSITIVE_LIMIT = 4;
	public static final double INFLUENCE_OF_POWERPILL_NEGATIVE_LIMIT = -4;
	public static final int POWERPILL_DISTANCE_THRESHOLD_PER_GHOST = 85;
	
	public static final int INFLUENCE_OF_FREEDOM_OF_CHOICE = 90;
	public static final double INFLUENCE_OF_FREEDOM_OF_CHOICE_LIMIT = 0.9;
	public static final double FREEDOM_OF_CHOICE_THRESHOLD = -15;
	
	//General constants
	public static final double INFLUENCE_FACTOR = 0.95;
	public static final int FORCE_DIRECTION_COUNT = 60;
	public static final Random random = new Random();
	
}

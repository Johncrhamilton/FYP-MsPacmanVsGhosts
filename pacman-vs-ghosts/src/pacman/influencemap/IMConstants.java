package pacman.influencemap;

public class IMConstants {
	
	//Constants for Ms. Pacman
	public static final int INFLUENCE_OF_PILL = 4;
	public static final double INFLUENCE_OF_PILL_LIMIT = 0.04;
	public static final double INFLUENCE_FACTOR_OF_PILL = 0.95;
	
	public static final int INFLUENCE_OF_GHOST = -80;
	public static final double INFLUENCE_OF_GHOST_LIMIT = -8;
	public static final double INFLUENCE_FACTOR_OF_GHOST = 0.95;
	
	public static final int INFLUENCE_OF_EDIBLE_GHOST = 60;
	public static final double INFLUENCE_OF_EDIBLE_GHOST_LIMIT = 0.6;
	public static final double INFLUENCE_FACTOR_OF_EDIBLE_GHOST = 0.95;
	
	public static final int INFLUENCE_OF_POWERPILL = 32;
	public static final double INFLUENCE_OF_POWERPILL_POSITIVE_LIMIT = 0.32;
	public static final double INFLUENCE_OF_POWERPILL_NEGATIVE_LIMIT = -0.32;
	public static final int POWERPILL_DISTANCE_THRESHOLD_PER_GHOST = 115;
	public static final double INFLUENCE_FACTOR_OF_POWERPILL = 0.95;
	
	public static final int INFLUENCE_OF_FREEDOM_OF_CHOICE = 60;
	public static final int INFLUENCE_OF_FREEDOM_OF_CHOICE_THRESHOLD = -30;
	public static final double INFLUENCE_OF_FREEDOM_OF_CHOICE_LIMIT = 0.6;
	public static final double INFLUENCE_FACTOR_OF_FREEDOM_OF_CHOICE = 0.95;
	
	public static final int FORCE_DIRECTION_COUNT = 62;
	
}

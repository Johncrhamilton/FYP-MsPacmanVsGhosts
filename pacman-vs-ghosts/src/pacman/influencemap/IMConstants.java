package pacman.influencemap;

public class IMConstants {
	
	//Constants for Ms. Pacman
	public static final int INFLUENCE_OF_PILL = 4;
	public static final double INFLUENCE_FACTOR_OF_PILL = 0.95;
	
	public static final int INFLUENCE_OF_GHOST = -80;
	public static final double INFLUENCE_OF_GHOST_LIMIT = -8;
	public static final double INFLUENCE_FACTOR_OF_GHOST = 0.95;
	
	public static final int INFLUENCE_OF_EDIBLE_GHOST = 60;
	public static final double INFLUENCE_FACTOR_OF_EDIBLE_GHOST = 0.95;
	
	public static final int INFLUENCE_OF_POWERPILL = 30;
	public static final int POWERPILL_DISTANCE_THRESHOLD_PER_GHOST = 110;
	public static final double INFLUENCE_FACTOR_OF_POWERPILL = 0.95;
	
	public static final int INFLUENCE_OF_FREEDOM_OF_CHOICE = 60;
	public static final int INFLUENCE_OF_FREEDOM_OF_CHOICE_THRESHOLD = -30;
	public static final double INFLUENCE_FACTOR_OF_FREEDOM_OF_CHOICE = 0.95;
	
	public static final int FORCE_DIRECTION_COUNT = 62;
	
	//Constants for Ghosts
	public static final double INFLUENCE_OF_PACMAN = 200;
	public static final double POWER_PILL_DISTANCE_FACTOR = 36;
	public static final double POWER_PILL_THRESHOLD = 0.85;
	public static final double INFLUENCE_FACTOR_OF_PACMAN = 0.95;
	
	public static final double INFLUENCE_GHOST_WEIGHT = -20;
	public static final double INFLUENCE_FACTOR_OF_GHOST_WEIGHT = 0.9;
	public static final double LIMITING_INFLUENCE_OF_PACMAN = 0;
	
}

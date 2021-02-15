package pacman.influencemap;

public class IMConstants {

	//Constants for Ms. Pacman, no longer "final" due to parameter tuning
	public static double INFLUENCE_OF_PILL = 4;
	public static double INFLUENCE_FACTOR_OF_PILL = 0.95;

	public static double INFLUENCE_OF_GHOST = -80;
	public static double INFLUENCE_OF_GHOST_LIMIT = INFLUENCE_OF_GHOST/10;
	public static double INFLUENCE_FACTOR_OF_GHOST = 0.95;

	public static double INFLUENCE_OF_EDIBLE_GHOST = 60;
	public static double INFLUENCE_FACTOR_OF_EDIBLE_GHOST = 0.95;

	public static double INFLUENCE_OF_POWERPILL = 30;
	public static double POWERPILL_DISTANCE_THRESHOLD_PER_GHOST = 110;
	public static double INFLUENCE_FACTOR_OF_POWERPILL = 0.95;

	public static double INFLUENCE_OF_FREEDOM_OF_CHOICE = 60;
	public static double INFLUENCE_OF_FREEDOM_OF_CHOICE_THRESHOLD = -30;
	public static double INFLUENCE_FACTOR_OF_FREEDOM_OF_CHOICE = 0.95;

	public static double FORCE_DIRECTION_COUNT = 62;

	//Constants for Ghosts, no longer "final" due to parameter tuning
	public static double INFLUENCE_OF_PACMAN = 200;
	public static double POWER_PILL_DISTANCE_FACTOR = 36;
	public static double POWER_PILL_THRESHOLD = 0.85;
	public static double INFLUENCE_FACTOR_OF_PACMAN = 0.95;

	public static double INFLUENCE_GHOST_WEIGHT = -20;
	public static double INFLUENCE_FACTOR_OF_GHOST_WEIGHT = 0.9;
	public static double LIMITING_INFLUENCE_OF_PACMAN = 0;

	/**
	 * Get a constant's value with its enum
	 * @param pacmanParam
	 * @return constant's value
	 */
	public static double getPacmanConstant(IMPacmanParams pacmanParam) 
	{
		switch(pacmanParam) 
		{
		case ENUM_INFLUENCE_OF_PILL:
			return INFLUENCE_OF_PILL;
		case ENUM_INFLUENCE_FACTOR_OF_PILL:
			return INFLUENCE_FACTOR_OF_PILL;

		case ENUM_INFLUENCE_OF_GHOST:
			return INFLUENCE_OF_GHOST;			
		case ENUM_INFLUENCE_FACTOR_OF_GHOST:
			return INFLUENCE_FACTOR_OF_GHOST;

		case ENUM_INFLUENCE_OF_EDIBLE_GHOST:
			return INFLUENCE_OF_EDIBLE_GHOST;
		case ENUM_INFLUENCE_FACTOR_OF_EDIBLE_GHOST:
			return INFLUENCE_FACTOR_OF_EDIBLE_GHOST;

		case ENUM_INFLUENCE_OF_POWERPILL:
			return INFLUENCE_OF_POWERPILL;
		case ENUM_POWERPILL_DISTANCE_THRESHOLD_PER_GHOST:
			return POWERPILL_DISTANCE_THRESHOLD_PER_GHOST;
		case ENUM_INFLUENCE_FACTOR_OF_POWERPILL:
			return INFLUENCE_FACTOR_OF_POWERPILL;

		case ENUM_INFLUENCE_OF_FREEDOM_OF_CHOICE:
			return INFLUENCE_OF_FREEDOM_OF_CHOICE;
		case ENUM_INFLUENCE_OF_FREEDOM_OF_CHOICE_THRESHOLD:
			return INFLUENCE_OF_FREEDOM_OF_CHOICE_THRESHOLD;
		case ENUM_INFLUENCE_FACTOR_OF_FREEDOM_OF_CHOICE:
			return INFLUENCE_FACTOR_OF_FREEDOM_OF_CHOICE;

		case ENUM_FORCE_DIRECTION_COUNT:
			return FORCE_DIRECTION_COUNT;
		default:
			throw new UnsupportedOperationException(pacmanParam.toString() + " not supported.");
		}
	}

	/**
	 * Set a constant's value with its enum
	 * @param pacmanParam
	 * @param newValue
	 */
	public static void setPacmanConstant(IMPacmanParams pacmanParam, double newValue) 
	{
		switch(pacmanParam) 
		{
		case ENUM_INFLUENCE_OF_PILL:
			INFLUENCE_OF_PILL = newValue;
			break;
		case ENUM_INFLUENCE_FACTOR_OF_PILL:
			INFLUENCE_FACTOR_OF_PILL = newValue;
			break;

		case ENUM_INFLUENCE_OF_GHOST:
			INFLUENCE_OF_GHOST = newValue;
			break;			
		case ENUM_INFLUENCE_FACTOR_OF_GHOST:
			INFLUENCE_FACTOR_OF_GHOST = newValue;
			break;

		case ENUM_INFLUENCE_OF_EDIBLE_GHOST:
			INFLUENCE_OF_EDIBLE_GHOST = newValue;
			break;
		case ENUM_INFLUENCE_FACTOR_OF_EDIBLE_GHOST:
			INFLUENCE_FACTOR_OF_EDIBLE_GHOST = newValue;
			break;

		case ENUM_INFLUENCE_OF_POWERPILL:
			INFLUENCE_OF_POWERPILL = newValue;
			break;
		case ENUM_POWERPILL_DISTANCE_THRESHOLD_PER_GHOST:
			POWERPILL_DISTANCE_THRESHOLD_PER_GHOST = newValue;
			break;
		case ENUM_INFLUENCE_FACTOR_OF_POWERPILL:
			INFLUENCE_FACTOR_OF_POWERPILL = newValue;
			break;

		case ENUM_INFLUENCE_OF_FREEDOM_OF_CHOICE:
			INFLUENCE_OF_FREEDOM_OF_CHOICE = newValue;
			break;
		case ENUM_INFLUENCE_OF_FREEDOM_OF_CHOICE_THRESHOLD:
			INFLUENCE_OF_FREEDOM_OF_CHOICE_THRESHOLD = newValue;
			break;
		case ENUM_INFLUENCE_FACTOR_OF_FREEDOM_OF_CHOICE:
			INFLUENCE_FACTOR_OF_FREEDOM_OF_CHOICE = newValue;
			break;

		case ENUM_FORCE_DIRECTION_COUNT:
			FORCE_DIRECTION_COUNT = newValue;
			break;
		default:
			throw new UnsupportedOperationException(pacmanParam.toString() + " not supported.");
		}
	}	

	/**
	 * Get a constant's value with its enum
	 * @param ghostParam
	 * @return constant's value
	 */
	public static double getGhostConstant(IMGhostParams ghostParam) 
	{
		switch(ghostParam) 
		{
		case ENUM_INFLUENCE_OF_PACMAN:
			return INFLUENCE_OF_PACMAN;
		case ENUM_POWER_PILL_DISTANCE_FACTOR:
			return POWER_PILL_DISTANCE_FACTOR;
		case ENUM_POWER_PILL_THRESHOLD:
			return POWER_PILL_THRESHOLD;
		case ENUM_INFLUENCE_FACTOR_OF_PACMAN:
			return INFLUENCE_FACTOR_OF_PACMAN;

		case ENUM_INFLUENCE_GHOST_WEIGHT:
			return INFLUENCE_GHOST_WEIGHT;
		case ENUM_INFLUENCE_FACTOR_OF_GHOST_WEIGHT:
			return INFLUENCE_FACTOR_OF_GHOST_WEIGHT;
		case ENUM_LIMITING_INFLUENCE_OF_PACMAN:
			return LIMITING_INFLUENCE_OF_PACMAN;
		default:
			throw new UnsupportedOperationException(ghostParam.toString() + " not supported.");
		}
	}

	/**
	 * Set a constant's value with its enum
	 * @param ghostParam
	 * @param newValue
	 */
	public static void setGhostConstant(IMGhostParams ghostParam, double newValue) 
	{
		switch(ghostParam) 
		{
		case ENUM_INFLUENCE_OF_PACMAN:
			INFLUENCE_OF_PACMAN = newValue;
			break;
		case ENUM_POWER_PILL_DISTANCE_FACTOR:
			POWER_PILL_DISTANCE_FACTOR = newValue;
			break;
		case ENUM_POWER_PILL_THRESHOLD:
			POWER_PILL_THRESHOLD = newValue;
			break;
		case ENUM_INFLUENCE_FACTOR_OF_PACMAN:
			INFLUENCE_FACTOR_OF_PACMAN = newValue;
			break;

		case ENUM_INFLUENCE_GHOST_WEIGHT:
			INFLUENCE_GHOST_WEIGHT = newValue;
			break;
		case ENUM_INFLUENCE_FACTOR_OF_GHOST_WEIGHT:
			INFLUENCE_FACTOR_OF_GHOST_WEIGHT = newValue;
			break;
		case ENUM_LIMITING_INFLUENCE_OF_PACMAN:
			LIMITING_INFLUENCE_OF_PACMAN = newValue;
			break;
		default:
			throw new UnsupportedOperationException(ghostParam.toString() + " not supported.");
		}
	}
	
	public enum IMGhostParams
	{
		ENUM_INFLUENCE_OF_PACMAN,
		ENUM_POWER_PILL_DISTANCE_FACTOR,
		ENUM_POWER_PILL_THRESHOLD,
		ENUM_INFLUENCE_FACTOR_OF_PACMAN,
		
		ENUM_INFLUENCE_GHOST_WEIGHT,
		ENUM_INFLUENCE_FACTOR_OF_GHOST_WEIGHT,
		ENUM_LIMITING_INFLUENCE_OF_PACMAN
	}
	
	public enum IMPacmanParams 
	{
		ENUM_INFLUENCE_OF_PILL,
		ENUM_INFLUENCE_FACTOR_OF_PILL,
		
		ENUM_INFLUENCE_OF_GHOST,
		ENUM_INFLUENCE_FACTOR_OF_GHOST,
		
		ENUM_INFLUENCE_OF_EDIBLE_GHOST,
		ENUM_INFLUENCE_FACTOR_OF_EDIBLE_GHOST,
		
		ENUM_INFLUENCE_OF_POWERPILL,
		ENUM_POWERPILL_DISTANCE_THRESHOLD_PER_GHOST,
		ENUM_INFLUENCE_FACTOR_OF_POWERPILL,
		
		ENUM_INFLUENCE_OF_FREEDOM_OF_CHOICE,
		ENUM_INFLUENCE_OF_FREEDOM_OF_CHOICE_THRESHOLD,
		ENUM_INFLUENCE_FACTOR_OF_FREEDOM_OF_CHOICE,
		
		ENUM_FORCE_DIRECTION_COUNT
	}
}

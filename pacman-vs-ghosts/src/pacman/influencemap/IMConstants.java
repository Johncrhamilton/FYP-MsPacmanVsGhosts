package pacman.influencemap;

import java.util.ArrayList;

public class IMConstants {
	
	//All tunable Ms. Pacman parameters
	public static final ArrayList<IMTunableParameter> PACMAN_PARAMETERS = new ArrayList<IMTunableParameter>() {{
		add(new IMTunableParameter(IMAPControllerParameter.P_FORCE_DIRECTION_COUNT, new double[] {0.0, 15.0, 30.0, 45.0, 60.0, 75.0, 90.0, 105.0, 120.0}));		
		add(new IMTunableParameter(IMAPControllerParameter.P_INFLUENCE_OF_GHOST, new double[] {-100.0, -90.0, -85.0, -80.0, -75.0, -70.0, -60.0}));		
		add(new IMTunableParameter(IMAPControllerParameter.P_INFLUENCE_OF_EDIBLE_GHOST, new double[] {40.0, 50.0, 55.0, 60.0, 65.0, 70.0, 80.0}));		
		add(new IMTunableParameter(IMAPControllerParameter.P_INFLUENCE_OF_POWERPILL, new double[] {10.0, 20.0, 25.0, 30.0, 35.0, 40.0, 50.0}));
		add(new IMTunableParameter(IMAPControllerParameter.P_INFLUENCE_OF_FREEDOM_OF_CHOICE_THRESHOLD, new double[] {-50.0, -40.0, -35.0, -30.0, -25.0, -20.0, -10.0, 0.0}));
		add(new IMTunableParameter(IMAPControllerParameter.P_POWERPILL_DISTANCE_THRESHOLD_PER_GHOST, new double[] {95.0, 100.0, 105.0, 110.0, 115.0, 120.0, 125.0}));		
		add(new IMTunableParameter(IMAPControllerParameter.P_INFLUENCE_OF_FREEDOM_OF_CHOICE, new double[] {20.0, 40.0, 50.0, 60.0, 70.0, 80.0, 100.0}));		
		add(new IMTunableParameter(IMAPControllerParameter.P_INFLUENCE_OF_PILL, new double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 8.0}));		
		add(new IMTunableParameter(IMAPControllerParameter.P_INFLUENCE_FACTOR_OF_GHOST, new double[] {0.8, 0.85, 0.9, 0.95}));		
		add(new IMTunableParameter(IMAPControllerParameter.P_INFLUENCE_FACTOR_OF_EDIBLE_GHOST, new double[] {0.8, 0.85, 0.9, 0.95}));
		add(new IMTunableParameter(IMAPControllerParameter.P_INFLUENCE_FACTOR_OF_POWERPILL, new double[] {0.8, 0.85, 0.9, 0.95}));
		add(new IMTunableParameter(IMAPControllerParameter.P_INFLUENCE_FACTOR_OF_FREEDOM_OF_CHOICE, new double[] {0.8, 0.85, 0.9, 0.95}));
		add(new IMTunableParameter(IMAPControllerParameter.P_INFLUENCE_FACTOR_OF_PILL, new double[] {0.8, 0.85, 0.9, 0.95}));		
	}};

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

	//All tunable Ghost parameters in order of importance
	public static final ArrayList<IMTunableParameter> GHOST_PARAMETERS = new ArrayList<IMTunableParameter>() {{
		add(new IMTunableParameter(IMAPControllerParameter.G_INFLUENCE_OF_PACMAN, new double[] {140.0, 170.0, 180.0, 190.0, 200.0, 210.0, 220.0, 230.0, 260.0}));
		add(new IMTunableParameter(IMAPControllerParameter.G_POWER_PILL_DISTANCE_FACTOR, new double[] {20.0, 27.5, 30.0, 32.5, 35.0, 37.5, 40.0, 42.5, 50.0}));
		add(new IMTunableParameter(IMAPControllerParameter.G_POWER_PILL_THRESHOLD, new double[] {0.75, 0.8, 0.85, 0.9, 0.95}));
		add(new IMTunableParameter(IMAPControllerParameter.G_INFLUENCE_GHOST_WEIGHT, new double[] {-35.0, -30.0, -27.5, -25.0, -22.5, -20.0, -17.5, -15.0, -12.5, -10.0, -15.0}));	
		add(new IMTunableParameter(IMAPControllerParameter.G_INFLUENCE_FACTOR_OF_PACMAN, new double[] {0.75, 0.8, 0.85, 0.9, 0.95}));
		add(new IMTunableParameter(IMAPControllerParameter.G_INFLUENCE_FACTOR_OF_GHOST_WEIGHT, new double[] {0.75, 0.8, 0.85, 0.9, 0.95}));
		add(new IMTunableParameter(IMAPControllerParameter.G_LIMITING_INFLUENCE_OF_PACMAN, new double[] {-10.0, -7.5, -5.0, -2.5, 0.0, 2.5, 5.0, 7.5, 10.0}));
	}};
	
	//Constants for Ghosts, no longer "final" due to parameter tuning	
	public static double INFLUENCE_OF_PACMAN = 200;
	public static double POWER_PILL_DISTANCE_FACTOR = 35;
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
	public static double getConstant(IMAPControllerParameter param) 
	{
		switch(param) 
		{
		//Pacman
		case P_INFLUENCE_OF_PILL:
			return INFLUENCE_OF_PILL;
		case P_INFLUENCE_FACTOR_OF_PILL:
			return INFLUENCE_FACTOR_OF_PILL;

		case P_INFLUENCE_OF_GHOST:
			return INFLUENCE_OF_GHOST;			
		case P_INFLUENCE_FACTOR_OF_GHOST:
			return INFLUENCE_FACTOR_OF_GHOST;

		case P_INFLUENCE_OF_EDIBLE_GHOST:
			return INFLUENCE_OF_EDIBLE_GHOST;
		case P_INFLUENCE_FACTOR_OF_EDIBLE_GHOST:
			return INFLUENCE_FACTOR_OF_EDIBLE_GHOST;

		case P_INFLUENCE_OF_POWERPILL:
			return INFLUENCE_OF_POWERPILL;
		case P_POWERPILL_DISTANCE_THRESHOLD_PER_GHOST:
			return POWERPILL_DISTANCE_THRESHOLD_PER_GHOST;
		case P_INFLUENCE_FACTOR_OF_POWERPILL:
			return INFLUENCE_FACTOR_OF_POWERPILL;

		case P_INFLUENCE_OF_FREEDOM_OF_CHOICE:
			return INFLUENCE_OF_FREEDOM_OF_CHOICE;
		case P_INFLUENCE_OF_FREEDOM_OF_CHOICE_THRESHOLD:
			return INFLUENCE_OF_FREEDOM_OF_CHOICE_THRESHOLD;
		case P_INFLUENCE_FACTOR_OF_FREEDOM_OF_CHOICE:
			return INFLUENCE_FACTOR_OF_FREEDOM_OF_CHOICE;

		case P_FORCE_DIRECTION_COUNT:
			return FORCE_DIRECTION_COUNT;
		
		//Ghost
		case G_INFLUENCE_OF_PACMAN:
			return INFLUENCE_OF_PACMAN;
		case G_POWER_PILL_DISTANCE_FACTOR:
			return POWER_PILL_DISTANCE_FACTOR;
		case G_POWER_PILL_THRESHOLD:
			return POWER_PILL_THRESHOLD;
		case G_INFLUENCE_FACTOR_OF_PACMAN:
			return INFLUENCE_FACTOR_OF_PACMAN;

		case G_INFLUENCE_GHOST_WEIGHT:
			return INFLUENCE_GHOST_WEIGHT;
		case G_INFLUENCE_FACTOR_OF_GHOST_WEIGHT:
			return INFLUENCE_FACTOR_OF_GHOST_WEIGHT;
		case G_LIMITING_INFLUENCE_OF_PACMAN:
			return LIMITING_INFLUENCE_OF_PACMAN;
		default:
			throw new UnsupportedOperationException(param.toString() + " not supported.");
		}
	}

	/**
	 * Set a constant's value with its enum
	 * @param pacmanParam
	 * @param newValue
	 */
	public static void setConstant(IMAPControllerParameter param, double newValue) 
	{
		switch(param) 
		{
		//Pacman
		case P_INFLUENCE_OF_PILL:
			INFLUENCE_OF_PILL = newValue;
			break;
		case P_INFLUENCE_FACTOR_OF_PILL:
			INFLUENCE_FACTOR_OF_PILL = newValue;
			break;

		case P_INFLUENCE_OF_GHOST:
			INFLUENCE_OF_GHOST = newValue;
			break;			
		case P_INFLUENCE_FACTOR_OF_GHOST:
			INFLUENCE_FACTOR_OF_GHOST = newValue;
			break;

		case P_INFLUENCE_OF_EDIBLE_GHOST:
			INFLUENCE_OF_EDIBLE_GHOST = newValue;
			break;
		case P_INFLUENCE_FACTOR_OF_EDIBLE_GHOST:
			INFLUENCE_FACTOR_OF_EDIBLE_GHOST = newValue;
			break;

		case P_INFLUENCE_OF_POWERPILL:
			INFLUENCE_OF_POWERPILL = newValue;
			break;
		case P_POWERPILL_DISTANCE_THRESHOLD_PER_GHOST:
			POWERPILL_DISTANCE_THRESHOLD_PER_GHOST = newValue;
			break;
		case P_INFLUENCE_FACTOR_OF_POWERPILL:
			INFLUENCE_FACTOR_OF_POWERPILL = newValue;
			break;

		case P_INFLUENCE_OF_FREEDOM_OF_CHOICE:
			INFLUENCE_OF_FREEDOM_OF_CHOICE = newValue;
			break;
		case P_INFLUENCE_OF_FREEDOM_OF_CHOICE_THRESHOLD:
			INFLUENCE_OF_FREEDOM_OF_CHOICE_THRESHOLD = newValue;
			break;
		case P_INFLUENCE_FACTOR_OF_FREEDOM_OF_CHOICE:
			INFLUENCE_FACTOR_OF_FREEDOM_OF_CHOICE = newValue;
			break;

		case P_FORCE_DIRECTION_COUNT:
			FORCE_DIRECTION_COUNT = newValue;
			break;
		
		//Ghost
		case G_INFLUENCE_OF_PACMAN:
			INFLUENCE_OF_PACMAN = newValue;
			break;
		case G_POWER_PILL_DISTANCE_FACTOR:
			POWER_PILL_DISTANCE_FACTOR = newValue;
			break;
		case G_POWER_PILL_THRESHOLD:
			POWER_PILL_THRESHOLD = newValue;
			break;
		case G_INFLUENCE_FACTOR_OF_PACMAN:
			INFLUENCE_FACTOR_OF_PACMAN = newValue;
			break;

		case G_INFLUENCE_GHOST_WEIGHT:
			INFLUENCE_GHOST_WEIGHT = newValue;
			break;
		case G_INFLUENCE_FACTOR_OF_GHOST_WEIGHT:
			INFLUENCE_FACTOR_OF_GHOST_WEIGHT = newValue;
			break;
		case G_LIMITING_INFLUENCE_OF_PACMAN:
			LIMITING_INFLUENCE_OF_PACMAN = newValue;
			break;	
		
		default:
			throw new UnsupportedOperationException(param.toString() + " not supported.");
		}
	}
	
	public enum IMAPControllerParameter 
	{
		P_INFLUENCE_OF_PILL,
		P_INFLUENCE_FACTOR_OF_PILL,
		
		P_INFLUENCE_OF_GHOST,
		P_INFLUENCE_FACTOR_OF_GHOST,
		
		P_INFLUENCE_OF_EDIBLE_GHOST,
		P_INFLUENCE_FACTOR_OF_EDIBLE_GHOST,
		
		P_INFLUENCE_OF_POWERPILL,
		P_POWERPILL_DISTANCE_THRESHOLD_PER_GHOST,
		P_INFLUENCE_FACTOR_OF_POWERPILL,
		
		P_INFLUENCE_OF_FREEDOM_OF_CHOICE,
		P_INFLUENCE_OF_FREEDOM_OF_CHOICE_THRESHOLD,
		P_INFLUENCE_FACTOR_OF_FREEDOM_OF_CHOICE,
		
		P_FORCE_DIRECTION_COUNT,
		
		G_INFLUENCE_OF_PACMAN,
		G_POWER_PILL_DISTANCE_FACTOR,
		G_POWER_PILL_THRESHOLD,
		G_INFLUENCE_FACTOR_OF_PACMAN,
		
		G_INFLUENCE_GHOST_WEIGHT,
		G_INFLUENCE_FACTOR_OF_GHOST_WEIGHT,
		G_LIMITING_INFLUENCE_OF_PACMAN
	}
}

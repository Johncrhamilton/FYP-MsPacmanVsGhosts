package pacman.influencemap;
import pacman.influencemap.IMConstants.IMAP_CONTROLLER_PARAMETER;

public class IMTunableParameter {
	
	private IMAP_CONTROLLER_PARAMETER param;
	private double[] paramValues;
	
	//A tunable parameter has defined as a parameter enum and a parameter space (parameter values)
	public IMTunableParameter(IMAP_CONTROLLER_PARAMETER param, double[] paramValues) 
	{
		this.param = param;
		this.paramValues = paramValues;
	}
	
	public IMAP_CONTROLLER_PARAMETER getParamEnum() 
	{
		return param;
	}
	
	public double[] getParamValues() 
	{
		return paramValues;
	}
	
}

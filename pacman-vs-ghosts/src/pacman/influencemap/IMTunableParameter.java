package pacman.influencemap;
import pacman.influencemap.IMConstants.IMAPControllerParameter;

public class IMTunableParameter {
	
	private IMAPControllerParameter param;
	private double[] paramValues;
	
	//A tunable parameter has defined as a parameter enum and a parameter space (parameter values)
	public IMTunableParameter(IMAPControllerParameter param, double[] paramValues) 
	{
		this.param = param;
		this.paramValues = paramValues;
	}
	
	public IMAPControllerParameter getParamEnum() 
	{
		return param;
	}
	
	public double[] getParamValues() 
	{
		return paramValues;
	}
	
}

package pacman.game.util;

public class ExperimentResult {
	
	private final double average;
	private final double standardDeviation;
	private final double[] runScores;
	
	public ExperimentResult(double average, double standardDeviation, double[] runScores) 
	{
		this.average = average;
		this.standardDeviation = standardDeviation;
		this.runScores = runScores;
	}
	
	public double getAverageScore() 
	{
		return average;
	}

	public double getStandardDeviation()
	{
		return standardDeviation;
	}

	public double[] getRunScores()
	{
		return runScores;
	}
	
	public String toString() 
	{
		String s = "Average score: " + average + "\n";
		s = s + "Standard deviation: " + standardDeviation + "\n";
		
		for(int i = 0; i < runScores.length; i++) 
		{
			s = s + "Run" + i + ": " + runScores[i] + "\n";
		}
		
		return s;		
	}
}

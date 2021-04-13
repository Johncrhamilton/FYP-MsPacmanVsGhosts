package pacman;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Random;
import pacman.controllers.Controller;
import pacman.controllers.HumanController;
import pacman.controllers.KeyBoardInput;
import pacman.controllers.examples.AggressiveGhosts;
import pacman.controllers.examples.Legacy;
import pacman.controllers.examples.Legacy2TheReckoning;
import pacman.controllers.examples.NearestPillPacMan;
import pacman.controllers.examples.NearestPillPacManVS;
import pacman.controllers.examples.RandomGhosts;
import pacman.controllers.examples.RandomNonRevPacMan;
import pacman.controllers.examples.RandomPacMan;
import pacman.controllers.examples.StarterGhosts;
import pacman.controllers.examples.StarterPacMan;
import pacman.entries.ghosts.DummyGhosts;
import pacman.entries.ghosts.FlockingStrategyGhosts;
import pacman.entries.ghosts.InfluenceMapGhosts;
import pacman.entries.pacman.InfluenceMapPacMan;
import pacman.game.Game;
import pacman.game.GameView;
import pacman.game.util.ExperimentResult;
import pacman.influencemap.IMConstants;
import pacman.influencemap.IMTunableParameter;
import pacman.influencemap.IMConstants.IMAP_CONTROLLER_PARAMETER;
import pacman.strategy.flocking.FSConstants;
import pacman.strategy.flocking.FlockingStrategy;
import pacman.strategy.flocking.GeneticAlgorithm;
import pacman.strategy.flocking.FSConstants.ACTOR;
import pacman.strategy.flocking.FSConstants.GHOST_STATE;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.inference.OneWayAnova;
import org.apache.commons.math3.stat.inference.TTest;

import static pacman.game.Constants.*;

/**
 * This class may be used to execute the game in timed or un-timed modes, with or without
 * visuals. Competitors should implement their controllers in game.entries.ghosts and 
 * game.entries.pacman respectively. The skeleton classes are already provided. The package
 * structure should not be changed (although you may create sub-packages in these packages).
 */
@SuppressWarnings("unused")
public class Executor
{	
	private final static int NUM_EXPERIMENT_RUNS = 50;
	private final static double ALPHA = 0.05;

	private static OneWayAnova oneWayAnova = new OneWayAnova();
	private static StandardDeviation standardDeviation = new StandardDeviation();

	/**
	 * The main method. Several options are listed - simply remove comments to use the option you want.
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args)
	{
		Executor exec = new Executor();

		//Tune parameters for IMAP Based MsPacman
		//tuneIMPacmanParameters(exec);

		//Tune parameters for IMAP Based Ghosts
		//tuneIMGhostParameters(exec);

		/*
		//run multiple games in batch mode - good for testing.
		int numTrials=10;
		exec.runExperiment(new RandomPacMan(),new RandomGhosts(),numTrials);
		 */
		
		GeneticAlgorithm GA = new GeneticAlgorithm(exec);
		GA.bestFlockingStrategy().toString();
		
		/*
		 */
		//run a game in synchronous mode: game waits until controllers respond.
		//int delay = 20;
		//boolean visual=true;
		//Single games with visuals
		//exec.runGame(new StarterPacMan(), new DummyGhosts(), visual, delay);
		//exec.runGame(new StarterPacMan(), new InfluenceMapGhosts(), visual, delay);
		//exec.runGame(new InfluenceMapPacMan(), new StarterGhosts(),visual,delay);
		//exec.runGame(new StarterPacMan(), new FlockingStrategyGhosts(new FlockingStrategy(FSConstants.NEIGHBOURHOODS, FSConstants.ACTOR_CONTEXT_MATRIX_MAGNITUDES)), visual, delay);
		
		//Long startTime = System.currentTimeMillis();

		//Multiple games without visuals
		//System.out.print(exec.runExperiment(new StarterPacMan(), new StarterGhosts(), NUM_EXPERIMENT_RUNS).toString());
		//System.out.print(exec.runExperiment(new InfluenceMapPacMan(), new StarterGhosts(), NUM_EXPERIMENT_RUNS).toString());
		//System.out.print(exec.runExperiment(new StarterPacMan(), new InfluenceMapGhosts(), NUM_EXPERIMENT_RUNS).toString());
		//System.out.print(exec.runExperiment(new InfluenceMapPacMan(), new InfluenceMapGhosts(), NUM_EXPERIMENT_RUNS).toString());
		//System.out.print(exec.runExperiment(new StarterPacMan(), new Legacy2TheReckoning(), NUM_EXPERIMENT_RUNS).toString());
		//System.out.print(exec.runExperiment(new InfluenceMapPacMan(), new Legacy2TheReckoning(), NUM_EXPERIMENT_RUNS).toString());
		//System.out.print(exec.runExperiment(new StarterPacMan(), new FlockingStrategyGhosts(new FlockingStrategy(FSConstants.NEIGHBOURHOODS, FSConstants.ACTOR_CONTEXT_MATRIX_MAGNITUDES)), NUM_EXPERIMENT_RUNS).toString());
		
		//Long endTime = System.currentTimeMillis();
		//System.out.println("Duration: " + (endTime - startTime));
		
		///*
		//run the game in asynchronous mode.
		//boolean visual=true;
		//		exec.runGameTimed(new NearestPillPacMan(),new AggressiveGhosts(),visual);
		//		exec.runGameTimed(new StarterPacMan(),new StarterGhosts(),visual);
		//		exec.runGameTimed(new StarterPacMan(),new DummyGhosts(),visual);
		//		exec.runGameTimed(new InfluenceMapPacman(),new DummyGhosts(),visual);
		//		exec.runGameTimed(new HumanController(new KeyBoardInput()),new StarterGhosts(),visual);	
		//*/

		/*
		//run the game in asynchronous mode but advance as soon as both controllers are ready  - this is the mode of the competition.
		//time limit of DELAY ms still applies.
		boolean visual=true;
		boolean fixedTime=false;
		exec.runGameTimedSpeedOptimised(new RandomPacMan(),new RandomGhosts(),fixedTime,visual);
		 */

		/*
		//run game in asynchronous mode and record it to file for replay at a later stage.
		boolean visual=true;
		String fileName="replay.txt";
		exec.runGameTimedRecorded(new HumanController(new KeyBoardInput()),new RandomGhosts(),visual,fileName);
		//exec.replayGame(fileName,visual);
		 */
	}

	/**
	 * Find and set the best pacman parameters
	 * @param exec
	 */
	private static void tuneIMPacmanParameters(Executor exec) 
	{
		for(int index = 0; index < IMConstants.PACMAN_PARAMETERS.size(); index++) 
		{
			String filePath = "data/IMapPacman/" + index + "_" + IMConstants.PACMAN_PARAMETERS.get(index).getParamEnum().toString() + ".txt";
			
			setBestParameterValue(exec, new InfluenceMapPacMan(), new StarterGhosts(), true, IMConstants.PACMAN_PARAMETERS.get(index).getParamEnum(), 
					IMConstants.PACMAN_PARAMETERS.get(index).getParamValues(), filePath);
		}
	}

	/**
	 * Find and set the best ghost parameters
	 * @param exec
	 */
	private static void tuneIMGhostParameters(Executor exec) 
	{
		for(int index = 0; index < IMConstants.GHOST_PARAMETERS.size(); index++) 
		{
			String filePath = "data/IMapGhost/" + index + "_" + IMConstants.GHOST_PARAMETERS.get(index).getParamEnum().toString() + ".txt";
			
			setBestParameterValue(exec, new StarterPacMan(), new InfluenceMapGhosts(), false, IMConstants.GHOST_PARAMETERS.get(index).getParamEnum(), 
					IMConstants.GHOST_PARAMETERS.get(index).getParamValues(), filePath);
		}		
	}

	/**
	 * Run experiments for each parameter value for a given parameter (enum) and then select the best value
	 * @param exec
	 * @param pacmanParam
	 * @param parameterValues
	 */
	private static void setBestParameterValue(Executor exec, Controller<MOVE> pacManController, Controller<EnumMap<GHOST,MOVE>> ghostController, 
			boolean tunePacman, IMAP_CONTROLLER_PARAMETER parameter, double[] parameterValues, String filePath) 
	{		
		//Default parameter value
		double bestParameterValue = IMConstants.getConstant(parameter);
		String s = parameter.toString() + " default: " + bestParameterValue + "\n";

		//Collect the experimental performance of each parameter
		ArrayList<ExperimentResult> parameterExperimentResults = new ArrayList<ExperimentResult>(parameterValues.length);
		ArrayList<double[]> allParameterRunScores = new ArrayList<double[]>(parameterValues.length);

		for(int i = 0; i < parameterValues.length; i++)
		{
			IMConstants.setConstant(parameter, parameterValues[i]);
			ExperimentResult parameterExperimentResult = exec.runExperiment(pacManController, ghostController, NUM_EXPERIMENT_RUNS);	

			parameterExperimentResults.add(parameterExperimentResult);
			allParameterRunScores.add(parameterExperimentResult.getRunScores());
		}

		//Calculate OneWayAnova Test Result for the experiment results
		//boolean anovaTestResult = oneWayAnova.anovaTest(allParameterRunScores, ALPHA);
		double annovaPValue = oneWayAnova.anovaPValue(allParameterRunScores);

		s += "Anova P value " + annovaPValue + "\n";
		
		//If there is a significant difference between groups i.e parameter value results
		if(annovaPValue < ALPHA)
		{
			//A better pacman parameter will yield a higher score
			if(tunePacman)
			{
				//To start off bestParameterExperimentResult will be the worst parameter experiment result
				ExperimentResult bestParameterExperimentResult = new ExperimentResult(-Double.MAX_VALUE, 0.0, new double[1]);;
				
				for(int i = 0; i < parameterExperimentResults.size(); i++) 
				{
					if(parameterExperimentResults.get(i).getAverageScore() > bestParameterExperimentResult.getAverageScore()) 
					{
						bestParameterValue = parameterValues[i];
						bestParameterExperimentResult = parameterExperimentResults.get(i);
					}
				}
			}
			//A better ghost parameter will yield a lower score
			else
			{
				//To start off bestParameterExperimentResult will be the worst parameter experiment result
				ExperimentResult bestParameterExperimentResult = new ExperimentResult(Double.MAX_VALUE, 0.0, new double[1]);;
				
				for(int i = 0; i < parameterExperimentResults.size(); i++) 
				{
					if(parameterExperimentResults.get(i).getAverageScore() < bestParameterExperimentResult.getAverageScore()) 
					{
						bestParameterValue = parameterValues[i];
						bestParameterExperimentResult = parameterExperimentResults.get(i);
					}
				}
			}
		}

		//Set the best parameter value
		IMConstants.setConstant(parameter, bestParameterValue);;

		//Save the data in a file
		s += "Set " + bestParameterValue + " as the best parameter value.\n";
		s += "\n//////////////////////////////////////////////////\n\n";

		for(int i = 0; i < parameterValues.length; i++) 
		{
			s += parameterValues[i] + "\n";
			s += parameterExperimentResults.get(i).toString();
			s += "-----------------------------------------------\n";
		}

		System.out.println(s);
		//saveToFile(s, filePath, true);
	}

	/**
	 * For running multiple games without visuals. This is useful to get a good idea of how well a controller plays
	 * against a chosen opponent: the random nature of the game means that performance can vary from game to game. 
	 * Running many games and looking at the average score (and standard deviation/error) helps to get a better
	 * idea of how well the controller is likely to do in the competition.
	 *
	 * @param pacManController The Pac-Man controller
	 * @param ghostController The Ghosts controller
	 * @param trials The number of trials to be executed
	 */
	public ExperimentResult runExperiment(Controller<MOVE> pacManController, Controller<EnumMap<GHOST,MOVE>> ghostController, int trials)
	{		
		//Average score in experiment
		double avgScore=0;

		//Stored score of each run
		double[] runScores = new double[trials];

		Random rnd=new Random(0);
		Game game;

		for(int i=0;i<trials;i++)
		{
			game=new Game(rnd.nextLong());

			while(!game.gameOver())
			{
				game.advanceGame(pacManController.getMove(game.copy(),System.currentTimeMillis()+DELAY),
						ghostController.getMove(game.copy(),System.currentTimeMillis()+DELAY));
			}

			avgScore += game.getScore();
			runScores[i] = game.getScore();

			//System.out.println(i+"\t"+game.getScore());
		}
		//System.out.println(avgScore/trials);

		//Return the ExperimentResult = Average score, standard deviation and the individual run scores
		return new ExperimentResult(avgScore/trials, standardDeviation.evaluate(runScores, avgScore/trials), runScores);
	}

	/**
	 * Run a game in asynchronous mode: the game waits until a move is returned. In order to slow thing down in case
	 * the controllers return very quickly, a time limit can be used. If fasted gameplay is required, this delay
	 * should be put as 0.
	 *
	 * @param pacManController The Pac-Man controller
	 * @param ghostController The Ghosts controller
	 * @param visual Indicates whether or not to use visuals
	 * @param delay The delay between time-steps
	 */
	public void runGame(Controller<MOVE> pacManController,Controller<EnumMap<GHOST,MOVE>> ghostController,boolean visual,int delay)
	{
		Game game=new Game(0);

		GameView gv=null;

		if(visual)
			gv=new GameView(game).showGame();

		while(!game.gameOver())
		{
			game.advanceGame(pacManController.getMove(game.copy(),-1),ghostController.getMove(game.copy(),-1));

			try{Thread.sleep(delay);}catch(Exception e){}

			if(visual)
				gv.repaint();
		}
	}

	/**
	 * Run the game with time limit (asynchronous mode). This is how it will be done in the competition. 
	 * Can be played with and without visual display of game states.
	 *
	 * @param pacManController The Pac-Man controller
	 * @param ghostController The Ghosts controller
	 * @param visual Indicates whether or not to use visuals
	 */
	public void runGameTimed(Controller<MOVE> pacManController,Controller<EnumMap<GHOST,MOVE>> ghostController,boolean visual)
	{
		Game game=new Game(0);

		GameView gv=null;

		if(visual)
			gv=new GameView(game).showGame();

		if(pacManController instanceof HumanController)
			gv.getFrame().addKeyListener(((HumanController)pacManController).getKeyboardInput());

		new Thread(pacManController).start();
		new Thread(ghostController).start();

		while(!game.gameOver())
		{
			pacManController.update(game.copy(),System.currentTimeMillis()+DELAY);
			ghostController.update(game.copy(),System.currentTimeMillis()+DELAY);

			try
			{
				Thread.sleep(DELAY);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}

			game.advanceGame(pacManController.getMove(),ghostController.getMove());	   

			if(visual)
				gv.repaint();
		}

		pacManController.terminate();
		ghostController.terminate();
	}

	/**
	 * Run the game in asynchronous mode but proceed as soon as both controllers replied. The time limit still applies so 
	 * so the game will proceed after 40ms regardless of whether the controllers managed to calculate a turn.
	 *     
	 * @param pacManController The Pac-Man controller
	 * @param ghostController The Ghosts controller
	 * @param fixedTime Whether or not to wait until 40ms are up even if both controllers already responded
	 * @param visual Indicates whether or not to use visuals
	 */
	public void runGameTimedSpeedOptimised(Controller<MOVE> pacManController,Controller<EnumMap<GHOST,MOVE>> ghostController,boolean fixedTime,boolean visual)
	{
		Game game=new Game(0);

		GameView gv=null;

		if(visual)
			gv=new GameView(game).showGame();

		if(pacManController instanceof HumanController)
			gv.getFrame().addKeyListener(((HumanController)pacManController).getKeyboardInput());

		new Thread(pacManController).start();
		new Thread(ghostController).start();

		while(!game.gameOver())
		{
			pacManController.update(game.copy(),System.currentTimeMillis()+DELAY);
			ghostController.update(game.copy(),System.currentTimeMillis()+DELAY);

			try
			{
				int waited=DELAY/INTERVAL_WAIT;

				for(int j=0;j<DELAY/INTERVAL_WAIT;j++)
				{
					Thread.sleep(INTERVAL_WAIT);

					if(pacManController.hasComputed() && ghostController.hasComputed())
					{
						waited=j;
						break;
					}
				}

				if(fixedTime)
					Thread.sleep(((DELAY/INTERVAL_WAIT)-waited)*INTERVAL_WAIT);

				game.advanceGame(pacManController.getMove(),ghostController.getMove());	
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}

			if(visual)
				gv.repaint();
		}

		pacManController.terminate();
		ghostController.terminate();
	}

	/**
	 * Run a game in asynchronous mode and recorded.
	 *
	 * @param pacManController The Pac-Man controller
	 * @param ghostController The Ghosts controller
	 * @param visual Whether to run the game with visuals
	 * @param fileName The file name of the file that saves the replay
	 */
	public void runGameTimedRecorded(Controller<MOVE> pacManController,Controller<EnumMap<GHOST,MOVE>> ghostController,boolean visual,String fileName)
	{
		StringBuilder replay=new StringBuilder();

		Game game=new Game(0);

		GameView gv=null;

		if(visual)
		{
			gv=new GameView(game).showGame();

			if(pacManController instanceof HumanController)
				gv.getFrame().addKeyListener(((HumanController)pacManController).getKeyboardInput());
		}		

		new Thread(pacManController).start();
		new Thread(ghostController).start();

		while(!game.gameOver())
		{
			pacManController.update(game.copy(),System.currentTimeMillis()+DELAY);
			ghostController.update(game.copy(),System.currentTimeMillis()+DELAY);

			try
			{
				Thread.sleep(DELAY);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}

			game.advanceGame(pacManController.getMove(),ghostController.getMove());	        

			if(visual)
				gv.repaint();

			replay.append(game.getGameState()+"\n");
		}

		pacManController.terminate();
		ghostController.terminate();

		saveToFile(replay.toString(),fileName,false);
	}

	/**
	 * Replay a previously saved game.
	 *
	 * @param fileName The file name of the game to be played
	 * @param visual Indicates whether or not to use visuals
	 */
	public void replayGame(String fileName,boolean visual)
	{
		ArrayList<String> timeSteps=loadReplay(fileName);

		Game game=new Game(0);

		GameView gv=null;

		if(visual)
			gv=new GameView(game).showGame();

		for(int j=0;j<timeSteps.size();j++)
		{			
			game.setGameState(timeSteps.get(j));

			try
			{
				Thread.sleep(DELAY);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			if(visual)
				gv.repaint();
		}
	}

	//save file for replays
	public static void saveToFile(String data,String name,boolean append)
	{
		try 
		{
			FileOutputStream outS=new FileOutputStream(name,append);
			PrintWriter pw=new PrintWriter(outS);

			pw.println(data);
			pw.flush();
			outS.close();

		} 
		catch (IOException e)
		{
			System.out.println("Could not save data!");	
		}
	}  

	//load a replay
	private static ArrayList<String> loadReplay(String fileName)
	{
		ArrayList<String> replay=new ArrayList<String>();

		try
		{         	
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));	 
			String input=br.readLine();		

			while(input!=null)
			{
				if(!input.equals(""))
					replay.add(input);

				input=br.readLine();	
			}
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}

		return replay;
	}
}
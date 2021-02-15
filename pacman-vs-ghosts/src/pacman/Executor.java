package pacman;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import pacman.entries.ghosts.InfluenceMapGhosts;
import pacman.entries.pacman.InfluenceMapPacMan;
import pacman.game.Game;
import pacman.game.GameView;
import pacman.influencemap.IMConstants;
import pacman.influencemap.IMConstants.IMGhostParams;
import pacman.influencemap.IMConstants.IMPacmanParams;

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
	private final static int NUM_EXPERIMENT_RUNS = 60;
	
	/**
	 * The main method. Several options are listed - simply remove comments to use the option you want.
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args)
	{
		Executor exec = new Executor();

		//Pacman parameter tuning ordered from first to last
		//1st
		//fixBestPacmanParameterValue(exec, IMPacmanParams.ENUM_FORCE_DIRECTION_COUNT, new double[] {0.0, 15.0, 30.0, 45.0, 60.0, 75.0, 90.0, 115.0, 130.0});		
		//2nd
		//fixBestPacmanParameterValue(exec, IMPacmanParams.ENUM_INFLUENCE_OF_GHOST, new double[] {-65.0, -0.75, -80.0, -0.85, -95.0});		
		//3rd
		//fixBestPacmanParameterValue(exec, IMPacmanParams.ENUM_INFLUENCE_OF_EDIBLE_GHOST, new double[] {40.0, 50.0, 60.0, 70.0, 80.0});
		//4th
		//fixBestPacmanParameterValue(exec, IMPacmanParams.ENUM_INFLUENCE_OF_POWERPILL, new double[] {20.0, 25.0, 30.0, 35.0, 40.0});		
		//5th
		//fixBestPacmanParameterValue(exec, IMPacmanParams.ENUM_INFLUENCE_OF_FREEDOM_OF_CHOICE, new double[] {20.0, 40.0, 50.0, 60.0, 70.0, 80.0, 100.0});
		//6th
		//fixBestPacmanParameterValue(exec, IMPacmanParams.ENUM_INFLUENCE_OF_PILL, new double[] {2.0, 4.0, 6.0, 8.0});
		//7th
		//fixBestPacmanParameterValue(exec, IMPacmanParams.ENUM_INFLUENCE_FACTOR_OF_GHOST, new double[] {0.8, 0.85, 0.9, 0.95});
		//8th
		//fixBestPacmanParameterValue(exec, IMPacmanParams.ENUM_INFLUENCE_FACTOR_OF_EDIBLE_GHOST, new double[] {0.8, 0.85, 0.9, 0.95});
		//9th
		//fixBestPacmanParameterValue(exec, IMPacmanParams.ENUM_POWERPILL_DISTANCE_THRESHOLD_PER_GHOST, new double[] {90.0, 100.0, 110.0, 120.0});
		//10th
		//fixBestPacmanParameterValue(exec, IMPacmanParams.ENUM_INFLUENCE_FACTOR_OF_POWERPILL, new double[] {0.8, 0.85, 0.9, 0.95});		
		//11th
		//fixBestPacmanParameterValue(exec, IMPacmanParams.ENUM_INFLUENCE_OF_FREEDOM_OF_CHOICE_THRESHOLD, new double[] {0.0, -15.0, -25.0, -30.0, -35.0 -45.0, -60.0});
		//12th
		//fixBestPacmanParameterValue(exec, IMPacmanParams.ENUM_INFLUENCE_FACTOR_OF_FREEDOM_OF_CHOICE, new double[] {0.8, 0.85, 0.9, 0.95});
		//13th
		//fixBestPacmanParameterValue(exec, IMPacmanParams.ENUM_INFLUENCE_FACTOR_OF_PILL, new double[] {0.8, 0.85, 0.9, 0.95});
		
		
		//Ghost parameter tuning ordered from first to last		
		//1st
		//fixBestGhostParameterValue(exec, IMGhostParams.ENUM_INFLUENCE_OF_PACMAN, new double[] {50.0, 100.0, 125.0, 150.0, 175.0, 200.0, 250.0, 300.0});
		//2nd
		//fixBestGhostParameterValue(exec, IMGhostParams.ENUM_POWER_PILL_DISTANCE_FACTOR, new double[] {5.0, 10.0, 15.0, 20.0, 25.0, 30.0, 35.0, 40.0});
		//3rd
		//fixBestGhostParameterValue(exec, IMGhostParams.ENUM_POWER_PILL_THRESHOLD, new double[] {0.75, 0.8, 0.85, 0.9, 0.95});		
		//4th
		//fixBestGhostParameterValue(exec, IMGhostParams.ENUM_INFLUENCE_GHOST_WEIGHT, new double[] {-5.0, -15.0, -20.0, -22.5, -25.0, -27.5, -30.0});
		//5th
		//fixBestGhostParameterValue(exec, IMGhostParams.ENUM_INFLUENCE_FACTOR_OF_PACMAN, new double[] {0.75, 0.8, 0.85, 0.9, 0.95});
		//6th
		//fixBestGhostParameterValue(exec, IMGhostParams.ENUM_INFLUENCE_FACTOR_OF_GHOST_WEIGHT, new double[] {0.75, 0.8, 0.85, 0.9, 0.95});
		//7th
		//fixBestGhostParameterValue(exec, IMGhostParams.ENUM_LIMITING_INFLUENCE_OF_PACMAN, new double[] {-10.0, -5.0, 0.0, 5.0, 10.0, 20.0, 30.0, 40.0});		

		/*
		//run multiple games in batch mode - good for testing.
		int numTrials=10;
		exec.runExperiment(new RandomPacMan(),new RandomGhosts(),numTrials);
		 */

		/*
		 */
		//run a game in synchronous mode: game waits until controllers respond.
		//int delay = 30;
		boolean visual=true;
		//exec.runGame(new InfluenceMapPacMan(), new StarterGhosts(),visual,delay);
		//exec.runExperiment(new InfluenceMapPacMan(), new StarterGhosts(), 30);

		//exec.runGame(new StarterPacMan(), new InfluenceMapGhosts(),visual,delay);
		//exec.runExperiment(new StarterPacMan(), new InfluenceMapGhosts(), 60);

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
	 * Run experiments for each parameter value for a given parameter (enum) and then select the best value
	 * @param exec
	 * @param pacmanParam
	 * @param paramValues
	 */
	private static void fixBestPacmanParameterValue(Executor exec, IMPacmanParams pacmanParam, double[] paramValues) 
	{
		double bestParamValue = IMConstants.getPacmanConstant(pacmanParam);
		double bestParamPerformanceValue = exec.runExperiment(new InfluenceMapPacMan(), new StarterGhosts(), NUM_EXPERIMENT_RUNS);
		
		System.out.println("Default: " + bestParamValue);
		
		//Find the best pacman parameter value
		for(int i = 0; i < paramValues.length; i++)
		{
			IMConstants.setPacmanConstant(pacmanParam, paramValues[i]);;
			double newParamPerformanceValue = exec.runExperiment(new InfluenceMapPacMan(), new StarterGhosts(), NUM_EXPERIMENT_RUNS);
			
			//Better performance for pacman is greater than current performance
			//System.out.println(paramValues[i] + " < " + bestParamValue);
			//System.out.println(newParamPerformanceValue + " < " + bestParamPerformanceValue);
			//System.out.println(newParamPerformanceValue < bestParamPerformanceValue);
			//System.out.println();
			if(newParamPerformanceValue > bestParamPerformanceValue)
			{
				bestParamValue = paramValues[i];
				bestParamPerformanceValue = newParamPerformanceValue;
			}
		}
		
		//Set the best pacman parameter value
		IMConstants.setPacmanConstant(pacmanParam, bestParamValue);;
		System.out.println(pacmanParam.toString() + ": Set " + bestParamValue + " as best parameter value.");
	}
	
	/**
	 * Run experiments for each parameter value for a given parameter (enum) and then select the best value
	 * @param exec
	 * @param ghostParam
	 * @param paramValues
	 */
	private static void fixBestGhostParameterValue(Executor exec, IMGhostParams ghostParam, double[] paramValues) 
	{
		double bestParamValue = IMConstants.getGhostConstant(ghostParam);
		double bestParamPerformanceValue = exec.runExperiment(new StarterPacMan(), new InfluenceMapGhosts(), NUM_EXPERIMENT_RUNS);
		
		System.out.println("Default: " + bestParamValue);
		
		//Find the best ghost parameter value
		for(int i = 0; i < paramValues.length; i++)
		{
			IMConstants.setGhostConstant(ghostParam, paramValues[i]);
			double newParamPerformanceValue = exec.runExperiment(new StarterPacMan(), new InfluenceMapGhosts(), NUM_EXPERIMENT_RUNS);
			
			//Better performance for ghosts is less than current performance
			//System.out.println(paramValues[i] + " < " + bestParamValue);
			//System.out.println(newParamPerformanceValue + " < " + bestParamPerformanceValue);
			//System.out.println(newParamPerformanceValue < bestParamPerformanceValue);
			//System.out.println();
			if(newParamPerformanceValue < bestParamPerformanceValue)
			{
				bestParamValue = paramValues[i];
				bestParamPerformanceValue = newParamPerformanceValue;
			}
		}
		
		//Set the best ghost parameter value
		IMConstants.setGhostConstant(ghostParam, bestParamValue);
		System.out.println(ghostParam.toString() + ": Set " + bestParamValue + " as best parameter value.");
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
	public double runExperiment(Controller<MOVE> pacManController,Controller<EnumMap<GHOST,MOVE>> ghostController,int trials)
	{
		double avgScore=0;

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

			avgScore+=game.getScore();
			//System.out.println(i+"\t"+game.getScore());
		}

		//System.out.println(avgScore/trials);
		return avgScore/trials;
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
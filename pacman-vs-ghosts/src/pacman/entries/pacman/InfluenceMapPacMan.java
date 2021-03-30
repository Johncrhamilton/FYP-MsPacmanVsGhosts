package pacman.entries.pacman;

import java.util.Map.Entry;

import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Node;
import pacman.influencemap.IMConstants;
import pacman.influencemap.InfluenceMap;

/*
 * InfluenceMapPacMan, is a Ms. Pacman Controller that follows an Influence Map-based approach where game entities propagate their influences through the maze and
 * pacman can then choose directions that have the best influence.
 */
public class InfluenceMapPacMan extends Controller<MOVE>
{
	private double forceDirectionCount = 0;
	
	public MOVE getMove(Game game, long timeDue) 
	{
		//Setup Influence Map
		InfluenceMap.getInstance(game);
		
		//Generate influences
		InfluenceMap.generateMsPacmanInfluenceMap(game);
		
		//Get best Move from IMap
		MOVE move = getBestMove(game);
		
		//Force direction if needed
		if(forceDirectionCount > 0)
		{
			forceDirectionCount--;
		} 
		else if(move == game.getPacmanLastMoveMade().opposite()) 
		{
			forceDirectionCount = IMConstants.FORCE_DIRECTION_COUNT;
		}
		
		return move;
	}
	
	/**
	 * Return Move for Ms. Pacman that leads to the node with the highest influence
	 * @param game
	 * @return Move
	 */
	private MOVE getBestMove(Game game) 
	{		
		MOVE move = MOVE.NEUTRAL;
		double highestNodeInfluence = -Double.MAX_VALUE;
		
		Node currentPacmanMazeNode = game.getCurrentMaze().graph[game.getPacmanCurrentNodeIndex()];
		
		//Check each node's influence around Ms. Pacman's current node index
		for(Entry<MOVE,Integer> entry : currentPacmanMazeNode.neighbourhood.entrySet()) 
		{
			if(forceDirectionCount > 0 && entry.getKey() == game.getPacmanLastMoveMade().opposite()) 
			{
				continue;
			}
			
			double nodeInfluence = InfluenceMap.getPacmanInfluenceNodes().get(entry.getValue()).getInfluence();
			
			if(nodeInfluence > highestNodeInfluence)
			{
				highestNodeInfluence = nodeInfluence;
				move = entry.getKey();
			}
		}

		return move;
	}
}

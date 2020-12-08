package pacman.entries.pacman;

import java.util.Map.Entry;

import pacman.controllers.Controller;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Node;

import pacman.influencemap.IMConstants;
import pacman.influencemap.InfluenceMap;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class InfluenceMapPacMan extends Controller<MOVE>
{
	private int forceDirectionCount = 0;
	
	public MOVE getMove(Game game, long timeDue) 
	{
		InfluenceMap.getInstance(game);
		InfluenceMap.generateMsPacmanInfluenceMap(game);
		
		MOVE move = getBestMove(game);
		
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

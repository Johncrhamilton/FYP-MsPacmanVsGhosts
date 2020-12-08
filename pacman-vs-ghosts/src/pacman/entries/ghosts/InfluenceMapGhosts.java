package pacman.entries.ghosts;

import java.util.EnumMap;
import java.util.Map.Entry;

import pacman.controllers.Controller;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.internal.Node;
import pacman.influencemap.InfluenceMap;

public class InfluenceMapGhosts extends Controller<EnumMap<GHOST,MOVE>> 
{
	private EnumMap<GHOST, MOVE> myMoves = new EnumMap<GHOST, MOVE>(GHOST.class);

	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue)
	{
		myMoves.clear();

		InfluenceMap.getInstance(game);
		InfluenceMap.generateGhostsInfluenceMap(game);

		for(GHOST ghost : GHOST.values()) 
		{			
			if(game.doesGhostRequireAction(ghost)) 
			{
				MOVE move = getBestMove(game, ghost);
				myMoves.put(ghost, move);			
			}
		}

		return myMoves;
	}

	/**
	 * Return Move for Ghost that leads to the node with the highest influence
	 * @param game
	 * @return Move
	 */
	private MOVE getBestMove(Game game, GHOST ghost) 
	{		
		MOVE move = MOVE.NEUTRAL;
		double highestNodeInfluence = -Double.MAX_VALUE;

		Node currentGhostMazeNode = game.getCurrentMaze().graph[game.getGhostCurrentNodeIndex(ghost)];

		//Check each node's influence around Ghost's current node index
		for(Entry<MOVE,Integer> entry : currentGhostMazeNode.allNeighbourhoods.get(game.getGhostLastMoveMade(ghost)).entrySet()) 
		{
			double nodeInfluence = InfluenceMap.getGhostInfluenceNodes().get(entry.getValue()).getInfluence();

			if(nodeInfluence > highestNodeInfluence)
			{
				highestNodeInfluence = nodeInfluence;
				move = entry.getKey();
			}
		}

		return move;
	}
}
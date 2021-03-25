package pacman.entries.ghosts;

import java.util.EnumMap;

import pacman.game.Game;
import pacman.strategy.flocking.FlockingStrategy;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class FlockingStrategyGhosts {

	private EnumMap<GHOST, MOVE> myMoves = new EnumMap<GHOST, MOVE>(GHOST.class);
	
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue)
	{
		myMoves.clear();
		
		for(GHOST ghost : GHOST.values())
		{
			if(game.doesGhostRequireAction(ghost)) 
			{
				myMoves.put(ghost, FlockingStrategy.highestSteeringForceMove(game, ghost));
			}
		}

		return myMoves;
	}
}

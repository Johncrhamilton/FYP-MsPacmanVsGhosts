package pacman.entries.ghosts;

import java.util.ArrayList;
import java.util.EnumMap;

import pacman.controllers.Controller;
import pacman.game.Game;
import pacman.strategy.flocking.FlockingStrategy;
import pacman.strategy.flocking.FSConstants;
import pacman.strategy.flocking.FSConstants.GHOST_STATE;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class FlockingStrategyGhosts extends Controller<EnumMap<GHOST,MOVE>> {

	private EnumMap<GHOST, MOVE> myMoves = new EnumMap<GHOST, MOVE>(GHOST.class);
	private ArrayList<FlockingStrategy> flockingStrategies;

	public FlockingStrategyGhosts(ArrayList<FlockingStrategy> flockingStrategies) 
	{
		super();
		this.flockingStrategies = flockingStrategies;
	}

	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue)
	{
		myMoves.clear();

		for(GHOST ghost : GHOST.values())
		{
			if(game.doesGhostRequireAction(ghost)) 
			{
				GHOST_STATE ghostState;

				//Hunter Ghost State
				if(!game.isGhostEdible(ghost)) 
				{
					ghostState = GHOST_STATE.HUNTER;
				}
				//Hunted Ghost State
				else if(game.getGhostEdibleTime(ghost) > 30) 
				{
					ghostState = GHOST_STATE.HUNTED;				
				}
				//Flashing Ghost State
				else
				{
					ghostState = GHOST_STATE.FLASH;
				}
				
				FlockingStrategy selectedFlockingStrategy;

				if(FSConstants.HOMOGENEOUS_GHOSTS) 
				{
					//Get the strategy that applies to all ghosts
					selectedFlockingStrategy = flockingStrategies.get(0);
				}
				else
				{
					//Get this specific ghost's strategy
					selectedFlockingStrategy = flockingStrategies.get(ghost.ordinal());					
				}
				
				myMoves.put(ghost, selectedFlockingStrategy.steeringForceMove(game, ghost, ghostState));
			}
		}

		return myMoves;
	}
}

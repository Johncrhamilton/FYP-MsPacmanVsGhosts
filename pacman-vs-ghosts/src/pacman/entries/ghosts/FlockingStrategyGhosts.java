package pacman.entries.ghosts;

import java.util.EnumMap;

import pacman.controllers.Controller;
import pacman.game.Game;
import pacman.strategy.flocking.FSConstants;
import pacman.strategy.flocking.FlockingStrategy;
import pacman.strategy.flocking.FSConstants.GHOST_STATE;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class FlockingStrategyGhosts extends Controller<EnumMap<GHOST,MOVE>> {

	private EnumMap<GHOST, MOVE> myMoves = new EnumMap<GHOST, MOVE>(GHOST.class);
	private FlockingStrategy FS;
	
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue)
	{
		//If the flocking strategy hasn't been created
		if(FS == null)
		{       
			FS = new FlockingStrategy(FSConstants.ACTOR_CONTEXT_MATRIX_MAGNITUDES);
		}
		
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
				myMoves.put(ghost, FS.steeringForceMove(game, ghost, ghostState));
			}
		}

		return myMoves;
	}
}

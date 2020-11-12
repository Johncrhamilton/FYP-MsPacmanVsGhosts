package pacman.entries.ghosts;

import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getActions() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.ghosts.mypackage).
 */
public class DummyGhosts extends Controller<EnumMap<GHOST,MOVE>>
{
	private static final float MOVE_CONSISTENCY = 0.9f;
	private static final int POWERPILL_THREAT_DISTANCE = 25;

	private EnumMap<GHOST, MOVE> myMoves = new EnumMap<GHOST, MOVE>(GHOST.class);
	private MOVE[] moves = MOVE.values();

	private Random random = new Random();

	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue)
	{
		myMoves.clear();

		for(GHOST ghost : GHOST.values()) 
		{			
			if(game.doesGhostRequireAction(ghost)) 
			{
				//If Ms. Pacman is close to a Power Pill OR ghost is edible THEN retreat.
				if(pacmanCloseToPowerPill(game) || game.isGhostEdible(ghost)) 
				{
					myMoves.put(ghost, game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghost), 
							game.getPacmanCurrentNodeIndex(), 
							game.getGhostLastMoveMade(ghost), 
							DM.PATH));

					continue;
				}

				//If Ms. Pacman is not close to Power Pill AND ghost is consistent with its move THEN the ghost is on the offensive
				if(!pacmanCloseToPowerPill(game) && random.nextFloat() < MOVE_CONSISTENCY)
				{
					myMoves.put(ghost, game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), 
							game.getPacmanCurrentNodeIndex(), 
							game.getGhostLastMoveMade(ghost), 
							DM.PATH));

					continue;
				}

				//If true move randomly
				if(true) 
				{
					myMoves.put(ghost, moves[random.nextInt(moves.length)]);

					continue;
				}				
			}
		}

		return myMoves;
	}

	/**
	 * 
	 * Check whether Ms. Pacman is close to any power pill given a distance threshold
	 * @param game
	 * @return boolean
	 */
	private boolean pacmanCloseToPowerPill(Game game) 
	{
		int[] powerPillsIndices = game.getActivePowerPillsIndices();
		int pacmanCurrentNodeIndex = game.getPacmanCurrentNodeIndex();

		for(int i = 0; i < powerPillsIndices.length; i++) 
		{
			if(game.getShortestPathDistance(pacmanCurrentNodeIndex, powerPillsIndices[i]) < POWERPILL_THREAT_DISTANCE)
			{
				return true;
			}
		}

		return false;
	}
}
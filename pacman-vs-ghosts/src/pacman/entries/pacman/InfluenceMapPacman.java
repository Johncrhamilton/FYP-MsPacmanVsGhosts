package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.influencemap.InfluenceMap;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class InfluenceMapPacman extends Controller<MOVE>
{
	private InfluenceMap influenceMap;

	public MOVE getMove(Game game, long timeDue) 
	{		
		influenceMap = new InfluenceMap(game);
		
		int currentPacmanIndex = game.getPacmanCurrentNodeIndex();
		
		return influenceMap.getBestMoveMsPacman(currentPacmanIndex);
	}
}

package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.influencemap.IMConstants;
import pacman.influencemap.InfluenceMap;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class InfluenceMapPacMan extends Controller<MOVE>
{
	private MOVE move;	
	private int forceDirectionCount = 0;
	
	public MOVE getMove(Game game, long timeDue) 
	{
		InfluenceMap.getInstance(game);
		InfluenceMap.generateMsPacmanInfluenceMap(game);
		
		move = InfluenceMap.getBestMoveMsPacman(game, forceDirectionCount);		
		InfluenceMap.clearInfluences();
		
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
}

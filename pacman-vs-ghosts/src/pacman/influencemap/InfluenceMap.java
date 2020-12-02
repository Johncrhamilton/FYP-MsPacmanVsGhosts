package pacman.influencemap;

import java.util.HashMap;
import java.util.Map.Entry;

import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.internal.Node;

public class InfluenceMap {

	private static InfluenceMap INSTANCE;
	private static HashMap<Integer, InfluenceNode> influenceNodes;
	private static Node[] graph;

	private InfluenceMap(Game game)
	{	
		graph = game.getCurrentMaze().graph;

		influenceNodes = new HashMap<Integer, InfluenceNode>(graph.length);

		//Create the mapping of NodeIndexes to InfluenceNodes
		for(int i = 0; i < graph.length; i++) 
		{
			influenceNodes.put(graph[i].nodeIndex, new InfluenceNode(graph[i]));
		}
	}

	/**
	 * Get instance of the InfluenceMap
	 * @param game
	 * @return
	 */
	public static InfluenceMap getInstance(Game game) 
	{
		if(INSTANCE == null)
		{
			INSTANCE = new InfluenceMap(game);
		}
		//Recreate the InfluenceMap structure when maze changes
		else if(game.getCurrentMaze().graph != graph) 
		{
			INSTANCE = new InfluenceMap(game);		
		}

		return INSTANCE;
	}

	/**
	 * Generate the Ms. Pacman Influence map
	 * @param game
	 */
	public static void generateMsPacmanInfluenceMap(Game game) 
	{	
		//Calculate Influence of InfluenceNodes that have Pills
		int[] activePillsIndices = game.getActivePillsIndices();
		for(int i = 0; i < activePillsIndices.length; i++)
		{
			influenceNodes.get(activePillsIndices[i]).updatePillInfluence(game, influenceNodes, activePillsIndices[i]);
		}

		//Calculate Influence of InfluenceNodes that have ghosts
		for(GHOST ghost : GHOST.values())
		{
			//Ghost outside lair
			if(game.getGhostLairTime(ghost) == 0) 
			{
				if(!game.isGhostEdible(ghost))
				{
					influenceNodes.get(game.getGhostCurrentNodeIndex(ghost)).updateGhostInfluence(game, influenceNodes, ghost);
				}
				else
				{	
					influenceNodes.get(game.getGhostCurrentNodeIndex(ghost)).updateEdibleGhostInfluence(game, influenceNodes, ghost);
				}
			}
		}

		//Calculate Influence of the InfluenceNode that has the closest Power Pill
		int closestPowerPillIndex = closestPowerPillIndexToMsPacman(game);
		if(closestPowerPillIndex != -1)
		{
			influenceNodes.get(closestPowerPillIndex).updatePowerPillInfluence(game, influenceNodes, closestPowerPillIndex, isPowerPillAttractive(game, closestPowerPillIndex));
		}

		//Calculate Influence of the InfluenceNode that has the closest Junction under the right conditions
		int[] allNeighbouringNodeIndexes = influenceNodes.get(game.getPacmanCurrentNodeIndex()).getMazeNode().allNeighbouringNodes.get(MOVE.NEUTRAL);

		//If in corridor
		if(allNeighbouringNodeIndexes.length == 2) 
		{
			double summedInfluence = influenceNodes.get(allNeighbouringNodeIndexes[0]).getInfluence() + influenceNodes.get(allNeighbouringNodeIndexes[0]).getInfluence();

			//If summed influence of the way forward and back is below threshold
			if(summedInfluence < IMConstants.INFLUENCE_OF_FREEDOM_OF_CHOICE_THRESHOLD)
			{
				int closestJunctionIndex = closestJunctionIndexToMsPacman(game);
				
				if(closestJunctionIndex != -1 && closestJunctionIndex != game.getPacmanCurrentNodeIndex()) 
				{
					influenceNodes.get(closestJunctionIndex).updateFreedomOfChoiceInfluence(game, influenceNodes, closestJunctionIndex);
				}
			}
		}
	}

	/**
	 * Return Move that leads to node with the highest influence
	 * @param currentPacmanIndex
	 * @return Move
	 */
	public static MOVE getBestMoveMsPacman(Game game, int forceDirectionCount) 
	{		
		MOVE move = MOVE.NEUTRAL;
		double highestNodeInfluence = -Double.MAX_VALUE;
		
		//Check each node's influence around Ms. Pacman's current node index
		for(Entry<MOVE,Integer> entry : influenceNodes.get(game.getPacmanCurrentNodeIndex()).getMazeNode().neighbourhood.entrySet()) 
		{
			if(forceDirectionCount > 0 && entry.getKey() == game.getPacmanLastMoveMade().opposite()) 
			{
				continue;
			}

			double nodeInfluence = influenceNodes.get(entry.getValue()).getInfluence();
			
			if(nodeInfluence > highestNodeInfluence)
			{
				highestNodeInfluence = nodeInfluence;
				move = entry.getKey();
			}
		}

		return move;
	}

	/**
	 * Find the closest Power Pill index to Ms. Pacman
	 * @param game
	 * @return boolean
	 */
	private static int closestPowerPillIndexToMsPacman(Game game) 
	{
		int[] powerPillsIndices = game.getActivePowerPillsIndices();
		int pacmanCurrentNodeIndex = game.getPacmanCurrentNodeIndex();

		int closestPowerPillIndex = -1;
		int closestPowerPillDistance = Integer.MAX_VALUE;

		for(int i = 0; i < powerPillsIndices.length; i++) 
		{
			int distanceToPowerPill = game.getShortestPathDistance(pacmanCurrentNodeIndex, powerPillsIndices[i]);
			if(distanceToPowerPill < closestPowerPillDistance)
			{
				closestPowerPillIndex = powerPillsIndices[i];
				closestPowerPillDistance = distanceToPowerPill;
			}
		}

		return closestPowerPillIndex;
	}

	/**
	 * Find the closest Junction index to Ms. Pacman
	 * @param game
	 * @return boolean
	 */
	private static int closestJunctionIndexToMsPacman(Game game) 
	{
		int[] junctionIndices = game.getJunctionIndices();
		int pacmanCurrentNodeIndex = game.getPacmanCurrentNodeIndex();

		int closestJunctionIndex = -1;
		int closestJunctionDistance = Integer.MAX_VALUE;

		for(int i = 0; i < junctionIndices.length; i++) 
		{
			int distanceToJunction = game.getShortestPathDistance(pacmanCurrentNodeIndex, junctionIndices[i]);
			
			//If closer junction but Ms. Pacman isn't on this junction
			if(distanceToJunction < closestJunctionDistance)
			{
				closestJunctionIndex = junctionIndices[i];
				closestJunctionDistance = distanceToJunction;
			}
		}
		
		return closestJunctionIndex;
	}

	/**
	 * Determine whether a power pill is attractive to Ms. Pacman considering ghost distances to power pill
	 * @param powerPillIndex
	 * @return boolean
	 */
	private static boolean isPowerPillAttractive(Game game, int powerPillIndex) 
	{
		double sumOfGhostDistancesToPowerPill = 0.0;
		int activeGhosts = 0;

		for(GHOST ghost : GHOST.values()) 
		{
			if(game.getGhostLairTime(ghost) == 0 && !game.isGhostEdible(ghost)) 
			{
				sumOfGhostDistancesToPowerPill += game.getShortestPathDistance(powerPillIndex, game.getGhostCurrentNodeIndex(ghost));
				activeGhosts++;
			}
		}

		//If the sum of all ghost distances are within threshold this power pill is Attractive
		if(activeGhosts >= 3)
		{
			if(sumOfGhostDistancesToPowerPill < IMConstants.POWERPILL_DISTANCE_THRESHOLD_PER_GHOST * activeGhosts) 
			{
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Clear Influences
	 */
	public static void clearInfluences() 
	{
		//Clear old influences
		for(InfluenceNode influenceNode : influenceNodes.values()) 
		{
			influenceNode.clearInfluence();
		}		
	}

	/**
	 * Get the InfluenceNode Mappings
	 * @return InfluenceNodes
	 */
	public static HashMap<Integer, InfluenceNode> getInfluenceNodes() 
	{
		return influenceNodes;
	}

}

package pacman.influencemap;

import java.util.HashMap;
import java.util.Map.Entry;

import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.internal.Maze;
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
			influenceNodes.put(graph[i].nodeIndex, new InfluenceNode(game, graph[i]));
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
		//Clear old influences
		for(InfluenceNode influenceNode : influenceNodes.values()) 
		{
			influenceNode.clearInfluence();						
		}				
		
		//Calculate Influence of InfluenceNodes that have Pills
		int[] activePillsIndices = game.getActivePillsIndices();
		for(int i = 0; i < activePillsIndices.length; i++)
		{
			influenceNodes.get(activePillsIndices[i]).updatePillInfluence(influenceNodes, activePillsIndices[i]);
		}

		//Calculate Influence of InfluenceNodes that have ghosts
		for(GHOST ghost : GHOST.values())
		{
			if(!game.isGhostEdible(ghost))
			{
				influenceNodes.get(game.getGhostCurrentNodeIndex(ghost)).updateGhostInfluence(influenceNodes, ghost);
			}
			else 
			{	
				influenceNodes.get(game.getGhostCurrentNodeIndex(ghost)).updateEdibleGhostInfluence(influenceNodes, game.getGhostCurrentNodeIndex(ghost));	
			}
		}

		//Calculate Influence of the InfluenceNode that has the closest Power Pill
		int closestPowerPillIndex = closestPowerPillIndexToMsPacman(game);
		if(closestPowerPillIndex != -1) 
		{
			influenceNodes.get(closestPowerPillIndex).updatePowerPillInfluence(influenceNodes, closestPowerPillIndex, isPowerPillAttractive(game, closestPowerPillIndex));
		}

		//Calculate Influence of the InfluenceNode that has the closest Junction under the right conditions
		int[] allNeighbouringNodeIndexes = influenceNodes.get(game.getPacmanCurrentNodeIndex()).getMazeNode().allNeighbouringNodes.get(MOVE.NEUTRAL);
		double summedInfluence = influenceNodes.get(allNeighbouringNodeIndexes[0]).getInfluence() + influenceNodes.get(allNeighbouringNodeIndexes[0]).getInfluence();
		
		//If in corridor and the summed influence of the way forward and back is below threshold
		if(allNeighbouringNodeIndexes.length == 2 && summedInfluence < IMapConstants.INFLUENCE_OF_FREEDOM_OF_CHOICE) 
		{
			int closestJunctionIndex = closestJunctionIndexToMsPacman(game);
			if(closestJunctionIndex != -1) 
			{
				influenceNodes.get(closestJunctionIndex).updateFreedomOfChoiceInfluence(influenceNodes, closestJunctionIndex);
			}
		}	
	}

	/**
	 * Return Move that leads to node with the highest influence
	 * @param currentPacmanIndex
	 * @return Move
	 */
	public static MOVE getBestMoveMsPacman(int currentPacmanIndex) 
	{		
		MOVE move = MOVE.NEUTRAL;

		double highestNodeInfluence = -Double.MAX_VALUE;

		//Check each node's influence around Ms. Pacman's current node index
		for(Entry<MOVE,Integer> entry : influenceNodes.get(currentPacmanIndex).getMazeNode().neighbourhood.entrySet()) 
		{ 
			double nodeInfluence = getInfluenceOfNode(entry.getValue());
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
			if(game.getShortestPathDistance(pacmanCurrentNodeIndex, powerPillsIndices[i]) < closestPowerPillDistance)
			{
				closestPowerPillIndex = powerPillsIndices[i];
			}
		}

		return closestPowerPillIndex;
	}

	/**
	 * Determine whether a power pill is attractive to Ms. Pacman considering ghost distances to power pill
	 * @param powerPillIndex
	 * @return boolean
	 */
	private static boolean isPowerPillAttractive(Game game, int powerPillIndex) 
	{
		double allGhostDistancesToPowerPill = -1;

		for(GHOST ghost : GHOST.values()) 
		{
			//Ignore ghosts that are edible
			if(!game.isGhostEdible(ghost)) 
			{
				allGhostDistancesToPowerPill += game.getDistance(powerPillIndex, game.getGhostCurrentNodeIndex(ghost), DM.PATH);
			}
		}

		//If the sum of all ghost distances are within threshold this power pill is Attractive
		if(allGhostDistancesToPowerPill < IMapConstants.POWERPILL_DISTANCE_THRESHOLD && allGhostDistancesToPowerPill >= 0.0) 
		{
			return true;
		}

		return false;
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
			if(game.getShortestPathDistance(pacmanCurrentNodeIndex, junctionIndices[i]) < closestJunctionDistance)
			{
				closestJunctionIndex = junctionIndices[i];
			}
		}

		return closestJunctionIndex;
	}

	/**
	 * Get Graph
	 * @return graph
	 */
	public static Node[] getGraph() 
	{
		return graph;
	}

	/**
	 * Get a node
	 * @param nodeIndex
	 * @return Node
	 */
	public static Node getNode(int nodeIndex) 
	{
		return influenceNodes.get(nodeIndex).getMazeNode();
	}

	/**
	 * Get influence of a node
	 * @param nodeIndex
	 * @return influence
	 */
	public static double getInfluenceOfNode(int nodeIndex) 
	{
		return influenceNodes.get(nodeIndex).getInfluence();
	}
}

package pacman.influencemap;

import java.util.HashMap;

import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.internal.Node;

public class InfluenceMap {

	private static InfluenceMap INSTANCE;
	private static HashMap<Integer, PacmanInfluenceNode> pacmanInfluenceNodes;
	private static HashMap<Integer, GhostInfluenceNode> ghostInfluenceNodes;
	private static Node[] graph;

	private InfluenceMap(Game game)
	{	
		graph = game.getCurrentMaze().graph;

		pacmanInfluenceNodes = new HashMap<Integer, PacmanInfluenceNode>(graph.length);
		ghostInfluenceNodes = new HashMap<Integer, GhostInfluenceNode>(graph.length);

		//Create the mapping of NodeIndexes to InfluenceNodes
		for(int i = 0; i < graph.length; i++) 
		{
			pacmanInfluenceNodes.put(graph[i].nodeIndex, new PacmanInfluenceNode(graph[i]));
			ghostInfluenceNodes.put(graph[i].nodeIndex, new GhostInfluenceNode(graph[i]));
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
		for(PacmanInfluenceNode influenceNode : pacmanInfluenceNodes.values()) 
		{
			influenceNode.clearInfluence();
		}	

		//Calculate Influence of Pills
		int[] activePillsIndices = game.getActivePillsIndices();
		for(int i = 0; i < activePillsIndices.length; i++)
		{
			pacmanInfluenceNodes.get(activePillsIndices[i]).updatePillInfluence(game, pacmanInfluenceNodes, activePillsIndices[i]);
		}

		//Calculate Influence of Ghosts
		for(GHOST ghost : GHOST.values())
		{
			//Ghost outside lair
			if(game.getGhostLairTime(ghost) == 0) 
			{
				if(!game.isGhostEdible(ghost))
				{
					pacmanInfluenceNodes.get(game.getGhostCurrentNodeIndex(ghost)).updateGhostInfluence(game, pacmanInfluenceNodes, ghost);
				}
				else
				{	
					pacmanInfluenceNodes.get(game.getGhostCurrentNodeIndex(ghost)).updateEdibleGhostInfluence(game, pacmanInfluenceNodes, ghost);
				}
			}
		}

		//Calculate Influence of the Closest Power Pill
		int closestPowerPillIndex = closestPowerPillIndexToMsPacman(game);
		if(closestPowerPillIndex != -1)
		{
			pacmanInfluenceNodes.get(closestPowerPillIndex).updatePowerPillInfluence(game, pacmanInfluenceNodes, closestPowerPillIndex);
		}

		//Calculate Influence of Freedom of Choice
		int[] allNeighbouringNodeIndexes = pacmanInfluenceNodes.get(game.getPacmanCurrentNodeIndex()).getMazeNode().allNeighbouringNodes.get(MOVE.NEUTRAL);

		//If in corridor
		if(allNeighbouringNodeIndexes.length == 2) 
		{
			double summedInfluence = pacmanInfluenceNodes.get(allNeighbouringNodeIndexes[0]).getInfluence() + pacmanInfluenceNodes.get(allNeighbouringNodeIndexes[0]).getInfluence();

			//If summed influence of the way forward and back is below threshold
			if(summedInfluence < IMConstants.INFLUENCE_OF_FREEDOM_OF_CHOICE_THRESHOLD)
			{
				int closestJunctionIndex = closestJunctionIndexToMsPacman(game);

				if(closestJunctionIndex != -1 && closestJunctionIndex != game.getPacmanCurrentNodeIndex()) 
				{
					pacmanInfluenceNodes.get(closestJunctionIndex).updateFreedomOfChoiceInfluence(game, pacmanInfluenceNodes, closestJunctionIndex);
				}
			}
		}
	}

	/**
	 * Generate the Ghosts' Influence map
	 * @param game
	 */
	public static void generateGhostsInfluenceMap(Game game) 
	{
		//Clear old influences
		for(GhostInfluenceNode influenceNode : ghostInfluenceNodes.values()) 
		{
			influenceNode.clearInfluence();
		}

		//Calculate Influence of Ms Pacman
		ghostInfluenceNodes.get(game.getPacmanCurrentNodeIndex()).updatePacmanInfluence(game, ghostInfluenceNodes, game.getPacmanCurrentNodeIndex());
		
		//Calculate Influence of Ghosts
		for(GHOST ghost : GHOST.values())
		{
			//Ghost outside lair
			if(game.getGhostLairTime(ghost) == 0) 
			{	
				//ghostInfluenceNodes.get(game.getGhostCurrentNodeIndex(ghost)).updateGhostInfluence(game, ghostInfluenceNodes, ghost);
			}
		}
	}

	/**
	 * Find the closest Power Pill index to Ms. Pacman
	 * @param game
	 * @return boolean
	 */
	public static int closestPowerPillIndexToMsPacman(Game game) 
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
	public static int closestJunctionIndexToMsPacman(Game game) 
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
	 * Get the PacmanInfluenceNode Mappings
	 * @return InfluenceNodes
	 */
	public static HashMap<Integer, PacmanInfluenceNode> getPacmanInfluenceNodes() 
	{
		return pacmanInfluenceNodes;
	}

	/**
	 * Get the GhostInfluenceNode Mappings
	 * @return InfluenceNodes
	 */
	public static HashMap<Integer, GhostInfluenceNode> getGhostInfluenceNodes() 
	{
		return ghostInfluenceNodes;
	}

}

package pacman.influencemap;

import java.util.ArrayList;
import java.util.HashMap;

import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.internal.Node;

/**
 * @author John
 * Specialised influence node for Ms. Pacman that calculates the values of influences based on their specific formula and
 * propagates the influence values to other Ms. Pacman influence nodes using recursion.
 */
public class GhostInfluenceNode {

	private Node mazeNode;

	//Influences for Ghosts
	private double influenceOfPacman;
	private double influenceOfGhosts;

	public GhostInfluenceNode(Node mazeNode)
	{
		this.mazeNode = mazeNode;

		influenceOfPacman = 0.0;
		influenceOfGhosts = 0.0;
	}

	/**
	 * Update Ms Pacman Influence
	 * @param game
	 * @param influenceNodes
	 * @param originIndex
	 */
	public void updatePacmanInfluence(Game game, HashMap<Integer, GhostInfluenceNode> influenceNodes, int originIndex, GHOST ghost) 
	{
		double distanceFromCurrentToOrigin = game.getShortestPathDistance(mazeNode.nodeIndex, originIndex);
		double powerPillFactor = calculatePowerPillFactor(game);

		if(!game.isGhostEdible(ghost))
		{
			//Regular pacman influence calculation when ghost isn't edible
			if(powerPillFactor >= IMConstants.POWER_PILL_THRESHOLD)
			{
				influenceOfPacman = IMConstants.INFLUENCE_OF_PACMAN * Math.pow(IMConstants.INFLUENCE_FACTOR_OF_PACMAN, distanceFromCurrentToOrigin);
			}
			else
			{
				influenceOfPacman = -IMConstants.INFLUENCE_OF_PACMAN * Math.pow((IMConstants.INFLUENCE_FACTOR_OF_PACMAN - powerPillFactor), distanceFromCurrentToOrigin);
			}
		}
		else
		{
			//If ghost is edible pacman will emit a negative influence
			influenceOfPacman = -IMConstants.INFLUENCE_OF_PACMAN * Math.pow(IMConstants.INFLUENCE_FACTOR_OF_PACMAN, distanceFromCurrentToOrigin);		
		}

		//Propagate the influence to this InfluenceNode's neighbours
		for(GhostInfluenceNode influenceNode : getAppropriateNeighbours(influenceNodes, MOVE.NEUTRAL)) 
		{
			//If the node neighbour is further away from origin than this node then propagate influence
			if(game.getShortestPathDistance(influenceNode.getNodeIndex(), originIndex) > distanceFromCurrentToOrigin) 
			{
				influenceNode.updatePacmanInfluence(game, influenceNodes, originIndex, ghost);
			}
		}
	}

	/**
	 * Update Ghost Influence
	 * @param game
	 * @param influenceNodes
	 * @param ghost
	 */
	public void updateGhostInfluence(Game game, HashMap<Integer, GhostInfluenceNode> influenceNodes, GHOST ghost) 
	{
		double distanceFromCurrentToOrigin = game.getShortestPathDistance(mazeNode.nodeIndex, game.getGhostCurrentNodeIndex(ghost));		
		double ghostInfluenceValue = IMConstants.INFLUENCE_GHOST_WEIGHT * Math.pow(IMConstants.INFLUENCE_FACTOR_OF_GHOST_WEIGHT, distanceFromCurrentToOrigin);

		//Calculate the influence limiting factor
		double distanceFromCurrentToPacman = game.getShortestPathDistance(mazeNode.nodeIndex, game.getPacmanCurrentNodeIndex());
		double distanceFromGhostToPacman = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex());

		double influenceOfGhostLimit = (IMConstants.INFLUENCE_GHOST_WEIGHT - distanceFromCurrentToPacman * IMConstants.LIMITING_INFLUENCE_OF_PACMAN) / distanceFromGhostToPacman;

		//Limit Influence and only the most negative ghost influence is considered
		if(ghostInfluenceValue <= influenceOfGhostLimit && ghostInfluenceValue < influenceOfGhosts)
		{
			influenceOfGhosts = ghostInfluenceValue;

			//Propagate the influence to this InfluenceNode's neighbours
			for(GhostInfluenceNode influenceNode : getAppropriateNeighbours(influenceNodes, MOVE.NEUTRAL)) 
			{
				//If the node neighbour is further away from origin than this node then propagate influence
				if(game.getShortestPathDistance(influenceNode.getNodeIndex(), game.getGhostCurrentNodeIndex(ghost)) > distanceFromCurrentToOrigin) 
				{
					influenceNode.updateGhostInfluence(game, influenceNodes, ghost);
				}
			}
		}
	}

	/**
	 * Get Appropriate InfluenceNode Neighbours
	 * @param appropriateNeighbours
	 */
	private ArrayList<GhostInfluenceNode> getAppropriateNeighbours(HashMap<Integer, GhostInfluenceNode> influenceNodes, MOVE move) 
	{
		ArrayList<GhostInfluenceNode> appropriateNeighbours = new ArrayList<GhostInfluenceNode>();

		int[] allNeighbouringNodeIndexes = mazeNode.allNeighbouringNodes.get(move);

		for(int i = 0; i < allNeighbouringNodeIndexes.length; i++) 
		{
			appropriateNeighbours.add(influenceNodes.get(allNeighbouringNodeIndexes[i]));
		}

		return appropriateNeighbours;
	}

	/**
	 * Calculate Power Pill Factor
	 * @param game
	 * @return powerPillFactor
	 */
	private double calculatePowerPillFactor(Game game) 
	{
		//If there is no power pill left for Ms. Pacman to eat return IMConstants.POWER_PILL_THRESHOLD (0.85)
		//This will result in the Pacman Influence being positive
		if(InfluenceMap.closestPowerPillIndexToMsPacman(game) == -1)
		{
			return IMConstants.POWER_PILL_THRESHOLD;
		}

		return ((double) game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), InfluenceMap.closestPowerPillIndexToMsPacman(game)) / IMConstants.POWER_PILL_DISTANCE_FACTOR);
	}

	/**
	 * Get the Pacman Influence of this InfluenceNode
	 * @return
	 */
	public double getInfluence() 
	{
		return influenceOfPacman + influenceOfGhosts;
	}

	/**
	 * Get Maze Node Index that relates to this InfluenceNode
	 * @return
	 */
	public int getNodeIndex() 
	{
		return mazeNode.nodeIndex;
	}

	/**
	 * Get Maze Node that relates to this InfluenceNode
	 * @return Node
	 */
	public Node getMazeNode() 
	{
		return mazeNode;
	}

	/**
	 * Clear Pacman influences on node
	 */
	public void clearInfluence() 
	{
		influenceOfPacman = 0.0;
		influenceOfGhosts = 0.0;	
	}

}

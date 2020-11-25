package pacman.influencemap;

import java.util.ArrayList;
import java.util.HashMap;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Node;

public class InfluenceNode {

	private Node mazeNode;
	private double influenceOfPills;
	private double influenceOfGhosts;
	private double influenceOfEdibleGhosts;
	private double influenceOfPowerPill;
	private double influenceOfFreedomOfChoice;

	public InfluenceNode(Node mazeNode)
	{
		this.mazeNode = mazeNode;
		influenceOfPills = 0.0;
		influenceOfGhosts = 0.0;
		influenceOfEdibleGhosts = 0.0;
		influenceOfPowerPill = 0.0;
		influenceOfFreedomOfChoice = 0.0;
	}

	/**
	 * Update Pill Influence
	 * @param game
	 * @param influenceNodes
	 * @param originIndex
	 */
	public void updatePillInfluence(Game game, HashMap<Integer, InfluenceNode> influenceNodes, int originIndex) 
	{
		double distanceFromCurrentToOrigin = game.getShortestPathDistance(mazeNode.nodeIndex, originIndex);
		double pillInfluenceValue = IMConstants.INFLUENCE_OF_PILL * Math.pow(IMConstants.INFLUENCE_FACTOR, distanceFromCurrentToOrigin);

		//Limit Influence and only the most positive pill influence is considered
		if(pillInfluenceValue >= IMConstants.INFLUENCE_OF_PILL / 100 && pillInfluenceValue > influenceOfPills)
		{
			influenceOfPills = pillInfluenceValue;

			//Propagate the influence to this InfluenceNode's neighbours
			for(InfluenceNode influenceNode : getAppropriateNeighbours(influenceNodes, MOVE.NEUTRAL)) 
			{
				//If the node neighbour is further away from origin than this node then propagate influence
				if(game.getShortestPathDistance(influenceNode.getNodeIndex(), originIndex) > distanceFromCurrentToOrigin) 
				{
					influenceNode.updatePillInfluence(game, influenceNodes, originIndex);
				}
			}
		}
	}

	/**
	 * Update Ghost Influence
	 * @param game
	 * @param influenceNodes
	 * @param ghost
	 */
	public void updateGhostInfluence(Game game, HashMap<Integer, InfluenceNode> influenceNodes, GHOST ghost) 
	{
		double distanceFromCurrentToOrigin = game.getShortestPathDistance(mazeNode.nodeIndex, game.getGhostCurrentNodeIndex(ghost));
		double ghostInfluenceValue = IMConstants.INFLUENCE_OF_GHOST * Math.pow(IMConstants.INFLUENCE_FACTOR, distanceFromCurrentToOrigin);

		//Limit Influence and only the most negative ghost influence is considered
		if(ghostInfluenceValue <= IMConstants.INFLUENCE_OF_GHOST / 10 && ghostInfluenceValue < influenceOfGhosts)
		{
			influenceOfGhosts = ghostInfluenceValue;

			ArrayList<InfluenceNode> neighbours;
			//Only consider neighbours in front of ghost initially
			if(distanceFromCurrentToOrigin == 0.0)
			{
				neighbours = getAppropriateNeighbours(influenceNodes, game.getGhostLastMoveMade(ghost));
			}
			else
			{
				neighbours = getAppropriateNeighbours(influenceNodes, MOVE.NEUTRAL);					
			}

			//Propagate the influence to this InfluenceNode's neighbours
			for(InfluenceNode influenceNode : neighbours) 
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
	 * Update Edible Ghost Influence
	 * @param game
	 * @param influenceNodes
	 * @param ghost
	 */
	public void updateEdibleGhostInfluence(Game game, HashMap<Integer, InfluenceNode> influenceNodes, GHOST ghost) 
	{
		double distanceFromCurrentToOrigin = game.getShortestPathDistance(mazeNode.nodeIndex, game.getGhostCurrentNodeIndex(ghost));
		double edibleGhostInfluenceValue = IMConstants.INFLUENCE_OF_EDIBLE_GHOST * Math.pow(IMConstants.INFLUENCE_FACTOR, distanceFromCurrentToOrigin);

		//Limit Influence and only the most positive edible ghost influence is considered
		if(edibleGhostInfluenceValue >= IMConstants.INFLUENCE_OF_EDIBLE_GHOST / 10 && edibleGhostInfluenceValue > influenceOfEdibleGhosts) 
		{
			influenceOfEdibleGhosts = edibleGhostInfluenceValue;

			//Propagate the influence to this InfluenceNode's neighbours
			for(InfluenceNode influenceNode : getAppropriateNeighbours(influenceNodes, MOVE.NEUTRAL)) 
			{
				//If the node neighbour is further away from origin than this node then propagate influence
				if(game.getShortestPathDistance(influenceNode.getNodeIndex(), game.getGhostCurrentNodeIndex(ghost)) > distanceFromCurrentToOrigin) 
				{
					influenceNode.updateEdibleGhostInfluence(game, influenceNodes, ghost);
				}
			}
		}
	}

	/**
	 * Update Power Pill Influence
	 * @param game
	 * @param influenceNodes
	 * @param originIndex
	 */
	public void updatePowerPillInfluence(Game game, HashMap<Integer, InfluenceNode> influenceNodes, int originIndex) 
	{
		double distanceFromCurrentToOrigin = game.getShortestPathDistance(mazeNode.nodeIndex, originIndex);
		if(isPowerPillAttractive(game, originIndex)) 
		{
			double powerPillInfluenceValue = IMConstants.INFLUENCE_OF_POWERPILL * Math.pow(IMConstants.INFLUENCE_FACTOR, distanceFromCurrentToOrigin);

			//Limit Influence and only the most positive power pill influence is considered
			if(powerPillInfluenceValue >= IMConstants.INFLUENCE_OF_POWERPILL / 10 && powerPillInfluenceValue > influenceOfPowerPill) 
			{
				influenceOfPowerPill = powerPillInfluenceValue;

				//Propagate the influence to this InfluenceNode's neighbours
				for(InfluenceNode influenceNode : getAppropriateNeighbours(influenceNodes, MOVE.NEUTRAL)) 
				{
					//If the node neighbour is further away from origin than this node then propagate influence
					if(game.getShortestPathDistance(influenceNode.getNodeIndex(), originIndex) > distanceFromCurrentToOrigin) 
					{
						influenceNode.updatePowerPillInfluence(game, influenceNodes, originIndex);
					}
				}
			}
		}
		else
		{
			double powerPillInfluenceValue = -IMConstants.INFLUENCE_OF_POWERPILL * Math.pow(IMConstants.INFLUENCE_FACTOR, distanceFromCurrentToOrigin);

			//Limit Influence and only the most negative power pill influence is considered
			if(powerPillInfluenceValue <= -(IMConstants.INFLUENCE_OF_POWERPILL / 10) && powerPillInfluenceValue < influenceOfPowerPill) 
			{
				influenceOfPowerPill = powerPillInfluenceValue;

				//Propagate the influence to this InfluenceNode's neighbours
				for(InfluenceNode influenceNode : getAppropriateNeighbours(influenceNodes, MOVE.NEUTRAL)) 
				{
					//If the node neighbour is further away from origin than this node then propagate influence
					if(game.getShortestPathDistance(influenceNode.getNodeIndex(), originIndex) > distanceFromCurrentToOrigin) 
					{
						influenceNode.updatePowerPillInfluence(game, influenceNodes, originIndex);
					}
				}
			}
		}
	}

	/**
	 * Update Freedom Of Choice Influence
	 * @param game
	 * @param influenceNodes
	 * @param originIndex
	 */
	public void updateFreedomOfChoiceInfluence(Game game, HashMap<Integer, InfluenceNode> influenceNodes, int originIndex) 
	{
		double distanceFromCurrentToOrigin = game.getShortestPathDistance(mazeNode.nodeIndex, originIndex);
		double freedomOfChoiceInfluenceValue = IMConstants.INFLUENCE_OF_FREEDOM_OF_CHOICE * Math.pow(IMConstants.INFLUENCE_FACTOR, distanceFromCurrentToOrigin);

		//Limit Influence and only the most positive power pill influence is considered
		if(freedomOfChoiceInfluenceValue >= 0 && freedomOfChoiceInfluenceValue > influenceOfFreedomOfChoice) 
		{
			influenceOfFreedomOfChoice = freedomOfChoiceInfluenceValue;

			//Propagate the influence to this InfluenceNode's neighbours
			for(InfluenceNode influenceNode : getAppropriateNeighbours(influenceNodes, MOVE.NEUTRAL)) 
			{				
				//If the node neighbour is further away from origin than this node then propagate influence
				if(game.getShortestPathDistance(influenceNode.getNodeIndex(), originIndex) > distanceFromCurrentToOrigin) 
				{
					influenceNode.updateFreedomOfChoiceInfluence(game, influenceNodes, originIndex);
				}
			}
		}
	}

	/**
	 * Get Appropriate InfluenceNode Neighbours
	 * @param appropriateNeighbours
	 */
	private ArrayList<InfluenceNode> getAppropriateNeighbours(HashMap<Integer, InfluenceNode> influenceNodes, MOVE move) 
	{
		ArrayList<InfluenceNode> appropriateNeighbours = new ArrayList<InfluenceNode>();

		int[] allNeighbouringNodeIndexes = mazeNode.allNeighbouringNodes.get(move);

		for(int i = 0; i < allNeighbouringNodeIndexes.length; i++) 
		{
			appropriateNeighbours.add(influenceNodes.get(allNeighbouringNodeIndexes[i]));
		}

		return appropriateNeighbours;
	}

	/**
	 * Determine whether a power pill is attractive to Ms. Pacman considering ghost distances to power pill
	 * @param powerPillIndex
	 * @return boolean
	 */
	private static boolean isPowerPillAttractive(Game game, int powerPillIndex) 
	{
		double allGhostDistancesToPowerPill = 0.0;

		int activeGhosts = 0;

		for(GHOST ghost : GHOST.values()) 
		{
			if(game.getGhostLairTime(ghost) == 0) 
			{
				allGhostDistancesToPowerPill += game.getShortestPathDistance(powerPillIndex, game.getGhostCurrentNodeIndex(ghost));
				activeGhosts++;
			}
		}

		//If the sum of all ghost distances are within threshold this power pill is Attractive
		if(activeGhosts > 0) 
		{
			if(allGhostDistancesToPowerPill < IMConstants.POWERPILL_DISTANCE_THRESHOLD_PER_GHOST * activeGhosts) 
			{
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Get the Influence of this InfluenceNode
	 * @return
	 */
	public double getInfluence() 
	{
		return influenceOfPills + influenceOfGhosts + influenceOfEdibleGhosts + influenceOfPowerPill + influenceOfFreedomOfChoice;
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
	 * Clear influences on node
	 */
	public void clearInfluence() 
	{
		influenceOfPills = 0.0;
		influenceOfGhosts = 0.0;
		influenceOfEdibleGhosts = 0.0;
		influenceOfPowerPill = 0.0;
		influenceOfFreedomOfChoice = 0.0;		
	}
}

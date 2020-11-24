package pacman.influencemap;

import java.util.ArrayList;
import java.util.HashMap;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Node;

public class InfluenceNode {

	private Game game;
	private Node mazeNode;
	private double influenceOfPills;
	private double influenceOfGhosts;
	private double influenceOfEdibleGhosts;
	private double influenceOfPowerPill;
	private double influenceOfFreedomOfChoice;

	public InfluenceNode(Game game, Node mazeNode)
	{
		this.game = game;
		this.mazeNode = mazeNode;
		influenceOfPills = 0.0;
		influenceOfGhosts = 0.0;
		influenceOfEdibleGhosts = 0.0;
		influenceOfPowerPill = 0.0;
		influenceOfFreedomOfChoice = 0.0;
	}

	public void updatePillInfluence(HashMap<Integer, InfluenceNode> influenceNodes, int originIndex) 
	{
		double distanceFromCurrentToOrigin = game.getDistance(mazeNode.nodeIndex, originIndex, DM.PATH);
		double pillInfluenceValue = IMapConstants.INFLUENCE_OF_PILL * Math.pow(IMapConstants.INFLUENCE_FACTOR, distanceFromCurrentToOrigin);

		//Limit Influence and only the most positive pill influence is considered
		if(pillInfluenceValue >= IMapConstants.INFLUENCE_OF_PILL / 10 && pillInfluenceValue > influenceOfPills)
		{
			influenceOfPills = pillInfluenceValue;

			//Propagate the influence to this InfluenceNode's neighbours
			for(InfluenceNode influenceNode : getAppropriateNeighbours(influenceNodes, MOVE.NEUTRAL)) 
			{
				double distanceFromNodeToOrigin = game.getDistance(influenceNode.getNodeIndex(), originIndex, DM.PATH);

				//If the node is further away from origin than this node then propagate influence
				if(distanceFromNodeToOrigin > distanceFromCurrentToOrigin) 
				{
					influenceNode.updatePillInfluence(influenceNodes, originIndex);
				}
			}
		}
	}

	public void updateGhostInfluence(HashMap<Integer, InfluenceNode> influenceNodes, GHOST ghost) 
	{
		double distanceFromCurrentToOrigin = game.getDistance(mazeNode.nodeIndex, game.getGhostCurrentNodeIndex(ghost), DM.PATH);
		double ghostInfluenceValue = IMapConstants.INFLUENCE_OF_GHOST * Math.pow(IMapConstants.INFLUENCE_FACTOR, distanceFromCurrentToOrigin);

		//Limit Influence and only the most negative ghost influence is considered
		if(ghostInfluenceValue <= IMapConstants.INFLUENCE_OF_GHOST / 10 && ghostInfluenceValue < influenceOfGhosts)
		{
			influenceOfGhosts = ghostInfluenceValue;

			//Propagate the influence to this InfluenceNode's neighbours
			ArrayList<InfluenceNode> neighbours;
			if(distanceFromCurrentToOrigin == 0.0) 
			{
				neighbours = getAppropriateNeighbours(influenceNodes, game.getGhostLastMoveMade(ghost));
			}
			else
			{
				neighbours = getAppropriateNeighbours(influenceNodes, MOVE.NEUTRAL);					
			}

			for(InfluenceNode influenceNode : neighbours) 
			{
				double distanceFromNodeToOrigin = game.getDistance(influenceNode.getNodeIndex(), game.getGhostCurrentNodeIndex(ghost), DM.PATH);

				//If the node is further away from origin than this node then propagate influence
				if(distanceFromNodeToOrigin > distanceFromCurrentToOrigin) 
				{
					influenceNode.updateGhostInfluence(influenceNodes, ghost);
				}
			}
		}
	}

	public void updateEdibleGhostInfluence(HashMap<Integer, InfluenceNode> influenceNodes, int originIndex) 
	{
		double distanceFromCurrentToOrigin = game.getDistance(mazeNode.nodeIndex, originIndex, DM.PATH);
		double edibleGhostInfluenceValue = IMapConstants.INFLUENCE_OF_EDIBLE_GHOST * Math.pow(IMapConstants.INFLUENCE_FACTOR, distanceFromCurrentToOrigin);

		//Limit Influence and only the most positive edible ghost influence is considered
		if(edibleGhostInfluenceValue >= IMapConstants.INFLUENCE_OF_EDIBLE_GHOST / 10 && edibleGhostInfluenceValue > influenceOfGhosts) 
		{
			influenceOfEdibleGhosts = edibleGhostInfluenceValue;

			//Propagate the influence to this InfluenceNode's neighbours
			for(InfluenceNode influenceNode : getAppropriateNeighbours(influenceNodes, MOVE.NEUTRAL)) 
			{
				double distanceFromNodeToOrigin = game.getDistance(influenceNode.getNodeIndex(), originIndex, DM.PATH);

				//If the node is further away from origin than this node then propagate influence
				if(distanceFromNodeToOrigin > distanceFromCurrentToOrigin) 
				{
					influenceNode.updateEdibleGhostInfluence(influenceNodes, originIndex);
				}
			}
		}
	}

	public void updatePowerPillInfluence(HashMap<Integer, InfluenceNode> influenceNodes, int originIndex, boolean attractivePowerPill) 
	{
		double distanceFromCurrentToOrigin = game.getDistance(mazeNode.nodeIndex, originIndex, DM.PATH);
		double powerPillInfluenceValue;
		
		if(attractivePowerPill) 
		{
			powerPillInfluenceValue = IMapConstants.INFLUENCE_OF_POWERPILL * Math.pow(IMapConstants.INFLUENCE_FACTOR, distanceFromCurrentToOrigin);

			//Limit Influence and only the most positive power pill influence is considered
			if(powerPillInfluenceValue >= IMapConstants.INFLUENCE_OF_POWERPILL / 10 && powerPillInfluenceValue > influenceOfPowerPill) 
			{
				influenceOfPowerPill = powerPillInfluenceValue;

				//Propagate the influence to this InfluenceNode's neighbours
				for(InfluenceNode influenceNode : getAppropriateNeighbours(influenceNodes, MOVE.NEUTRAL)) 
				{
					double distanceFromNodeToOrigin = game.getDistance(influenceNode.getNodeIndex(), originIndex, DM.PATH);

					//If the node is further away from origin than this node then propagate influence
					if(distanceFromNodeToOrigin > distanceFromCurrentToOrigin) 
					{
						influenceNode.updatePowerPillInfluence(influenceNodes, originIndex, attractivePowerPill);
					}
				}
			}
		}
		else
		{
			powerPillInfluenceValue = IMapConstants.INFLUENCE_OF_POWERPILL * -(Math.pow(IMapConstants.INFLUENCE_FACTOR, distanceFromCurrentToOrigin));

			//Limit Influence and only the most negative power pill influence is considered
			if(powerPillInfluenceValue <= -IMapConstants.INFLUENCE_OF_POWERPILL / 10 && powerPillInfluenceValue < influenceOfPowerPill) 
			{
				influenceOfPowerPill = powerPillInfluenceValue;

				//Propagate the influence to this InfluenceNode's neighbours
				for(InfluenceNode influenceNode : getAppropriateNeighbours(influenceNodes, MOVE.NEUTRAL)) 
				{
					double distanceFromNodeToOrigin = game.getDistance(influenceNode.getNodeIndex(), originIndex, DM.PATH);

					//If the node is further away from origin than this node then propagate influence
					if(distanceFromNodeToOrigin > distanceFromCurrentToOrigin) 
					{
						influenceNode.updatePowerPillInfluence(influenceNodes, originIndex, attractivePowerPill);
					}
				}				
			}
		}
	}
	
	public void updateFreedomOfChoiceInfluence(HashMap<Integer, InfluenceNode> influenceNodes, int originIndex) 
	{
		double distanceFromCurrentToOrigin = game.getDistance(mazeNode.nodeIndex, originIndex, DM.PATH);
		double freedomOfChoiceInfluenceValue = IMapConstants.INFLUENCE_OF_FREEDOM_OF_CHOICE * Math.pow(IMapConstants.INFLUENCE_FACTOR, distanceFromCurrentToOrigin);
		
		//Limit Influence and only the most positive power pill influence is considered
		if(freedomOfChoiceInfluenceValue <= IMapConstants.INFLUENCE_OF_FREEDOM_OF_CHOICE / 10 && freedomOfChoiceInfluenceValue < influenceOfFreedomOfChoice) 
		{
			//Propagate the influence to this InfluenceNode's neighbours
			for(InfluenceNode influenceNode : getAppropriateNeighbours(influenceNodes, MOVE.NEUTRAL)) 
			{
				double distanceFromNodeToOrigin = game.getDistance(influenceNode.getNodeIndex(), originIndex, DM.PATH);

				//If the node is further away from origin than this node then propagate influence
				if(distanceFromNodeToOrigin > distanceFromCurrentToOrigin) 
				{
					influenceNode.updateFreedomOfChoiceInfluence(influenceNodes, originIndex);
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

package pacman.influencemap;

import java.util.HashMap;

import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Node;

public class InfluenceNode {

	private Node mazeNode;
	private Double influenceOfPills;

	public InfluenceNode(Node mazeNode)
	{
		this.mazeNode = mazeNode;
		this.influenceOfPills = 0.0;
	}

	/**
	 * Update this node's specific influence and propagate to Neighbouring Nodes
	 * @param influence
	 * @param game
	 * @param influenceNodes
	 * @param originIndex
	 */
	public void updateInfluence(INFLUENCE influence, Game game, HashMap<Integer, InfluenceNode> influenceNodes, int originIndex) 
	{
		double distanceFromCurrentToOrigin = game.getDistance(mazeNode.nodeIndex, originIndex, DM.PATH);

		switch(influence) 
		{
		case PILL:
			double influenceValue = IMapConstants.INFLUENCE_OF_PILL * Math.pow(IMapConstants.INFLUENCE_FACTOR, distanceFromCurrentToOrigin);

			//Don't consider small influence value changes
			if(influenceValue > IMapConstants.INFLUENCE_MINIMUM)
			{
				//Update this node's Pill influence
				influenceOfPills += influenceValue;

				int[] allNeighbouringNodeIndexes = mazeNode.allNeighbouringNodes.get(MOVE.NEUTRAL);

				//Propagate the influence to this node's neighbours
				for(int i = 0; i < allNeighbouringNodeIndexes.length; i++) 
				{
					double distanceFromNodeToOrigin = game.getDistance(allNeighbouringNodeIndexes[i], originIndex, DM.PATH);

					//If the node is further away from origin than this node propagate
					if(distanceFromNodeToOrigin > distanceFromCurrentToOrigin) 
					{
						influenceNodes.get(allNeighbouringNodeIndexes[i]).updateInfluence(influence, game, influenceNodes, originIndex);
					}
				}
			}
			break;
		default:
			throw new UnsupportedOperationException("Influence: " + influence.toString() + " is not supported.");
		}
	}

	/**
	 * Get the Influence of this InfluenceNode
	 * @return
	 */
	public double getInfluence() 
	{
		return influenceOfPills;
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
}

package pacman.influencemap;

import java.util.ArrayList;
import java.util.HashMap;

import pacman.game.Constants.MOVE;
import pacman.game.internal.Node;

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
	 * Get Appropriate InfluenceNode Neighbours
	 * @param appropriateNeighbours
	 */
	private ArrayList<PacmanInfluenceNode> getAppropriateNeighbours(HashMap<Integer, PacmanInfluenceNode> influenceNodes, MOVE move) 
	{
		ArrayList<PacmanInfluenceNode> appropriateNeighbours = new ArrayList<PacmanInfluenceNode>();

		int[] allNeighbouringNodeIndexes = mazeNode.allNeighbouringNodes.get(move);

		for(int i = 0; i < allNeighbouringNodeIndexes.length; i++) 
		{
			appropriateNeighbours.add(influenceNodes.get(allNeighbouringNodeIndexes[i]));
		}

		return appropriateNeighbours;
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

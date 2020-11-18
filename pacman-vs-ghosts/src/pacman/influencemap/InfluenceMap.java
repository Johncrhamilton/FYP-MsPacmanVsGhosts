package pacman.influencemap;

import java.util.HashMap;
import java.util.Map.Entry;

import pacman.game.Game;
import pacman.game.Constants.MOVE;
import pacman.game.internal.Node;

public class InfluenceMap {
	
	private HashMap<Integer, InfluenceNode> influenceNodes;
	
	public InfluenceMap(Game game)
	{	
		Node[] graph = game.getCurrentMaze().graph;
		
		influenceNodes = new HashMap<Integer, InfluenceNode>(graph.length);
		
		//Create the mapping of NodeIndexes to InfluenceNodes
		for(int i = 0; i < graph.length; i++) 
		{
			influenceNodes.put(graph[i].nodeIndex, new InfluenceNode(graph[i]));
		}
		
		//Influences for Ms. Pacman
		
		//Pill Influence
		int[] activePillsIndices = game.getActivePillsIndices();
		
		//Update Influence Nodes that have Pills
		for(int i = 0; i < activePillsIndices.length; i++)
		{
			influenceNodes.get(activePillsIndices[i]).updateInfluence(INFLUENCE.PILL, game, influenceNodes, activePillsIndices[i]);
		}		
	}
	
	/**
	 * Return Move that leads to node with the highest influence
	 * @param currentPacmanIndex
	 * @return Move
	 */
	public MOVE getBestMoveMsPacman(int currentPacmanIndex) 
	{		
		MOVE move = null;
		
		double highestNodeInfluence = -Double.MAX_VALUE;
		
		//Check each direction's node influence
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
	 * Get influence of a node
	 * @param nodeIndex
	 * @return influence
	 */
	public double getInfluenceOfNode(int nodeIndex) 
	{
		return influenceNodes.get(nodeIndex).getInfluence();
	}

	/**
	 * Get a node
	 * @param nodeIndex
	 * @return Node
	 */
	public Node getNode(int nodeIndex) 
	{
		return influenceNodes.get(nodeIndex).getMazeNode();
	}
	
}

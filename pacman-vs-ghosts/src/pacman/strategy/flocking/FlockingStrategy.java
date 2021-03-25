package pacman.strategy.flocking;

import java.util.HashMap;
import java.util.Map.Entry;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Node;
import pacman.strategy.flocking.FSConstants.Actor;

public class FlockingStrategy {

	private static MOVE move = MOVE.NEUTRAL;
	
	private double[] stateValues;
	private double[] actorValues;

	public FlockingStrategy(double[] stateValues, double[] actorValues) 
	{
		this.stateValues = stateValues;
		this.actorValues = actorValues;
	}

	public static MOVE highestSteeringForceMove(Game game, GHOST currentGhost)
	{
		HashMap<Actor, Integer> activeActors = findActiveActors(game, currentGhost);
		
		for(Entry<Actor, Integer> actor : activeActors.entrySet()) 
		{
			int neighbourhood = -1;
			
			//Determine neighbourhood
			int neighbourhoodIndex = 1;
			while(neighbourhoodIndex <= FSConstants.NUMBER_OF_NEIGHBOURHOODS) 
			{
				//If Euclidean Distance from current ghost to actor is less than the maximum distance boundary for the neighbourhood
				if(game.getEuclideanDistance(game.getGhostCurrentNodeIndex(currentGhost), actor.getValue()) < neighbourhoodIndex * FSConstants.NEIGHBOURHOOD_INTERVAL) 
				{
					neighbourhood = neighbourhoodIndex;
				}
			}
		}
		
		return move;
	}

	/**
	 * Find the active actors in the current game state
	 * @param game
	 * @return Active actors in format HashMap<Actor, position>
	 */
	private static HashMap<Actor, Integer> findActiveActors(Game game, GHOST currentGhost)
	{
		//Active actors estimated as 1 Pacman, 0-4 PowerPills, 0-4 Ghosts
		HashMap<Actor, Integer> activeActors = new HashMap<Actor, Integer>(10);

		Node[] mazeNodes = game.getCurrentMaze().graph;

		//Pacman Actor
		activeActors.put(Actor.PACMAN, game.getPacmanCurrentNodeIndex());

		//Active PowerPills
		int[] powerPillsIndices = game.getActivePowerPillsIndices();
		if(powerPillsIndices.length > 0) 
		{
			for(int i = 0; i < powerPillsIndices.length; i++) 
			{
				activeActors.put(Actor.POWERPILL, powerPillsIndices[i]);
			}
		}

		//Active Ghosts
		for(GHOST ghost : GHOST.values())
		{
			//If the current ghost isn't this ghost and this ghost is outside lair
			if(!currentGhost.equals(ghost) && game.getGhostLairTime(ghost) == 0) 
			{
				Actor ghostType;
				
				//Filter Hunter Ghosts
				if(!game.isGhostEdible(ghost)) 
				{
					ghostType = Actor.HUNTER;
				}
				//Filter Hunted Ghosts
				if(game.getGhostEdibleTime(ghost) > 30) 
				{
					ghostType = Actor.HUNTED;				
				}
				//Filter Out Flashing Ghosts
				else
				{
					ghostType = Actor.FLASH;
				}
				
				activeActors.put(ghostType, game.getGhostCurrentNodeIndex(ghost));	
			}
		}

		return activeActors;
	}

}

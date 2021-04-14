package pacman.strategy.flocking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Node;
import pacman.strategy.flocking.FSConstants.ACTOR;
import pacman.strategy.flocking.FSConstants.GHOST_STATE;

public class FlockingStrategy {

	private ArrayList<Double> neighbourhoods;
	private double[][][] actorContextMatrixMagnitudes;

	public FlockingStrategy(ArrayList<Double> neighbourhoods, double[][][] actorContextMatrixMagnitudes) 
	{
		this.neighbourhoods = neighbourhoods;
		this.actorContextMatrixMagnitudes = actorContextMatrixMagnitudes;
	}

	/**
	 * Determine the neighbourhood and steering force of all active actors, then using this information calculate the total steering force.
	 * Finally translate and return a feasible move from the total steering that has the highest rank.
	 * @param game
	 * @param currentGhost
	 * @param ghostState
	 * @return Move
	 */
	public MOVE steeringForceMove(Game game, GHOST currentGhost, GHOST_STATE ghostState)
	{
		HashMap<Integer, ACTOR> activeActors = findActiveActors(game, currentGhost);		
		Node[] mazeNodes = game.getCurrentMaze().graph;

		double[] totalSteeringForce = new double[2];		
		int currentGhostIndex = game.getGhostCurrentNodeIndex(currentGhost);

		for(Entry<Integer, ACTOR> actor : activeActors.entrySet())
		{			
			//Determine neighbourhood (delta) the actor falls into

			double ghostToActorEuclideanDistance = game.getEuclideanDistance(currentGhostIndex, actor.getKey());			
			int neighbourhood = -1;

			for(int neighbourhoodIndex = 0; neighbourhoodIndex < neighbourhoods.size(); neighbourhoodIndex++)
			{
				//If Euclidean Distance from current ghost to actor is less than the maximum distance boundary specified for the neighbourhood
				if(ghostToActorEuclideanDistance < neighbourhoods.get(neighbourhoodIndex)) 
				{
					neighbourhood = neighbourhoodIndex;
					break;
				}
			}

			//Next determine the steering force (F_alpha) for the actor

			//Calculate the positional difference
			double[] steeringForce = {mazeNodes[actor.getKey()].x - mazeNodes[currentGhostIndex].x, 
					mazeNodes[actor.getKey()].y - mazeNodes[currentGhostIndex].y};

			for(int j = 0; j < steeringForce.length; j++) 
			{
				//Divide by the euclidean distance between actor and ghost
				steeringForce[j] /= ghostToActorEuclideanDistance;

				//Multiply by the actor's magnitude given the context alpha_(ghostState,actor,neighbourhood)
				steeringForce[j] *= actorContextMatrixMagnitudes[ghostState.ordinal()][actor.getValue().ordinal()][neighbourhood];
			}

			//Add the steering force (F_alpha) for the actor to the total steering force
			totalSteeringForce[0] += steeringForce[0];
			totalSteeringForce[1] += steeringForce[1];
		}

		//Return a feasible move with the highest rank from the total steering force
		return translateTotalSteeringForce(game, totalSteeringForce, currentGhost);
	}

	/**
	 * Find the active actors in the current game state, storing their nodeIndex and Enum
	 * @param game
	 * @return Active actors in format HashMap<Integer, ACTOR>
	 */
	private HashMap<Integer, ACTOR> findActiveActors(Game game, GHOST currentGhost)
	{
		//Active actors estimated as 1 Pacman, 0-4 PowerPills, 0-4 Ghosts at any given time
		HashMap<Integer, ACTOR> activeActors = new HashMap<Integer, ACTOR>(10);

		//Pacman Actor
		activeActors.put(game.getPacmanCurrentNodeIndex(), ACTOR.PACMAN);

		//Active PowerPills
		int[] powerPillsIndices = game.getActivePowerPillsIndices();
		if(powerPillsIndices.length > 0) 
		{
			for(int i = 0; i < powerPillsIndices.length; i++) 
			{
				activeActors.put(powerPillsIndices[i], ACTOR.POWERPILL);
			}
		}

		//Active Ghosts
		for(GHOST ghost : GHOST.values())
		{
			//If the current ghost isn't this ghost and this ghost is outside lair
			if(!currentGhost.equals(ghost) && game.getGhostLairTime(ghost) == 0) 
			{
				ACTOR ghostType;

				//Filter Hunter Ghosts
				if(!game.isGhostEdible(ghost)) 
				{
					ghostType = ACTOR.HUNTER;
				}
				//Filter Hunted Ghosts
				else if(game.getGhostEdibleTime(ghost) > 30) 
				{
					ghostType = ACTOR.HUNTED;				
				}
				//Filter Out Flashing Ghosts
				else
				{
					ghostType = ACTOR.FLASH;
				}

				activeActors.put(game.getGhostCurrentNodeIndex(ghost), ghostType);	
			}
		}

		return activeActors;
	}

	/**
	 * Translates the Total Steering Force into a Move that the ghost should take
	 * @param game
	 * @param totalSteeringForce
	 * @param currentGhost
	 * @return MOVE
	 */
	private MOVE translateTotalSteeringForce(Game game, double[] totalSteeringForce, GHOST currentGhost) 
	{	
		MOVE move;

		//Determine the horizontal move
		MOVE moveHorizontal;
		if(totalSteeringForce[0] >= 0)
		{
			moveHorizontal = MOVE.RIGHT;
		}
		else 
		{
			moveHorizontal = MOVE.LEFT;
		}

		//Determine the vertical move
		MOVE moveVertical;
		if(totalSteeringForce[1] >= 0)
		{
			moveVertical = MOVE.DOWN;
		}
		else 
		{
			moveVertical = MOVE.UP;
		}

		//Determine whether the vertical or horizontal move has the higher rank
		if(Math.abs(totalSteeringForce[0]) >= Math.abs(totalSteeringForce[1]))
		{
			move = moveHorizontal;

			//If the move goes against the previous move, make the vertical move instead
			if(move == game.getGhostLastMoveMade(currentGhost).opposite()) 
			{
				move = moveVertical;
			}
		}
		else
		{
			move = moveVertical;

			//If the move goes against the previous move, make the horizontal move instead
			if(move == game.getGhostLastMoveMade(currentGhost).opposite()) 
			{
				move = moveHorizontal;
			}
		}

		return move;
	}

	/**
	 * Get Neighbourhoods
	 * @return neighbourhoods
	 */
	public ArrayList<Double> getNeighbourhoods()
	{
		return neighbourhoods;
	}

	/**
	 * Get Actor Context Matrix Magnitudes
	 * @return actorContextMatrixMagnitudes
	 */
	public double[][][] getActorContextMatrixMagnitudes() 
	{
		return actorContextMatrixMagnitudes;
	}

	/**
	 * String representation of the FlockingStrategy
	 * @return String
	 */
	public String toString() 
	{	
		StringBuilder sb = new StringBuilder();

		//Neighbourhoods
		sb.append("\n----FlockingStrategy----");
		sb.append("\nNeighbourhoods ");
		for(int n = 0; n < neighbourhoods.size(); n++) 
		{
			sb.append(neighbourhoods.get(n) + " ");
		}

		//Actor Context Matrix Magnitudes
		sb.append("\n\nActor Context Matrix Magnitudes\n");
		for(GHOST_STATE ghostState : GHOST_STATE.values())
		{
			sb.append("\n" + ghostState);
			for(ACTOR actor : ACTOR.values())
			{
				sb.append("\n" + actor + " ");
				for(int n = 0; n < neighbourhoods.size(); n++)
				{
					sb.append(actorContextMatrixMagnitudes[ghostState.ordinal()][actor.ordinal()][n] + " ");
				}
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	/**
	 * Returns a clone of the FlockingStrategy
	 * @return FlockingStrategy
	 */
	public FlockingStrategy clone() 
	{
		return new FlockingStrategy(((ArrayList<Double>)neighbourhoods.clone()), actorContextMatrixMagnitudes.clone());
	}
}

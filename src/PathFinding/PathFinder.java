package PathFinding;

import java.util.ArrayList;

import misc.Location;
import Map.MapCreator;


/**
 * Find an optimal path from a list of Location objects (stores) given
 * 
 * TODO:  Try to find ways to make it more efficient
 */



public class PathFinder {

	private final int MAXRADIUS = 30;   
	private ArrayList<Location> inOrderPath = new ArrayList<Location>();		//keeps track of stores to visit in order
	
	public PathFinder(ArrayList<Location> storesInSection, Location nearestCenter, boolean getMap, int mapCount) {
		
		
		// give each location an id # from 0..size, this will help label nodes in digraph  
		for (int i = 0; i < storesInSection.size(); i++) 
			storesInSection.get(i).setID(i);
		
		
		nearestNeighbour(storesInSection, nearestCenter);

		//generate output files from the path created
		new MapCreator(inOrderPath, mapCount, getMap);
	}
	
		
	

	/**
	 * This function recursively calls itself with a shorter list of valid store locations,
	 * and a new center point within the store list.
	 * When two stores are found to be optimally close together, they are connected by an edge in G
	 * 
	 * @param storeList		valid Stores to create a path from
	 * @param center			position to calculate distance from
	 */
	public void nearestNeighbour(ArrayList<Location> storeList, Location center) {
		
		int size = storeList.size();
		if (size == 0) return;		// base case
		
		Location tempCenter = null;
		
		this.inOrderPath.add(center);
		storeList.remove(center);		
		
		for (int radius = 0; radius < MAXRADIUS; radius++) {
			if (storeList.isEmpty()) return;     //avoids unecessary loops caused by recursive call
			
			tempCenter =  storesInRadius(center,storeList,radius);	 // check if any stores are within current radius

			if (tempCenter != null) {                               // if true, a store has been found
				nearestNeighbour(storeList, tempCenter);					
			}
		}
	}
	
	
	
	/**
	 * Finds the FIRST Location/store that is within the radius,
	 * If two or more stores are found within the radius it will pick the first one it finds,
	 * not necessarily the closest one.
	 * @param center	Position to calculate distance from
	 * @param stores	List of valid stores to measure distances
	 * @param r		radius bound
	 * @return	returns the first location, else it returns null if nothing is found
	 */
	private Location storesInRadius(Location center, ArrayList<Location> stores,int r) {
		// if stores is empty, the loop will not run, and will return null
		
		for (Location store: stores) {
			if (center.getDistance(store) <= r*1000) // multiply by 1000 to get meters
				return store;
		}
		
		return null;
	}
}

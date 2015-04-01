package PathFinding;

import java.util.ArrayList;

import misc.Location;


/**
 * Helps to hold multiple information required when splitting a cirlce
 * by number of drivers
 */
public class Helper {

	
	ArrayList<ArrayList<Location>> sections = new ArrayList<ArrayList<Location>>();
	
	//each of these values repr. the location closest to the Base/citycenter
	private Location minDistNorth = new Location(300,300,"","");
	private Location minDistEast = new Location(300,300,"","");
	private Location minDistSouth = new Location(300,300,"","");
	private Location minDistWest = new Location(300,300,"","");
	
	
	/* 	===			SETTERS	   ===			 */
	public void setMinDistNorth(Location l) {
		minDistNorth= l;
	}
	
	public void setMinDistEast(Location l) {
		minDistEast= l;
	}
	
	public void setMinDistSouth(Location l) {
		minDistSouth= l;
	}
	
	public void setMinDistWest(Location l) {
		minDistWest= l;
	}
	
	/* 		===		GETTERS		 ===		 */
	public Location getMinDistNorth() {
		return minDistNorth;
	}
	
	public Location getMinDistEast() {
		return minDistEast;
	}
	
	public Location getMinDistSouth() {
		return minDistSouth;
	}
	
	public Location getMinDistWest() {
		return minDistWest;
	}
	
	
	
}

package PathFinding;
import java.util.ArrayList;

import misc.Location;
/**
 *
 * Depending on the number of drivers selected (1,2 or 4)
 * The area of the circle created by the radius is divided accordingly
 * And the the optimal path algorithm will run on each sub section
 * 
 * TODO: find efficent way to return these sections
 *
 */
public class AreaDivider {
	
	
	private Helper h  = new Helper();
	
	private ArrayList<Location> sectionNorth = new ArrayList<Location>();
	private ArrayList<Location> sectionSouth = new ArrayList<Location>();
	private ArrayList<Location> sectionEast = new ArrayList<Location>();
	private ArrayList<Location> sectionWest = new ArrayList<Location>();
	
	private int numOfDrivers; 
	
	
	/**
	 * Create a valid sections from constructor 
	 * @param numOfDrivers
	 * @param validStores
	 * @param base
	 */
	public AreaDivider(int numOfDrivers,ArrayList<Location> validStores, Location base) {
		this.numOfDrivers = numOfDrivers;
		divideStores(validStores, base);
	}
	
	
	public ArrayList<ArrayList<Location>> getSections() {
		return h.sections;
	}
	
	public Location getMinDist() {
		return h.getMinDistNorth();
	}
	
	/**
	 * 
	 * @param validStores  All stores found within radius
	 * @param base  Center point of the radius (the location object of the selected city)
	 * @param numOfDrivers		Number of drivers selected (1, 2 or 4)
	 */ 
	public void divideStores(ArrayList<Location> validStores, Location base) {

		if (validStores.size() < 2) return;
		if (base == null) return;
		
		
		if (numOfDrivers == 1) divideByOne(validStores, base);
		if (numOfDrivers == 2) divideByTwo(validStores, base);
		if (numOfDrivers == 4) divideByFour(validStores, base);
		
	}
	
	/**
	 * Creates one section
	 * the closeset location to center will be identified as the minDistNorth
	 */
	private void divideByOne(ArrayList<Location> validStores, Location base) {
		
		h.sections.add(validStores);
		
		for (Location store: validStores) {
			if (base.getDistance(store) < base.getDistance(h.getMinDistNorth())) {
				h.setMinDistNorth(store);
			}
		}
	}
	
	/**
	 * Creates two sections North & South  OR East & West
	 * The choice for which set of section to use, is determined by which set is more balanced
	 */
	private void divideByTwo(ArrayList<Location> validStores, Location base) {
		
		
		final double LAT = base.getLat();
		final double LON = base.getLon();
	
		for (Location store: validStores) {
			double tempLat = store.getLat();
			double tempLon = store.getLon();
			
			if (tempLat > LAT) 
				sectionNorth.add(store);
			else 
				sectionSouth.add(store);
			
			if (tempLon < LON) 
				sectionWest.add(store);
			else
				sectionEast.add(store);
		}
		
		// check which two sections should be used, by seeing which pair has the smallest differece
		// it can either be split north to south,  or east to west
		if (Math.abs(sectionNorth.size() - sectionSouth.size()) < Math.abs(sectionEast.size()) - sectionWest.size()) {
			h.sections.add(sectionNorth);
			h.sections.add(sectionSouth);
		}
		
		else {
			h.sections.add(sectionEast);
			h.sections.add(sectionWest);
		}


	}
	
	
	/**
	 * Divides the set of stores into 4 subsections
	 */
	private void divideByFour(ArrayList<Location> validStores, Location base) {
		final double LAT = base.getLat();
		final double LON = base.getLon();
	
		
		
		for (Location store: validStores) {
			double tempLat = store.getLat();
			double tempLon = store.getLon();
			
			double tempDist = base.getDistance(store);
			
			if (tempLat > LAT) {
				if (tempLon < LON) {
					sectionWest.add(store);		// top left circle
					if (tempDist < base.getDistance(h.getMinDistWest())) {
						h.setMinDistWest(store);
					}
				}
				else {
					sectionNorth.add(store); // top right circle
					if (tempDist < base.getDistance(h.getMinDistNorth())) {
						h.setMinDistNorth(store);
					}
				}
			}
			
			else { 
				if (tempLon < LON)
					sectionSouth.add(store);	// bottom left
				if (tempDist < base.getDistance(h.getMinDistSouth())) {
					h.setMinDistSouth(store);
				}
				else
					sectionEast.add(store);  // bottom right
				if (tempDist < base.getDistance(h.getMinDistEast())) {
					h.setMinDistEast(store);
				}
			}
		}
		
		h.sections.add(sectionEast);
		h.sections.add(sectionNorth);
		h.sections.add(sectionSouth);
		h.sections.add(sectionWest);
		
	}

		 
}

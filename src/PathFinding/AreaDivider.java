package PathFinding;
import java.util.ArrayList;

import misc.Location;
/**
 *
 * Depending on the number of drivers selected (1,2 or 4)
 * The area of the circle created by the radius is divided accordingly
 * And the the optimal path algorithm will run on each sub section
 * 
 */
public class AreaDivider {
	
	
	
	private ArrayList<Location> sectionNorth = new ArrayList<Location>();  
	private ArrayList<Location> sectionSouth = new ArrayList<Location>();
	private ArrayList<Location> sectionEast = new ArrayList<Location>();
	private ArrayList<Location> sectionWest = new ArrayList<Location>();
	
	ArrayList<ArrayList<Location>> sections = new ArrayList<ArrayList<Location>>();
	
	private int numOfDrivers; 
	
	
	/**
	 * Create a valid sections from constructor 
	 */
	public AreaDivider(int numOfDrivers, ArrayList<Location> validStores, Location base) {
		this.numOfDrivers = numOfDrivers;
		divideStores(validStores, base);
	}
	
	
	public ArrayList<ArrayList<Location>> getSections() {
		return sections;
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
	 * Creates one section, trivial case  */
	private void divideByOne(ArrayList<Location> validStores, Location base) {
		sections.add(validStores);
	}
	
	/**
	 * Creates two sections North & South  OR East & West
	 * The choice for which set of section to use, is determined by which set of two section is 
	 * more balanced. 
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
			sections.add(sectionNorth);
			sections.add(sectionSouth);
		}
		
		else {
			sections.add(sectionEast);
			sections.add(sectionWest);
		}
	}
	

	
	
	/**
	 * Divides the set of stores into 4 subsections
	 * North is top right of circle
	 * East is bottom right of circle
	 * South is bottom left of circle
	 * West is top left of circle
	 * 
	 */
	private void divideByFour(ArrayList<Location> validStores, Location base) {
		final double LAT = base.getLat();
		final double LON = base.getLon();
	
		// Latitude increases from North to South
		// Longitude decreases from east to west
		
		for (Location store: validStores) {
			double tempLat = store.getLat();
			double tempLon = store.getLon();
			
			if (tempLat > LAT) {
				if (tempLon < LON) 
					sectionWest.add(store);		// top left circle
				
				else 
					sectionNorth.add(store); // top right circle
			}
			
			else { 
				if (tempLon < LON)
					sectionSouth.add(store);	// bottom left
				
				else
					sectionEast.add(store);  // bottom right
			}
		}
		
		sections.add(sectionEast);
		sections.add(sectionNorth);
		sections.add(sectionSouth);
		sections.add(sectionWest);
		
	}

		 
}

import java.util.ArrayList;
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
	
	
	private ArrayList<Location> sectionNorth = new ArrayList<Location>();
	private ArrayList<Location> sectionSouth = new ArrayList<Location>();
	private ArrayList<Location> sectionEast = new ArrayList<Location>();
	private ArrayList<Location> sectionWest = new ArrayList<Location>();

	//each of these values repr. the location closest to the Base/citycenter
	//TODO: implement these...
	private Location minDistNorth;
	private Location minDistEast;
	private Location minDistSouth;
	private Location minDistWest;
	
	/**
	 * 
	 * @param validStores  All stores found within radius
	 * @param base  Center point of the radius (the location object of the selected city)
	 * @param n		Number of drivers selected (1, 2 or 4)
	 */ 
	public void divideStores(ArrayList<Location> validStores, Location base, int n) {
		
		if (validStores.size() < 2) return;
		if (base == null) return;
		if (n == 1) return; 	
		
		if (n == 2) divideByTwo(validStores, base);
		if (n == 4) divideByFour(validStores, base);
		
	}
	
	/**
	 * Creates two sections North & South  OR East & West
	 * The choice for which set of section to use, is determined by which set is more balanced
	 */
	private void divideByTwo(ArrayList<Location> validStores, Location base) {
		//ArrayList<ArrayList<Location>> sections = new ArrayList<ArrayList<Location>>();
		
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
				sectionWest.add(store);
		}
		
		// check which two sections should be used, by seeing which pair has the smallest differece
		// it can either be split north to south,  or east to west
		
//		if (Math.abs(sectionNorth.size() - sectionSouth.size()) < Math.abs(sectionEast.size()) - sectionWest.size()) {
//			sections.add(sectionNorth);
//			sections.add(sectionSouth);
//			return sections;
//		}
//		
//		else {
//			sections.add(sectionEast);
//			sections.add(sectionWest);
//			return sections;
//		}


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
		
	}

		 
}

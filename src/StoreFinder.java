

/**
 * 
 * @author Jean Lucas
 * 
 * This class is responsible for finding all stores within a city and radius
 * as described by the user input.
 * 
 * It will use the location of the city found in CityFinder, as the center 
 * point of the radius.
 * 
 * TODO: Find efficient method to store the cities (BSTs, Hashmap, hashtable, arrays..)
 *
 */


public class StoreFinder {
	
	private final Location BASE;		// BASE corresponds to the center point of the radius
	private final int RADIUS;				// Radius described by user
	private String storeName;				// The name of the store to look for
	
	
	public StoreFinder(String city, String state, int r, String name) {
		this.BASE = new CityFinder(city,state).getUSALocation();
		this.RADIUS = r;
		this.storeName = name;
	}

}

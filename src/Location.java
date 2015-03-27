/**
 * @author Jean Lucas
 *
 * Represent a unique store on a map, with attributes such as:
 * chain name, address, and latitude and longitude points.
 * 
 * TODO: implement an alternative of toString() method
 */

public class Location {
	private final double R = 6378100;
	private double latidude;		// latitude point with format XX.xxxx
	private double longitude;		// longitude point with formath YY.yyyy
	private String storeName; 		// Name of store (Starbucks, WalMart, etc..)
	private String address;			// Includes street name, street number and zip
	private String city;
	private String state;
	private final int ID; 			// each location has an unique ID, helps when using graph algorithms
	
	// standard constructor for a Location object
	public Location(double lat, double lon, String name, String address, String city, String state, int id) {
		this.latidude = lat;
		this.longitude = lon;
		this.storeName = name;
		this.address = address;
		this.city = city;
		this.state = state;
		this.ID = id;  
	}
	

	// secondary constructor for city Locations where the ID, name, and address are irrelevant
	public Location(double lat, double lon, String city, String state) {
		this(lat, lon, "", "", city, state, 0);
	}
	
	
	
	public double getLat() { 
		return this.latidude; 
	}
	public double getLon() {
		return this.longitude; 
	}
	
	public String getName() {
		return this.storeName; 
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public String getCity() {
		return this.city;
	}
	
	public String getState() {
		return this.state;
	}
	
	public int getID() { 
		return this.ID;
	}
	
	
//calculates the distance using harversine function from this object, to Location object b
		public int getDistance(Location b) {
			double dLat = Math.toRadians(b.getLat() - this.getLat());
			double dLon = Math.toRadians(b.getLon() - this.getLon());
			double lat1 = Math.toRadians(this.getLat());
			double lat2 = Math.toRadians(b.getLat());
			
			double val1 = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
			double c = 2*Math.asin(Math.sqrt(val1));
			return (int) Math.floor(R*c);
		}

	@Override
	public String toString() {
		return String.format("%0.4f  %0.4f /t  %-20s  \t %-20s  %-20s",
												 this.getLat(),this.getLon(), this.getName(), this.getCity(), this.getAddress() );
	}
	
	

	//TODO: make for tests for equality, such as latitude & longitude 
	@Override 
	public boolean equals(Object obj) {
		if (obj == null) return false;																		
		if (!(obj instanceof Location)) return false;										
	
		Location objTest = (Location) obj;	//temporary Locatiob object for testing purposes
		
		if (objTest.getID() == this.getID()) 
			return true;
		
		return false;
	}


	
	//testing toString
	public static void main(String[] args) {
		
		Location a = new Location(31.11,35.33214, "Starbucks","123 main street XYZ 789", "Hamilton", "Ontario",0);
		System.out.println(a.toString());
	}
	
	
}



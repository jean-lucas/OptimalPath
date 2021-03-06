package misc;

/**
 * Represent a unique store on a map, with attributes such as:
 * chain name, address, and latitude and longitude points.
 * 
 */

public class Location {
	private final double R = 6378100;			// radius of the earth in meters
	private double latidude;		// latitude point with format XX.xxxx
	private double longitude;		// longitude point with formath YY.yyyy
	private String storeName; 		// Name of store (Starbucks, WalMart, etc..)
	private String address;			// Includes street name, street number and zip
	private String city;
	private String state;
	private int ID; 			// each location has an unique ID, helps when using graph algorithms
	
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
	// useful when creating Location objects of cities, rather than stores
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
		return this.storeName.trim(); 
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public String getCity() {
		return this.city.trim();
	}
	
	public String getState() {
		return this.state.trim();
	}
	
	public int getID() { 
		return this.ID;
	}
	
	public void setID(int id) {
		this.ID = id;
	}
	
	
//calculates the distance using harversine function from this object, to Location object b
		public int getDistance(Location b) {
			double dLat = Math.toRadians(b.getLat() - this.getLat());
			double dLon = Math.toRadians(b.getLon() - this.getLon());
			double lat1 = Math.toRadians(this.getLat());
			double lat2 = Math.toRadians(b.getLat());
			
			double val1 = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
			double c = 2*Math.asin(Math.sqrt(val1));
			int result = (int) Math.floor(R*c);
			
			if (result < 1000) result = 1000;
			return result;
		}

		
		
	/**
	 * This toString method is designed to be used with HTML format,
	 * it uses the table data tags <td> to allow a properly formatted and spaced output
	 */
	public String toHTMLString() {
	
	return String.format("%s %s  %s  %s %s %4f , %4f %s ",
			"<td>",this.getAddress(),"</td><td>", this.getCity(), "</td><td>",
			this.getLat(),this.getLon(),"</td>");
	}
	
	
	@Override
	public String toString() {
		return String.format("%s %s %s %s \t (%2.2f %2.2f)", this.storeName, this.address, this.city, 
																												 this.state, this.latidude, this.longitude);
	}
	

}



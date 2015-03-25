import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * 
 * @author Jean Lucas
 *
 * Find the location of a city, given the city name and state
 * 
 */
public class CityFinder {
	
	private String city;
	private String state;
	
	public CityFinder(String city, String state) {
		this.city = city;
		this.state = state;
	}
	

	/**
	 * Read cities_usa.txt to find the corresponding city and create a Location object from it.
	 * When the city is found, it adds it to a "cache-like" text file where it can be accessed much faster
	 * if it is used as input again recently.
	 * Using this "cache-like" text file can decrease the worst case scenario time by a factor of about ~15
	 * 
	 * @return Location object representing the city
	 */
	public Location getUSALocation() {
		
		try {
			Scanner recentInput = new Scanner(new File("data/recent_cities_usa.txt"));   
			Scanner input = new Scanner(new File("data/cities_usa.txt"));   
			
			Location targetCity = null;
			
			// first check if the city can be found in the recent input text file
			targetCity = readFile(recentInput);
			
			// if city was not found in the recent input, look in the full list, and then add it to the recent input text file
			if (targetCity == null) {
				targetCity = readFile(input);
				addToRecent(targetCity);
			}
			
			// if city has still NOT be found, then possibly it is mispelled
			if (targetCity == null) 
				System.out.println("City not found within the data set, please check the spelling");
			
			return targetCity;
		}
		
		
		catch (FileNotFoundException e) {
			System.out.println(e.getLocalizedMessage());
		}
		
		
		return null;
	}
	
	
	public Location readFile(Scanner in) {

		while (in.hasNext()) {
			String temp = in.nextLine().substring(10).replaceAll("(\\s(\\p{Lower}+|\\p{Upper}{3}+)\\s*)|[+]|(?=-)","");  // Formatting text line
			String[] currLine = temp.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)"); // split at the numbers .... source = http://goo.gl/DBkajk
			
			
			String city = currLine[0].substring(0, currLine[0].length()-3).trim();  // get the all characters up to and not including the last two character
			String state = currLine[0].substring(currLine[0].length()-3).trim();		 // get only the last two characters of string
			String lat = currLine[1];
			String lon = currLine[3];

			if (this.city.equals(city) && this.state.equals(state)) { 
			
				//removing the zero in front of the numerical value represented by string (ie 0123 becomes 123) 
				// and also adding negative sign to all values.
				if (lon.charAt(0) == '0') 
					lon = lon.replaceFirst("0","-");
				else 
					lon = "-"+lon;
	
				// Now we can create a Location object representing the city
				Double latitude = Double.parseDouble(lat)/1000000.0;  
				Double longitude = Double.parseDouble(lat)/1000000.0;
				in.close();
				return new Location(latitude, longitude, city, state);
			}
			
		}
		in.close();
		
		// if city not found, return null
		return null;
	}
	
	private void addToRecent(Location newLoc) {
		if (newLoc == null) 
			return;
		
		// allows text to be appended to an existing textfile
		try {
			PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter("data/recent_cities_usa.txt", true))); 
			
			// format is ugly, but it is like this, so that it can match with the original data format
			// TODO: find a way to make this better
			output.println(" 00 00000 "+ newLoc.getCity() + " " + newLoc.getState() +" +" + 
										 (int) (newLoc.getLat()*1000000) + " -"+ (int) (newLoc.getLon()*1000000) );
			
			output.close();
		}
		
		catch (IOException e) { 
			System.out.println(e.getLocalizedMessage());
		}


	}
	
	
	
//	//testing
//	public static void main(String[] args)  { 
//		
//		double t1 = System.nanoTime();
	
//		CityFinder a = new CityFinder("Yoder", "WY");
//		Location b = a.getUSALocation();
	
//		double t2 = System.nanoTime();
//		
//		System.out.println((t2-t1)/1000);
//
//	}
}

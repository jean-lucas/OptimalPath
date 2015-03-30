
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import StringSorts.MSDsort;
import StringSorts.LSDsort;

/**
 * TODO: change Scanner objects to FileReader objects to read textfiles. Check if that is more efficient
 * TODO: decided if sorting should overwrite original textfile or just write to a temp file
 * 
 * This class is responsible for all text manipulation required by the program
 * All the data files that will be manipuated are found the the "data" folder
 * 
 * IMPORTANT FORMATTING:
 * any file containing information for locations of stores and cities MUST be in the following format:
 * 
 *                State, City, address(if its a store), latitude, longitude
 */
public class FileOperator {
	
	private String fileName = "data/";
	
	private String city;
	private String state;

	
	public FileOperator(String fileName) {
		this.fileName += fileName;
	}
	
	public FileOperator(String city, String state) {
		this.city = city;
		this.state = state;
	}
	
	public FileOperator(String fileName, String city, String state) {
		this.fileName += fileName;
		this.city = city;
		this.state = state;
	}
	
	
	
	
	/***********************************************************************************************
	 *                     Methods responsible for sorting a text file
	 ***********************************************************************************************/
	
	/**
	 * To allow for a more efficient search of stores, each file can be sorted by state name.
	 * 
	 * Since we are dealing with strings of all the same length of two, we can apply LSD string sort.
	 *  
	 *  The design for this algorithm has been taken from  Robert Sedgewick 4e textbook "Algorithms" page 707
	 *	It has been modified to receive input from a textfile, and output it to a textfile
	 *
	 * WARNING: This class will overwrite the exisiting file in tempSorted.txt
	 */
	public void sortFile() {
		
		MSDsort msd = new MSDsort();
		LSDsort lsd;
		
		try {
			
			Scanner in = new Scanner(new File(this.fileName));
			PrintStream out = new PrintStream("data/tempSorted.txt");				//output overwrites existing file in tempSorted.txt
			System.setOut(out);
			
			String numOfEntries = in.nextLine().split(":")[1].trim();				//first line of textfile is number of entries
			int fileSize = Integer.parseInt(numOfEntries);									
			
			System.out.println("NUMBER OF ENTRIES: " + fileSize);						//make sure the sorted file still has numOfEntries info
			
			String[] inputArray = new String[fileSize-1];											//will hold every entry of the textfile
			
			int lineNumber = 0;
			while (in.hasNext()) {
				inputArray[lineNumber++] = in.nextLine();
			}
			
			lsd = new LSDsort();
			//sort by state name (lsd), and then by city name (msd)
			lsd.sort(inputArray, 2);	// the 2 represents that we are only sorting based on the first two entries of the string
			msd.sort(inputArray);			// since it is a stable sort we will not scramble the sorting from LSDsort
			
			
			//print each index of the sorted array to output file
			for (String line: inputArray) 
				System.out.println(line);
			
			in.close();
			out.close();
			 
		}
		catch (FileNotFoundException e) {
			System.out.println(e.getLocalizedMessage());
		}
		
	}
	

	
	
	
	
	/***********************************************************************************************
	 *                     Methods for finding a city in a text file
	 ***********************************************************************************************/
	
	/**
	 * Read cities_usa.txt to find the corresponding city and create a Location object from it.
	 * When the city is found, it is added to a "cache-like" text file where it can be accessed much faster
	 * if it is used as input again.
	 * Using this "cache-like" text file can greatly decrease search times for large files
	 * 
	 * @return Location object representing the city
	 */
	public Location getCityLocation() {
		
		//Check if city and state have been properly initialized
		if (city == null || state == null) {
			System.out.println("No city name and/or state name given");
			return null;
		}
		
		
		try {
			
			Scanner recentInput = new Scanner(new File("data/recent_cities_usa.txt"));   
			Scanner input = new Scanner(new File("data/cities_usa.txt"));   
			
			Location targetCity = null; //just adding a comment
			
			// first check if the city can be found in the recent input text file
			targetCity = readFile(recentInput);
			
			// if city was not found in the recent input, look in the full list, and then add it to the recent input text file
			if (targetCity == null) {
				targetCity = readFile(input);
				addToRecent(targetCity);
			}
			
			// if city has still NOT be found, then possibly it is mispelled or it is not in the data set
			if (targetCity == null) 
				System.out.println("City not found, please check the spelling");
			
			return targetCity;
		}
		
		
		catch (FileNotFoundException e) {
			System.out.println(e.getLocalizedMessage());
		}
		
		//if all else fails 
		return null;
	}
	
	
	
	/**
	 * Reads through textfile specified by the Scanner
	 * @param in  Scanner object
	 * @return		<strong>br</strong> if no city found, else return Location object representing that city
	 */
	private Location readFile(Scanner in)  {
		
		while (in.hasNext()) {
			
			String[] currLine = in.nextLine().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)"); // split at the digits
			
			String city = currLine[0].substring(0, currLine[0].length()-3).trim();  // get the all characters up to and not including the last two character
			String state = currLine[0].substring(currLine[0].length()-3).trim();		 // get only the last two characters of string
			String lat = currLine[1];
			String lon = currLine[3];
			
			
			if (this.city.equalsIgnoreCase(city) && this.state.equalsIgnoreCase(state)) { 		
			
				//removing the zero in front of the numerical value represented by string (ie 0123 becomes 123) 
				// and also adding negative sign to all values.
				if (lon.charAt(0) == '0') 
					lon = lon.replaceFirst("0","-");
				else 
					lon = "-"+lon;
	
				// Now we can create a Location object representing the city
				Double latitude = Double.parseDouble(lat)/1000000.0;  
				Double longitude = Double.parseDouble(lon)/1000000.0; 
				in.close();
				
				return new Location(latitude, longitude, city, state);
			}
		}
		
		in.close();
		
		// if city not found, return null
		return null;
	}
	
	/**
	 * Adds the information of a city to the recent text files for faster searching if needed again
	 * @param newLoc  Location object represent the recently found city
	 */
	private void addToRecent(Location newLoc) {
		
		if (newLoc == null) 
			return;
		
		try {
			PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter("data/recent_cities_usa.txt", true))); 
			
			output.println(newLoc.getCity() + " " + newLoc.getState() +" " + 
										 (int) (newLoc.getLat()*1000000) + " "+ (int) (newLoc.getLon()*1000000) );
			
			output.close();
		}
		
		catch (IOException e) { 
			System.out.println(e.getLocalizedMessage());
		}


	}
	
	
	/***********************************************************************************************
	 *             Methods responsible for finding all valid stores in a specified radius
	 ***********************************************************************************************/
	
	
	public ArrayList<Location> getStoresInRadius(Location center, int radius) {
		return null;
	}
	

	public static void main(String[] args) {
		FileOperator a= new FileOperator("starbucks_locations.txt");
		a.sortFile();
	}
}

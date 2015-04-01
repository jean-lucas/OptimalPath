package FileOp;

import misc.Location;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import StringSorts.MSDsort;
import StringSorts.LSDsort;

/**
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
	
	// at the start of the program, these arrays MUST be filled by its respective textfile
	private Location[] startbucksLocations;
	private Location[] mcdonaldsLocations;
	private Location[] homedepotLocations;
	private Location[] walmartLocations;
	private boolean fullLocationArrays = false;			//true iff all location arrays are full
	
	private String fileName = "data/";
	
	private String city;
	private String state;
	private String storeName;
	
	
	/**
	 * Constructor for when a new file is inserted in data
	 * it will NOT automatically sort the file, if we wish to sort a new textfile
	 * we must explicitly call sortFile(). 
	 * @param fileName    name of textfile, musht include extension .txt
	 */
	public FileOperator(String fileName) {
		this.fileName += fileName;
	}
	
	
	/**
	 * General constructor for any other purpose. It should only be needed to construct ONE FileOperator
	 * object per client call.
	 * @param fileName
	 * @param city
	 * @param state
	 */
	public FileOperator(String fileName, String city, String state, String storeName) {
		this.fileName += fileName;
		this.city = city.trim();
		this.state = state.trim();
		this.storeName = storeName.trim();
		setupArrays();
	}
	
	
	/***********************************************************************************************
	 *                Methods responsible for populating location arrays from textfile
	 ***********************************************************************************************/
	
	private void setupArrays() {
		if (fullLocationArrays) return;		// dont do anything if arrays are already made

		startbucksLocations = readFile("data/starbucks_locations.txt");
		mcdonaldsLocations = readFile("data/mcdonalds_locations.txt");
		homedepotLocations = readFile("data/homedepot_locations.txt");
		walmartLocations = readFile("data/walmart_locations.txt");
		
		fullLocationArrays = true;
		
	}
	
	private Location[] readFile(String path) {
		Location[] inputArray = null;
		
		try {
			
			Scanner in = new Scanner(new File(path));
			
			String numOfEntries = in.nextLine().split(":")[1].trim();				//first line of textfile is number of entries
			int fileSize = Integer.parseInt(numOfEntries);									
			
			
			inputArray = new Location[fileSize-1];											//will hold every entry of the textfile
			
			int lineNumber = 0;
			while (in.hasNext() && lineNumber < fileSize-1) {
				String[] currLine = in.nextLine().split(",");
				
				String state = currLine[0];
				String city = currLine[1];
				String address = currLine[2];
				Double lat = Double.parseDouble(currLine[3]);
				Double lon = Double.parseDouble(currLine[4]);
				
				inputArray[lineNumber++] = new Location(lat,lon,storeName,address,city,state,lineNumber);		//linenumber is given as ID
			}
			
			in.close();
		}
		catch (FileNotFoundException e) {
			System.out.println(e.getLocalizedMessage());
		}
		
		return inputArray;
	}
	
	
	/***********************************************************************************************
	 *               Method responsible for finding ALL cities within a state
	 ***********************************************************************************************/	
	
	public Map<String, ArrayList<String>> getAllCitiesByState() {
		
		
		Map<String, ArrayList<String>> tempMap = new HashMap<String, ArrayList<String>>();
	
		ArrayList<String> cities = new ArrayList<String>();
		
		String currentState = "XX";
		
		try {
			Scanner in = new Scanner(new File(fileName));
			
			while (in.hasNext()) {
				
				String[] currLine = in.nextLine().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)"); // split at the digits
				
				String city = currLine[0].substring(0, currLine[0].length()-3).trim();  // get the all characters up to and not including the last two character
				String state = currLine[0].substring(currLine[0].length()-3).trim();		 // get only the last two characters of string
				
				
				if (state.equalsIgnoreCase(currentState))
					cities.add(city);
				
				else {
					tempMap.put(currentState, cities);
					cities = new ArrayList<String>();	//clear the current list
					cities.add(city);
					currentState = state;
				}
			}
		}
		
		catch (FileNotFoundException e) {
			System.out.println(e.getLocalizedMessage());
		}
		
		return tempMap;
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
		LSDsort lsd = new LSDsort();		
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
			throw new Error("City or state names were NOT set");
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
 	
	/**
	 * Given a center location, find all stores (storename specified in FileOperatioer constructor implicitly)
	 * that are within the given radius.
	 * @param center	Location object 
	 * @param radius  max distance from center allowed
	 * @return ArrayList<Location> of all stores found in radius
	 */
	public ArrayList<Location> getStoreInRadius(Location center, int radius) {
		
		// validating input
		if (center == null)  return null;
		if (radius < 0) return null;
		
		
		String targetLocation = center.getState()+center.getCity();	// to be used for binary search
		
		int position = -1;		// represents the array index of which the city is found in the Location Arrays

		//find out which store name we are searching for
		// once we find the store we want, we call a method that calculates the distance between all stores in the city
		// and checks which ones are within radius
		switch (storeName) {
		
		case "homedepot":
				position = binarySearchLocation(homedepotLocations, targetLocation);
				return getStoreInRadius(center,radius,homedepotLocations,position);
				
		case "mcdonalds":
				position = binarySearchLocation(mcdonaldsLocations, targetLocation);
				return getStoreInRadius(center,radius,mcdonaldsLocations,position);
				
		case "starbucks":
				position = binarySearchLocation(startbucksLocations, targetLocation);
				return getStoreInRadius(center,radius,startbucksLocations,position);
			
		case "walmart":
				position = binarySearchLocation(walmartLocations, targetLocation);
				return getStoreInRadius(center,radius,walmartLocations,position);
				
		}
		
		// nothing was found, return an empty list
		return new ArrayList<Location>();
	}
	
	
	/**
	 * Populates the validLocation array
	 * 
	 * @param center	Location object 
	 * @param radius  max distance from center allowed
	 * @param locations  array of Loction objects
	 * @param index		index of which the first city is found in locations[]
	 * @return ArrayList<Location> of all stores found in radius
	 */
	private ArrayList<Location> getStoreInRadius(Location center, int radius, Location[] locations, int index) {
		
		ArrayList<Location> validLocations = new ArrayList<Location>();
		
		
		//city not in locations[], return empty list
		if (index < 0 ) return validLocations;
		
		
		//once this is false we can stop searching and break out of the loop
		boolean stillInCity = center.getCity().equalsIgnoreCase(locations[index].getCity());
		
		while (stillInCity) {
			if (center.getDistance(locations[index]) <= radius*1000) {
				validLocations.add(locations[index]);
			}
			stillInCity = center.getCity().equalsIgnoreCase(locations[++index].getCity());
			
		}
		return validLocations;
	}

	
	/**
	 * 
	 * @param a   Location array for a particular store
	 * @param target  a CONCATENATION of the state and city name (no space between state and cityname)
	 * @return	index of the first store in the state and city specified else
	 * 					returns -1 if target not found
	 */
	private int binarySearchLocation(Location[] a, String target) {
		
		int n = a.length;
		int hi = n-1;
		int lo = 0;
		int mid = -1;
		
		while (lo <= hi) {
			mid = lo + (hi - lo) / 2;

			String temp = a[mid].getState()+a[mid].getCity();		// a[mid] corresponds to a location object
			
			if (temp.compareToIgnoreCase(target) > 0)
				hi = mid-1;	
			
			else if (temp.compareToIgnoreCase(target) < 0)
				lo = mid+1;
	
			else {
				// the target has been found... but now must find where the first occurance appears
				for (int i = mid; i >= 0; i--) {
					temp = a[i].getState()+a[i].getCity();
					if (temp.compareToIgnoreCase(target) != 0)
						return mid = i+3;
				}
			}
			
		}
		return -1;
	}

	
	
	
	
	public static void main(String[] args) {
		
		FileOperator a= new FileOperator("mcdonalds_locations.txt");
		System.out.println("CENTER LOCATION:   " + a.getCityLocation());
		
		
		double t2 = System.nanoTime();
		
		ArrayList<Location> d = a.getStoreInRadius(a.getCityLocation(),3);
		System.out.println("============================================================"); 
		System.out.println(d.size());
		for(Location b: d) {
			System.out.println(b.toString());
		}
		System.out.println(("time for second method (ms):   " +(System.nanoTime() -t2)/1000000));
	}
}






////using that filename that is stored within this class the following
////method will return all the stores that are within the radius of the 
////given center location object
//public ArrayList<Location> getStoresInRadius(Location center, int radius) {
//Scanner in;
//if (center == null)  return null;
////make a location ArrayList
//ArrayList<Location> list = new ArrayList<Location>();
//
////start a counter for the array list
//int counter = 0;
//
//String[] inputArray = null;
//try {
//	
//	//import the file using the stored filename
//	in = new Scanner(new File(fileName));
//	String numOfEntries = in.nextLine().split(":")[1].trim();
//	int fileSize = Integer.parseInt(numOfEntries);	
//	System.out.println("NUMBER OF ENTRIES: " + fileSize);
//	inputArray = new String[fileSize];
//	int lineNumber = 0;
//	while (in.hasNext()) {
//		inputArray[lineNumber++] = in.nextLine();
//	}
//	in.close();
//	
//} catch (FileNotFoundException e) {
//	System.out.println(e.getLocalizedMessage());
//}
//	
//	
//	for(int i = 0; i < inputArray.length;i++){
//		
//		
//		//split up the current line to get
//		String[] currentLine;
//		try{
//			currentLine = inputArray[i].split(",");
//		}catch(NullPointerException u){
//			return list;
//		}
//		
//		
//		
//		//so it will only run if the current location it is looking at is in the proper state
//		if(currentLine[0].substring(1).equals(center.getState())){
//			
//			//distance ====== sqrt( (x2-x1)^2 + (y2-y1)^2 )
//			
//			
//			//assume the center is x1 and the store that we are currently checking against to be x2
//			//likewise for y
//			//assuming that the first given number is Lat and the second Lon
//			double x = Double.parseDouble(currentLine[3]) - center.getLat();
//			double y = Double.parseDouble(currentLine[4]) - center.getLon();
//			
//			
//			//plug values into square root formula
//			double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
//			//System.out.println(distance);
//			
//			if(distance <= radius){
//				
//				//create a new location object if the store is within the radius
//				String name[] = fileName.split("_");
//				Location loc = new Location(Double.parseDouble(currentLine[3]), Double.parseDouble(currentLine[4]), 
//						name[0].substring(5), currentLine[2], currentLine[1], currentLine[0], counter);
//				
//				//add one to the counter
//				counter++;
//				
//				//System.out.println(counter);
//				//add the location object to the arraylist
//				list.add(loc);
//			}
//		}
//	}
//return list;
//}
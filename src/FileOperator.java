
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * TODO: change Scanner objects to FileReader objects to read textfiles. Check if that is more efficient
 * 
 * This class is responsible for all text manipulation required by the program
 * All the data files that will be manipuated are found the the "data" folder
 */
public class FileOperator {
	
	private String fileName = "data/";
	
	private String city;
	private String state;
	//comment
	
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
	 * 												Methods responsible for sorting a text file
	 ***********************************************************************************************/
	
	/**
	 * To allow for a more efficient search of stores, each file can be sorted by state name.
	 * 
	 * Since we are dealing with strings of all the same length of two, we can apply LSD string sort.
	 *  
	 *  The design for this algorithm has been taken from  Robert Sedgewick 4e textbook "Algorithms" page 707
	 *	It has been modified to receive input from a textfile, and output it to a textfile
	 *
	 * WARNING: This class will overwrite the exisiting file given in constructor
	 */
	public void sortFile() {
		
		try {
			
			Scanner in = new Scanner(new File(this.fileName));
			PrintStream out = new PrintStream(this.fileName);				//output overwrites existing file
			 
			ArrayList<String> temp = new ArrayList<String>();				//ArrayList used since file size is unknown
				
			while (in.hasNext()) {
				String[] line = in.nextLine().split(",");
				temp.add(line[1]+","+line[0]+","+line[2]+","+line[3]+","+line[4]);		
			}
			
			//Converting ArrayList to String[]
			String[] inputArray = (String[]) temp.toArray(new String[0]);
			
			LSDsort(inputArray,2);		// the 2 represents that we are only sorting based on the first two entries of the string
			
			//print each index of the sorted array to output
			for (String line: inputArray) 
				System.out.println(line);
			
			in.close();
			out.close();
			 
		}
		catch (FileNotFoundException e) {
			System.out.println(e.getLocalizedMessage());
		}
		
	}
	
	/**
	 * Least Significant Digit (LSD) sort
	 * Taken from Sedgewick 4ed "Algorithms" page 707
	 * @param a  array to be sorted
	 * @param W	 size of string to be sorted
	 */
	private void LSDsort(String[] a, int W) {
    int N = a.length;
    int R = 256;   // extend ASCII alphabet size
    String[] aux = new String[N];

    for (int d = W-1; d >= 0; d--) {
        // sort by key-indexed counting on dth character

        // compute frequency counts
        int[] count = new int[R+1];
        for (int i = 0; i < N; i++)
            count[a[i].charAt(d) + 1]++;

        // compute cumulates
        for (int r = 0; r < R; r++)
            count[r+1] += count[r];

        // move data
        for (int i = 0; i < N; i++)
            aux[count[a[i].charAt(d)]++] = a[i];

        // copy back
        for (int i = 0; i < N; i++)
            a[i] = aux[i];
    }
	}
	
	
	
	
	/***********************************************************************************************
	 * 												Methods for finding a city in a text file
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
			
			Location targetCity = null;
			
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
	 * 							Methods responsible for finding all valid stores in a specified radius
	 ***********************************************************************************************/
	
	
	public ArrayList<Location> getStoresInRadius(Location center, int radius) {
		return null;
	}
	

}

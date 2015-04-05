package Map;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import PathFinding.Digraph;
import misc.Location;



/**
 * This class is responsible for all the outputs to the client (HTML file and googleMaps)
 */
public class MapCreator {

	ArrayList<Location> list;
	Digraph G;
	int count;
	boolean getMap;
	
	public MapCreator(ArrayList<Location> Storelist, Digraph g, int mapCount, boolean b) {
		list = Storelist;
		G = g;
		count = mapCount;
		getMap = b;
		setup();
	}
	
	public void setup() {
		generateOutputHTML();
		if (getMap) generateGoogleMap();
	}
	
	

	/**
	 * From template.html found in data folder, we can create an output.html with any information we wish.
	 * In this case, we will genereate the a list of directions in order from first to last store to visit.
	 * 
	 * Each driver will be given a different generated file
	 */
	private void generateOutputHTML() {

		
		try {
			Scanner in = new Scanner(new File("data/template.html"));
			PrintStream out = new PrintStream("data/output"+count+".html");
			System.setOut(out);
			
			while (in.hasNext()) {
				String line = in.nextLine();
				System.out.print(line);
				
				if (line.equals("<title>")) System.out.println("Path #" +count);
				
			
				if (line.equals("<body>")) { 										 // this is where all the good stuff goes..
					
					System.out.println("<strong>In order path for driver #"+count+" "
							+ "</strong><br><br><hr style='width:85%;'/>");
				
					System.out.println("<ol start='1'>");
					
					for (Location c: list) {
						System.out.println("<li>" + c.toString() +"</li>");			// creating list items for each stop in route
					}
					
					System.out.println("</ol>");
					System.out.println("<br>");
				}
				
				System.out.println();
			}
			in.close();
			out.close();
			
			//opening html file to the default browser in the system
			File htmlFile = new File("data/output"+count+".html");
			Desktop.getDesktop().browse(htmlFile.toURI());
		}
		
		catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		} 
	}
	
	
	/**
	 * A map representaion of the route will only be created if the client desires.
	 * If so, this function will generate a URL with the syntax of Google Maps URL.
	 * 
	 * The only issue is that the number of stores generated on this map can be at MOST 25.
	 */
	private void generateGoogleMap() {
		String[] points = new String[list.size()];		
		
		// generate a list of latitude and longitude points
		for (int i = 0 ; i < list.size(); i++) {
			String lat = list.get(i).getLat() + "";
			String lon = ""+list.get(i).getLon() + "";
			points[i] = lat+","+lon;
		}
		
		String path = "http://maps.google.com/maps?saddr=";
		
		//initial point
		path += points[0];
		
		path += "&daddr=";
		
		// add all other stops in between the route
		for (int i = 1; i < list.size(); i++) {
			if (i == 1) path += points[i];
			else   path += "+to:" + points[i];
		}
		
		
		// wrap back around to final position
		path+= "+to:"+points[0];
		
		try {
			java.awt.Desktop.getDesktop().browse(java.net.URI.create(path));
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
}


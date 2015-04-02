package Map;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import misc.Location;


public class MapCreator {

	public void generateMap(ArrayList<Location> list, int mapCount, boolean getMap) {
		String[] points = new String[list.size()];
		
		System.out.println(list.get(0).getLon());
		for (int i = 0 ; i < list.size(); i++) {
			String lat = ""+list.get(i).getLat();
			String lon = ""+list.get(i).getLon();
			points[i] = lat+","+lon;
		}
		
		
		String path = "http://maps.google.com/maps?saddr=";
		
		//initial point
		path += points[0];
		
		path += "&daddr=";
		
		for (int i = 1; i < list.size(); i++) {
			if (i == 1) path += points[i];
			else   path += "+to:" + points[i];
		}
		
		
		// wrap back around to final position
		path+= "+to:"+points[0];
				
		
		//open page with instructions/directions
			try {
				Scanner in = new Scanner(new File("data/template.html"));
				PrintStream out = new PrintStream("data/output"+mapCount+".html");
				System.setOut(out);
				
				while (in.hasNext()) {
					String line = in.nextLine();
					System.out.print(line);
					
					if (line.equals("<title>")) System.out.println("Path #" +mapCount);
					
					
					if (line.equals("<body>")) {
						System.out.println("<strong>In order path for driver #"+mapCount+" "
								+ "</strong><br><br><hr style='width:85%;'/>");
						System.out.println("<ol type='i'>");
						for (Location c: list) 
							System.out.println("<li>" + c.toString() +"</li>");
						System.out.println("</ol>");
					}
					System.out.println();
				}
				in.close();
				out.close();
			}
			catch (FileNotFoundException e) {
				System.out.println(e.getLocalizedMessage());
			}
			
			try {
				if (getMap) // only open google maps if client requested... but always open the order of stores to visit
					java.awt.Desktop.getDesktop().browse(java.net.URI.create(path));
				
				
				//opening html file in data folder
				File htmlFile = new File("data/output"+mapCount+".html");
				Desktop.getDesktop().browse(htmlFile.toURI());
			}
			catch (java.io.IOException e) {
				System.out.println(e.getLocalizedMessage());
			}
	}
}


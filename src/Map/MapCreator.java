package Map;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import PathFinding.Digraph;
import misc.Location;
public class MapCreator {

	public void generateMap(ArrayList<Location> list) {
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
				
		try {
//"http://maps.google.com/maps?saddr=33.542550,-112.071399&daddr=33.538090,-112.047250+to:33.523892,-112.093669+to:61.13751,-149.838";
			java.awt.Desktop.getDesktop().browse(java.net.URI.create(path));
			
			
			//opening html file in data folder
			File htmlFile = new File("data/output.html");
			Desktop.getDesktop().browse(htmlFile.toURI());
		}
		catch (java.io.IOException e) {
			System.out.println(e.getLocalizedMessage());
		}
		
		
		
		
		//open page with instructions/directions
			try {
				Scanner in = new Scanner(new File("data/template.html"));
				PrintStream out = new PrintStream("data/output.html");
				System.setOut(out);
				
				while (in.hasNext()) {
					String line = in.nextLine();
					System.out.println(line);
					
					if (line.equals("<title>")) System.out.println("Map 1");
					
					if (line.equals("<body>")) {
						for (Location c: list) 
							System.out.println(c.toString() + "<br>");
					}
				}
				in.close();
				out.close();
			}
			catch (FileNotFoundException e) {
				System.out.println(e.getLocalizedMessage());
			}
	}
}


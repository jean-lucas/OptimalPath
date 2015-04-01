package Map;

import java.util.ArrayList;

import FileOp.FileOperator;
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
			//else path += points[i];
//			if (i < points.length - 1)
		}
		
		path+= "+to:"+points[0];
				
		try {
			String url3 = "http://maps.google.com/maps?saddr=33.542550,-112.071399&daddr=33.538090,-112.047250+to:33.523892,-112.093669+to:61.13751,-149.838";
			java.awt.Desktop.getDesktop().browse(java.net.URI.create(path));
		}
		catch (java.io.IOException e) {
			System.out.println(e.getLocalizedMessage());
		}
	}

}

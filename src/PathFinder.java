import java.util.ArrayList;


/**
 * 
 * Will use the nearest neighbour algorithm to find the optimal path
 * in each section provided by AreaDivider()
 */
public class PathFinder {

	
	
	/**
	 * For now arrayList will be used.. but MUST be changed later for better algorithm
	 * 
	 * This is for one subsection
	 */
	public void NearestNeighbour(ArrayList<Location> stores) {
		int size = stores.size();
		if (size == 0) return;
		
		Location[] inOrderPath = new Location[size];
		Location firstStore = stores.get(0);		//TODO: firstStore should be the one CLOSEST to the BASE
		
		double[][] allDistances = new double[stores.size()][stores.size()];
		
		//construct matrix of distances
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				allDistances[i][j] = stores.get(i).getDistance(stores.get(j));
			}
		}
		
		for (int i = 0; i < size; i++) {
			
			
		}
		
		
	}
}

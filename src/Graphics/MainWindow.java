package Graphics;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import misc.Location;
import ExceptionChecks.EmptyListException;
import ExceptionChecks.InconsistentDatasetException;
import ExceptionChecks.InvalidNumberOfDriversException;
import FileOp.FileOperator;
import PathFinding.AreaDivider;
import PathFinding.PathFinder;

/**
 * TODO: Fix the size of radius input box
 * TODO: Add a title to the GUI window
 * TODO: Remove static declarations
 * 
 *
 */
public class MainWindow extends JFrame {
	

	/* ******  Static Declarations ******* */
	
	private static final long serialVersionUID = 1L;
	private static String[] defaultStores = {"Starbucks","McDonalds","HomeDepot","WalMart"};
	private static Map<String, LinkedHashSet<String>> citiesByState;		//used to represent a list of cities by respecitve state
	private static String store;
	private static MainWindow win;
	
	
	
	
	/* ******  Non-Static Declarations ******* */
	
  private String[] defaultCityList = {"Please pick a state"};
  
	private  String [] states = {"--","AL","AK","AZ","AR","CA","CO","CT","DE","FL","GA"
			,"HI","ID","IL","IN","IA","KS","KY","LA","ME",
			"MD","MA","MI","MN","MS","MO","MT","NE","NV",
			"NH","NM","NY","NC","ND","OH","OK","OR","PA","RI","SC","SD","TN",
			"TX","UT","VT","VA","WA","WV","WI","WY"};
	
	private JButton goButton = new JButton("GO");
	
	private JButton changeStore = new JButton("Change Store");
	
	private JLabel cityLabel = new JLabel("City:");
	private JLabel stateLabel = new JLabel("State:");
	private JLabel driverLabel = new JLabel("Number of Drivers");
	private JLabel radiusLabel = new JLabel("Radius (km):");
	
	
	private JRadioButton driverOption1 = new JRadioButton("1");
	private JRadioButton driverOption2 = new JRadioButton("2");
	private JRadioButton driverOption3 = new JRadioButton("4");
	
	private JCheckBox mapCheckButton = new JCheckBox("Show Map");
	
	private JTextField radiusField = new JTextField(15); 
	private JComboBox<String> cityField = new JComboBox<String>(defaultCityList);
	
	private JMenuItem exitMI = new JMenuItem("Exit");
	private JMenuItem storeMI = new JMenuItem("Set Store");
	private JMenuItem instructMI = new JMenuItem("Instructions");
	private JComboBox<String> statesBox = new JComboBox<>(states); 
	
	private JMenuItem importFile = new JMenuItem("Import File");
	
	//constructor
	private MainWindow(){
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(storeMI);
		fileMenu.add(importFile);
		fileMenu.add(exitMI);
		
		
		JMenu helpMenu = new JMenu("Help");
		helpMenu.add(instructMI); 
		
		JMenuBar windowBar = new JMenuBar(); 
		windowBar.add(fileMenu);
		windowBar.add(helpMenu);
		setJMenuBar(windowBar); 
		

		ButtonGroup driverGroup = new ButtonGroup();
		driverGroup.add(driverOption1);
		driverGroup.add(driverOption2);
		driverGroup.add(driverOption3);
		
		JPanel driversPanel = new JPanel(new GridLayout(4,1));
		driversPanel.add(driverLabel);
		driversPanel.add(driverOption1);
		driversPanel.add(driverOption2);
		driversPanel.add(driverOption3);
		
		JPanel locationPanel = new JPanel(new GridLayout(1,1));
		locationPanel.add(stateLabel);
		locationPanel.add(statesBox);
		locationPanel.add(new JLabel());
		locationPanel.add(cityLabel);
		locationPanel.add(cityField);
		
		
		
		JPanel mapPanel = new JPanel(new GridLayout(3,2));
		mapPanel.add(radiusLabel);
		mapPanel.add(radiusField);
		mapPanel.add(mapCheckButton);
		mapPanel.add(Box.createVerticalStrut(20));
		mapPanel.add(changeStore);
		
		
		
		
		Container container = getContentPane();
		container.add(locationPanel,BorderLayout.NORTH); 
		container.add(driversPanel,BorderLayout.WEST);
		container.add(mapPanel,BorderLayout.EAST);
		container.add(goButton,BorderLayout.SOUTH);
		
		
		
/**************************************************
 * 	Action Listeners and their implementation		
 ***************************************************/
		
		
		storeMI.addActionListener(new StoreSelectListener()); 
		exitMI.addActionListener(new QuitListener());
		instructMI.addActionListener(new InstructListner());
		statesBox.addActionListener(new StateBoxListener());
		goButton.addActionListener(new goListener());
		importFile.addActionListener(new importFileListener());
		changeStore.addActionListener(new ChangeStoreListener());
		
	}
	
	//  ********    Change Stores Button     ********
	// When clicked, the program will simply restart, and re-prompt user to pick a new store.
	private class ChangeStoreListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			win.dispose();		//close the current window
			main(null);				//and restart
		}
	}
	
	
	//TODO: implement this function..
	// ********    Importing a new file into the program   ***********
	
	private class importFileListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			addTextFile();
		}
	}
	
	
	
	// ********    GO BUTTON   ***********
	
	private class goListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			try {
				String fileName = store +"_locations.txt";
				String cityName = (String) cityField.getSelectedItem();
				String state =  (String) statesBox.getSelectedItem();
			
				boolean getMap = mapCheckButton.isSelected();
				
				int radius = Integer.parseInt(radiusField.getText());
				int mapCount = 1;	//keeps track which map is being displayed
				int numOfDrivers = 0;
				if (driverOption1.isSelected())  numOfDrivers = 1;
				if (driverOption2.isSelected())  numOfDrivers = 2;
				if (driverOption3.isSelected())  numOfDrivers = 4;
				
				// check valid radius
				if (radius <= 0 || radius > 80) throw new IllegalArgumentException();
				
				// check valid number of drivers
				if (numOfDrivers < 1 || numOfDrivers > 4) throw new InvalidNumberOfDriversException();
	
				
				FileOperator fileOp = new FileOperator(fileName, cityName, state, store);
				Location center = fileOp.getCityLocation();		
			 
				//get ALL valid stores to visit
				ArrayList<Location> validStores = fileOp.getStoreInRadius(center, radius);	

				// this will create a list of list of valid stores to visit
				AreaDivider area = new AreaDivider(numOfDrivers, validStores, center);	
		
				//checking for valid sections
				if (area.getSections().isEmpty()) throw new EmptyListException();
				for (ArrayList<Location> section: area.getSections()) {
					if (section.isEmpty()) continue;
					
					new PathFinder(section, center, getMap, mapCount++);
				}
			}
			
			
			
			// 	********    EXCEPTION HANDLING   ************** //
			
			catch (InvalidNumberOfDriversException e1) {
				JOptionPane.showMessageDialog(null, "Please select number of drivers!");
			}
			catch (InconsistentDatasetException e1) {
				JOptionPane.showMessageDialog(null, "Our apologies!\nThe city selected was not \n"
																							+ "found in our cities database.");
			}
			catch (EmptyListException e1) {
				JOptionPane.showMessageDialog(null, "No stores were found in this city radius. \n"
																						+ "Try again with a bigger radius or a new city.");
			}
			catch (IllegalArgumentException e1) {
				JOptionPane.showMessageDialog(null, "Radius must be between 1 and 30. ");
			}
			catch (Exception e1) {
				JOptionPane.showMessageDialog(null, "Something went wrong! \nPlease check all inputs.");
			}
		}
	}
		
	
	
	
	// ********    STATE SELECTION   ***********
	
	//Auto Fills the cityField ComboBox depending on current state selected
	private class StateBoxListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
				
			try {
				String stateSelected = (String) statesBox.getSelectedItem();
				LinkedHashSet<String> cities = citiesByState.get(stateSelected);		// get the arrayList of all cities repr. by stateSelected

				//clear the field, and add all new city names
				cityField.removeAllItems();		
				for (String c: cities) 
					cityField.addItem(c);
			}
			
			catch (Exception e1) {
				cityField.addItem("No cities found...");
			}
		}
	}
	
	
	
	// quit button
	private class QuitListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			System.exit(0);
			
			}
		}
		
	
	// help button
	private class InstructListner implements ActionListener{
		public void actionPerformed(ActionEvent e){
			JOptionPane.showMessageDialog(null,
					"Coming Soon...");
			}
		}
	
	
	
	// change stores button
	private class StoreSelectListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			
			// close the window and restart program
			win.dispose();
			main(null);
		}
	}
	
	
	/**
	 * Import textfile through GUI
	 */
	private void addTextFile() {
		
		
		JOptionPane.showMessageDialog(null, "All textfile MUST follow the format:\n\n"
																	+ "XX, cityName, address, latitude, longitude\n\n"
																	+ "where XX is state abbreviatation");
		
//		JFileChooser fileChooser = new JFileChooser();
//    int response = fileChooser.showOpenDialog(null);
//   
//    if (response == JFileChooser.APPROVE_OPTION) {
//      File file = fileChooser.getSelectedFile();
//      String filePath = "data/" + file.getName();
//      
//      try {
//        Scanner in = new Scanner(new FileReader(file));
//        PrintStream out = new PrintStream(filePath);
//        System.setOut(out);
//        
//        int lineCount = 0;
//        while(in.hasNext()) {
//        	System.out.println(in.nextLine());
//        	lineCount++;
//        }
//        
//        RandomAccessFile f = new RandomAccessFile(new File(filePath), "rw");
//        f.seek(0); // to the beginning
//        f.write(("NUMBER OF ENTRIES: "+lineCount).getBytes());
//        
//        f.close();
//        in.close();
//        out.close();
//      }
//      catch (IOException e1 ) {
//      	System.out.println(e1.getLocalizedMessage());
//      	throw new Error();
//      }
//
//      }
	}
	
	
	
	
	
	
	
	// Instantiation of the program

	public static void main(String[] args) {
		
		win = new MainWindow();
		
		 //the following code prompts the user for their store before actually going to the program 
		store = (String)JOptionPane.showInputDialog(
				null,
				"Select your store",
				"Store Selection",
				JOptionPane.PLAIN_MESSAGE,
				null, defaultStores,
				"Starbucks");
		
		try {  
			store = store.toLowerCase(); 		// Client pressed X or Cancel, so quit the application
			
			//populate the citesByState based on which store was selected
			String fileName = store + "_locations.txt";
			FileOperator fOp = new FileOperator(fileName); // this fOp should ONLY be used for constructing the citiesByState map
			citiesByState = fOp.getAllCitiesByState();
		}
		
		
		//exit the program 
		catch (NullPointerException e) {
			return;
		}
		
			win.setVisible(true);
			win.setTitle("Optimal Delivery Path" +  " - " + store.toUpperCase());
			win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
//			win.setSize(500, 500);
			win.setResizable(false);
			win.pack();
	}
	
	
	
}
	
	
	



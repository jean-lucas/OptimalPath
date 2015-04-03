package Graphics;
/*************************************
 I've deicded not to include the program title in the main part of the GUI 
 because I believe that it would look better in the window's title.
 *************************************/
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import misc.Location;
import FileOp.FileOperator;
import PathFinding.AreaDivider;
import PathFinding.PathFinder;


public class MainWindow extends JFrame {
	public static String store;
	public static Object[] Stores = {"Starbucks","McDonalds","HomeDepot","WalMart"};
	public static void main(String[] args) {
		 //the following code prompts the user for their store before actually going to the program 
		store = (String)JOptionPane.showInputDialog(
				null,
				"Select your store",
				"Store Selection",
				JOptionPane.PLAIN_MESSAGE,
				null, Stores,
				"Starbucks");
		store = store.toLowerCase();
		
		MainWindow test = new MainWindow();
			test.setVisible(true);
			test.setTitle("Optimal Delivery Path");
			test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			test.setSize(250, 250);
			test.setResizable(false);
			test.pack();
			

	}
	private static final long serialVersionUID = 1L;
	
  String[] defaultCityList = {"Please pick a state"};
  String[] defaultStores = {"Starbucks","McDonalds","HomeDepot","WalMart"};
  
	private  String [] states = {"--","AL","AK","AZ","AR","CA","CO","CT","DE","FL","GA"
			,"HI","ID","IL","IN","IA","KS","KY","LA","ME",
			"MD","MA","MI","MN","MS","MO","MT","NE","NV",
			"NH","HJ","NM","NY","NC","ND","OH","OK","OR","PA","RI","SC","SD","TN",
			"TX","UT","VT","VA","WA","WV","WI","WY"};
	
	private JButton goButton = new JButton("GO");
	
	private JLabel cityLabel = new JLabel("City:");
	private JLabel stateLabel = new JLabel("State:");
	private JLabel driverLabel = new JLabel("Number of Drivers");
	private JLabel radiusLabel = new JLabel("Radius:");
	
	
	private JRadioButton driverOption1 = new JRadioButton("1");
	private JRadioButton driverOption2 = new JRadioButton("2");
	private JRadioButton driverOption3 = new JRadioButton("4");
	
	private JCheckBox mapCheckButton = new JCheckBox("Show Map");
	
	private JTextField radiusField = new JTextField(15); 
	private JComboBox<String> cityField = new JComboBox<String>(defaultCityList);
	private JComboBox<String> storeField = new JComboBox<String>(defaultStores);
	
	private JMenuItem exitMI = new JMenuItem("Exit");
	private JMenuItem storeMI = new JMenuItem("Set Store");
	private JMenuItem instructMI = new JMenuItem("Instructions");
	private JComboBox<String> statesBox = new JComboBox<>(states); 
	
	
	public MainWindow(){
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(storeMI);
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
		locationPanel.add(cityLabel);
		locationPanel.add(cityField);
		locationPanel.add(stateLabel);
		locationPanel.add(statesBox);
		
		JPanel storePanel = new JPanel(new GridLayout(1,1));
		storePanel.add(storeField);
		
		JPanel mapPanel = new JPanel(new GridLayout(2,2));
		mapPanel.add(radiusLabel);
		mapPanel.add(radiusField);
		mapPanel.add(mapCheckButton);
		
		
		
		
		
		
		Container container = getContentPane();
		container.add(locationPanel,BorderLayout.NORTH); 
		container.add(driversPanel,BorderLayout.WEST);
		//container.add(storePanel,BorderLayout.CENTER);
		container.add(mapPanel,BorderLayout.EAST);
		container.add(goButton,BorderLayout.SOUTH);
		
		storeMI.addActionListener(new StoreSelectListener()); 
		exitMI.addActionListener(new QuitListener());
		instructMI.addActionListener(new InstructListner());
		statesBox.addActionListener(new StateBoxListener());
		goButton.addActionListener(new goListener());
			
		
	}
	


	
	private class goListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			//String storeName = ((String) storeField.getSelectedItem()).toLowerCase();
			String fileName = store +"_locations.txt";
			//String fileName = storeName +"_locations.txt";
			String cityName = (String) cityField.getSelectedItem();
			String state =  (String) statesBox.getSelectedItem();
			int radius = Integer.parseInt(radiusField.getText());
			int numOfDrivers = 0;
			if (driverOption1.isSelected())  numOfDrivers = 1;
			if (driverOption2.isSelected())  numOfDrivers = 2;
			if (driverOption3.isSelected())  numOfDrivers = 4;
			
			boolean getMap = mapCheckButton.isSelected();

			FileOperator fileOp = new FileOperator(fileName, cityName, state, store);
			Location center = fileOp.getCityLocation();		
			
			ArrayList<Location> validStores = fileOp.getStoreInRadius(center, radius);	//get ALL valid stores to visit
			
			AreaDivider area = new AreaDivider(numOfDrivers, validStores, center);	// this will create a list of list of valid stores to visit
			
			int mapCount = 1;	//keeps track which map is being displayed
			
			for (ArrayList<Location> section: area.getSections()) {
				if (section.isEmpty()) continue;
				
				new PathFinder(section, section.get(0), getMap, mapCount++);
			}
		}
	}
		
	//Auto Fills the cityField ComboBox depending on current state selected
	private class StateBoxListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			
			String fileName = store+ "_locations.txt";
			FileOperator fOp = new FileOperator(fileName); // this fOp should ONLY be used for constructing the citiesByState map
			
		  Map<String, LinkedHashSet<String>> citiesByState = fOp.getAllCitiesByState();		//used to represent a list of cities by respecitve state
			
			String stateSelected = (String) statesBox.getSelectedItem();
			LinkedHashSet<String> cities = citiesByState.get(stateSelected);		// get the arrayList of all cities repr. by stateSelected
			
			//clear the field, and add all new city names
			cityField.removeAllItems();		
			for (String c: cities) 
				cityField.addItem(c);
		}
	}
	
	private class QuitListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			System.exit(0);
			
			}
		}
		
	private class InstructListner implements ActionListener{
		public void actionPerformed(ActionEvent e){
			JOptionPane.showMessageDialog(null,
					"Coming Soon...");
			}
		}
	private class StoreSelectListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			store = (String)JOptionPane.showInputDialog(
					null,
					"Select your store",
					"Store Selection",
					JOptionPane.PLAIN_MESSAGE,
					null, Stores,
					"Starbucks");
			store = store.toLowerCase();
		}
	}
	
	
	
	
}


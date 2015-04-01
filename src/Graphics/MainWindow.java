package Graphics;
/*************************************
 I've deicded not to include the program title in the main part of the GUI 
 because I believe that it would look better in the window's title.
 *************************************/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import FileOp.FileOperator;


public class MainWindow extends JFrame {
	public static void main(String[] args) {

		MainWindow test = new MainWindow();
			test.setVisible(true);
			test.setTitle("Optimal Delivery Path");
			test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			test.setSize(500, 500);
			test.pack();
			

	}
	private static final long serialVersionUID = 1L;
	
	FileOperator fOp = new FileOperator("cities_usa.txt"); // this fOp should ONLY be used for constructing the citiesByState map
	
  String[] defaultCityList = {"Please pick a state"};
	
  Map<String, ArrayList<String>> citiesByState = fOp.getAllCitiesByState();		//used to represent a list of cities by respecitve state
  
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
	
	private JTextField radiusField = new JTextField(); 
	private JComboBox<String> cityField = new JComboBox<String>(defaultCityList);
	
	private JMenuItem exitMI = new JMenuItem("Exit");
	private JMenuItem instructMI = new JMenuItem("Instructions");
	private JComboBox<String> statesBox = new JComboBox<>(states); 
	
	
	public MainWindow(){
		JMenu fileMenu = new JMenu("File");
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
		

		
		JPanel mapPanel = new JPanel(new GridLayout(2,2));
		mapPanel.add(radiusLabel);
		mapPanel.add(radiusField);
		mapPanel.add(mapCheckButton);
		
		Container container = getContentPane();
		container.add(locationPanel,BorderLayout.NORTH); 
		container.add(driversPanel,BorderLayout.WEST);
		container.add(mapPanel,BorderLayout.EAST);
		container.add(goButton,BorderLayout.SOUTH);
		
		exitMI.addActionListener(new QuitListener());
		instructMI.addActionListener(new InstructListner());
		statesBox.addActionListener(new StateBoxListener());
			
		
	}

	//Auto Fills the cityField ComboBox depending on current state selected
	private class StateBoxListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			
			String stateSelected = (String) statesBox.getSelectedItem();
			ArrayList<String> cities = citiesByState.get(stateSelected);		// get the arrayList of all cities repr. by stateSelected
			
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
	
	
	
	
	private HashMap<String,String[]> getCitiesInState() {
		
		
		
		
		return null;
	}
	
   
	
	
	
	
}


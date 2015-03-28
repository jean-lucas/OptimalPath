package Graphics;
/*************************************
 I've deicded not to include the program title in the main part of the GUI 
 because I believe that it would look better in the window's title.
 *************************************/
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {
	public static void main(String[] args) throws FileNotFoundException {

		MainWindow test = new MainWindow();
			test.setVisible(true);
			test.setTitle("Optimal Delivery Path");
			test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			test.setSize(500, 500);
			test.pack();
			

	}
	private static final long serialVersionUID = 1L;
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
	private JRadioButton driverOption3 = new JRadioButton("3");
	
	private JCheckBox mapCheckButton = new JCheckBox("Show Map");
	
	private JTextField radiusField = new JTextField(); 
	private JTextField cityField = new JTextField();
	
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
			
		
	
		
		
		
	}
		private class QuitListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			System.exit(0);
			
			}
		}
		
		private class InstructListner implements ActionListener{
			public void actionPerformed(ActionEvent e){
				JOptionPane.showMessageDialog(null,
						"A simple connect four game");
			}
		}
	
	
	
   
	
	
	
	
}


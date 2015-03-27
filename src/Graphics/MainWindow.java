package Graphics;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.swing.*;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	public MainWindow() {
		setupWindow();

	}
	
	private void setupWindow() {
		setTitle("Optimal Delivery Path");
		setSize(800,500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
   
	
	public static void main(String[] args) throws FileNotFoundException {
		MainWindow test = new MainWindow();
			test.setVisible(true);
			

	}
}


package DAC;

import javax.swing.JOptionPane;

public class DAC {
	public static void main(String[] args) {
		String planeID = JOptionPane.showInputDialog("ID: ");
		
		
		DACApp app = new DACApp(DACApp.STYLE.GRAPHIC, planeID);
		app.run();
	}
}

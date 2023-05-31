package DAC.graphic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import DAC.DACApp;
import DAC.data.PlaneData;
import DAC.data.State;
import DAC.graphic.plane.PlaneJPanel;
import utils.structures.Pair;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	DACApp app;
	
	private int width = 1080;
	private int height = 720;
	private MainFrameActionListener actionListener;
	
	private JPanel toolsBar;
	private int toolsBarHeight = 25;
	
	private JPanel mainPanel;
	private PlaneJPanel planePanel;
	
	public MainFrame(DACApp app) {
		this.app = app;
		
		windowSetup();
		prepareListener();
		loadToolsBar();
		loadPanels();
	}
	
	private void windowSetup() {
		setResizable(false);
		setUndecorated(true);
		setLayout(null);
		
		setTitle("DAC - Distributed Air Control");
		
		setBackground(Color.RED);
		setSize(width, height);
		setPreferredSize(new Dimension(width,height));
		setLocationRelativeTo(null);
		
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void prepareListener() {
		actionListener = new MainFrameActionListener();
	}
	
	private void loadToolsBar() {
		int w = 0, h = 0,
			x = 0, y = 0;
		
		
		
		w = getPercentageWidth(100); h = toolsBarHeight;
		x = 0; y = 0;
		
		toolsBar = new JPanel();
		toolsBar.setSize(w, h);
		toolsBar.setPreferredSize(new Dimension(w, h));
		toolsBar.setLocation(x, y);
		// toolsBar.setBackground(Color.pink);
		
		
		w = getPercentageWidth(8);
		h = toolsBarHeight;
		x = width-w;
		y = 0;
		
		JButton closeB = new JButton("X");
		closeB.setActionCommand(MainFrameActionListener.CMD.EXIT.getValue());
		closeB.addActionListener(actionListener);
		closeB.setSize(w, h);
		closeB.setPreferredSize(new Dimension(w, h));
		closeB.setLocation(x, y);
		closeB.setBackground(Color.RED);
		toolsBar.add(closeB);
		
		add(toolsBar);
	}
	
	private void loadPanels() {
		int w = getPercentageWidth(76), h = getPercentageHeight(80),
			x = getPercentageWidth(12), y = getPercentageHeight(10) + toolsBarHeight/2;
		
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setSize(w,h);
		mainPanel.setPreferredSize(new Dimension(w,h));
		mainPanel.setLocation(x,y);
		mainPanel.setBackground(Color.GREEN);
		
		mainPanel.add(new DACOptions(this,w,h));
		
		add(mainPanel);
	}
	
	private int getPercentageWidth(double percentage) {
		return (int)((width*percentage)/100.0);
	}
	
	private int getPercentageHeight(double percentage) {
		return (int)((height*percentage)/100.0);
	}
	
	public void showMainMenuPanel() {
		System.out.println("Enseñame el menu del inicio");
		mainPanel.removeAll();
		mainPanel.add(new DACOptions(this, mainPanel.getWidth(), mainPanel.getHeight()));
		// mainPanel.repaint();
		repaint();
	}
	
	
	public void showPlanePanel() {
		mainPanel.removeAll();
		planePanel = new PlaneJPanel(app, this, mainPanel.getWidth(), mainPanel.getHeight());
		mainPanel.add(planePanel);
		// mainPanel.repaint();
		repaint();
	}
	
	public void showNodePanel() { // SOLO ON y OFF y Return
		planePanel = null;
		mainPanel.removeAll();
		mainPanel.repaint();
	}
	
	public void showVisorPanel() {
		planePanel = null;
		mainPanel.removeAll();
		// mainPanel.add(new VisorPanel(app, this, mainPanel.getWidth(), mainPanel.getHeight()));
		mainPanel.repaint();
	}
	
	
	private class MainFrameActionListener implements ActionListener {
		public enum CMD {
			EXIT("EXIT");
			
			private String value;
			
			private CMD(String str) {
				value = str;
			}
			
			public String getValue() { 
				return value;
			}
		};
		
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals(CMD.EXIT.getValue())) {
				System.exit(0);
			}
		}
		
	}
	
	public void updateStateAndData(Pair<State,PlaneData> data) {
		planePanel.updateStateAndData(data);
	}
}

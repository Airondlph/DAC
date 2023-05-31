package DAC.graphic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import DAC.graphic.tools.Fonts;

public class DACOptions extends JPanel {
	private static final long serialVersionUID = 1L;
	private int width;
	private int height;
	
	private DACOptionsActionListener actionListener;
	
	private MainFrame parent;
	
	public DACOptions(MainFrame parent, int width, int height) {
		this.width = width;
		this.height = height;
		
		this.parent = parent; 
		actionListener = new DACOptionsActionListener();
		
		panelSetup();
		loadOptions();
	}
	
	private void panelSetup() {
		setSize(width,height);
		setPreferredSize(new Dimension(width,height));
		setLocation(0,0);
		setLayout(null);
		setBackground(Color.BLUE);
	}
	
	private  void loadOptions() {
		int w = getPercentageWidth(80), h = 60;
		int marginLeft = getPercentageWidth(10), marginTop = 10;
		
		int nOptions = 3;
		int x = marginLeft, y = (height - (nOptions * (h + marginTop)))/2;
		
		Font optionsFont = Fonts.getComicSansMS();
		if(optionsFont == null) optionsFont = new Font("Arial", Font.PLAIN, 40);
		optionsFont = optionsFont.deriveFont(30.0f);
		
		JButton opt1 = new JButton("Plane");
		opt1.setActionCommand(DACOptionsActionListener.OPTION.PLANE.getValue());
		opt1.addActionListener(actionListener);
		opt1.setSize(w,h);
		opt1.setPreferredSize(new Dimension(w,h));
		opt1.setLocation(x, y);
		y += (marginTop + h);		
		opt1.setBackground(Color.white);
		opt1.setFont(optionsFont);
		add(opt1);
		
		/*
		JButton opt2 = new JButton("Node");
		opt2.setActionCommand(DACOptionsActionListener.OPTION.NODE.getValue());
		opt2.addActionListener(actionListener);
		opt2.setSize(w,h);
		opt2.setPreferredSize(new Dimension(w,h));
		opt2.setLocation(x, y);
		y += (marginTop + h);		
		opt2.setBackground(Color.white);
		opt2.setFont(optionsFont);
		add(opt2);
		
		
		JButton opt3 = new JButton("Visor");
		opt3.setActionCommand(DACOptionsActionListener.OPTION.VISOR.getValue());
		opt3.addActionListener(actionListener);
		opt3.setSize(w,h);
		opt3.setPreferredSize(new Dimension(w,h));
		opt3.setLocation(x, y);
		y += (marginTop + h);		
		opt3.setBackground(Color.white);
		opt3.setFont(optionsFont);
		add(opt3);
		*/
	}
	
	
	private int getPercentageWidth(double percentage) {
		return (int)((width*percentage)/100.0);
	}
	
	
	private class DACOptionsActionListener implements ActionListener {
		public enum OPTION {
			PLANE("PLANE"),
			NODE("NODE"),
			VISOR("VISOR");
			
			private String value;
			
			private OPTION(String str) {
				value = str;
			}
			
			public String getValue() { 
				return value;
			}
		};
		

		public DACOptionsActionListener() {}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals(OPTION.PLANE.getValue())) {
				parent.showPlanePanel();
			} else if(e.getActionCommand().equals(OPTION.NODE.getValue())) {
				parent.showNodePanel();
			}  else if(e.getActionCommand().equals(OPTION.VISOR.getValue())) {
				parent.showVisorPanel();
			}
		}
		
	}
	
}

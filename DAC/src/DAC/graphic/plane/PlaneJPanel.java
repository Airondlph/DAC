package DAC.graphic.plane;

import DAC.DACApp;
import DAC.data.Entry;
import DAC.data.ID;
import DAC.data.IDTypes;
import DAC.data.PlaneData;
import DAC.data.PlaneID;
import DAC.data.State;
import DAC.graphic.MainFrame;
import DAC.graphic.tools.Fonts;
import utils.structures.Pair;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;



public class PlaneJPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private int width;
	private int height;
	
	private PlaneJPanelActionListener actionListener;
	
	private DACApp app;
	private MainFrame parent;
	
	
	
	public PlaneJPanel(DACApp app, MainFrame parent, int width, int height) {
		this.app = app;
		this.parent = parent;
		
		this.width = width;
		this.height = height;
		
		actionListener = new PlaneJPanelActionListener();
		
		app.startPlaneNode((Pair<State,PlaneData> data) -> { updateStateAndData(data); return null; });
		
		
		setupPanel();
		showOptions(app.getState());
	}
	
	private void setupPanel() {
		setSize(width,height);
		setPreferredSize(new Dimension(width,height));
		setLocation(0,0);
		setLayout(null);
		setBackground(Color.BLUE);
	}
	
	
	/**
	 * List of <Button text, Action>
	 * @param options
	 */
	private void addOptions(ArrayList<Pair<String,PlaneJPanelActionListener.ACTION>> options) {
		int w = getPercentageWidth(40), h = 50;
		int marginLeft = getPercentageWidth(8), marginRight = getPercentageWidth(-4), marginTop = 20;
		
		int nOptions = 9;
		int x = marginLeft, y = 140 + (height - (((int)Math.floor(nOptions/2)) * (h + marginTop)))/2;
		int pos = 0;
		
		Font optionsFont = Fonts.getComicSansMS(22);
		if(optionsFont == null) optionsFont = new Font("Arial", Font.PLAIN, 22);
		
		for(Pair<String, PlaneJPanelActionListener.ACTION> opt : options) {
			x = (pos%2 == 0) ? marginLeft : (marginLeft + w + marginRight + marginLeft);
			y = 140 + ((((int)Math.floor(pos/2)) + 1) * (marginTop + h));
			++pos;
			JButton optB = new JButton(opt.getFirst());
			optB.setActionCommand(opt.getSecond().getValue());
			optB.addActionListener(actionListener);
			optB.setSize(w,h);
			optB.setPreferredSize(new Dimension(w,h));
			optB.setLocation(x, y);	
			optB.setBackground(Color.white);
			optB.setFont(optionsFont);
			add(optB);
		}
	}
	
	private void addData(PlaneData data) {
		int w = getPercentageWidth(80), h = 30;
		int marginLeft = getPercentageWidth(10), marginTop = 40;
		int x = marginLeft, y = 20;
		
		JPanel dataPanel = new JPanel();
		dataPanel.setSize(w, 130);
		dataPanel.setPreferredSize(new Dimension(w, 130));
		dataPanel.setLocation(x,25);
		dataPanel.setBackground(Color.WHITE);
		x = 15;
		
		
		Font dataFont = Fonts.getComicSansMS(18);
		if(dataFont == null) dataFont = new Font("Arial", Font.PLAIN, 30);
		
		
		JLabel parking;
		if(data.parkingID() != null) {
			parking = new JLabel("ID del Parking: " + data.parkingID().toString());
		} else {
			parking = new JLabel("ID del Parking: NO ASIGNADO");
		}
		parking.setFont(dataFont);
		parking.setSize(w,h);
		parking.setPreferredSize(new Dimension(w,h));
		parking.setLocation(x,y);
		parking.setFont(dataFont);
		dataPanel.add(parking);
		
		y = y + marginTop*2/3;

		JLabel st = new JLabel("STATE: ", SwingConstants.CENTER);
		st.setFont(dataFont);
		st.setSize(150,h);
		st.setPreferredSize(new Dimension(150,h));
		st.setLocation(getPercentageWidth(60),y);
		st.setFont(dataFont);
		st.setForeground(Color.BLACK);
		st.setBackground(Color.orange);
		st.setOpaque(true);
		dataPanel.add(st);
		
		y = y + marginTop*1/3;
		
		JLabel runway;
		if(data.runwayID() != null) {
			runway = new JLabel("ID de la pista de despegue/aterrizaje: " + data.runwayID().toString());
		} else {
			runway = new JLabel("ID de la pista de despegue/aterrizaje: NO ASIGNADO");
		}
		runway.setFont(dataFont);
		runway.setSize(w,h);
		runway.setPreferredSize(new Dimension(w,h));
		runway.setLocation(x,y);
		runway.setFont(dataFont);
		dataPanel.add(runway);
		
		
		JLabel st2 = new JLabel(app.getState().toString(), SwingConstants.CENTER);
		st2.setFont(dataFont);
		st2.setSize(150,h);
		st2.setPreferredSize(new Dimension(150,h));
		st2.setLocation(getPercentageWidth(60),y+10);
		st2.setFont(dataFont);
		st2.setForeground(Color.BLACK);
		st2.setBackground(Color.orange);
		st2.setOpaque(true);
		dataPanel.add(st2);
		
		y = y + marginTop;
		
		JLabel rolling;
		if(data.rollingID() != null) {
			rolling = new JLabel("ID de la pista de rodaje: " + data.rollingID().toString());
		} else {
			rolling = new JLabel("ID de la pista de rodaje: NO ASIGNADO");
		}
		rolling.setFont(dataFont);
		rolling.setSize(w,h);
		rolling.setPreferredSize(new Dimension(w,h));
		rolling.setLocation(x,y);
		rolling.setFont(dataFont);
		dataPanel.add(rolling);
			
		y = y + marginTop;

		this.add(dataPanel);
	}
	
	private void showOptions(State state) {
		ArrayList<Pair<String,PlaneJPanelActionListener.ACTION>> options;
		options = new ArrayList<>();
		
		if(state == null || state.equals(State.BYE)) {
			Font startFont = Fonts.getComicSansMS(30);
			if(startFont == null) startFont = new Font("Arial", Font.PLAIN, 30);
			
			int w = getPercentageWidth(80), h = 80;
			int marginLeft = getPercentageWidth(10), marginTop = 150;
			
			JButton opt = new JButton("START");
			opt.setActionCommand(PlaneJPanelActionListener.ACTION.HELLO_CONTROL.getValue());
			opt.addActionListener(actionListener);
			opt.setSize(w,h);
			opt.setPreferredSize(new Dimension(w,h));
			opt.setLocation(marginLeft, marginTop);	
			opt.setBackground(Color.white);
			opt.setFont(startFont);
			add(opt);
			

		} else {
			if(state.equals(State.FLYING)) {
				options.add(new Pair<>("Land", PlaneJPanelActionListener.ACTION.LAND));	
			} else if(state.equals(State.LANDING)) {
				options.add(new Pair<>("At Runway", PlaneJPanelActionListener.ACTION.AT_RUNWAY));
				options.add(new Pair<>("Take Off", PlaneJPanelActionListener.ACTION.TAKE_OFF));
			} else if(state.equals(State.AT_RUNWAY)) {
				options.add(new Pair<>("Parking Request", PlaneJPanelActionListener.ACTION.PARKING_REQUEST));
				options.add(new Pair<>("Take Off", PlaneJPanelActionListener.ACTION.TAKE_OFF));
			} else if(state.equals(State.ROLLING)) {
				options.add(new Pair<>("Rolling to Parking", PlaneJPanelActionListener.ACTION.ROLLING_TO_PARKING));
				options.add(new Pair<>("Parked", PlaneJPanelActionListener.ACTION.PARKED));
				options.add(new Pair<>("Rolling to Runway", PlaneJPanelActionListener.ACTION.ROLLING_TO_RUNWAY));
				options.add(new Pair<>("At Runway", PlaneJPanelActionListener.ACTION.AT_RUNWAY));
			} else if(state.equals(State.PARKED)) {
				options.add(new Pair<>("Runway Request", PlaneJPanelActionListener.ACTION.RUNWAY_REQUEST));
			} else if(state.equals(State.TAKING_OFF)) {
				options.add(new Pair<>("On Air", PlaneJPanelActionListener.ACTION.ON_AIR));
				options.add(new Pair<>("Land", PlaneJPanelActionListener.ACTION.LAND));
			}
			
			options.add(new Pair<>("Bye", PlaneJPanelActionListener.ACTION.BYE));
			
		}
		
		addOptions(options);
		repaint();
	}
	
	public void updateStateAndData(Pair<State,PlaneData> data) {		
		State state = data.getFirst();
		if(state == null) {
			removeAll();
			showOptions(app.getState());
			repaint();
			return;
		}
		
		
		PlaneData planeData = data.getSecond();	
		if(planeData.inavlidTransition()) {
			JOptionPane.showMessageDialog(null, "Transicion Invalida");
			return;
			
		} else if(planeData.hasToWait()) {			
			if(app.getState() == State.FLYING) 	JOptionPane.showMessageDialog(null, "Ninguna pista de aterrizaje o parking esta libre.");
			else if(app.getState() == State.PARKED) JOptionPane.showMessageDialog(null, "Ninguna pista de despegue esta libre");
			else 									JOptionPane.showMessageDialog(null, "Ninguna pista de rodaje esta libre");
			
		}
		
		removeAll();
		addData(planeData);
		showOptions(app.getState());
		repaint();
	}
	
	
	private int getPercentageWidth(double percentage) {
		return (int)((width*percentage)/100.0);
	}
		
	
	private class PlaneJPanelActionListener implements ActionListener {
		public enum ACTION {
			LAND("LAND"),
			AT_RUNWAY("AT_RUNWAY"),
			PARKING_REQUEST("PARKING_REQUEST"),
			ROLLING_TO_PARKING("ROLLING_TO_PARKING"),
			ROLLING_TO_RUNWAY("ROLLING_TO_RUNWAY"),
			PARKED("PARKED"), 
			RUNWAY_REQUEST("RUNWAY_REQUEST"),
			TAKE_OFF("TAKE_OFF"),
			ON_AIR("ON_AIR"),
			HELLO_CONTROL("HELLO_CONTROL"),
			BYE("BYE");
			
			private String value;
			
			private ACTION(String str) {
				value = str;
			}
			
			public String getValue() { 
				return value;
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals(ACTION.LAND.getValue())) app.next(Entry.LAND);
			else if(e.getActionCommand().equals(ACTION.AT_RUNWAY.getValue())) app.next(Entry.AT_RUNWAY);
			else if(e.getActionCommand().equals(ACTION.PARKING_REQUEST.getValue())) app.next(Entry.PARKING_REQUEST);
			else if(e.getActionCommand().equals(ACTION.PARKED.getValue())) app.next(Entry.PARKED);
			else if(e.getActionCommand().equals(ACTION.RUNWAY_REQUEST.getValue())) app.next(Entry.RUNWAY_REQUEST);
			else if(e.getActionCommand().equals(ACTION.TAKE_OFF.getValue())) app.next(Entry.TAKE_OFF);
			else if(e.getActionCommand().equals(ACTION.ON_AIR.getValue())) app.next(Entry.ON_AIR);
			else if(e.getActionCommand().equals(ACTION.HELLO_CONTROL.getValue())) app.next(Entry.ON_AIR);
			else if(e.getActionCommand().equals(ACTION.BYE.getValue())) app.next(Entry.OUT);
		}
		
	}
}

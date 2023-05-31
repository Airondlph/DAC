package DAC;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Function;

import DAC.data.Entry;
import DAC.data.ID;
import DAC.data.IDTypes;
import DAC.data.ParkingID;
import DAC.data.PlaneData;
import DAC.data.PlaneID;
import DAC.data.RollingData;
import DAC.data.RollingID;
import DAC.data.RunwayID;
import DAC.data.State;
import DAC.data.StateMachineConfig;
import DAC.graphic.MainFrame;
import MyRa.ComplexStateMachineNode;
import MyRa.data.ServerID;
import utils.structures.Pair;

public class DACApp {
	public enum STYLE {TERMINAL, GRAPHIC};
	
	
	private StateMachine stateMachine;
	private PlaneID planeID;
	private Function<Pair<State,PlaneData>, Void> stateChangesCallback;
	private Function<Pair<PlaneID,Pair<State,HashMap<IDTypes,ID>>>, Void> visorCallback;
	
	private STYLE style;
	
	private ComplexStateMachineNode<PlaneID,State,Entry> node;
	
	
	
	public DACApp() {
		this.style = STYLE.TERMINAL;
	}
	
	public DACApp(STYLE style, String planeID) {
		this.planeID = new PlaneID(planeID);
		this.style = style;
	}
	
	private MainFrame mainFrame;
	
	public void run() {
		if(style.equals(STYLE.TERMINAL)) {
			System.out.println("Modo terminal no desarrollado");
		} else if(style.equals(STYLE.GRAPHIC)) {
			mainFrame = new MainFrame(this);
		}
	}
	
	public void startMyRANode(String MyRaID) {
		// If MyRA node is not init, start it
		if(node != null) return;
 
		
		StateMachineConfig stateMachineConf = loadConf();
		stateMachine = new StateMachine(stateMachineConf, (v) -> { advertisedCallback(); return null; } );
		
		MyRa.data.Configuration nodeConf = new MyRa.data.Configuration();
		nodeConf.serverID = new ServerID(MyRaID);
		nodeConf.socket = nodeConf.getSocket(nodeConf.serverID);
		
		
		node = new ComplexStateMachineNode<PlaneID,State,Entry>(nodeConf, stateMachine);
		node.follow();
	}
	
	public void startPlaneNode(Function<Pair<State,PlaneData>, Void> stateChangesCallback) {
		startMyRANode(planeID.toString());
		
		this.stateChangesCallback = stateChangesCallback;
	}
	
	public void startVisorNode(Function<Pair<PlaneID,Pair<State,HashMap<IDTypes,ID>>>, Void> visorCallback) {
		startMyRANode(planeID.toString());
		
		this.visorCallback = visorCallback;
	}
	

	
	
	public void next(Entry entry) {
		if(entry == null) return;
		
		if(node == null) {
			System.err.println("MyRA node its broken.");
			System.exit(-1);
		}
		
		
		System.out.println("Ref dac: " + stateMachine);
		
		node.appendEntry(new Pair<>(planeID, entry));
		
		System.out.println(entry);
		/*
		Pair<State,HashMap<IDTypes,ID>> complexState = stateMachine.next(planeID, entry);
		stateChangesCallback.apply(complexState);
		*/
	}
	
	public State getState() {
		return stateMachine.getState(planeID);
	}
	
	public void getData() {
		
	}
	
	public void advertisedCallback() {
		
		Pair<State,PlaneData> data = new Pair<>(stateMachine.getState(planeID), 
												new PlaneData(stateMachine.getParking(planeID), 
																stateMachine.getRunway(planeID), 
																stateMachine.getRolling(planeID), 
																stateMachine.hasToWait(planeID), 
																stateMachine.invalidTransition(planeID)));
		
		System.out.println("dataaaa: " + data);
		System.out.println(planeID);
		mainFrame.updateStateAndData(data);
	}
	
	public StateMachineConfig loadConf() {
		ParkingID[] parkings = new ParkingID[]{
				new ParkingID("0.0.0.1"), 
				new ParkingID("0.0.0.2"), 
				new ParkingID("0.0.0.3"),
				new ParkingID("0.0.0.4"), 
				new ParkingID("0.0.0.5"), 
				new ParkingID("0.0.0.6")};

		RunwayID[] runways = new RunwayID[]{
				new RunwayID("1.0.0.1"),
				new RunwayID("1.0.0.2"),
				new RunwayID("1.0.0.3")};

		RollingData[] rolling = new RollingData[] { 
			new RollingData(new RollingID("2.0.0.1"), 2), 
			new RollingData(new RollingID("2.0.0.2"), 2),
			new RollingData(new RollingID("2.0.0.3"), 2),
			new RollingData(new RollingID("2.0.0.4"), 2),
			new RollingData(new RollingID("2.0.0.5"), 2),
			new RollingData(new RollingID("2.0.0.6"), 2)};


	
		HashMap<RunwayID, HashSet<RollingID>> edgesRunway = new HashMap<RunwayID, HashSet<RollingID>>();
		HashSet<RollingID> aux;
		
		aux = new HashSet<RollingID>();
		aux.add(new RollingID("2.0.0.1"));
		aux.add(new RollingID("2.0.0.2"));
		aux.add(new RollingID("2.0.0.3"));
		edgesRunway.put(new RunwayID("1.0.0.1"), aux);
		
		aux = new HashSet<RollingID>();
		aux.add(new RollingID("2.0.0.2"));
		aux.add(new RollingID("2.0.0.3"));
		aux.add(new RollingID("2.0.0.4"));
		edgesRunway.put(new RunwayID("1.0.0.2"), aux);
		
		aux = new HashSet<RollingID>();
		aux.add(new RollingID("2.0.0.4"));
		aux.add(new RollingID("2.0.0.5"));
		aux.add(new RollingID("2.0.0.6"));
		edgesRunway.put(new RunwayID("1.0.0.3"), aux);
		
		
		HashMap<ParkingID, HashSet<RollingID>> edgesParking = new HashMap<ParkingID, HashSet<RollingID>>();
		aux = new HashSet<RollingID>();
		aux.add(new RollingID("2.0.0.6"));
		aux.add(new RollingID("2.0.0.4"));
		edgesParking.put(new ParkingID("0.0.0.1"), aux);
		
		aux = new HashSet<RollingID>();
		aux.add(new RollingID("2.0.0.1"));
		aux.add(new RollingID("2.0.0.3"));
		edgesParking.put(new ParkingID("0.0.0.2"), aux);
		
		aux = new HashSet<RollingID>();
		aux.add(new RollingID("2.0.0.2"));
		aux.add(new RollingID("2.0.0.5"));
		edgesParking.put(new ParkingID("0.0.0.3"), aux);
		
		aux = new HashSet<RollingID>();
		aux.add(new RollingID("2.0.0.6"));
		aux.add(new RollingID("2.0.0.4"));
		edgesParking.put(new ParkingID("0.0.0.4"), aux);
		
		aux = new HashSet<RollingID>();
		aux.add(new RollingID("2.0.0.1"));
		aux.add(new RollingID("2.0.0.3"));
		edgesParking.put(new ParkingID("0.0.0.5"), aux);
		
		aux = new HashSet<RollingID>();
		aux.add(new RollingID("2.0.0.2"));
		aux.add(new RollingID("2.0.0.5"));
		edgesParking.put(new ParkingID("0.0.0.6"), aux);
		
		
		HashMap<RollingID, HashSet<RollingID>> edgesRolling = new HashMap<RollingID, HashSet<RollingID>>();
		
		
		return new StateMachineConfig(parkings, runways, rolling, edgesRunway, edgesParking, edgesRolling);		
	}
	

}

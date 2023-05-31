package DAC;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Function;

import MyRa.data.ComplexStateMachine;
import MyRa.data.Snapshot;

import DAC.data.StateMachineConfig;
import DAC.data.Entry;
import DAC.data.State;

import DAC.data.PlaneID;
import DAC.data.ParkingID;
import DAC.data.RunwayID;
import DAC.data.RollingID;
import DAC.data.RollingData;




public class StateMachine implements ComplexStateMachine<PlaneID, State, Entry>{
	private HashMap<PlaneID,State> planesState;
	private HashMap<PlaneID,Boolean> planesWaiting, planesInvalidTransition;
	private HashMap<PlaneID,RunwayID> planesRunway;
	private HashMap<PlaneID,RollingID> planesRolling;
	private HashMap<PlaneID,ParkingID> planesParking;
	
	private HashSet<RunwayID> availableRunway;
	private HashMap<RollingID,Integer> availableRolling; // rolling id, capacity
	private HashSet<ParkingID> availableParking;
	
	// Paths
	private HashMap<RunwayID, HashSet<RollingID>> edgesRunway;
	private HashMap<RollingID, HashSet<RollingID>> edgesRolling;
	private HashMap<ParkingID, HashSet<RollingID>> edgesParking;
	
	private Function<Void,Void> advertiseCallback;
	
	
	
	public StateMachine(StateMachineConfig conf) {
		this(conf, null);
	}
	
	public StateMachine(StateMachineConfig conf, Function<Void,Void> advertiseCallback) {
		this.advertiseCallback = advertiseCallback;
		
		planesState = new HashMap<>();
		planesRunway = new HashMap<>();
		planesRolling = new HashMap<>();
		planesParking = new HashMap<>();
		
		planesWaiting = new HashMap<>();
		planesInvalidTransition = new HashMap<>();
		
		availableRunway = new HashSet<>();
		availableRolling = new HashMap<>();
		availableParking = new HashSet<>();
		
		
		for(RunwayID id : conf.runways() ) {
			availableRunway.add(id);
		}
		
		for(RollingData rollingData : conf.rolling() ) {
			availableRolling.put(rollingData.id(), rollingData.capacity());
		}
		
		for(ParkingID id : conf.parkings() ) {
			availableParking.add(id);
		}
		
		edgesRunway = conf.edgesRunway();
		edgesParking = conf.edgesParking();
		edgesRolling = conf.edgesRolling();
		
	}
	
	private void newPlane(PlaneID id) {
		planesState.put(id, State.FLYING);
		planesRunway.put(id, null);
		planesRolling.put(id, null);
		planesParking.put(id, null);
		planesWaiting.put(id, false);
		planesInvalidTransition.put(id, false);
	}
	
	private void removePlane(PlaneID id) {
		freeRunway(id);
		freeRolling(id);
		freeParking(id);
		planesState.remove(id);
		planesRunway.remove(id);
		planesRolling.remove(id);
		planesParking.remove(id);
		planesWaiting.remove(id);
		planesInvalidTransition.remove(id);
	}
	
	private boolean saveParking(PlaneID id) {
		if(planesParking.get(id) != null) return true;
		
		if(availableParking.size() > 0) {
			ParkingID parking = availableParking.iterator().next();
			availableParking.remove(parking);
			planesParking.put(id, parking);
			
			return true;
		}
		
		return false;
	}
	
	private boolean saveRunway(PlaneID id) {
		if(planesRunway.get(id) != null) return true;
		
		if(availableRunway.size() > 0) {
			RunwayID runway = availableRunway.iterator().next();
			availableRunway.remove(runway);
			planesRunway.put(id, runway);
			
			return true;
		}
		
		return false;
	}
	
	private boolean saveRolling(PlaneID id, RollingID rollingID) {
		Integer capacity = availableRolling.get(rollingID);
		
		if(capacity == null) return false;
		if(capacity <= 0) return false;
		
		freeRolling(id); // Libera la pista anterior
		availableRolling.put(rollingID, capacity-1);
		planesRolling.put(id, rollingID);
		return true;
	}
	
	private RollingID searchPathToParking(RunwayID runwayID, ParkingID parkingID) {
		HashSet<RollingID> edgesFrom = edgesRunway.get(runwayID);
		HashSet<RollingID> edgesTo = edgesParking.get(parkingID);
		
		if(edgesFrom == null) return null;
		if(edgesTo == null) return null;
		
		for(RollingID from : edgesFrom) {
			if(edgesTo.contains(from)) return from;
		}
			
		return null;
	}
	
	private RollingID searchPathToRunway(ParkingID parkingID, RunwayID runwayID) {
		HashSet<RollingID> edgesFrom = edgesParking.get(parkingID);
		HashSet<RollingID> edgesTo = edgesRunway.get(runwayID);
		
		if(edgesFrom == null) return null;
		if(edgesTo == null) return null;
		
		for(RollingID from : edgesFrom) {
			if(edgesTo.contains(from)) return from;
		}
			
		return null;
	}
	
	private void freeParking(PlaneID id) {
		ParkingID parking = planesParking.get(id);
		if(parking == null) return;
		
		planesParking.put(id, null);
		availableParking.add(parking);
		
	}
	
	private void freeRunway(PlaneID id) {
		RunwayID runway = planesRunway.get(id);
		if(runway == null) return;
		
		planesRunway.put(id, null);
		availableRunway.add(runway);
	}
	
	private void freeRolling(PlaneID id) {
		RollingID rolling = planesRolling.get(id);
		if(rolling == null) return;
		
		planesRolling.put(id, null);
		availableRolling.put(rolling, availableRolling.get(rolling) + 1);
	}

	@Override
	public State next(PlaneID key, Entry entry) {
		planesInvalidTransition.put(key, false);
		
		if(key == null) {
			planesInvalidTransition.put(key, true);
			return State.INVALID_TRANSITION;
		}
		if(entry == null) {
			planesInvalidTransition.put(key, true);
			return State.INVALID_TRANSITION;
		}
		
		System.out.println("KEY: " + key.toString());
		System.out.println("Entry: " + entry.toString());
		
		// New plane on system
		if(!planesState.containsKey(key)) {
			if(entry.equals(Entry.ON_AIR)) {
				newPlane(key);
				return State.FLYING;
			}
		}
		
		if(entry.equals(Entry.OUT)) {
			removePlane(key);
			return State.BYE;
		}
		
		State state = planesState.get(key);
		if(state == null) {
			planesInvalidTransition.put(key, true);
			return State.INVALID_TRANSITION;
		}
		
		planesWaiting.put(key, false); // reset waiting
		if(state.equals(State.FLYING)) {
			if(entry.equals(Entry.LAND)) {
				if(C1(key)) {
					planesState.put(key, State.LANDING);
					return State.LANDING;
				} else {
					planesWaiting.put(key, true);
					return State.WAIT;
				}
			}
		} else if(state.equals(State.LANDING)) {
			if(entry.equals(Entry.AT_RUNWAY)) {
				planesState.put(key, State.AT_RUNWAY);
				return State.AT_RUNWAY;
			} else if(entry.equals(Entry.TAKE_OFF)) {
				planesState.put(key, State.TAKING_OFF);
				return State.TAKING_OFF;
			}
		} else if(state.equals(State.AT_RUNWAY)) {
			if(entry.equals(Entry.PARKING_REQUEST)) {
				if(C2(key, entry)) {
					freeRunway(key);
					planesState.put(key, State.ROLLING);
					return State.ROLLING;
				} else {
					planesWaiting.put(key, true);
					return State.WAIT;
				}
			} else if(entry.equals(Entry.TAKE_OFF)) {
				planesState.put(key, State.TAKING_OFF);
				return State.TAKING_OFF;
			} 
		} else if(state.equals(State.ROLLING)) {
			if(entry.equals(Entry.ROLLING_TO_PARKING) || entry.equals(Entry.ROLLING_TO_RUNWAY)) {
				if(C2(key, entry)) {
					planesState.put(key, State.ROLLING);
					return State.ROLLING;
				} else {
					planesWaiting.put(key, true);
					return State.WAIT;
				}
			} else if(entry.equals(Entry.PARKED)) {
				freeRolling(key);
				planesState.put(key, State.PARKED);
				return State.PARKED;
			} else if(entry.equals(Entry.AT_RUNWAY)) {
				freeRolling(key);
				planesState.put(key, State.AT_RUNWAY);
				return State.AT_RUNWAY;
			}
		} else if(state.equals(State.PARKED)) {
			if(entry.equals(Entry.RUNWAY_REQUEST)) {
				if(C3(key)) {
					planesState.put(key, State.ROLLING);
					return State.ROLLING;
				}
			}
		} else if(state.equals(State.TAKING_OFF)) {
			if(entry.equals(Entry.ON_AIR)) {
				freeRunway(key);
				freeParking(key);
				
				planesState.put(key, State.FLYING);
				return State.FLYING;
			} else if(entry.equals(Entry.LAND)) {
				planesState.put(key, State.LANDING);
				return State.LANDING;
			}
		}
		 
		planesInvalidTransition.put(key, true);
		return State.INVALID_TRANSITION;
	}
	
	@Override
	public Snapshot<State> saveSnapshoot() {
		return null;
	}

	@Override
	public boolean loadSnapshoot(Snapshot<State> snapshoot) {
		return false;
	}

	@Override
	public void advertise() {
		if(advertiseCallback != null) advertiseCallback.apply(null);
	}
	
	private boolean C1(PlaneID id) {
		if(saveParking(id)) {
			return saveRunway(id);
		}
		
		return false;
		
	}
	
	private boolean C2(PlaneID id, Entry entry) {
		RollingID aux = null;
		
		if(entry.equals(Entry.ROLLING_TO_PARKING) || entry.equals(Entry.PARKING_REQUEST)) {
			aux = searchPathToParking(planesRunway.get(id), planesParking.get(id));
		} else {
			aux = searchPathToRunway(planesParking.get(id), planesRunway.get(id));
		}
		
		return saveRolling(id, aux);
	}
	
	private boolean C3(PlaneID id) {
		if(saveRunway(id)) {
			RollingID aux = searchPathToRunway(planesParking.get(id), planesRunway.get(id));
			return saveRolling(id, aux);
		}
		
		
		return false;
	}
	
	
	// GETTERS
	public State getState(PlaneID key) {
		return planesState.get(key);
	}
	
	public boolean hasToWait(PlaneID key) {
		Boolean res = planesWaiting.get(key);
		
		if(res == null) return false;
		return res;
	}
	
	public boolean invalidTransition(PlaneID key) {
		Boolean res = planesInvalidTransition.get(key);
		if(res == null) return false;
		return res;
		

	}
	
	public ParkingID getParking(PlaneID key) {
		return planesParking.get(key);
	}
	
	public RunwayID getRunway(PlaneID key) {
		return planesRunway.get(key);
	}
	
	public RollingID getRolling(PlaneID key) {
		return planesRolling.get(key);
	}	
}

package DAC.data;

import java.util.HashMap;
import java.util.HashSet;

public record StateMachineConfig(
		ParkingID[] parkings,
		RunwayID[] runways,
		RollingData[] rolling,
		HashMap<RunwayID, HashSet<RollingID>> edgesRunway,
		HashMap<ParkingID, HashSet<RollingID>> edgesParking,
		HashMap<RollingID, HashSet<RollingID>> edgesRolling) {
	
}

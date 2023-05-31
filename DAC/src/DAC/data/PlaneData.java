package DAC.data;

public record PlaneData(
		ParkingID parkingID,
		RunwayID runwayID,
		RollingID rollingID,
		boolean hasToWait,
		boolean inavlidTransition) {
	
}

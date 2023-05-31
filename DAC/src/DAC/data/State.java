package DAC.data;
/*
public enum State {
	TO_PARKING, TO_TAKE_OFF_TRACK, TAKING_OFF, LANDING,				// Estados transitivos (necesitan un estatico vacio y tienen tiempo maximo)
	PARKED, ON_TAKE_OFF_TRACK, ON_AIR, ON_LANDING_TRACK,			// Estados estaticos (tiempo ilimitado)
	BUSY,															// The place where you want to go is busy, we will let you know when it is available. DO NOT USE THIS STATE (its only informative state)
	GOOD_LUCK														// Confirmation that the plane has left the system. DO NOT USE THIS STATE (its only informative state)
	
}
*/

public enum State {
	FLYING, LANDING, AT_RUNWAY, ROLLING, PARKED, TAKING_OFF,
	WAIT,								// Resources not available
	BYE,
	INVALID_TRANSITION					// Not valid transition
}
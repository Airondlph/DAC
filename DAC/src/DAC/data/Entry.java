package DAC.data;

/*
public enum Entry {
	WANTS_TO_GO_TO_TAKE_OFF_TRACK, WANTS_TO_FLY, WANTS_TO_LAND, WANTS_TO_PARK,		// Entradas intencionales (requieren comprobacion)
	AT_TAKE_OFF_TRACK, AT_AIR, AT_LANDING_TRACK, PARKED,							// Entradas de confirmacion (ya fueron comprobados en la intencion)
	HELLO_CONTROL, WISH_ME_LUCK_CONTROL												// Special entry to add/remove a plane
}
*/

/*
public enum Entry {
	APPROACH, ON_LAND, PARKED, RUNWAY_REQUEST, ROLLING_REQUEST, AT_RUNWAY, DEPARTURE, ON_AIR,
	INFO, // GETS INFO ABOUT IT STATE
	HELLO_CONTROL, WISH_ME_LUCK_CONTROL														// Special entry to add/remove a plane
}
*/

public enum Entry {
	LAND, AT_RUNWAY, PARKING_REQUEST, ROLLING_TO_PARKING, PARKED, 
	RUNWAY_REQUEST, ROLLING_TO_RUNWAY, TAKE_OFF, ON_AIR,
	OUT
}
package DAC.data;

public class ParkingID extends ID {
	private static final long serialVersionUID = 2002L;
	
	public ParkingID(byte B3, byte B2, byte B1, byte B0) {
		super(B3,B2,B1,B0);
	}
	
	public ParkingID(String id) {
		super(id);
	}
}

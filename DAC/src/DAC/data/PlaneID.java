package DAC.data;

public class PlaneID extends ID {
	private static final long serialVersionUID = 2000L;

	public PlaneID(byte B3, byte B2, byte B1, byte B0) {
		super(B3,B2,B1,B0);
	}
	
	public PlaneID(String id) {
		super(id);
	}
}

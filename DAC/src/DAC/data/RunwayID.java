package DAC.data;

public class RunwayID extends ID {
	private static final long serialVersionUID = 2004L;
	
	public RunwayID(byte B3, byte B2, byte B1, byte B0) {
		super(B3,B2,B1,B0);
	}
	
	public RunwayID(String id) {
		super(id);
	}
}

package DAC.data;

public class RollingID extends ID {
	private static final long serialVersionUID = 2003L;
	
	public RollingID(byte B3, byte B2, byte B1, byte B0) {
		super(B3,B2,B1,B0);
	}
	
	public RollingID(String id) {
		super(id);
	}
}

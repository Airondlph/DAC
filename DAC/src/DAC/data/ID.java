package DAC.data;

import java.io.Serializable;
import java.util.regex.Pattern;

public class ID implements Comparable<ID>, Serializable {
	private static final long serialVersionUID = 2001L;
	
	private byte id[] = new byte[8];
	private boolean valid;
	
	/**
	 * ID specified from more significant byte to less significant byte 
	 * from 0000.0000.0000.0000 to 1111.1111.1111.1111 
	 * or from 0.0.0.0 to 255.255.255.255
	 * 
	 * Significant order: B3.B2.B1.B0 == FEDC.BA98.7654.3210
	 * 
	 * @param B3 More significant byte
	 * @param B2 Second more significant byte
	 * @param B1 Third more significant byte
	 * @param B0 Less significant byte
	 * 
	 */
	public ID(byte B3, byte B2, byte B1, byte B0) {
		loadID(B3,B2,B1,B0);
		valid = true;
	}
	
	
	/**
	 * ID specified from more significant byte to less significant byte using a string. 
	 * from "0.0.0.0" to "255.255.255.255".
	 * 
	 * 
	 * @param addr IPv4 address in string format.
	 * 
	 */
	public ID(String id) {
		try {
			String[] idBytesStr = id.split(Pattern.quote("."));
			loadID((byte)Integer.parseInt(idBytesStr[0]), 
					(byte)Integer.parseInt(idBytesStr[1]), 
					(byte)Integer.parseInt(idBytesStr[2]), 
					(byte)Integer.parseInt(idBytesStr[3]));
			
			valid = true;
		} catch (Exception e) {
			System.err.println("La direccion IP no es valida ([0.0.0.0, 255.255.255.255])");
			valid = false;
		}
	}
	
	
	/**
	 * Loads the bytes of the address.
	 * 
	 * @param B3 More significant byte
	 * @param B2 Second more significant byte
	 * @param B1 Third more significant byte
	 * @param B0 Less significant byte
	 * 
	 */
	private void loadID(byte B3, byte B2, byte B1, byte B0) {		
		id[3] = B3;
		id[2] = B2;
		id[1] = B1;
		id[0] = B0;
	}
	
	
	
	
	/**
	 * Converts the IPv4Address to a readable string.
	 *  
	 */
	public String toString() {
		if(!valid) return "Not valid ID (code: NVID)";
		
		return "" 
				+ (id[3] & 0xFF) + "."
				+ (id[2] & 0xFF) + "."
				+ (id[1] & 0xFF) + "."
				+ (id[0] & 0xFF);
	}
	
	
	@Override
	public int compareTo(ID b) {
		System.out.println("COMPARE");
		for(int i = 0; i < this.id.length; i++) {
			if(this.id[i] > b.id[i]) return +1;
			if(this.id[i] < b.id[i]) return -1;
		}
		
		return 0;
	}
	
	@Override
	public boolean equals(Object o) {
		ID aux = (ID)o;
		
		for(int i = 0; i < this.id.length; i++) {
			if(this.id[i] != aux.id[i]) {
				return false;
			}
		}

		return true;
	}
	
	@Override
	public int hashCode() {
		return java.util.Arrays.hashCode(id);
		/*
		int hash = 7; // prime number
		for(byte b : id) hash = 31 * hash + Byte(b).;
		
		return this.id.hashCode();
		*/
	}
}

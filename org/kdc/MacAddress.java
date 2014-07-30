package org.kdc;

// This is an incomplete class that I threw together fairly quickly because
// I wanted to teach myself some more about Eclipse.

import java.util.regex.Pattern;
import java.util.regex.Matcher;

// This class encapsulates a IEEE MAC Address.
//
// Currently this class will construct an object from a string having any one
// of these formats:
//
//       00:11:22:33:44:55    (octets separated by colons)
//       aa:bb:cc:dd:ee:ff    (lowercase hex digits)
//       AA:BB:CC:dd:ee:ff    (uppercase hex digits)
//       00-aa-22-CC-33:DD    (digits separated by dashes)
//       0:0a:1:2:34:56       (missing leading digits assumed to be "0)
//
// If a client of this class wishes to verify that the format of
// a string is a valid MAC address, then they can invoke 
// MacAddress.isValidString(String).


// Design note:  I wrote this class in this way because in the past I have
//               worked with a system that dealt with a lot of bad MAC addresses
//               and there were lots of places in the code where either the code
//               either got into a bad state because of lack of input validation (this
//               both drove me nuts and cost a lot of time any money...)...or
//               else the code became really ugly because there were calls to
//               LegacyMacAddressClass.isValid() everywhere....even in code deeply
//               embedded in the system.  It is my belief that if a MacAddress object
//               exists in a running system, that the object should be valid.

public class MacAddress {
	
	// This array holds the raw bytes
	private short[] address;    // no unsigned ints in Java....
	
	// This array holds what the author has arbitrarily decided is a good String
	// representation of this object.  The string will look like this:
	//
	//        00:0a:0b:0c:0d:0e
	private String string;
	
	///////////////////////////////////////////////////////////////////////////
	
	private static final Pattern pattern1;
	
	static {
		pattern1 = Pattern.compile("^\\s*" + 
				"(\\p{XDigit}){1,2}[-:]" + // 1
				"(\\p{XDigit}){1,2}[-:]" + // 2
				"(\\p{XDigit}){1,2}[-:]" + // 3
				"(\\p{XDigit}){1,2}[-:]" + // 4
				"(\\p{XDigit}){1,2}[-:]" + // 5
				"(\\p{XDigit}){1,2}"     + // 6
				"\\s*$");
	}
	
	///////////////////////////////////////////////////////////////////////////
	public static boolean isValidString(String s)
	{
		Matcher matcher = pattern1.matcher(s);
	
		return matcher.matches();
	}
	
	///////////////////////////////////////////////////////////////////////////
	private class MacMatchResult {
		boolean valid = false;
        short[] address = null;        
	}
	
	///////////////////////////////////////////////////////////////////////////
	private static boolean isValidString(String s, MacMatchResult mmr) {
		Matcher matcher = pattern1.matcher(s);
		
		if (! matcher.matches()) {
			mmr.valid= false;
		}
		else {
			mmr.valid = true;
        	mmr.address = new short[6];
        	mmr.address[0] = Short.parseShort(matcher.group(1), 16);
        	mmr.address[1] = Short.parseShort(matcher.group(2), 16);
        	mmr.address[2] = Short.parseShort(matcher.group(3), 16);
        	mmr.address[3] = Short.parseShort(matcher.group(4), 16);
        	mmr.address[4] = Short.parseShort(matcher.group(5), 16);
        	mmr.address[5] = Short.parseShort(matcher.group(6), 16);
		}
		
		return mmr.valid;
	}

	///////////////////////////////////////////////////////////////////////////
	/**
	 * @param s The human-readable MAC Address string to create this object from.
	 */
	public MacAddress(String s)
	{
		MacMatchResult mmr = new MacMatchResult();
		
		if (! isValidString(s, mmr)) {
			throw new IllegalArgumentException("Malformed MAC Address: " + s);
		}

		this.address = mmr.address;
		mmr.address = null;
		
		this.string = String.format("%02x:%02x:%02x:%02x:%02x:%02x", 
					this.address[0], 
					this.address[1],
					this.address[2],
					this.address[3],
					this.address[4],
					this.address[5]);							
	}

	///////////////////////////////////////////////////////////////////////////
	public String toString()
	{
		return this.string;
	}

	///////////////////////////////////////////////////////////////////////////
	@Override public boolean equals(Object o) 
	{
		if (! (o instanceof MacAddress)) {
			return false;
		}
		
		MacAddress that = (MacAddress)o;
		
		return this.address[0] == that.address[0] &&
				this.address[1] == that.address[1] &&
				this.address[2] == that.address[2] &&
				this.address[3] == that.address[3] &&
				this.address[4] == that.address[4] &&
				this.address[5] == that.address[5];
	}
	
	///////////////////////////////////////////////////////////////////////////
	public int hashCode()
	{
		int result = 19;
		
		result = 31 * result + this.address[0];
		result = 31 * result + this.address[1];
		result = 31 * result + this.address[2];
		result = 31 * result + this.address[3];
		result = 31 * result + this.address[4];
		result = 31 * result + this.address[5];
		result = 31 * result + this.string.hashCode();
		
		return result;
	}
	
}

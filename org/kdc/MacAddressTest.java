package org.kdc;

import org.junit.*;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kdc.MacAddress;

public class MacAddressTest {

	@Before
	public void setUp()
	{
	        
	}

	@Test(expected=NullPointerException.class)
    public void offerNull()
    {
        new MacAddress(null);
    }
	
	@Test
	public void simpleTest() {
		new MacAddress("00:11:22:33:44:55");
		new MacAddress("00-11-22-33-44-55");
		new MacAddress("0-1-2-3-4-5");
	}

	@Test(expected=IllegalArgumentException.class)
	public void malformedTest1() {
		new MacAddress("foo");
	}

	@Test(expected=IllegalArgumentException.class)
	public void malformedTest2() {
		new MacAddress("00:11:22:33:44:ag");
	}

	@Test
	public void simpleEqualsTest1() {
		MacAddress a = new MacAddress("aa:bb:cc:dd:ee:ff");
		MacAddress b = new MacAddress("AA:BB:CC:DD:EE:FF");
		
		assertTrue(a.equals(b));
	}

	@Test
	public void simpleEqualsTest2() {
		MacAddress a = new MacAddress("01:02:03:0a:0b:0c");
		MacAddress b = new MacAddress("1:2:3:a:b:c");
		
		assertTrue(a.equals(b));
	}

	@Test
	public void simpleHashcodeTest() {
		MacAddress a = new MacAddress("aa:bb:cc:dd:ee:ff");
		MacAddress b = new MacAddress("AA:BB:CC:DD:EE:FF");
		
		assertTrue(a.hashCode() == b.hashCode());
	}
}

package com.gizumo.app;

import static org.junit.Assert.*;

import org.junit.Test;

public class sampleTest {

	@Test
	public void testNum() {
		assertEquals(10, sample.num());
		assertEquals("10じゃない!!", 10, sample.num());
	}

}

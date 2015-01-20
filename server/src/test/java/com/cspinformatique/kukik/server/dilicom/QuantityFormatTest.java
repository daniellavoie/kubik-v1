package com.cspinformatique.kukik.server.dilicom;

import java.text.DecimalFormat;
import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

public class QuantityFormatTest {
	private DecimalFormat quantityNumberFormat;
	
	public QuantityFormatTest(){
		this.quantityNumberFormat = new DecimalFormat("00000.000");
	}
	
	@Test
	public void formatTest() throws ParseException{
		String decimalString = "00007500";
		Assert.assertEquals(quantityNumberFormat.parseObject(decimalString.subSequence(0, 5) + "." + decimalString.substring(5)), 7.5d);
	}
	
}

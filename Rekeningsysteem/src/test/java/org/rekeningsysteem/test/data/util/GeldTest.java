package org.rekeningsysteem.test.data.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.exception.GeldParseException;
import org.rekeningsysteem.test.data.EqualsHashCodeTest;

public final class GeldTest extends EqualsHashCodeTest {

	@Override
	protected Object makeInstance() {
		return new Geld(5.0);
	}

	@Override
	protected Object makeNotInstance() {
		return new Geld(6.33);
	}

	@Test(expected = GeldParseException.class)
	public void testStringConstructorWithInvalidCharacters() throws GeldParseException {
		new Geld("abc");
	}

	@Test
	public void testAdd() {
		Geld g = new Geld(36.4551);
		Geld g2 = g.add(new Geld(36.4999));
		assertEquals(new Geld(72.96), g2);
	}

	@Test
	public void testSubtract() {
		Geld g = new Geld(36.4551);
		Geld g2 = g.subtract(new Geld(36.40));
		assertEquals(new Geld(0.06), g2);
	}

	@Test
	public void testMultiply() {
		Geld g = new Geld(36.4551);
		Geld g2 = g.multiply(2.47);
		assertEquals(new Geld(90.06), g2);
	}

	@Test
	public void testDivide() {
		Geld g = new Geld(36.4551);
		Geld g2 = g.divide(2.47);
		assertEquals(new Geld(14.76), g2);
	}
}

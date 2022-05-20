package org.rekeningsysteem.test.data.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.exception.GeldParseException;

@Deprecated
public final class GeldTest {

	@Test(expected = GeldParseException.class)
	public void testStringConstructorWithInvalidCharacters() throws GeldParseException {
		new Geld("abc");
	}

	@Test
	public void testIsZero() {
		assertTrue(new Geld(0.0).isZero());
	}

	@Test
	public void testIsNotZero() {
		assertFalse(new Geld(1.0).isZero());
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

	@Test(expected = AssertionError.class)
	public void testDivideByZero() {
		Geld g = new Geld(12);
		g.divide(0.0);
	}
}

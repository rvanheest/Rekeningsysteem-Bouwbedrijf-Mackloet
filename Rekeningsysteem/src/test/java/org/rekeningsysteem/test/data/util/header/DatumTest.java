package org.rekeningsysteem.test.data.util.header;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;

import org.junit.Test;
import org.rekeningsysteem.data.util.header.Datum;
import org.rekeningsysteem.exception.DatumException;
import org.rekeningsysteem.test.data.EqualsHashCodeTest;

public final class DatumTest extends EqualsHashCodeTest {

	@Override
	protected Datum makeInstance() {
		try {
			return new Datum(30, 7, 1992);
		}
		catch (DatumException exception) {
			fail();
			return null;
		}
	}

	@Override
	protected Datum makeNotInstance() {
		return new Datum();
	}

	@Test
	public void testCorrecteDatum() throws DatumException {
		Datum datum = new Datum(30, 7, 1992);
		assertEquals(30, datum.getDag());
		assertEquals(7, datum.getMaand());
		assertEquals(1992, datum.getJaar());
	}

	@Test(expected = DatumException.class)
	public void testNegatieveMaand() throws DatumException {
		new Datum(30, -3, 1992);
	}

	@Test(expected = DatumException.class)
	public void testTeHogeMaand() throws DatumException {
		new Datum(30, 15, 1992);
	}

	@Test(expected = DatumException.class)
	public void testNegatieveDag() throws DatumException {
		new Datum(-3, 7, 1992);
	}

	@Test(expected = DatumException.class)
	public void testTeHogeDag() throws DatumException {
		new Datum(32, 7, 1992);
	}

	@Test
	public void testSchrikkeldagBestaat1992() throws DatumException {
		Datum datum = new Datum(29, 2, 1992);
		assertEquals(29, datum.getDag());
		assertEquals(2, datum.getMaand());
		assertEquals(1992, datum.getJaar());
	}

	@Test
	public void testSchrikkeldagBestaat2000() throws DatumException {
		Datum datum = new Datum(29, 2, 2000);
		assertEquals(29, datum.getDag());
		assertEquals(2, datum.getMaand());
		assertEquals(2000, datum.getJaar());
	}

	@Test(expected = DatumException.class)
	public void testSchrikkeldagBestaatNiet1993() throws DatumException {
		new Datum(29, 2, 1993);
	}

	@Test(expected = DatumException.class)
	public void testSchrikkeldagBestaatNiet1900() throws DatumException {
		new Datum(29, 2, 1900);
	}

	@Test
	public void testDatumAlsStringCorrect() throws DatumException {
		Datum datum = new Datum("30-7-1992");
		assertEquals(30, datum.getDag());
		assertEquals(7, datum.getMaand());
		assertEquals(1992, datum.getJaar());
	}

	@Test(expected = NoSuchElementException.class)
	public void testDatumAlsStringMissendJaar() throws DatumException {
		new Datum("20-6");
	}

	@Test(expected = NoSuchElementException.class)
	public void testDatumAlsStringGeenDatum() throws DatumException {
		new Datum("boe");
	}

	@Test(expected = InputMismatchException.class)
	public void testDatumAlsStringFooBarBoe() throws DatumException {
		new Datum("foo-bar-boe");
	}

	@Test(expected = DatumException.class)
	public void testDatumAlsStringBestaatNiet() throws DatumException {
		new Datum("29-2-1900");
	}

	@Test
	public void testEqualsOtherDag() throws DatumException {
		assertFalse(new Datum(30, 07, 1992).equals(new Datum(31, 07, 1992)));
	}

	@Test
	public void testEqualsOtherMaand() throws DatumException {
		assertFalse(new Datum(30, 7, 1992).equals(new Datum(30, 8, 1992)));
	}

	@Test
	public void testEqualsOtherJaar() throws DatumException {
		assertFalse(new Datum(30, 7, 1992).equals(new Datum(30, 7, 1993)));
	}

	@Test
	public void testToString() throws DatumException {
		assertEquals("<Datum[31-07-1992]>", new Datum(31, 7, 1992).toString());
	}

	@Test
	public void testToPrintable() throws DatumException {
		assertEquals("<Datum[05-12-1992]>", new Datum(5, 12, 1992).toString());
	}
}

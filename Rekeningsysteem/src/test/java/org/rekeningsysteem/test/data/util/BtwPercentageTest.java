package org.rekeningsysteem.test.data.util;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.BtwPercentage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BtwPercentageTest {

	private BtwPercentage btwPercentage;
	private final double percentage = 21;
	private final boolean verlegd = false;

	protected BtwPercentage makeInstance() {
		return new BtwPercentage(this.percentage, this.verlegd);
	}

	@Before
	public void setUp() {
		this.btwPercentage = this.makeInstance();
	}

	@Test
	public void testFormattedStringWithVerlegd() {
		assertEquals("21.0%, verlegd", new BtwPercentage(21.0, true).formattedString());
	}

	@Test
	public void testFormattedStringWithoutVerlegd() {
		assertEquals("6.0%", new BtwPercentage(6.0, false).formattedString());
	}

	@Test
	public void testCompareDifferentPercentage() {
		BtwPercentage b = new BtwPercentage(this.percentage - 2, this.verlegd);
		assertEquals(1, this.btwPercentage.compareTo(b));
		assertEquals(-1, b.compareTo(this.btwPercentage));
	}

	@Test
	public void testCompareSamePercentageAndVerlegd() {
		assertEquals(0, this.btwPercentage.compareTo(this.makeInstance()));
	}

	@Test
	public void testCompareSamePercentageDifferentVerlegd() {
		BtwPercentage b = new BtwPercentage(this.percentage, !this.verlegd);
		assertEquals(-1, this.btwPercentage.compareTo(b));
		assertEquals(1, b.compareTo(this.btwPercentage));
	}

	@Test
	public void testCompareInSorting() {
		BtwPercentage b1 = new BtwPercentage(9, false);
		BtwPercentage b2 = new BtwPercentage(9, true);
		BtwPercentage b3 = new BtwPercentage(21, false);
		BtwPercentage b4 = new BtwPercentage(21, true);
		List<BtwPercentage> list = Arrays.asList(b4, b2, b1, b3);
		Collections.sort(list);
		assertEquals(Arrays.asList(b1, b2, b3, b4), list);
	}
}

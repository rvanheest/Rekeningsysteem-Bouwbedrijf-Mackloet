package org.rekeningsysteem.test.data.mutaties;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.util.Geld;

public class MutatiesInkoopOrderTest {

	private MutatiesInkoopOrder order;
	private Geld materiaal;

	@Before
	public void setUp() {
		this.materiaal = new Geld(10);
		this.order = new MutatiesInkoopOrder("omschrijving", "ordernummer", this.materiaal);
	}

	@Test
	public void testMateriaalBedrag() {
		assertEquals(this.materiaal, this.order.materiaal());
	}

	@Test
	public void testGeenLoonBedrag() {
		assertEquals(new Geld(0), this.order.loon());
	}

	@Test
	public void testGetTotaal() {
		assertEquals(this.materiaal, this.order.getTotaal());
	}
}

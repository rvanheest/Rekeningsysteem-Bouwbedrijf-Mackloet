package org.rekeningsysteem.test.data.reparaties;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.util.Geld;

public class ReparatiesInkoopOrderTest {

	private ReparatiesInkoopOrder order;
	private Geld loon;
	private Geld materiaal;

	@Before
	public void setUp() {
		this.loon = new Geld(1);
		this.materiaal = new Geld(12);
		this.order = new ReparatiesInkoopOrder("omschrijving", "ordernummer", this.loon, this.materiaal);
	}

	@Test
	public void testMateriaalBedrag() {
		assertEquals(this.materiaal, this.order.materiaal());
	}

	@Test
	public void testLoonBedrag() {
		assertEquals(this.loon, this.order.loon());
	}

	@Test
	public void testGetTotaal() {
		assertEquals(new Geld(this.materiaal.bedrag() + this.loon.bedrag()), this.order.getTotaal());
	}
}

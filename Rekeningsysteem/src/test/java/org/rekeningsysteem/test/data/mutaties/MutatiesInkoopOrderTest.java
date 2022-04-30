package org.rekeningsysteem.test.data.mutaties;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.test.data.util.ListItemTest;

public class MutatiesInkoopOrderTest extends ListItemTest {

	private MutatiesInkoopOrder order;
	private final String omschrijving = "omschrijving";
	private final String ordernummer = "ordernummer";
	private final Geld materiaal = new Geld(10);

	@Override
	protected MutatiesInkoopOrder makeInstance() {
		return new MutatiesInkoopOrder(this.omschrijving, this.ordernummer, this.materiaal);
	}

	@Override
	protected MutatiesInkoopOrder makeNotInstance() {
		return new MutatiesInkoopOrder(this.omschrijving + ".", this.ordernummer, this.materiaal);
	}

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.order = this.makeInstance();
	}

	@Test
	public void testGetTotaal() {
		assertEquals(this.materiaal.bedrag(), this.order.getTotaal().bedrag(), 0);
	}
}

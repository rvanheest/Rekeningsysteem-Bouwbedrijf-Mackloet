package org.rekeningsysteem.test.data.reparaties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;
import org.rekeningsysteem.test.data.util.ListItemTest;

@RunWith(MockitoJUnitRunner.class)
public class ReparatiesInkoopOrderTest extends ListItemTest {

	private ReparatiesInkoopOrder order;
	private final String omschrijving = "omschrijving";
	private final String ordernummer = "ordernummer";
	private final Geld loon = new Geld(1);
	private final Geld materiaal = new Geld(12);
	@Mock private ListItemVisitor<Object> visitor;

	@Override
	protected ReparatiesInkoopOrder makeInstance() {
		return new ReparatiesInkoopOrder(this.omschrijving, this.ordernummer, this.loon, this.materiaal);
	}

	@Override
	protected ReparatiesInkoopOrder makeNotInstance() {
		return new ReparatiesInkoopOrder(this.omschrijving + ".", this.ordernummer, this.loon,
				this.materiaal);
	}

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.order = this.makeInstance();
	}

	@Test
	public void testGetOmschrijving() {
		assertEquals(this.omschrijving, this.order.getOmschrijving());
	}

	@Test
	public void testGetInkoopOrderNummer() {
		assertEquals(this.ordernummer, this.order.getInkoopOrderNummer());
	}

	@Test
	public void testGetLoon() {
		assertEquals(this.loon, this.order.getLoon());
	}

	@Test
	public void testGetMateriaal() {
		assertEquals(this.materiaal, this.order.getMateriaal());
	}

	@Test
	public void testAccept() {
		this.order.accept(this.visitor);

		verify(this.visitor).visit(eq(this.order));
	}

	@Test
	public void testEqualsFalseOtherOmschrijving() {
		ReparatiesInkoopOrder mb = new ReparatiesInkoopOrder(this.omschrijving + ".", this.ordernummer, this.loon,
				this.materiaal);
		assertFalse(this.order.equals(mb));
	}

	@Test
	public void testEqualsFalseOtherInkoopOrderNummer() {
		ReparatiesInkoopOrder mb = new ReparatiesInkoopOrder(this.omschrijving, this.ordernummer + ".", this.loon,
				this.materiaal);
		assertFalse(this.order.equals(mb));
	}

	@Test
	public void testEqualsFalseOtherLoon() {
		ReparatiesInkoopOrder mb = new ReparatiesInkoopOrder(this.omschrijving, this.ordernummer, new Geld(3),
				this.materiaal);
		assertFalse(this.order.equals(mb));
	}

	@Test
	public void testEqualsFalseOtherMateriaal() {
		ReparatiesInkoopOrder mb = new ReparatiesInkoopOrder(this.omschrijving, this.ordernummer, this.loon,
				new Geld(3));
		assertFalse(this.order.equals(mb));
	}

	@Test
	public void testToString() {
		assertEquals("<ReparatiesInkoopOrder[omschrijving, ordernummer, <Geld[1,00]>, <Geld[12,00]>]>",
				this.order.toString());
	}
}

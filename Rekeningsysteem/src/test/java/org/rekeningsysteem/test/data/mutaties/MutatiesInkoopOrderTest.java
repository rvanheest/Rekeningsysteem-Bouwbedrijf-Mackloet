package org.rekeningsysteem.test.data.mutaties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;
import org.rekeningsysteem.test.data.util.ListItemTest;

@RunWith(MockitoJUnitRunner.class)
public class MutatiesInkoopOrderTest extends ListItemTest {

	private MutatiesInkoopOrder order;
	private final String omschrijving = "omschrijving";
	private final String ordernummer = "ordernummer";
	private final Geld prijs = new Geld(10);
	@Mock private ListItemVisitor<Object> visitor;

	@Override
	protected MutatiesInkoopOrder makeInstance() {
		return new MutatiesInkoopOrder(this.omschrijving, this.ordernummer, this.prijs);
	}

	@Override
	protected MutatiesInkoopOrder makeNotInstance() {
		return new MutatiesInkoopOrder(this.omschrijving + ".", this.ordernummer, this.prijs);
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
		assertEquals(new Geld(0), this.order.getLoon());
	}

	@Test
	public void testGetMateriaal() {
		assertEquals(this.prijs, this.order.getMateriaal());
	}

	@Test
	public void testAccept() {
		this.order.accept(this.visitor);

		verify(this.visitor).visit(eq(this.order));
	}

	@Test
	public void testEqualsFalseOtherOmschrijving() {
		MutatiesInkoopOrder mb = new MutatiesInkoopOrder(this.omschrijving + ".", this.ordernummer, this.prijs);
		assertFalse(this.order.equals(mb));
	}

	@Test
	public void testEqualsFalseOtherInkoopOrderNummerummer() {
		MutatiesInkoopOrder mb = new MutatiesInkoopOrder(this.omschrijving, this.ordernummer + ".", this.prijs);
		assertFalse(this.order.equals(mb));
	}

	@Test
	public void testEqualsFalseOtherPrijs() {
		MutatiesInkoopOrder mb = new MutatiesInkoopOrder(this.omschrijving, this.ordernummer, this.prijs.multiply(2));
		assertFalse(this.order.equals(mb));
	}

	@Test
	public void testToString() {
		assertEquals("<MutatiesInkoopOrder[omschrijving, ordernummer, <Geld[10,00]>]>", this.order.toString());
	}
}

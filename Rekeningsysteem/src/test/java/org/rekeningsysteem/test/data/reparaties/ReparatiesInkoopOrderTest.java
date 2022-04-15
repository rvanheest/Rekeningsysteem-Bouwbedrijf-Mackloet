package org.rekeningsysteem.test.data.reparaties;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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
		return new ReparatiesInkoopOrder(this.omschrijving + ".", this.ordernummer, this.loon, this.materiaal);
	}

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.order = this.makeInstance();
	}

	@Test
	public void testGetTotaal() {
		assertEquals(this.materiaal.bedrag() + this.loon.bedrag(), this.order.getTotaal().bedrag(), 0.0);
	}

	@Test
	public void testAccept() {
		this.order.accept(this.visitor);

		verify(this.visitor).visit(eq(this.order));
	}
}

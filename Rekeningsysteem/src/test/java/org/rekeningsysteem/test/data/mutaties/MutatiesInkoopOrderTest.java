package org.rekeningsysteem.test.data.mutaties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;
import org.rekeningsysteem.test.data.util.ListItemTest;

@RunWith(MockitoJUnitRunner.class)
public class MutatiesInkoopOrderTest extends ListItemTest {

	private MutatiesInkoopOrder order;
	private final String omschrijving = "omschrijving";
	private final String ordernummer = "ordernummer";
	private final Geld materiaal = new Geld(10);
	@Mock private ListItemVisitor<Object> visitor;

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

	@Test
	public void testAccept() {
		this.order.accept(this.visitor);

		verify(this.visitor).visit(eq(this.order));
	}
}

package org.rekeningsysteem.test.io.xml.adaptee.mutaties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdapteeVisitor;
import org.rekeningsysteem.io.xml.adaptee.mutaties.MutatiesInkoopOrderAdaptee;
import org.rekeningsysteem.test.io.xml.adaptee.ListItemAdapteeVisitableTest;

@RunWith(MockitoJUnitRunner.class)
public class MutatiesInkoopOrderNummerAdapteeTest extends ListItemAdapteeVisitableTest {

	private final String omschrijving = "omschr";
	private final String ordernummer = "ordernrnr";
	private final Geld prijs = new Geld(12.04);
	@Mock private ListItemAdapteeVisitor<Object> visitor;

	@Override
	protected MutatiesInkoopOrderAdaptee makeInstance() {
		return MutatiesInkoopOrderAdaptee.build(a -> a
				.setOmschrijving(this.omschrijving)
				.setBonnummer(this.ordernummer)
				.setPrijs(this.prijs));
	}

	@Override
	protected MutatiesInkoopOrderAdaptee getInstance() {
		return (MutatiesInkoopOrderAdaptee) super.getInstance();
	}

	@Test
	public void testSetGetOmschrijving() {
		assertEquals(this.omschrijving, this.getInstance().getOmschrijving());
	}

	@Test
	public void testSetGetOrderNummer() {
		assertEquals(this.ordernummer, this.getInstance().getBonnummer());
	}

	@Test
	public void testSetGetPrijs() {
		assertEquals(this.prijs, this.getInstance().getPrijs());
	}

	@Test
	public void testAccept() {
		this.getInstance().accept(this.visitor);

		verify(this.visitor).visit(eq(this.getInstance()));
	}
}

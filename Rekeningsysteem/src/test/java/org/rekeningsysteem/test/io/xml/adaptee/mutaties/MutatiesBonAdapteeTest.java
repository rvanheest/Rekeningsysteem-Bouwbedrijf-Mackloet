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
import org.rekeningsysteem.io.xml.adaptee.mutaties.MutatiesBonAdaptee;
import org.rekeningsysteem.test.io.xml.adaptee.ListItemAdapteeTest;

@RunWith(MockitoJUnitRunner.class)
public class MutatiesBonAdapteeTest extends ListItemAdapteeTest {

	@Mock private ListItemAdapteeVisitor<Object> visitor;

	@Override
	protected MutatiesBonAdaptee makeInstance() {
		return new MutatiesBonAdaptee();
	}

	@Override
	protected MutatiesBonAdaptee getInstance() {
		return (MutatiesBonAdaptee) super.getInstance();
	}

	@Test
	public void testSetGetOmschrijving() {
		this.getInstance().setOmschrijving("omschr");
		assertEquals("omschr", this.getInstance().getOmschrijving());
	}

	@Test
	public void testSetGetBonnummer() {
		this.getInstance().setBonnummer("bonnr");
		assertEquals("bonnr", this.getInstance().getBonnummer());
	}

	@Test
	public void testSetGetPrijs() {
		Geld prijs = new Geld(12.04);
		this.getInstance().setPrijs(prijs);
		assertEquals(prijs, this.getInstance().getPrijs());
	}

	@Test
	public void testAccept() {
		this.getInstance().accept(this.visitor);

		verify(this.visitor).visit(eq(this.getInstance()));
	}
}

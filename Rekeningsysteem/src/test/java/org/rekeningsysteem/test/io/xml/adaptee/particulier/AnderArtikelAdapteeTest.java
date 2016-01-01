package org.rekeningsysteem.test.io.xml.adaptee.particulier;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdapteeVisitor;
import org.rekeningsysteem.io.xml.adaptee.particulier.AnderArtikelAdaptee;
import org.rekeningsysteem.test.io.xml.adaptee.ListItemAdapteeTest;

@RunWith(MockitoJUnitRunner.class)
public class AnderArtikelAdapteeTest extends ListItemAdapteeTest {

	@Mock private ListItemAdapteeVisitor<Object> visitor;

	@Override
	protected AnderArtikelAdaptee makeInstance() {
		return new AnderArtikelAdaptee();
	}

	@Override
	protected AnderArtikelAdaptee getInstance() {
		return (AnderArtikelAdaptee) super.getInstance();
	}

	@Test
	public void testSetGetOmschrijving() {
		this.getInstance().setOmschrijving("omschr");
		assertEquals("omschr", this.getInstance().getOmschrijving());
	}

	@Test
	public void testSetGetPrijs() {
		Geld prijs = new Geld(12.00);
		this.getInstance().setPrijs(prijs);
		assertEquals(prijs, this.getInstance().getPrijs());
	}

	@Test
	public void testSetGetMateriaalBtwPercentage() {
		this.getInstance().setMateriaalBtwPercentage(0.8);
		assertEquals(0.8, this.getInstance().getMateriaalBtwPercentage(), 0.0);
	}

	@Test
	public void testAccept() {
		this.getInstance().accept(this.visitor);

		verify(this.visitor).visit(eq(this.getInstance()));
	}
}

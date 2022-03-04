package org.rekeningsysteem.test.io.xml.adaptee.particulier;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdapteeVisitor;
import org.rekeningsysteem.io.xml.adaptee.particulier.AnderArtikelAdaptee;
import org.rekeningsysteem.test.io.xml.adaptee.ListItemAdapteeVisitableTest;

@RunWith(MockitoJUnitRunner.class)
public class AnderArtikelAdapteeTest extends ListItemAdapteeVisitableTest {

	private final String omschrijving = "omschr";
	private final Geld prijs = new Geld(12.00);
	private final BtwPercentage materiaalBtw = new BtwPercentage(0.8, false);
	@Mock private ListItemAdapteeVisitor<Object> visitor;

	@Override
	protected AnderArtikelAdaptee makeInstance() {
		return AnderArtikelAdaptee.build(a -> a
				.setOmschrijving(this.omschrijving)
				.setPrijs(this.prijs)
				.setMateriaalBtwPercentage(this.materiaalBtw));
	}

	@Override
	protected AnderArtikelAdaptee getInstance() {
		return (AnderArtikelAdaptee) super.getInstance();
	}

	@Test
	public void testSetGetOmschrijving() {
		assertEquals(this.omschrijving, this.getInstance().getOmschrijving());
	}

	@Test
	public void testSetGetPrijs() {
		assertEquals(this.prijs, this.getInstance().getPrijs());
	}

	@Test
	public void testSetGetMateriaalBtwPercentage() {
		assertEquals(this.materiaalBtw, this.getInstance().getMateriaalBtwPercentage());
	}

	@Test
	public void testAccept() {
		this.getInstance().accept(this.visitor);

		verify(this.visitor).visit(eq(this.getInstance()));
	}
}

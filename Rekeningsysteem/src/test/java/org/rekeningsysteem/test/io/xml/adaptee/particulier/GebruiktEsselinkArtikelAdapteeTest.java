package org.rekeningsysteem.test.io.xml.adaptee.particulier;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdapteeVisitor;
import org.rekeningsysteem.io.xml.adaptee.particulier.GebruiktEsselinkArtikelAdaptee;
import org.rekeningsysteem.test.io.xml.adaptee.ListItemAdapteeVisitableTest;

@RunWith(MockitoJUnitRunner.class)
public class GebruiktEsselinkArtikelAdapteeTest extends ListItemAdapteeVisitableTest {

	private final String omschrijving = "abc";
	private final EsselinkArtikel artikel = new EsselinkArtikel("artnr", "omschr", 12, "eenheid", new Geld(
			12.30));
	private final double aantal = 12.43;
	private final BtwPercentage materiaalBtw = new BtwPercentage(0.7, false);
	@Mock private ListItemAdapteeVisitor<Object> visitor;

	@Override
	protected GebruiktEsselinkArtikelAdaptee makeInstance() {
		return GebruiktEsselinkArtikelAdaptee.build(a -> a
				.setOmschrijving(this.omschrijving)
				.setArtikel(this.artikel)
				.setAantal(this.aantal)
				.setMateriaalBtwPercentage(this.materiaalBtw));
	}

	@Override
	protected GebruiktEsselinkArtikelAdaptee getInstance() {
		return (GebruiktEsselinkArtikelAdaptee) super.getInstance();
	}

	@Test
	public void testSetGetOmschrijving() {
		assertEquals(this.omschrijving, this.getInstance().getOmschrijving());
	}

	@Test
	public void testSetGetArtikel() {
		assertEquals(this.artikel, this.getInstance().getArtikel());
	}

	@Test
	public void testSetGetAantal() {
		assertEquals(this.aantal, this.getInstance().getAantal(), 0.0);
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

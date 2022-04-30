package org.rekeningsysteem.test.data.particulier;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.util.AbstractFactuur;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.Totalen;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;
import org.rekeningsysteem.test.data.util.AbstractFactuurTest;

import java.time.LocalDate;
import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ParticulierFactuurTest extends AbstractFactuurTest<ParticulierArtikel> {

	private ParticulierFactuur factuur;
	private final FactuurHeader header = new FactuurHeader(new Debiteur("a", "b", "c", "d", "e"), LocalDate.of(1992, 7, 30));
	@Mock private RekeningVisitor<Object> visitor;

	@Override
	protected ParticulierFactuur makeInstance() {
		return (ParticulierFactuur) super.makeInstance();
	}

	@Override
	protected AbstractFactuur<ParticulierArtikel> makeInstance(FactuurHeader header,
			Currency currency, ItemList<ParticulierArtikel> itemList) {
		return new ParticulierFactuur(this.header, "g", currency, itemList);
	}

	@Override
	protected ParticulierFactuur makeNotInstance() {
		return (ParticulierFactuur) super.makeNotInstance();
	}

	@Override
	protected AbstractFactuur<ParticulierArtikel> makeNotInstance(FactuurHeader otherHeader,
			Currency currency, ItemList<ParticulierArtikel> itemList) {
		FactuurHeader otherHeader2 = new FactuurHeader(new Debiteur("", "", "", "", ""), LocalDate.of(1992, 7, 30));
		return new ParticulierFactuur(otherHeader2, "", currency, itemList);
	}

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.factuur = this.makeInstance();
	}

	@Test
	@Override
	public void testGetTotalen() {
		Totalen t1 = Totalen.Empty();
		Totalen expected = Totalen.Empty();
		when(this.factuur.getItemList().getTotalen()).thenReturn(t1);

		assertEquals(expected, this.factuur.getTotalen());
		verify(this.factuur.getItemList()).getTotalen();
	}

	@Test
	public void testAccept() throws Exception {
		this.factuur.accept(this.visitor);

		verify(this.visitor).visit(eq(this.factuur));
	}

	@Test
	public void testToString() {
		assertEquals("<ParticulierFactuur[FactuurHeader[debiteur=Debiteur[debiteurID=Optional.empty, naam=a, straat=b, " 
				+ "nummer=c, postcode=d, plaats=e, btwNummer=Optional.empty], datum=1992-07-30, factuurnummer=Optional.empty], EUR, itemList]>",
				this.factuur.toString());
	}
}

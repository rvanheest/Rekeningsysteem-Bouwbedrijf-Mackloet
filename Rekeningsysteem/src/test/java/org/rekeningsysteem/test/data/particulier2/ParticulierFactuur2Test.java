package org.rekeningsysteem.test.data.particulier2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.rekeningsysteem.data.particulier2.ParticulierArtikel2;
import org.rekeningsysteem.data.particulier2.ParticulierFactuur2;
import org.rekeningsysteem.data.util.AbstractFactuur;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.Totalen;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;
import org.rekeningsysteem.logic.factuurnummer.FactuurnummerManager;
import org.rekeningsysteem.test.data.util.AbstractFactuurTest;

// TODO ParticulierFactuurTest
public class ParticulierFactuur2Test extends AbstractFactuurTest<ParticulierArtikel2> {

	private ParticulierFactuur2 factuur;
	private final OmschrFactuurHeader header = new OmschrFactuurHeader(
			new Debiteur("a", "b", "c", "d", "e"), LocalDate.of(1992, 7, 30), "g");
	@Mock private RekeningVisitor visitor;

	@Override
	protected ParticulierFactuur2 makeInstance() {
		return (ParticulierFactuur2) super.makeInstance();
	}

	@Override
	protected AbstractFactuur<ParticulierArtikel2> makeInstance(FactuurHeader header,
			Currency currency, ItemList<ParticulierArtikel2> itemList) {
		return new ParticulierFactuur2(this.header, currency, itemList);
	}

	@Override
	protected ParticulierFactuur2 makeNotInstance() {
		return (ParticulierFactuur2) super.makeNotInstance();
	}

	@Override
	protected AbstractFactuur<ParticulierArtikel2> makeNotInstance(FactuurHeader otherHeader,
			Currency currency, ItemList<ParticulierArtikel2> itemList) {
		OmschrFactuurHeader otherHeader2 = new OmschrFactuurHeader(
				new Debiteur("", "", "", "", ""), LocalDate.of(1992, 7, 30), "");
		return new ParticulierFactuur2(otherHeader2, currency, itemList);
	}

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.factuur = this.makeInstance();
	}

	@Test
	@Override
	public void testInitFactuurnummer() {
		// this test is overriden because we have another factuurheader here
		assertFalse(this.header.getFactuurnummer().isPresent());

		FactuurnummerManager manager = mock(FactuurnummerManager.class);
		when(manager.getFactuurnummer()).thenReturn("12014");

		this.factuur.initFactuurnummer(manager);

		assertEquals(Optional.of("12014"), this.header.getFactuurnummer());
		verify(manager).getFactuurnummer();
	}

	@Test
	@Override
	public void testSameFactuurnummer() {
		// this test is overriden because we have another factuurheader here
		this.header.setFactuurnummer("12013");
		assertTrue(this.header.getFactuurnummer().isPresent());

		FactuurnummerManager manager = mock(FactuurnummerManager.class);
		this.factuur.initFactuurnummer(manager);

		assertEquals(Optional.of("12013"), this.header.getFactuurnummer());
		verifyZeroInteractions(manager);
	}

	@Test
	@Override
	public void testGetTotalen() {
		Totalen t1 = new Totalen();
		Totalen expected = new Totalen();
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
		assertEquals("<ParticulierFactuur2[<FactuurHeader[<Debiteur[Optional.empty, a, b, c, "
				+ "d, e, Optional.empty]>, 1992-07-30, Optional.empty, g]>, EUR, itemList]>",
				this.factuur.toString());
	}
}

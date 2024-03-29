package org.rekeningsysteem.test.data.particulier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

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
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;
import org.rekeningsysteem.data.util.visitor.RekeningVoidVisitor;
import org.rekeningsysteem.logic.factuurnummer.FactuurnummerManager;
import org.rekeningsysteem.test.data.util.AbstractFactuurTest;

@RunWith(MockitoJUnitRunner.class)
public class ParticulierFactuurTest extends AbstractFactuurTest<ParticulierArtikel> {

	private ParticulierFactuur factuur;
	private final OmschrFactuurHeader header = new OmschrFactuurHeader(
			new Debiteur("a", "b", "c", "d", "e"), LocalDate.of(1992, 7, 30), "g");
	@Mock private RekeningVisitor<Object> visitor;
	@Mock private RekeningVoidVisitor voidVisitor;

	@Override
	protected ParticulierFactuur makeInstance() {
		return (ParticulierFactuur) super.makeInstance();
	}

	@Override
	protected AbstractFactuur<ParticulierArtikel> makeInstance(FactuurHeader header,
			Currency currency, ItemList<ParticulierArtikel> itemList) {
		return new ParticulierFactuur(this.header, currency, itemList);
	}

	@Override
	protected ParticulierFactuur makeNotInstance() {
		return (ParticulierFactuur) super.makeNotInstance();
	}

	@Override
	protected AbstractFactuur<ParticulierArtikel> makeNotInstance(FactuurHeader otherHeader,
			Currency currency, ItemList<ParticulierArtikel> itemList) {
		OmschrFactuurHeader otherHeader2 = new OmschrFactuurHeader(
				new Debiteur("", "", "", "", ""), LocalDate.of(1992, 7, 30), "");
		return new ParticulierFactuur(otherHeader2, currency, itemList);
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
		verifyNoInteractions(manager);
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
	public void testAcceptVoid() throws Exception {
		this.factuur.accept(this.voidVisitor);

		verify(this.voidVisitor).visit(eq(this.factuur));
	}

	@Test
	public void testToString() {
		assertEquals("<ParticulierFactuur[<FactuurHeader[<Debiteur[Optional.empty, a, b, c, "
				+ "d, e, Optional.empty]>, 1992-07-30, Optional.empty, g]>, EUR, itemList]>",
				this.factuur.toString());
	}
}

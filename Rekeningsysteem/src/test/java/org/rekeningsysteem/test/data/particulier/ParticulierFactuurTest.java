package org.rekeningsysteem.test.data.particulier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.Totalen;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.logic.factuurnummer.FactuurnummerManager;
import org.rekeningsysteem.test.data.util.AbstractFactuurTest;

public class ParticulierFactuurTest extends AbstractFactuurTest<ParticulierArtikel> {

	private OmschrFactuurHeader header;
	@Mock private AbstractLoon loon1;
	@Mock private AbstractLoon loon2;

	@Override
	protected ParticulierFactuur getInstance() {
		return (ParticulierFactuur) super.getInstance();
	}

	@Override
	protected OmschrFactuurHeader getTestFactuurHeader() {
		return this.header;
	}

	@Override
	protected ParticulierFactuur makeInstance() {
		return new ParticulierFactuur(this.getTestFactuurHeader(),
				this.getTestCurrency(), new ItemList<>(), new ItemList<>(),
				this.getTestBtwPercentage());
	}

	@Override
	protected ParticulierFactuur makeNotInstance() {
		BtwPercentage old = this.getTestBtwPercentage();
		return new ParticulierFactuur(this.getTestFactuurHeader(), this.getTestCurrency(),
				new ItemList<>(), new ItemList<>(), new BtwPercentage(old.getLoonPercentage() + 1,
						old.getMateriaalPercentage()));
	}

	@Before
	@Override
	public void setUp() {
		this.header = new OmschrFactuurHeader(new Debiteur("a", "b", "c", "d", "e"),
				LocalDate.of(1992, 7, 30), "g");
		super.setUp();
	}

	@Test
	public void testGetLoonList() {
		assertEquals(new ItemList<>(), this.getInstance().getLoonList());
	}
	
	@Test
	@Override
	public void testInitFactuurnummer() {
		assertFalse(this.header.getFactuurnummer().isPresent());
		
		FactuurnummerManager manager = mock(FactuurnummerManager.class);
		when(manager.getFactuurnummer()).thenReturn("12014");
		
		this.getInstance().initFactuurnummer(manager);
		
		assertEquals(Optional.of("12014"), this.header.getFactuurnummer());
		verify(manager).getFactuurnummer();
	}
	
	@Test
	@Override
	public void testSameFactuurnummer() {
		this.header.setFactuurnummer("12013");
		assertTrue(this.header.getFactuurnummer().isPresent());
		
		FactuurnummerManager manager = mock(FactuurnummerManager.class);
		this.getInstance().initFactuurnummer(manager);
		
		assertEquals(Optional.of("12013"), this.header.getFactuurnummer());
		verifyZeroInteractions(manager);
	}
	
	@Test
	@Override
	public void testGetTotalen() {
		when(this.loon1.getLoon()).thenReturn(new Geld(3.00));
		when(this.loon1.getMateriaal()).thenReturn(new Geld(0.00));
		
		ItemList<AbstractLoon> loon = this.getInstance().getLoonList();
		loon.add(this.loon1);
		loon.add(this.loon1);
		
		assertEquals(new Totalen().withLoon(new Geld(6.00))
				.withLoonBtw(new Geld(3.00))
				.withMateriaal(new Geld(0.00))
				.withMateriaalBtw(new Geld(0.00)),
				this.getInstance().getTotalen());
	}

	@Test
	public void testAccept() throws Exception {
		this.getInstance().accept(this.getMockedVisitor());

		verify(this.getMockedVisitor()).visit(eq(this.getInstance()));
	}

	@Test
	@Override
	public void testEqualsFalseOtherFactuurHeader() {
		this.header = new OmschrFactuurHeader(new Debiteur("", "", "", "", ""),
				LocalDate.now(), "test", "foo");
		assertFalse(this.getInstance().equals(this.makeInstance()));
	}

	@Test
	public void testToString() {
		String expected = "<ParticulierFactuur[<FactuurHeader[<Debiteur[a, b, c, d, e, "
				+ "Optional.empty]>, 1992-07-30, Optional.empty, g]>, EUR, [], [], "
				+ "<BtwPercentage[50.0, 100.0]>]>";
		assertEquals(expected, this.getInstance().toString());
	}
}

package org.rekeningsysteem.test.data.aangenomen;

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
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.rekeningsysteem.data.aangenomen.AangenomenFactuur;
import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.AbstractFactuur;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;
import org.rekeningsysteem.logic.factuurnummer.FactuurnummerManager;
import org.rekeningsysteem.test.data.util.AbstractFactuurTest;

@Ignore
@Deprecated
public class AangenomenFactuurTest extends AbstractFactuurTest<AangenomenListItem> {

	private AangenomenFactuur factuur;
	private final OmschrFactuurHeader header = new OmschrFactuurHeader(
			new Debiteur("a", "b", "c", "d", "e"), LocalDate.of(1992, 7, 30), "f");
	@Mock private RekeningVisitor visitor;

	@Override
	protected AangenomenFactuur makeInstance() {
		return (AangenomenFactuur) super.makeInstance();
	}

	@Override
	protected AbstractFactuur<AangenomenListItem> makeInstance(FactuurHeader otherHeader,
			Currency currency, ItemList<AangenomenListItem> itemList) {
		return new AangenomenFactuur(this.header, currency, itemList);
	}

	@Override
	protected AangenomenFactuur makeNotInstance() {
		return (AangenomenFactuur) super.makeNotInstance();
	}

	@Override
	protected AangenomenFactuur makeNotInstance(FactuurHeader otherHeader,
			Currency currency, ItemList<AangenomenListItem> itemList) {
		OmschrFactuurHeader otherHeader2 = new OmschrFactuurHeader(
				new Debiteur("", "", "", "", ""), LocalDate.of(1992, 7, 30), "");
		return new AangenomenFactuur(otherHeader2, currency, itemList);
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
	public void testAccept() throws Exception {
		this.factuur.accept(this.visitor);

		verify(this.visitor).visit(eq(this.factuur));
	}

	@Test
	public void testToString() {
		String expected = "<AangenomenFactuur[<FactuurHeader[<Debiteur[Optional.empty, a, b, c, "
				+ "d, e, Optional.empty]>, 1992-07-30, Optional.empty, f]>, EUR, itemList]>";
		assertEquals(expected, this.factuur.toString());
	}
}

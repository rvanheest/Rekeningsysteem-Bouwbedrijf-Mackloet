package org.rekeningsysteem.test.data.util.header;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Optional;

import org.junit.Test;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;

public class OmschrFactuurHeaderTest extends FactuurHeaderTest {

	private final String omschrijving = "voor u uitgevoerde werkzaamheden";

	@Override
	protected OmschrFactuurHeader makeInstance() {
		return new OmschrFactuurHeader(this.getTestDebiteur(), this.getTestDatum(),
				this.getTestFatuurnummer(), this.omschrijving);
	}

	@Override
	protected OmschrFactuurHeader makeNotInstance() {
		return new OmschrFactuurHeader(this.getTestDebiteur(), this.getTestDatum(),
				this.getTestFatuurnummer() + ".", this.omschrijving);
	}

	@Override
	protected OmschrFactuurHeader getInstance() {
		return (OmschrFactuurHeader) super.getInstance();
	}

	@Test
	public void testGetOmschrijving() {
		assertEquals(this.omschrijving, this.getInstance().getOmschrijving());
	}

	@Test
	public void testSecondConstructorWithOmschrijving() {
		OmschrFactuurHeader header = new OmschrFactuurHeader(this.getInstance().getDebiteur(), this
				.getInstance().getDatum(), this.omschrijving);

		assertEquals(this.getInstance().getDebiteur(), header.getDebiteur());
		assertEquals(this.getInstance().getDatum(), header.getDatum());
		assertEquals(Optional.empty(), header.getFactuurnummer());
		assertEquals(this.omschrijving, header.getOmschrijving());
	}

	@Test
	public void testThirdConstructorWithOptionalFactuurnummer() {
		OmschrFactuurHeader header = new OmschrFactuurHeader(this.getInstance().getDebiteur(),
				this.getInstance().getDatum(), this.getInstance().getFactuurnummer(), this.omschrijving);
		
		assertEquals(this.getInstance().getDebiteur(), header.getDebiteur());
		assertEquals(this.getInstance().getDatum(), header.getDatum());
		assertEquals(Optional.of("32013"), header.getFactuurnummer());
		assertEquals(this.omschrijving, header.getOmschrijving());
	}

	@Test
	public void testEqualsFalseOtherOmschrijving() {
		assertFalse(this.getInstance().equals(
				new OmschrFactuurHeader(this.getTestDebiteur(), this.getTestDatum(),
						this.getTestFatuurnummer(), this.omschrijving + ".")));
	}

	@Test
	@Override
	public void testToString() {
		assertEquals("<FactuurHeader[<Debiteur[Optional.empty, RvH, PB, 116, 3241TA, MH, "
				+ "Optional.empty]>, " + this.getTestDatum().toString() + ", Optional[32013], "
				+ "voor u uitgevoerde werkzaamheden]>", this.getInstance().toString());
	}
}

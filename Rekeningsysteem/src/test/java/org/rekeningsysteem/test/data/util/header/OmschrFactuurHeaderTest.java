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
		OmschrFactuurHeader header = new OmschrFactuurHeader(this.getInstance().debiteur(), this
				.getInstance().datum(), this.omschrijving);

		assertEquals(this.getInstance().debiteur(), header.debiteur());
		assertEquals(this.getInstance().datum(), header.datum());
		assertEquals(Optional.empty(), header.factuurnummer());
		assertEquals(this.omschrijving, header.getOmschrijving());
	}

	@Test
	public void testThirdConstructorWithOptionalFactuurnummer() {
		OmschrFactuurHeader header = new OmschrFactuurHeader(this.getInstance().debiteur(),
				this.getInstance().datum(), this.getInstance().factuurnummer(), this.omschrijving);
		
		assertEquals(this.getInstance().debiteur(), header.debiteur());
		assertEquals(this.getInstance().datum(), header.datum());
		assertEquals(Optional.of("32013"), header.factuurnummer());
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
		assertEquals("<FactuurHeader[Debiteur[debiteurID=Optional.empty, naam=RvH, straat=PB, " 
			+ "nummer=116, postcode=3241TA, plaats=MH, btwNummer=Optional.empty], " + this.getTestDatum().toString() + ", Optional[32013], "
				+ "voor u uitgevoerde werkzaamheden]>", this.getInstance().toString());
	}
}

package org.rekeningsysteem.test.data.particulier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.data.util.loon.AbstractLoon;
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
				this.getTestValuta(), new ItemList<ParticulierArtikel>(this.getTestBtwPercentage()),
				new ItemList<AbstractLoon>(this.getTestBtwPercentage()));
	}

	@Override
	protected ParticulierFactuur makeNotInstance() {
		BtwPercentage old = this.getTestBtwPercentage();
		return new ParticulierFactuur(this.getTestFactuurHeader(), this.getTestValuta(),
				new ItemList<ParticulierArtikel>(new BtwPercentage(old.getLoonPercentage() + 1,
						old.getMateriaalPercentage())),
				new ItemList<AbstractLoon>(this.getTestBtwPercentage()));
	}

	@Before
	@Override
	public void setUp() {
		this.header = new OmschrFactuurHeader(new Debiteur("a", "b", "c", "d", "e"),
				LocalDate.of(1992, 7, 30), "f", "g");
		super.setUp();
	}

	@Test
	public void testGetLoonList() {
		assertEquals(new ItemList<AbstractLoon>(this.getTestBtwPercentage()),
				this.getInstance().getLoonList());
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
				+ "Optional.empty]>, 1992-07-30, Optional[f], g]>, euro, <ItemList[[], "
				+ "<BtwPercentage[6.0, 21.0]>, <Totalen[<Geld[0,00]>, <Geld[0,00]>, "
				+ "<Geld[0,00]>, <Geld[0,00]>, <Geld[0,00]>, <Geld[0,00]>]>]>, "
				+ "<ItemList[[], <BtwPercentage[6.0, 21.0]>, <Totalen[<Geld[0,00]>, "
				+ "<Geld[0,00]>, <Geld[0,00]>, <Geld[0,00]>, <Geld[0,00]>, <Geld[0,00]>]>]>]>";
		assertEquals(expected, this.getInstance().toString());
	}
}

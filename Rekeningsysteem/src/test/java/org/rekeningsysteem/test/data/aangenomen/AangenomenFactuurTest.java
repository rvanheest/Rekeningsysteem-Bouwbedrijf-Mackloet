package org.rekeningsysteem.test.data.aangenomen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.aangenomen.AangenomenFactuur;
import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.header.Datum;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.exception.DatumException;
import org.rekeningsysteem.test.data.util.AbstractFactuurTest;

public class AangenomenFactuurTest extends AbstractFactuurTest<AangenomenListItem> {

	private OmschrFactuurHeader header;

	@Override
	protected AangenomenFactuur getInstance() {
		return (AangenomenFactuur) super.getInstance();
	}

	@Override
	protected OmschrFactuurHeader getTestFactuurHeader() {
		return this.header;
	}

	@Override
	protected AangenomenFactuur makeInstance() {
		return new AangenomenFactuur(this.getTestFactuurHeader(), this.getTestValuta(),
				new ArrayList<AangenomenListItem>(), this.getTestBtwPercentage());
	}

	@Override
	protected AangenomenFactuur makeNotInstance() {
		BtwPercentage old = this.getTestBtwPercentage();
		return new AangenomenFactuur(this.getTestFactuurHeader(), this.getTestValuta(),
				new ArrayList<AangenomenListItem>(), new BtwPercentage(
						old.getLoonPercentage() + 1,
						old.getMateriaalPercentage()));
	}

	@Before
	@Override
	public void setUp() {
		try {
			this.header = new OmschrFactuurHeader(new Debiteur("a", "b", "c", "d", "e"),
					new Datum(30, 07, 1992), "f", "g");
		}
		catch (DatumException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		super.setUp();
	}

	@Test
	public void testAccept() throws Exception {
		this.getInstance().accept(this.getMockedVisitor());

		verify(this.getMockedVisitor()).visit(eq(this.getInstance()));
	}

	@Test
	@Override
	public void testEqualsFalseOtherFactuurHeader() {
		this.header = new OmschrFactuurHeader(new Debiteur("", "", "", "", ""), new Datum(),
				"test", "foo");
		assertFalse(this.getInstance().equals(this.makeInstance()));
	}

	@Test
	public void testToString() {
		String expected = "<AangenomenFactuur[<FactuurHeader[<Debiteur[a, b, c, d, e, "
				+ "Optional.empty]>, <Datum[30-07-1992]>, Optional[f], g]>, euro, [], "
				+ "<BtwPercentage[6.0, 21.0]>]>";
		assertEquals(expected, this.getInstance().toString());
	}
}

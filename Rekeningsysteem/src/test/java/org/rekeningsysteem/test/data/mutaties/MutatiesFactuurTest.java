package org.rekeningsysteem.test.data.mutaties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;

import org.junit.Test;
import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.test.data.util.AbstractFactuurTest;

public class MutatiesFactuurTest extends AbstractFactuurTest<MutatiesBon> {

	@Override
	protected MutatiesFactuur getInstance() {
		return (MutatiesFactuur) super.getInstance();
	}

	@Override
	protected MutatiesFactuur makeInstance() {
		return new MutatiesFactuur(this.getTestFactuurHeader(), this.getTestValuta(),
				new ArrayList<MutatiesBon>(), this.getTestBtwPercentage());
	}

	@Override
	protected MutatiesFactuur makeNotInstance() {
		BtwPercentage old = this.getTestBtwPercentage();
		return new MutatiesFactuur(this.getTestFactuurHeader(), this.getTestValuta(),
				new ArrayList<MutatiesBon>(), new BtwPercentage(old.getLoonPercentage() + 1,
						old.getMateriaalPercentage()));
	}

	@Test
	public void testAccept() throws Exception {
		this.getInstance().accept(this.getMockedVisitor());

		verify(this.getMockedVisitor()).visit(eq(this.getInstance()));
	}

	@Test
	public void testToString() {
		String expected = "<MutatiesFactuur[<FactuurHeader[<Debiteur[a, b, c, d, e, "
				+ "Optional.empty]>, <Datum[30-07-1992]>, Optional[f]]>, euro, [], "
				+ "<BtwPercentage[6.0, 21.0]>]>";
		assertEquals(expected, this.getInstance().toString());
	}
}

package org.rekeningsysteem.test.data.reparaties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;

import org.junit.Test;
import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.test.data.util.AbstractFactuurTest;

public class ReparatiesFactuurTest extends AbstractFactuurTest<ReparatiesBon> {

	@Override
	protected ReparatiesFactuur getInstance() {
		return (ReparatiesFactuur) super.getInstance();
	}

	@Override
	protected ReparatiesFactuur makeInstance() {
		return new ReparatiesFactuur(this.getTestFactuurHeader(), this.getTestValuta(),
				new ArrayList<ReparatiesBon>(), this.getTestBtwPercentage());
	}

	@Override
	protected ReparatiesFactuur makeNotInstance() {
		BtwPercentage old = this.getTestBtwPercentage();
		return new ReparatiesFactuur(this.getTestFactuurHeader(), this.getTestValuta(),
				new ArrayList<ReparatiesBon>(), new BtwPercentage(old.getLoonPercentage() + 1,
						old.getMateriaalPercentage()));
	}

	@Test
	public void testAccept() throws Exception {
		this.getInstance().accept(this.getMockedVisitor());

		verify(this.getMockedVisitor()).visit(eq(this.getInstance()));
	}

	@Test
	public void testToString() {
		String expected = "<ReparatiesFactuur[<FactuurHeader[<Debiteur[a, b, c, d, e, "
				+ "Optional.empty]>, <Datum[30-07-1992]>, Optional[f]]>, euro, [], "
				+ "<BtwPercentage[6.0, 21.0]>]>";
		assertEquals(expected, this.getInstance().toString());
	}
}
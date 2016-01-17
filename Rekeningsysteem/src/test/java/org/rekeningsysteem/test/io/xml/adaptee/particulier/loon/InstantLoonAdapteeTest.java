package org.rekeningsysteem.test.io.xml.adaptee.particulier.loon;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdapteeVisitor;
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.InstantLoonAdaptee;
import org.rekeningsysteem.test.io.xml.adaptee.ListItemAdapteeVisitableTest;

@RunWith(MockitoJUnitRunner.class)
public class InstantLoonAdapteeTest extends ListItemAdapteeVisitableTest {

	private final String omschrijving = "foobar";
	private final Geld loon = new Geld(2.0);
	private final double loonBtw = 0.6;
	@Mock private ListItemAdapteeVisitor<Object> visitor;

	@Override
	protected InstantLoonAdaptee makeInstance() {
		return InstantLoonAdaptee.build(a -> a
				.setOmschrijving(this.omschrijving)
				.setLoon(this.loon)
				.setLoonBtwPercentage(this.loonBtw));
	}

	@Override
	protected InstantLoonAdaptee getInstance() {
		return (InstantLoonAdaptee) super.getInstance();
	}

	@Test
	public void testSetGetOmschrijving() {
		assertEquals(this.omschrijving, this.getInstance().getOmschrijving());
	}

	@Test
	public void testSetGetLoon() {
		assertEquals(this.loon, this.getInstance().getLoon());
	}

	@Test
	public void testSetGetLoonBtwPercentage() {
		assertEquals(this.loonBtw, this.getInstance().getLoonBtwPercentage(), 0.0);
	}

	@Test
	public void testAccept() {
		this.getInstance().accept(this.visitor);

		verify(this.visitor).visit(eq(this.getInstance()));
	}
}

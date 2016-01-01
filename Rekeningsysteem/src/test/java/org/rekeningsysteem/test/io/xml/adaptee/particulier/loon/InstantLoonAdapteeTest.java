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
import org.rekeningsysteem.test.io.xml.adaptee.ListItemAdapteeTest;

@RunWith(MockitoJUnitRunner.class)
public class InstantLoonAdapteeTest extends ListItemAdapteeTest {

	@Mock private ListItemAdapteeVisitor<Object> visitor;

	@Override
	protected InstantLoonAdaptee makeInstance() {
		return new InstantLoonAdaptee();
	}

	@Override
	protected InstantLoonAdaptee getInstance() {
		return (InstantLoonAdaptee) super.getInstance();
	}

	@Test
	public void testSetGetOmschrijving() {
		this.getInstance().setOmschrijving("foobar");
		assertEquals("foobar", this.getInstance().getOmschrijving());
	}

	@Test
	public void testSetGetLoon() {
		Geld loon = new Geld(2.0);
		this.getInstance().setLoon(loon);
		assertEquals(loon, this.getInstance().getLoon());
	}

	@Test
	public void testSetGetLoonBtwPercentage() {
		this.getInstance().setLoonBtwPercentage(0.6);
		assertEquals(0.6, this.getInstance().getLoonBtwPercentage(), 0.0);
	}

	@Test
	public void testAccept() {
		this.getInstance().accept(this.visitor);

		verify(this.visitor).visit(eq(this.getInstance()));
	}
}

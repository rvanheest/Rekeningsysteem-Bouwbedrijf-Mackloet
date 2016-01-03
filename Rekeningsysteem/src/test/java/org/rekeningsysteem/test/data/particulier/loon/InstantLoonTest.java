package org.rekeningsysteem.test.data.particulier.loon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

@RunWith(MockitoJUnitRunner.class)
public class InstantLoonTest extends AbstractLoonTest {

	private InstantLoon item;
	private final Geld loon = new Geld(12);
	private final double loonBtwPercentage = 10;
	@Mock private ListItemVisitor<Object> visitor;

	@Override
	protected InstantLoon makeInstance() {
		return new InstantLoon(this.getTestOmschrijving(), this.loon, this.loonBtwPercentage);
	}

	@Override
	protected InstantLoon makeNotInstance() {
		return new InstantLoon(this.getTestOmschrijving(), this.loon.multiply(2),
				this.loonBtwPercentage);
	}

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.item = this.makeInstance();
	}

	@Test
	public void testGetLoon() {
		assertEquals(this.loon, this.item.getLoon());
	}

	@Test
	public void testGetLoonBtwPercentage() {
		assertEquals(this.loonBtwPercentage, this.item.getLoonBtwPercentage(), 0.0);
	}

	@Test
	public void testAccept() {
		this.item.accept(this.visitor);

		verify(this.visitor).visit(eq(this.item));
	}

	@Test
	public void testEqualsFalseOtherOmschrijving() {
		InstantLoon loon2 = new InstantLoon(this.getTestOmschrijving() + ".", this.loon,
				this.loonBtwPercentage);
		assertFalse(this.item.equals(loon2));
	}

	@Test
	public void testEqualsFalseOtherLoon() {
		InstantLoon loon2 = new InstantLoon(this.getTestOmschrijving(),
				this.loon.multiply(2), this.loonBtwPercentage);
		assertFalse(this.item.equals(loon2));
	}

	@Test
	public void testEqualsFalseOtherLoonBtwPercentage() {
		InstantLoon loon2 = new InstantLoon(this.getTestOmschrijving(),
				this.loon, this.loonBtwPercentage + 1.0);
		assertFalse(this.item.equals(loon2));
	}

	@Test
	public void testToString() {
		assertEquals("<InstantLoon[omschrijving, <Geld[12,00]>, 10.0]>", this.item.toString());
	}
}

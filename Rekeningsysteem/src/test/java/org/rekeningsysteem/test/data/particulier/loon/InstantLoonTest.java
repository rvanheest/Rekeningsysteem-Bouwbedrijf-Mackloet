package org.rekeningsysteem.test.data.particulier.loon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

@RunWith(MockitoJUnitRunner.class)
public class InstantLoonTest extends AbstractLoonTest {

	private InstantLoon item;
	private final Geld loon = new Geld(12);
	private final BtwPercentage loonBtwPercentage = new BtwPercentage(10, false);
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
		assertEquals(this.loon, this.item.loon());
	}

	@Test
	public void testGetLoonBtwPercentage() {
		assertEquals(this.loonBtwPercentage, this.item.getLoonBtwPercentage());
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
				this.loon, new BtwPercentage(this.loonBtwPercentage.percentage() + 1.0, false));
		assertFalse(this.item.equals(loon2));
	}

	@Test
	public void testToString() {
		assertEquals("<InstantLoon[omschrijving, Geld[bedrag=12.0], BtwPercentage[percentage=10.0, verlegd=false]]>", this.item.toString());
	}
}

package org.rekeningsysteem.test.data.particulier.loon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;

@RunWith(MockitoJUnitRunner.class)
public class ProductLoonTest extends AbstractLoonTest {

	private ProductLoon item;
	private final double uren = 10;
	private final Geld uurloon = new Geld(4);
	private final BtwPercentage loonBtwPercentage = new BtwPercentage(10, false);

	@Override
	protected ProductLoon makeInstance() {
		return new ProductLoon(this.getTestOmschrijving(), this.uren, this.uurloon, this.loonBtwPercentage);
	}

	@Override
	protected ProductLoon makeNotInstance() {
		return new ProductLoon(this.getTestOmschrijving(), this.uren + 2, this.uurloon, this.loonBtwPercentage);
	}

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.item = this.makeInstance();
	}

	@Test
	public void testGetUren() {
		assertEquals(this.uren, this.item.getUren(), 0.0);
	}

	@Test
	public void testGetUurloon() {
		assertEquals(this.uurloon, this.item.getUurloon());
	}

	@Test
	public void testGetLoon() {
		assertEquals(new Geld(40), this.item.loon());
	}

	@Test
	public void testEqualsFalseOtherOmschrijving() {
		ProductLoon loon2 = new ProductLoon(this.getTestOmschrijving() + ".", this.uren, this.uurloon, this.loonBtwPercentage);
		assertFalse(this.item.equals(loon2));
	}

	@Test
	public void testEqualsFalseOtherUren() {
		ProductLoon loon2 = new ProductLoon(this.getTestOmschrijving(), this.uren + 2, this.uurloon, this.loonBtwPercentage);
		assertFalse(this.item.equals(loon2));
	}

	@Test
	public void testEqualsFalseOtherUurloon() {
		ProductLoon loon2 = new ProductLoon(this.getTestOmschrijving(), this.uren, this.uurloon.multiply(3), this.loonBtwPercentage);
		assertFalse(this.item.equals(loon2));
	}

	@Test
	public void testEqualsFalseOtherLoonBtwPercentage() {
		ProductLoon loon2 = new ProductLoon(this.getTestOmschrijving(), this.uren, this.uurloon, new BtwPercentage(this.loonBtwPercentage.percentage() + 1.0, false));
		assertFalse(this.item.equals(loon2));
	}

	@Test
	public void testToString() {
		assertEquals("<ProductLoon[omschrijving, 10.0, Geld[bedrag=4.0], BtwPercentage[percentage=10.0, verlegd=false]]>", this.item.toString());
	}
}

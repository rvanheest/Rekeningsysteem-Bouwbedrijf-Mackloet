package org.rekeningsysteem.test.data.util.loon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.loon.ProductLoon;

public class ProductLoonTest extends AbstractLoonTest {

	private final double uren = 10;
	private final Geld uurloon = new Geld(4);

	@Override
	protected ProductLoon makeInstance() {
		return new ProductLoon(this.getTestOmschrijving(), this.uren, this.uurloon);
	}

	@Override
	protected ProductLoon makeNotInstance() {
		return new ProductLoon(this.getTestOmschrijving(), this.uren + 2, this.uurloon);
	}

	@Override
	protected ProductLoon getInstance() {
		return (ProductLoon) super.getInstance();
	}

	@Test
	public void testGetUren() {
		assertEquals(this.uren, this.getInstance().getUren(), 0.0);
	}

	@Test
	public void testGetUurloon() {
		assertEquals(this.uurloon, this.getInstance().getUurloon());
	}

	@Test
	public void testGetLoon() {
		assertEquals(new Geld(40), this.getInstance().getLoon());
	}

	@Test
	public void testEqualsFalseOtherUren() {
		ProductLoon loon2 = new ProductLoon(this.getTestOmschrijving(), this.uren + 2, this.uurloon);
		assertFalse(this.getInstance().equals(loon2));
	}

	@Test
	public void testEqualsFalseOtherUurloon() {
		ProductLoon loon2 = new ProductLoon(this.getTestOmschrijving(), this.uren,
				this.uurloon.multiply(3));
		assertFalse(this.getInstance().equals(loon2));
	}

	@Test
	public void testToString() {
		assertEquals("<ProductLoon[omschrijving, 10.0, <Geld[4,00]>]>",
				this.getInstance().toString());
	}
}

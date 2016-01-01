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
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.ProductLoonAdaptee;
import org.rekeningsysteem.test.io.xml.adaptee.ListItemAdapteeTest;

@RunWith(MockitoJUnitRunner.class)
public class ProductLoonAdapteeTest extends ListItemAdapteeTest {

	@Mock private ListItemAdapteeVisitor<Object> visitor;

	@Override
	protected ProductLoonAdaptee makeInstance() {
		return new ProductLoonAdaptee();
	}

	@Override
	protected ProductLoonAdaptee getInstance() {
		return (ProductLoonAdaptee) super.getInstance();
	}

	@Test
	public void testSetGetOmschrijving() {
		this.getInstance().setOmschrijving("foobar");
		assertEquals("foobar", this.getInstance().getOmschrijving());
	}

	@Test
	public void testSetGetUren() {
		this.getInstance().setUren(1.0);
		assertEquals(1.0, this.getInstance().getUren(), 0.0);
	}

	@Test
	public void testSetGetUurloon() {
		Geld uurloon = new Geld(1.0);
		this.getInstance().setUurloon(uurloon);
		assertEquals(uurloon, this.getInstance().getUurloon());
	}

	@Test
	public void testSetGetLoonBtwPercentage() {
		this.getInstance().setLoonBtwPercentage(0.4);
		assertEquals(0.4, this.getInstance().getLoonBtwPercentage(), 0.0);
	}

	@Test
	public void testAccept() {
		this.getInstance().accept(this.visitor);

		verify(this.visitor).visit(eq(this.getInstance()));
	}
}

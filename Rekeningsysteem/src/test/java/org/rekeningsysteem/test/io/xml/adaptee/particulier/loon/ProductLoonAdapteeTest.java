package org.rekeningsysteem.test.io.xml.adaptee.particulier.loon;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdapteeVisitor;
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.ProductLoonAdaptee;
import org.rekeningsysteem.test.io.xml.adaptee.ListItemAdapteeVisitableTest;

@RunWith(MockitoJUnitRunner.class)
public class ProductLoonAdapteeTest extends ListItemAdapteeVisitableTest {

	private final String omschrijving = "foobar";
	private final double uren = 1.0;
	private final Geld uurloon = new Geld(1.0);
	private final BtwPercentage loonBtw = new BtwPercentage(0.4, false);
	@Mock private ListItemAdapteeVisitor<Object> visitor;

	@Override
	protected ProductLoonAdaptee makeInstance() {
		return ProductLoonAdaptee.build(a -> a
				.setOmschrijving(this.omschrijving)
				.setUren(this.uren)
				.setUurloon(this.uurloon)
				.setLoonBtwPercentage(this.loonBtw));
	}

	@Override
	protected ProductLoonAdaptee getInstance() {
		return (ProductLoonAdaptee) super.getInstance();
	}

	@Test
	public void testSetGetOmschrijving() {
		assertEquals(this.omschrijving, this.getInstance().getOmschrijving());
	}

	@Test
	public void testSetGetUren() {
		assertEquals(this.uren, this.getInstance().getUren(), 0.0);
	}

	@Test
	public void testSetGetUurloon() {
		assertEquals(this.uurloon, this.getInstance().getUurloon());
	}

	@Test
	public void testSetGetLoonBtwPercentage() {
		assertEquals(this.loonBtw, this.getInstance().getLoonBtwPercentage());
	}

	@Test
	public void testAccept() {
		this.getInstance().accept(this.visitor);

		verify(this.visitor).visit(eq(this.getInstance()));
	}
}

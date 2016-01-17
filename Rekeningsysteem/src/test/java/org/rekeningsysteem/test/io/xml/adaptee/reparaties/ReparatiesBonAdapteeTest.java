package org.rekeningsysteem.test.io.xml.adaptee.reparaties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdapteeVisitor;
import org.rekeningsysteem.io.xml.adaptee.reparaties.ReparatiesBonAdaptee;
import org.rekeningsysteem.test.io.xml.adaptee.ListItemAdapteeVisitableTest;

@RunWith(MockitoJUnitRunner.class)
public class ReparatiesBonAdapteeTest extends ListItemAdapteeVisitableTest {

	private final String omschrijving = "omschr";
	private final String bonnummer = "bonnr";
	private final Geld loon = new Geld(12.04);
	private final Geld materiaal = new Geld(16.40);
	@Mock private ListItemAdapteeVisitor<Object> visitor;

	@Override
	protected ReparatiesBonAdaptee makeInstance() {
		return ReparatiesBonAdaptee.build(a -> a
				.setOmschrijving(this.omschrijving)
				.setBonnummer(this.bonnummer)
				.setLoon(this.loon)
				.setMateriaal(this.materiaal));
	}

	@Override
	protected ReparatiesBonAdaptee getInstance() {
		return (ReparatiesBonAdaptee) super.getInstance();
	}

	@Test
	public void testSetGetOmschrijving() {
		assertEquals(this.omschrijving, this.getInstance().getOmschrijving());
	}

	@Test
	public void testSetGetBonnummer() {
		assertEquals(this.bonnummer, this.getInstance().getBonnummer());
	}

	@Test
	public void testSetGetLoon() {
		assertEquals(this.loon, this.getInstance().getLoon());
	}

	@Test
	public void testSetGetMateriaal() {
		assertEquals(this.materiaal, this.getInstance().getMateriaal());
	}

	@Test
	public void testAccept() {
		this.getInstance().accept(this.visitor);

		verify(this.visitor).visit(eq(this.getInstance()));
	}
}

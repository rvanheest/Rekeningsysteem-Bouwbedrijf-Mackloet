package org.rekeningsysteem.test.io.xml.adaptee.particulier;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Currency;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.xml.adaptee.RekeningAdapteeVisitor;
import org.rekeningsysteem.io.xml.adaptee.particulier.ParticulierFactuurAdaptee;
import org.rekeningsysteem.test.io.xml.adaptee.RekeningAdapteeVisitableTest;

@RunWith(MockitoJUnitRunner.class)
public class ParticulierFactuurAdapteeTest extends RekeningAdapteeVisitableTest {

	private final OmschrFactuurHeader header = new OmschrFactuurHeader(
			new Debiteur("", "", "", "", "", ""), LocalDate.now(), "", "");
	private final Currency currency = Currency.getInstance("EUR");
	private final ItemList<ParticulierArtikel> list = new ItemList<>();
	@Mock private RekeningAdapteeVisitor<Object> visitor;

	@Override
	protected ParticulierFactuurAdaptee makeInstance() {
		return ParticulierFactuurAdaptee.build(a -> a
				.setFactuurHeader(this.header)
				.setCurrency(this.currency)
				.setList(this.list));
	}

	@Override
	protected ParticulierFactuurAdaptee getInstance() {
		return (ParticulierFactuurAdaptee) super.getInstance();
	}

	@Test
	public void testSetGetFactuurHeader() {
		assertEquals(this.header, this.getInstance().getFactuurHeader());
	}

	@Test
	public void testSetGetValuta() {
		assertEquals(this.currency, this.getInstance().getCurrency());
	}

	@Test
	public void testSetGetList() {
		assertEquals(this.list, this.getInstance().getList());
	}

	@Test
	public void testAccept() {
		this.getInstance().accept(this.visitor);

		verify(this.visitor).visit(eq(this.getInstance()));
	}
}

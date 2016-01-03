package org.rekeningsysteem.test.io.xml.adaptee.particulier;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Currency;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.xml.adaptee.RekeningAdapteeVisitor;
import org.rekeningsysteem.io.xml.adaptee.particulier.ParticulierFactuurAdaptee;
import org.rekeningsysteem.test.io.xml.adaptee.RekeningAdapteeVisitableTest;

@RunWith(MockitoJUnitRunner.class)
public class ParticulierFactuurAdapteeTest extends RekeningAdapteeVisitableTest {

	@Mock private RekeningAdapteeVisitor<Object> visitor;

	@Override
	protected ParticulierFactuurAdaptee makeInstance() {
		return new ParticulierFactuurAdaptee();
	}

	@Override
	protected ParticulierFactuurAdaptee getInstance() {
		return (ParticulierFactuurAdaptee) super.getInstance();
	}

	@Test
	public void testSetGetFactuurHeader() {
		OmschrFactuurHeader header = new OmschrFactuurHeader(new Debiteur("", "", "", "", "", ""),
				LocalDate.now(), "", "");
		this.getInstance().setFactuurHeader(header);
		assertEquals(header, this.getInstance().getFactuurHeader());
	}

	@Test
	public void testSetGetValuta() {
		this.getInstance().setCurrency(Currency.getInstance("EUR"));
		assertEquals(Currency.getInstance("EUR"), this.getInstance().getCurrency());
	}

	@Test
	public void testSetGetList() {
		ItemList<ParticulierArtikel> list = new ItemList<>();
		this.getInstance().setList(list);
		assertEquals(list, this.getInstance().getList());
	}

	@Test
	public void testAccept() {
		this.getInstance().accept(this.visitor);

		verify(this.visitor).visit(eq(this.getInstance()));
	}
}

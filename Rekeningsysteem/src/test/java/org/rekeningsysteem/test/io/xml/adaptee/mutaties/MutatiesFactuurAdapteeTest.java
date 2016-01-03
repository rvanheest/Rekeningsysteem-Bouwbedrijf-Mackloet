package org.rekeningsysteem.test.io.xml.adaptee.mutaties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Currency;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.xml.adaptee.RekeningAdapteeVisitor;
import org.rekeningsysteem.io.xml.adaptee.mutaties.MutatiesFactuurAdaptee;
import org.rekeningsysteem.test.io.xml.adaptee.RekeningAdapteeVisitableTest;

@RunWith(MockitoJUnitRunner.class)
public class MutatiesFactuurAdapteeTest extends RekeningAdapteeVisitableTest {

	@Mock private RekeningAdapteeVisitor<Object> visitor;

	@Override
	protected MutatiesFactuurAdaptee makeInstance() {
		return new MutatiesFactuurAdaptee();
	}

	@Override
	protected MutatiesFactuurAdaptee getInstance() {
		return (MutatiesFactuurAdaptee) super.getInstance();
	}

	@Test
	public void testSetGetFactuurHeader() {
		FactuurHeader header = new FactuurHeader(new Debiteur("", "", "", "", "", ""),
				LocalDate.now(), "");
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
		ItemList<MutatiesBon> list = new ItemList<>();
		this.getInstance().setList(list);
		assertEquals(list, this.getInstance().getList());
	}

	@Test
	public void testAccept() {
		this.getInstance().accept(this.visitor);

		verify(this.visitor).visit(eq(this.getInstance()));
	}
}

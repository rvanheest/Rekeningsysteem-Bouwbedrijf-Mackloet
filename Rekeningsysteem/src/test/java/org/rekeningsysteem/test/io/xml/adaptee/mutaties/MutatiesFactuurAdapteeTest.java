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
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.xml.adaptee.RekeningAdapteeVisitor;
import org.rekeningsysteem.io.xml.adaptee.mutaties.MutatiesFactuurAdaptee;
import org.rekeningsysteem.test.io.xml.adaptee.RekeningAdapteeVisitableTest;

@RunWith(MockitoJUnitRunner.class)
public class MutatiesFactuurAdapteeTest extends RekeningAdapteeVisitableTest {

	private final FactuurHeader header = new FactuurHeader(new Debiteur("", "", "", "", "", ""),
			LocalDate.now(), "");
	private final Currency currency = Currency.getInstance("EUR");
	private final ItemList<MutatiesInkoopOrder> list = new ItemList<>();
	@Mock private RekeningAdapteeVisitor<Object> visitor;

	@Override
	protected MutatiesFactuurAdaptee makeInstance() {
		return MutatiesFactuurAdaptee.build(a -> a
				.setFactuurHeader(this.header)
				.setCurrency(this.currency)
				.setList(this.list));
	}

	@Override
	protected MutatiesFactuurAdaptee getInstance() {
		return (MutatiesFactuurAdaptee) super.getInstance();
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

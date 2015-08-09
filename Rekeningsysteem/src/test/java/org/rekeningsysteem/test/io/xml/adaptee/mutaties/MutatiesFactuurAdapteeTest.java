package org.rekeningsysteem.test.io.xml.adaptee.mutaties;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.xml.adaptee.mutaties.MutatiesFactuurAdaptee;

public class MutatiesFactuurAdapteeTest {

	private MutatiesFactuurAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = new MutatiesFactuurAdaptee();
	}

	@Test
	public void testSetGetFactuurHeader() {
		FactuurHeader header = new FactuurHeader(new Debiteur("", "", "", "", "", ""),
				LocalDate.now(), "");
		this.adaptee.setFactuurHeader(header);
		assertEquals(header, this.adaptee.getFactuurHeader());
	}

	@Test
	public void testSetGetValuta() {
		this.adaptee.setCurrency(Currency.getInstance("EUR"));
		assertEquals(Currency.getInstance("EUR"), this.adaptee.getCurrency());
	}

	@Test
	public void testSetGetList() {
		ItemList<MutatiesBon> list = new ItemList<>();
		this.adaptee.setList(list);
		assertEquals(list, this.adaptee.getList());
	}
}

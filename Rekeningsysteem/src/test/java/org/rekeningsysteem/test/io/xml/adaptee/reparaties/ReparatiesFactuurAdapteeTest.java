package org.rekeningsysteem.test.io.xml.adaptee.reparaties;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.xml.adaptee.reparaties.ReparatiesFactuurAdaptee;

public class ReparatiesFactuurAdapteeTest {

	private ReparatiesFactuurAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = new ReparatiesFactuurAdaptee();
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
		this.adaptee.setValuta("valuta");
		assertEquals("valuta", this.adaptee.getValuta());
	}

	@Test
	public void testSetGetList() {
		ItemList<ReparatiesBon> list = new ItemList<>();
		this.adaptee.setList(list);
		assertEquals(list, this.adaptee.getList());
	}

	@Test
	public void testSetGetBtwPercentage() {
		BtwPercentage btw = new BtwPercentage(6, 21);
		this.adaptee.setBtwPercentage(btw);
		assertEquals(btw, this.adaptee.getBtwPercentage());
	}
}
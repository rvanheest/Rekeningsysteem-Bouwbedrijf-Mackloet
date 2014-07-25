package org.rekeningsysteem.test.io.xml.adaptee.particulier;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.io.xml.adaptee.particulier.ParticulierFactuurAdaptee;

public class ParticulierFactuurAdapteeTest {

	private ParticulierFactuurAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = new ParticulierFactuurAdaptee();
	}

	@Test
	public void testSetGetFactuurHeader() {
		OmschrFactuurHeader header = new OmschrFactuurHeader(new Debiteur("", "", "", "", "", ""),
				LocalDate.now(), "", "");
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
		ItemList<ParticulierArtikel> list = new ItemList<>();
		this.adaptee.setItemList(list);
		assertEquals(list, this.adaptee.getItemList());
	}

	@Test
	public void testSetGetBtwPercentage() {
		BtwPercentage btw = new BtwPercentage(6, 21);
		this.adaptee.setBtwPercentage(btw);
		assertEquals(btw, this.adaptee.getBtwPercentage());
	}

	@Test
	public void testSetGetLoonList() {
		ItemList<AbstractLoon> loonList = new ItemList<>();
		this.adaptee.setLoonList(loonList);
		assertEquals(loonList, this.adaptee.getLoonList());
	}
}

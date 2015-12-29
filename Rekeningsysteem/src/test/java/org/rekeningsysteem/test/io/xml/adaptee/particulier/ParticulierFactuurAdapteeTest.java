package org.rekeningsysteem.test.io.xml.adaptee.particulier;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
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
		this.adaptee.setCurrency(Currency.getInstance("EUR"));
		assertEquals(Currency.getInstance("EUR"), this.adaptee.getCurrency());
	}

	@Test
	public void testSetGetList() {
		ItemList<ParticulierArtikel> list = new ItemList<>();
		this.adaptee.setList(list);
		assertEquals(list, this.adaptee.getList());
	}
}

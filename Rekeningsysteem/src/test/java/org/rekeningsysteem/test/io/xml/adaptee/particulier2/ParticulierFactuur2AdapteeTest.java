package org.rekeningsysteem.test.io.xml.adaptee.particulier2;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.ParticulierArtikel2;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.xml.adaptee.particulier2.ParticulierFactuur2Adaptee;

public class ParticulierFactuur2AdapteeTest {

	private ParticulierFactuur2Adaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = new ParticulierFactuur2Adaptee();
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
		ItemList<ParticulierArtikel2> list = new ItemList<>();
		this.adaptee.setList(list);
		assertEquals(list, this.adaptee.getList());
	}
}

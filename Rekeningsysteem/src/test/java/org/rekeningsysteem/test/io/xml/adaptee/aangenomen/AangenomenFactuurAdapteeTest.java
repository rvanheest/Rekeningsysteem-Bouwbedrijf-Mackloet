package org.rekeningsysteem.test.io.xml.adaptee.aangenomen;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Currency;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.xml.adaptee.aangenomen.AangenomenFactuurAdaptee;

@Ignore
@Deprecated
public class AangenomenFactuurAdapteeTest {

	private AangenomenFactuurAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = new AangenomenFactuurAdaptee();
	}

	@Test
	public void testSetGetFactuurHeader() {
		OmschrFactuurHeader header = new OmschrFactuurHeader(new Debiteur("", "", "", "", "", ""),
				LocalDate.now(), "", "");
		this.adaptee.setFactuurHeader(header);
		assertEquals(header, this.adaptee.getFactuurHeader());
	}

	@Test
	public void testSetGetCurrency() {
		this.adaptee.setCurrency(Currency.getInstance("EUR"));
		assertEquals(Currency.getInstance("EUR"), this.adaptee.getCurrency());
	}

	@Test
	public void testSetGetList() {
		ItemList<AangenomenListItem> list = new ItemList<>();
		this.adaptee.setList(list);
		assertEquals(list, this.adaptee.getList());
	}
}

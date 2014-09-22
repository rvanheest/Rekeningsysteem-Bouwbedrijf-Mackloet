package org.rekeningsysteem.test.io.xml.root;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.aangenomen.AangenomenFactuur;
import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.xml.root.AangenomenFactuurRoot;

public class AangenomenFactuurRootTest {

	private AangenomenFactuurRoot root;

	@Before
	public void setUp() {
		this.root = new AangenomenFactuurRoot();
	}

	@Test
	public void testGetType() {
		assertEquals("AangenomenFactuur", this.root.getType());
	}

	@Test
	public void testSetGetFactuur() {
		ItemList<AangenomenListItem> itemList = new ItemList<>();
		itemList.add(new AangenomenListItem("omschr", new Geld(12.3), new Geld(49)));

		AangenomenFactuur expected = new AangenomenFactuur(new OmschrFactuurHeader(
				new Debiteur("a", "b", "c", "d", "e", "f"), LocalDate.now(), "g", "h"), "i",
				itemList, new BtwPercentage(6, 21));
		this.root.setRekening(expected);

		assertEquals(expected, this.root.getRekening());
	}
}
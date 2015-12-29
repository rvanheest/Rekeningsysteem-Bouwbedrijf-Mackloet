package org.rekeningsysteem.test.io.xml.adapter.aangenomen;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Currency;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.rekeningsysteem.data.aangenomen.AangenomenFactuur;
import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.xml.adapter.aangenomen.AangenomenFactuurAdapter;

@Ignore
@Deprecated
public class AangenomenFactuurAdapterTest {

	private AangenomenFactuurAdapter adapter;

	@Before
	public void setUp() {
		this.adapter = new AangenomenFactuurAdapter();
	}

	@Test
	public void testUnmarshalMarshal() {
		ItemList<AangenomenListItem> itemList = new ItemList<>();
		itemList.add(new AangenomenListItem("omschr", new Geld(12.3), 6, new Geld(49), 21));

		AangenomenFactuur expected = new AangenomenFactuur(new OmschrFactuurHeader(
				new Debiteur("a", "b", "c", "d", "e", "f"), LocalDate.now(), "g", "h"), Currency.getInstance("EUR"),
				itemList);

		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}

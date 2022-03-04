package org.rekeningsysteem.test.io.xml.adapter;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.io.xml.adapter.ItemListAdapter;

@RunWith(MockitoJUnitRunner.class)
public class ItemListAdapterTest {

	private ItemListAdapter adapter;
	@Mock private ListItem item;

	@Before
	public void setUp() {
		this.adapter = new ItemListAdapter();
	}

	@Test
	public void testSetGetList() {
		ItemList<ListItem> list = new ItemList<>();
		list.add(this.item);
		assertEquals(list, this.adapter.unmarshal(this.adapter.marshal(list)));
	}
}

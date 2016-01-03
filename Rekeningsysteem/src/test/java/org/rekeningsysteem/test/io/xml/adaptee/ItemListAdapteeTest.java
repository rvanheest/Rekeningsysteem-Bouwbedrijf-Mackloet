package org.rekeningsysteem.test.io.xml.adaptee;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.io.xml.adaptee.ItemListAdaptee;

@RunWith(MockitoJUnitRunner.class)
public class ItemListAdapteeTest {

	private ItemListAdaptee adaptee;
	@Mock private ListItem item;

	@Before
	public void setUp() {
		this.adaptee = new ItemListAdaptee();
	}

	@Test
	public void testSetGetList() {
		List<ListItem> list = new ArrayList<>();
		list.add(this.item);
		this.adaptee.setList(list);
		assertEquals(list, this.adaptee.getList());
	}
}

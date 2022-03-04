package org.rekeningsysteem.test.io.xml.adaptee;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.io.xml.adaptee.ItemListAdaptee;

@RunWith(MockitoJUnitRunner.class)
public class ItemListAdapteeTest {

	private List<ListItem> list;
	private ItemListAdaptee adaptee;
	@Mock private ListItem item;

	@Before
	public void setUp() {
		this.list = Arrays.asList(this.item);
		this.adaptee = ItemListAdaptee.build(a -> a
				.setList(this.list));
	}

	@Test
	public void testSetGetList() {
		assertEquals(this.list, this.adaptee.getList());
	}
}

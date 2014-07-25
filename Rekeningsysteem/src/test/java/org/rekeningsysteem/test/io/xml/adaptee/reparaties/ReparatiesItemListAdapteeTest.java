package org.rekeningsysteem.test.io.xml.adaptee.reparaties;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.io.xml.adaptee.reparaties.ReparatiesItemListAdaptee;

public class ReparatiesItemListAdapteeTest {

	private ReparatiesItemListAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = new ReparatiesItemListAdaptee();
	}

	@Test
	public void testSetGetList() {
		List<ReparatiesBon> list = new ArrayList<>();
		this.adaptee.setList(list);
		assertEquals(list, this.adaptee.getList());
	}
}

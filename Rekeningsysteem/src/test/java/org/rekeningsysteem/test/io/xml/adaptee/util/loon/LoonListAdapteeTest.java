package org.rekeningsysteem.test.io.xml.adaptee.util.loon;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.io.xml.adaptee.util.loon.LoonListAdaptee;

public class LoonListAdapteeTest {

	private LoonListAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = new LoonListAdaptee();
	}

	@Test
	public void testSetGetLoonList() {
		List<AbstractLoon> list = new ArrayList<>();
		this.adaptee.setList(list);
		assertEquals(list, this.adaptee.getList());
	}
}

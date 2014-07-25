package org.rekeningsysteem.test.io.xml.adaptee.mutaties;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.io.xml.adaptee.mutaties.MutatiesItemListAdaptee;

public class MutatiesItemListAdapteeTest {

	private MutatiesItemListAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = new MutatiesItemListAdaptee();
	}

	@Test
	public void testSetGetList() {
		List<MutatiesBon> list = new ArrayList<>();
		this.adaptee.setList(list);
		assertEquals(list, this.adaptee.getList());
	}
}

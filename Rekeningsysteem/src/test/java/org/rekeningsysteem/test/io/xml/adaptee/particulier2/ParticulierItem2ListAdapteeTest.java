package org.rekeningsysteem.test.io.xml.adaptee.particulier2;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.ParticulierArtikel2;
import org.rekeningsysteem.io.xml.adaptee.particulier2.ParticulierItem2ListAdaptee;

public class ParticulierItem2ListAdapteeTest {

	private ParticulierItem2ListAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = new ParticulierItem2ListAdaptee();
	}

	@Test
	public void testSetGetList() {
		List<ParticulierArtikel2> list = new ArrayList<>();
		this.adaptee.setList(list);
		assertEquals(list, this.adaptee.getList());
	}
}

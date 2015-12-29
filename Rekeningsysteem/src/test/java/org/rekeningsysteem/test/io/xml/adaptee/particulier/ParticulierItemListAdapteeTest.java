package org.rekeningsysteem.test.io.xml.adaptee.particulier;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.io.xml.adaptee.particulier.ParticulierItemListAdaptee;

@Ignore
@Deprecated
public class ParticulierItemListAdapteeTest {

	private ParticulierItemListAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = new ParticulierItemListAdaptee();
	}

	@Test
	public void testSetGetList() {
		List<ParticulierArtikel> list = new ArrayList<>();
		this.adaptee.setList(list);
		assertEquals(list, this.adaptee.getList());
	}
}

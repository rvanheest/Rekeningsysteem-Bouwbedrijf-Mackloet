package org.rekeningsysteem.test.io.xml.adaptee.aangenomen;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.io.xml.adaptee.aangenomen.AangenomenItemListAdaptee;

@Ignore
@Deprecated
public class AangenomenItemListAdapteeTest {

	private AangenomenItemListAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = new AangenomenItemListAdaptee();
	}

	@Test
	public void testSetGetList() {
		List<AangenomenListItem> list = new ArrayList<>();
		this.adaptee.setList(list);
		assertEquals(list, this.adaptee.getList());
	}
}

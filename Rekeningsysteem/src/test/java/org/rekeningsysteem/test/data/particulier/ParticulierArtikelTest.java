package org.rekeningsysteem.test.data.particulier;

import org.junit.Test;
import org.rekeningsysteem.test.data.EqualsHashCodeTest;
import org.rekeningsysteem.test.data.util.ListItemTest;

public abstract class ParticulierArtikelTest extends EqualsHashCodeTest implements ListItemTest {

	@Test
	public abstract void testToArray();
}

package org.rekeningsysteem.test.data.util;

import org.junit.Test;
import org.rekeningsysteem.data.util.BtwPercentages;

import static org.junit.Assert.assertFalse;

public class BtwPercentagesTest {

	@Test
	public void secondConstructorIsNietVerlegd() {
		BtwPercentages btwPercentages = new BtwPercentages(21, 9);
		
		assertFalse(btwPercentages.loonPercentage().verlegd());
		assertFalse(btwPercentages.materiaalPercentage().verlegd());
	}
}

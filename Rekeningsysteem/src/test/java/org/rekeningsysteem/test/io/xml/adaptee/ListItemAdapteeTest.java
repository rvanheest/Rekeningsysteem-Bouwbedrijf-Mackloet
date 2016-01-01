package org.rekeningsysteem.test.io.xml.adaptee;

import org.junit.Before;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdaptee;

public abstract class ListItemAdapteeTest {

	private ListItemAdaptee adaptee;

	protected abstract ListItemAdaptee makeInstance();

	protected ListItemAdaptee getInstance() {
		return this.adaptee;
	}

	@Before
	public void setUp() {
		this.adaptee = this.makeInstance();
	}
}

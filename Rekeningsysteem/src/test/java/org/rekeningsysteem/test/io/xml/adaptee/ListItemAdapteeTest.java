package org.rekeningsysteem.test.io.xml.adaptee;

import org.junit.Before;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdapteeVisitable;

public abstract class ListItemAdapteeTest {

	private ListItemAdapteeVisitable adaptee;

	protected abstract ListItemAdapteeVisitable makeInstance();

	protected ListItemAdapteeVisitable getInstance() {
		return this.adaptee;
	}

	@Before
	public void setUp() {
		this.adaptee = this.makeInstance();
	}
}

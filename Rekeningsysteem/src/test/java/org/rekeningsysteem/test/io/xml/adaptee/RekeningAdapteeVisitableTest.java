package org.rekeningsysteem.test.io.xml.adaptee;

import org.junit.Before;
import org.rekeningsysteem.io.xml.adaptee.RekeningAdapteeVisitable;

public abstract class RekeningAdapteeVisitableTest {

	private RekeningAdapteeVisitable adaptee;

	protected abstract RekeningAdapteeVisitable makeInstance();

	protected RekeningAdapteeVisitable getInstance() {
		return this.adaptee;
	}

	@Before
	public void setUp() {
		this.adaptee = this.makeInstance();
	}
}

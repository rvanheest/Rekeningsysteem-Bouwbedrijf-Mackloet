package org.rekeningsysteem.test.data.util.loon;

import org.junit.Before;
import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.test.data.EqualsHashCodeTest;

public abstract class AbstractLoonTest extends EqualsHashCodeTest {

	private AbstractLoon loon;
	private final String omschrijving = "omschrijving";

	@Override
	protected abstract AbstractLoon makeInstance();

	protected AbstractLoon getInstance() {
		return this.loon;
	}

	protected String getTestOmschrijving() {
		return this.omschrijving;
	}

	@Override
	@Before
	public void setUp() {
		super.setUp();
		this.loon = this.makeInstance();
	}

	// TODO do the rest of the testing stuff
}

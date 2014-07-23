package org.rekeningsysteem.test.data.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;
import org.rekeningsysteem.logic.factuurnummer.FactuurnummerManager;
import org.rekeningsysteem.test.data.EqualsHashCodeTest;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractRekeningTest extends EqualsHashCodeTest {

	private AbstractRekening rekening;
	private FactuurHeader header;
	@Mock private RekeningVisitor visitor;

	@Override
	protected abstract AbstractRekening makeInstance();

	protected AbstractRekening getInstance() {
		return this.rekening;
	}

	protected FactuurHeader getTestFactuurHeader() {
		return this.header;
	}

	protected RekeningVisitor getMockedVisitor() {
		return this.visitor;
	}

	@Override
	@Before
	public void setUp() {
		super.setUp();
		this.header = new FactuurHeader(new Debiteur("a", "b", "c", "d", "e"),
				LocalDate.of(1992, 7, 30));
		this.rekening = this.makeInstance();
	}

	@Test
	public void testGetHeader() {
		assertEquals(this.header, this.rekening.getFactuurHeader());
	}

	@Test
	public void testInitFactuurnummer() {
		assertFalse(this.header.getFactuurnummer().isPresent());
		
		FactuurnummerManager manager = mock(FactuurnummerManager.class);
		when(manager.getFactuurnummer()).thenReturn("12014");
		
		this.rekening.initFactuurnummer(manager);
		
		assertEquals(Optional.of("12014"), this.header.getFactuurnummer());
		verify(manager).getFactuurnummer();
	}
	
	@Test
	public void testSameFactuurnummer() {
		this.header.setFactuurnummer("12013");
		assertTrue(this.header.getFactuurnummer().isPresent());
		
		FactuurnummerManager manager = mock(FactuurnummerManager.class);
		this.rekening.initFactuurnummer(manager);
		
		assertEquals(Optional.of("12013"), this.header.getFactuurnummer());
		verifyZeroInteractions(manager);
	}

	@Test
	public void testEqualsFalseOtherFactuurHeader() {
		this.header = new FactuurHeader(new Debiteur("", "", "", "", ""), LocalDate.now(), "test");
		assertFalse(this.rekening.equals(this.makeInstance()));
	}
}

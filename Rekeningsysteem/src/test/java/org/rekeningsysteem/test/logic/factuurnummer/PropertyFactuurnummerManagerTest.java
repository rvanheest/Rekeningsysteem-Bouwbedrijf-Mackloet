package org.rekeningsysteem.test.logic.factuurnummer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.logic.factuurnummer.PropertyFactuurnummerManager;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyKey;
import org.rekeningsysteem.properties.PropertyModelEnum;

@RunWith(MockitoJUnitRunner.class)
public class PropertyFactuurnummerManagerTest {

	private PropertyFactuurnummerManager manager;
	@Mock private PropertiesWorker worker;
	private static int yearNow = LocalDate.now().getYear();
	private final PropertyKey key = PropertyModelEnum.FACTUURNUMMER;

	@Before
	public void setUp() {
		this.manager = new PropertyFactuurnummerManager(this.worker, this.key);
	}

	@Test
	public void testGetFactuurnummerSameYear() {
		when(this.worker.getProperty(eq(this.key))).thenReturn(Optional.of("12" + yearNow));

		assertEquals("12" + yearNow, this.manager.getFactuurnummer());
		verify(this.worker).getProperty(eq(this.key));
		verify(this.worker).setProperty(eq(this.key), eq("13" + yearNow));

		assertEquals("12" + yearNow, this.manager.getFactuurnummer());
		verifyNoMoreInteractions(this.worker);
	}

	@Test
	public void testGetFactuurnummerOverlappingNumbers() {
		when(this.worker.getProperty(eq(this.key))).thenReturn(Optional.of("202020"));

		assertEquals("202020", this.manager.getFactuurnummer());
		verify(this.worker).getProperty(eq(this.key));
		if (yearNow == 2020)
			verify(this.worker).setProperty(eq(this.key), eq("212020"));
		else
			verify(this.worker).setProperty(eq(this.key), eq("1" + yearNow));

		assertEquals("202020", this.manager.getFactuurnummer());
		verifyNoMoreInteractions(this.worker);
	}

	@Test
	public void testGetFactuurnummerOtherYear() {
		when(this.worker.getProperty(eq(this.key))).thenReturn(Optional.of("25" + (yearNow - 2)));

		assertEquals("1" + yearNow, this.manager.getFactuurnummer());
		verify(this.worker).getProperty(eq(this.key));
		verify(this.worker).setProperty(eq(this.key), eq("2" + yearNow));

		assertEquals("1" + yearNow, this.manager.getFactuurnummer());
		verifyNoMoreInteractions(this.worker);
	}

	@Test
	public void testGetFactuurnummerPropertyNotFound() {
		when(this.worker.getProperty(eq(this.key))).thenReturn(Optional.empty());
		
		assertEquals("1" + yearNow, this.manager.getFactuurnummer());
		verify(this.worker).getProperty(eq(this.key));
		verify(this.worker).setProperty(eq(this.key), eq("2" + yearNow));
		
		assertEquals("1" + yearNow, this.manager.getFactuurnummer());
		verifyNoMoreInteractions(this.worker);
	}
}

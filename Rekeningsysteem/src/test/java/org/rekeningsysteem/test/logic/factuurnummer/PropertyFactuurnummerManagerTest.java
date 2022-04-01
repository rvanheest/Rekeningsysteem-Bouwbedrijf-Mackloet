package org.rekeningsysteem.test.logic.factuurnummer;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.rekeningsysteem.logic.factuurnummer.Factuurnummer;
import org.rekeningsysteem.logic.factuurnummer.FactuurnummerFormatter;
import org.rekeningsysteem.logic.factuurnummer.PropertyFactuurnummerManager;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyKey;
import org.rekeningsysteem.properties.PropertyModelEnum;

@RunWith(MockitoJUnitRunner.class)
public class PropertyFactuurnummerManagerTest {

	private PropertyFactuurnummerManager manager;
	@Mock private PropertiesWorker worker;
	@Mock private FactuurnummerFormatter formatter;
	private static int yearNow = LocalDate.now().getYear();
	private final PropertyKey key = PropertyModelEnum.FACTUURNUMMER;

	@Before
	public void setUp() {
		this.manager = new PropertyFactuurnummerManager(this.worker, this.key, this.formatter);
	}

	@Test
	public void testGetFactuurnummerSameYear() {
		String existingNummer = "12" + yearNow;
		when(this.worker.getProperty(eq(this.key))).thenReturn(Optional.of(existingNummer));
		when(this.formatter.heeftJaar(eq(existingNummer), eq(String.valueOf(yearNow)))).thenReturn(true);
		when(this.formatter.parse(eq(existingNummer), eq(String.valueOf(yearNow)))).thenReturn(new Factuurnummer(String.valueOf(yearNow), 12));
		when(this.formatter.format(eq(new Factuurnummer(String.valueOf(yearNow), 12)))).thenReturn("12" + yearNow);
		when(this.formatter.format(eq(new Factuurnummer(String.valueOf(yearNow), 13)))).thenReturn("13" + yearNow);

		assertEquals("12" + yearNow, this.manager.getFactuurnummer());
		verify(this.worker).getProperty(eq(this.key));
		verify(this.worker).setProperty(eq(this.key), eq("13" + yearNow));
		verify(this.formatter).heeftJaar(eq(existingNummer), eq(String.valueOf(yearNow)));
		verify(this.formatter).parse(eq(existingNummer), eq(String.valueOf(yearNow)));
		verify(this.formatter).format(eq(new Factuurnummer(String.valueOf(yearNow), 12)));
		verify(this.formatter).format(eq(new Factuurnummer(String.valueOf(yearNow), 13)));
		verifyNoMoreInteractions(this.worker);
		verifyNoMoreInteractions(this.formatter);

		assertEquals("12" + yearNow, this.manager.getFactuurnummer());
		verifyNoMoreInteractions(this.worker);
		verifyNoMoreInteractions(this.formatter);
	}

	@Test
	public void testGetFactuurnummerOtherYear() {
		String existingNummer = "25" + (yearNow - 2);
		when(this.worker.getProperty(eq(this.key))).thenReturn(Optional.of(existingNummer));
		when(this.formatter.heeftJaar(eq(existingNummer), eq(String.valueOf(yearNow)))).thenReturn(false);
		when(this.formatter.format(new Factuurnummer(String.valueOf(yearNow), 1))).thenReturn("1" + yearNow);
		when(this.formatter.format(new Factuurnummer(String.valueOf(yearNow), 2))).thenReturn("2" + yearNow);

		assertEquals("1" + yearNow, this.manager.getFactuurnummer());
		verify(this.worker).getProperty(eq(this.key));
		verify(this.worker).setProperty(eq(this.key), eq("2" + yearNow));
		verify(this.formatter).heeftJaar(eq(existingNummer), eq(String.valueOf(yearNow)));
		verify(this.formatter).format(new Factuurnummer(String.valueOf(yearNow), 1));
		verify(this.formatter).format(new Factuurnummer(String.valueOf(yearNow), 2));
		verifyNoMoreInteractions(this.worker);
		verifyNoMoreInteractions(this.formatter);

		assertEquals("1" + yearNow, this.manager.getFactuurnummer());
		verifyNoMoreInteractions(this.worker);
		verifyNoMoreInteractions(this.formatter);
	}

	@Test
	public void testGetFactuurnummerPropertyNotFound() {
		when(this.worker.getProperty(eq(this.key))).thenReturn(Optional.empty());
		when(this.formatter.format(new Factuurnummer(String.valueOf(yearNow), 1))).thenReturn("1" + yearNow);
		when(this.formatter.format(new Factuurnummer(String.valueOf(yearNow), 2))).thenReturn("2" + yearNow);
		
		assertEquals("1" + yearNow, this.manager.getFactuurnummer());
		verify(this.worker).getProperty(eq(this.key));
		verify(this.worker).setProperty(eq(this.key), eq("2" + yearNow));
		verify(this.formatter).format(new Factuurnummer(String.valueOf(yearNow), 1));
		verify(this.formatter).format(new Factuurnummer(String.valueOf(yearNow), 2));
		verifyNoMoreInteractions(this.worker);
		verifyNoMoreInteractions(this.formatter);
		
		assertEquals("1" + yearNow, this.manager.getFactuurnummer());
		verifyNoMoreInteractions(this.worker);
		verifyNoMoreInteractions(this.formatter);
	}
}

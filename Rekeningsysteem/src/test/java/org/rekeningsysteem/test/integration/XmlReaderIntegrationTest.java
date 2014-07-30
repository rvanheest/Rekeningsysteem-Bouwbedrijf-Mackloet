package org.rekeningsysteem.test.integration;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.aangenomen.AangenomenFactuur;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.io.FactuurLoader;
import org.rekeningsysteem.io.xml.OldXmlReader;
import org.rekeningsysteem.io.xml.XmlReader;
import org.rekeningsysteem.io.xml.guice.XmlReaderModule;
import org.rekeningsysteem.utils.Try;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class XmlReaderIntegrationTest {

	private Injector injector;
	private FactuurLoader newLoader;
	private FactuurLoader oldLoader;

	@Before
	public void setUp() {
		this.injector = Guice.createInjector(new XmlReaderModule());

		this.newLoader = this.injector.getInstance(XmlReader.class);
		this.oldLoader = this.injector.getInstance(OldXmlReader.class);
	}

	@Test
	public void testReadNewAangenomenFactuurXmlWithNewLoader() {
		Try<? extends AbstractRekening> actual = this.newLoader.load(new File(
				"src\\test\\resources\\NewXml\\aangenomenFactuurXMLTest.xml"));

		assertTrue(actual.get() instanceof AangenomenFactuur);
	}

	@Test
	public void testReadNewMutatiesFactuurXmlWithNewLoader() {
		Try<? extends AbstractRekening> actual = this.newLoader.load(new File(
				"src\\test\\resources\\NewXml\\mutatiesFactuurXMLTest.xml"));

		assertTrue(actual.get() instanceof MutatiesFactuur);
	}

	@Test
	public void testReadOldMutatiesFactuurXmlWithOldLoader() {
		Try<? extends AbstractRekening> actual = this.oldLoader.load(new File(
				"src\\test\\resources\\OldXml\\MutatiesFactuur.xml"));

		assertTrue(actual.get() instanceof MutatiesFactuur);
	}

	@Test(expected = IllegalStateException.class)
	public void testReadOldMutatiesFactuurXmlWithNewLoader() {
		this.newLoader.load(new File("src\\test\\resources\\OldXml\\MutatiesFactuur.xml")).throwException();
	}

	@Test
	public void testReadNewOfferteXmlWithNewLoader() {
		Try<? extends AbstractRekening> actual = this.newLoader.load(new File(
				"src\\test\\resources\\NewXml\\offerteXMLTest.xml"));

		assertTrue(actual.get() instanceof Offerte);
	}

	@Test
	public void testReadOldOfferteXmlWithOldLoader() {
		Try<? extends AbstractRekening> actual = this.oldLoader.load(new File(
				"src\\test\\resources\\OldXml\\Offerte.xml"));

		assertTrue(actual.get() instanceof Offerte);
	}

	@Test(expected = IllegalStateException.class)
	public void testReadOldOfferteXmlWithNewLoader() {
		this.newLoader.load(new File("src\\test\\resources\\OldXml\\Offerte.xml")).throwException();
	}

	@Test
	public void testReadNewParticulierFactuurXmlWithNewLoader() {
		Try<? extends AbstractRekening> actual = this.newLoader.load(new File(
				"src\\test\\resources\\NewXml\\particulierFactuurXMLTest.xml"));

		assertTrue(actual.get() instanceof ParticulierFactuur);
	}

	@Test
	public void testReadOldPartFactuurXmlWithOldLoader() {
		Try<? extends AbstractRekening> actual = this.oldLoader.load(new File(
				"src\\test\\resources\\OldXml\\PartFactuur.xml"));

		assertTrue(actual.get() instanceof ParticulierFactuur);
	}

	@Test(expected = IllegalStateException.class)
	public void testReadOldPartFactuurXmlWithNewLoader() {
		this.newLoader.load(new File("src\\test\\resources\\OldXml\\PartFactuur.xml")).throwException();
	}

	@Test
	public void testReadOldParticulierFactuur1XmlWithOldLoader() {
		Try<? extends AbstractRekening> actual = this.oldLoader.load(new File(
				"src\\test\\resources\\OldXml\\ParticulierFactuur1.xml"));

		assertTrue(actual.get() instanceof ParticulierFactuur);
	}

	@Test(expected = IllegalStateException.class)
	public void testReadOldParticulierFactuur1XmlWithNewLoader() {
		this.newLoader.load(new File("src\\test\\resources\\OldXml\\ParticulierFactuur1.xml")).throwException();
	}

	@Test
	public void testReadOldParticulierFactuur2XmlWithOldLoader() {
		Try<? extends AbstractRekening> actual = this.oldLoader.load(new File(
				"src\\test\\resources\\OldXml\\ParticulierFactuur2.xml"));

		assertTrue(actual.get() instanceof ParticulierFactuur);
	}

	@Test(expected = IllegalStateException.class)
	public void testReadOldParticulierFactuur2XmlWithNewLoader() {
		this.newLoader.load(new File("src\\test\\resources\\OldXml\\ParticulierFactuur2.xml")).throwException();
	}

	@Test
	public void testReadNewReparatiesFactuurXmlWithNewLoader() {
		Try<? extends AbstractRekening> actual = this.newLoader.load(new File(
				"src\\test\\resources\\NewXml\\reparatiesFactuurXMLTest.xml"));

		assertTrue(actual.get() instanceof ReparatiesFactuur);
	}

	@Test
	public void testReadOldReparatiesFactuurXmlWithOldLoader() {
		Try<? extends AbstractRekening> actual = this.oldLoader.load(new File(
				"src\\test\\resources\\OldXml\\ReparatiesFactuur.xml"));

		assertTrue(actual.get() instanceof ReparatiesFactuur);
	}

	@Test(expected = IllegalStateException.class)
	public void testReadOldReparatiesFactuurXmlWithNewLoader() {
		this.newLoader.load(new File("src\\test\\resources\\OldXml\\ReparatiesFactuur.xml")).throwException();
	}
}

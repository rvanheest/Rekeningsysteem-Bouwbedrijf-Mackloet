package org.rekeningsysteem.test.integration;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.aangenomen.AangenomenFactuur;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.io.FactuurLoader;
import org.rekeningsysteem.io.xml.OldXmlReader;
import org.rekeningsysteem.io.xml.XmlReader;

public class XmlReaderIntegrationTest {

	private FactuurLoader newLoader;
	private FactuurLoader oldLoader;

	@Before
	public void setUp() {
		this.newLoader = new XmlReader();
		this.oldLoader = new OldXmlReader();
	}

	@Test
	public void testReadNewAangenomenFactuurXmlWithNewLoader() {
		this.newLoader.load(new File("src\\test\\resources\\NewXml\\aangenomenFactuurXMLTest.xml"))
				.forEach(rek -> assertTrue(rek instanceof AangenomenFactuur));
	}

	@Test
	public void testReadNewMutatiesFactuurXmlWithNewLoader() {
		this.newLoader.load(new File("src\\test\\resources\\NewXml\\mutatiesFactuurXMLTest.xml"))
				.forEach(rek -> assertTrue(rek instanceof MutatiesFactuur));
	}

	@Test
	public void testReadOldMutatiesFactuurXmlWithOldLoader() {
		this.oldLoader.load(new File("src\\test\\resources\\OldXml\\MutatiesFactuur.xml"))
				.forEach(rek -> assertTrue(rek instanceof MutatiesFactuur));
	}

	@Test
	public void testReadOldMutatiesFactuurXmlWithNewLoader() {
		this.newLoader.load(new File("src\\test\\resources\\OldXml\\MutatiesFactuur.xml"))
				.forEach(rek -> Assert.fail("item received: " + rek),
						error -> assertTrue(error instanceof IllegalArgumentException));
	}

	@Test
	public void testReadNewOfferteXmlWithNewLoader() {
		this.newLoader.load(new File("src\\test\\resources\\NewXml\\offerteXMLTest.xml"))
				.forEach(rek -> assertTrue(rek instanceof Offerte));
	}

	@Test
	public void testReadOldOfferteXmlWithOldLoader() {
		this.oldLoader.load(new File("src\\test\\resources\\OldXml\\Offerte.xml"))
				.forEach(rek -> assertTrue(rek instanceof Offerte));
	}

	@Test
	public void testReadOldOfferteXmlWithNewLoader() {
		this.newLoader.load(new File("src\\test\\resources\\OldXml\\Offerte.xml"))
				.forEach(rek -> Assert.fail("item received: " + rek),
						error -> assertTrue(error instanceof IllegalArgumentException));
	}

	@Test
	public void testReadNewParticulierFactuurXmlWithNewLoader() {
		this.newLoader
				.load(new File("src\\test\\resources\\NewXml\\particulierFactuurXMLTest.xml"))
				.forEach(rek -> assertTrue(rek instanceof ParticulierFactuur));
	}

	@Test
	public void testReadOldPartFactuurXmlWithOldLoader() {
		this.oldLoader.load(new File("src\\test\\resources\\OldXml\\PartFactuur.xml"))
				.forEach(rek -> assertTrue(rek instanceof ParticulierFactuur));
	}

	@Test
	public void testReadOldPartFactuurXmlWithNewLoader() {
		this.newLoader.load(new File("src\\test\\resources\\OldXml\\PartFactuur.xml"))
				.forEach(rek -> Assert.fail("item received: " + rek),
						error -> assertTrue(error instanceof IllegalArgumentException));
	}

	@Test
	public void testReadOldParticulierFactuur1XmlWithOldLoader() {
		this.oldLoader.load(new File("src\\test\\resources\\OldXml\\ParticulierFactuur1.xml"))
				.forEach(rek -> assertTrue(rek instanceof ParticulierFactuur));
	}

	@Test
	public void testReadOldParticulierFactuur1XmlWithNewLoader() {
		this.newLoader.load(new File("src\\test\\resources\\OldXml\\ParticulierFactuur1.xml"))
				.forEach(rek -> Assert.fail("item received: " + rek),
						error -> assertTrue(error instanceof IllegalArgumentException));
	}

	@Test
	public void testReadOldParticulierFactuur2XmlWithOldLoader() {
		this.oldLoader.load(new File("src\\test\\resources\\OldXml\\ParticulierFactuur2.xml"))
				.forEach(rek -> assertTrue(rek instanceof ParticulierFactuur));
	}

	@Test
	public void testReadOldParticulierFactuur2XmlWithNewLoader() {
		this.newLoader.load(new File("src\\test\\resources\\OldXml\\ParticulierFactuur2.xml"))
				.forEach(rek -> Assert.fail("item received: " + rek),
						error -> assertTrue(error instanceof IllegalArgumentException));
	}

	@Test
	public void testReadNewReparatiesFactuurXmlWithNewLoader() {
		this.newLoader.load(new File("src\\test\\resources\\NewXml\\reparatiesFactuurXMLTest.xml"))
				.forEach(rek -> assertTrue(rek instanceof ReparatiesFactuur));
	}

	@Test
	public void testReadOldReparatiesFactuurXmlWithOldLoader() {
		this.oldLoader.load(new File("src\\test\\resources\\OldXml\\ReparatiesFactuur.xml"))
				.forEach(rek -> assertTrue(rek instanceof ReparatiesFactuur));
	}

	@Test
	public void testReadOldReparatiesFactuurXmlWithNewLoader() {
		this.newLoader.load(new File("src\\test\\resources\\OldXml\\ReparatiesFactuur.xml"))
				.forEach(rek -> Assert.fail("item received: " + rek),
						error -> assertTrue(error instanceof IllegalArgumentException));
	}
}

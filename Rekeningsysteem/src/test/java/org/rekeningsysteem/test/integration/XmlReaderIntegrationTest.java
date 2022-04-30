package org.rekeningsysteem.test.integration;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.exception.XmlParseException;
import org.rekeningsysteem.io.xml.XmlLoader;
import org.rekeningsysteem.io.xml.XmlReader1;
import org.rekeningsysteem.io.xml.XmlReader2;
import org.rekeningsysteem.io.xml.XmlReader3;
import org.rekeningsysteem.io.xml.XmlReader4;

import java.nio.file.Paths;

public class XmlReaderIntegrationTest {

	private XmlLoader loader1;
	private XmlLoader loader2;
	private XmlLoader loader3;
	private XmlLoader loader4;

	@Before
	public void setUp() throws ParserConfigurationException {
		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		this.loader1 = new XmlReader1(documentBuilder);
		this.loader2 = new XmlReader2(documentBuilder);
		this.loader3 = new XmlReader3(documentBuilder);
		this.loader4 = new XmlReader4(documentBuilder);
	}

	@Test
	public void testReadMutatiesFactuur1XmlWithLoader1() {
		this.loader1.load(Paths.get("src", "test", "resources", "xml", "xml1", "MutatiesFactuur.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertValue(MutatiesFactuur.class)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testReadMutatiesFactuur1XmlWithLoader2() {
		this.loader2.load(Paths.get("src", "test", "resources", "xml", "xml1", "MutatiesFactuur.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadMutatiesFactuur1XmlWithLoader3() {
		this.loader3.load(Paths.get("src", "test", "resources", "xml", "xml1", "MutatiesFactuur.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadMutatiesFactuur1XmlWithLoader4() {
		this.loader4.load(Paths.get("src", "test", "resources", "xml", "xml1", "MutatiesFactuur.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadOfferte1XmlWithLoader1() {
		this.loader1.load(Paths.get("src", "test", "resources", "xml", "xml1", "Offerte.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertValue(Offerte.class)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testReadOfferte1XmlWithLoader2() {
		this.loader2.load(Paths.get("src", "test", "resources", "xml", "xml1", "Offerte.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadOfferte1XmlWithLoader3() {
		this.loader3.load(Paths.get("src", "test", "resources", "xml", "xml1", "Offerte.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadOfferte1XmlWithLoader4() {
		this.loader4.load(Paths.get("src", "test", "resources", "xml", "xml1", "Offerte.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadPartFactuur1XmlWithLoader1() {
		this.loader1.load(Paths.get("src", "test", "resources", "xml", "xml1", "PartFactuur.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertValue(ParticulierFactuur.class)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testReadPartFactuur1XmlWithLoader2() {
		this.loader2.load(Paths.get("src", "test", "resources", "xml", "xml1", "PartFactuur.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadPartFactuur1XmlWithLoader3() {
		this.loader3.load(Paths.get("src", "test", "resources", "xml", "xml1", "PartFactuur.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadPartFactuur1XmlWithLoader4() {
		this.loader4.load(Paths.get("src", "test", "resources", "xml", "xml1", "PartFactuur.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testRead1ParticulierFactuur1XmlWithLoader1() {
		this.loader1.load(Paths.get("src", "test", "resources", "xml", "xml1", "ParticulierFactuur1.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertValue(ParticulierFactuur.class)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testRead1ParticulierFactuur1XmlWithLoader2() {
		this.loader2.load(Paths.get("src", "test", "resources", "xml", "xml1", "ParticulierFactuur1.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testRead1ParticulierFactuur1XmlWithLoader3() {
		this.loader3.load(Paths.get("src", "test", "resources", "xml", "xml1", "ParticulierFactuur1.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testRead1ParticulierFactuur1XmlWithLoader4() {
		this.loader4.load(Paths.get("src", "test", "resources", "xml", "xml1", "ParticulierFactuur1.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testRead1ParticulierFactuur2XmlWithLoader1() {
		this.loader1.load(Paths.get("src", "test", "resources", "xml", "xml1", "ParticulierFactuur2.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertValue(ParticulierFactuur.class)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testRead1ParticulierFactuur2XmlWithLoader2() {
		this.loader2.load(Paths.get("src", "test", "resources", "xml", "xml1", "ParticulierFactuur2.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testRead1ParticulierFactuur2XmlWithLoader3() {
		this.loader3.load(Paths.get("src", "test", "resources", "xml", "xml1", "ParticulierFactuur2.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testRead1ParticulierFactuur2XmlWithLoader4() {
		this.loader4.load(Paths.get("src", "test", "resources", "xml", "xml1", "ParticulierFactuur2.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadReparatiesFactuur1XmlWithLoader1() {
		this.loader1.load(Paths.get("src", "test", "resources", "xml", "xml1", "ReparatiesFactuur.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertValue(ReparatiesFactuur.class)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testReadReparatiesFactuur1XmlWithNewLoader2() {
		this.loader2.load(Paths.get("src", "test", "resources", "xml", "xml1", "ReparatiesFactuur.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadReparatiesFactuur1XmlWithNewLoader3() {
		this.loader3.load(Paths.get("src", "test", "resources", "xml", "xml1", "ReparatiesFactuur.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadReparatiesFactuur1XmlWithNewLoader4() {
		this.loader4.load(Paths.get("src", "test", "resources", "xml", "xml1", "ReparatiesFactuur.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadAangenomenFactuur2XmlWithLoader1() {
		this.loader1.load(Paths.get("src", "test", "resources", "xml", "xml2", "aangenomenFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadAangenomenFactuur2XmlWithLoader2() {
		this.loader2.load(Paths.get("src", "test", "resources", "xml", "xml2", "aangenomenFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertValue(ParticulierFactuur.class)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testReadAangenomenFactuur2XmlWithLoader3() {
		this.loader3.load(Paths.get("src", "test", "resources", "xml", "xml2", "aangenomenFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadAangenomenFactuur2XmlWithLoader4() {
		this.loader4.load(Paths.get("src", "test", "resources", "xml", "xml2", "aangenomenFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadMutatiesFactuur2XmlWithLoader1() {
		this.loader1.load(Paths.get("src", "test", "resources", "xml", "xml2", "mutatiesFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadMutatiesFactuur2XmlWithLoader2() {
		this.loader2.load(Paths.get("src", "test", "resources", "xml", "xml2", "mutatiesFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertValue(MutatiesFactuur.class)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testReadMutatiesFactuur2XmlWithLoader3() {
		this.loader3.load(Paths.get("src", "test", "resources", "xml", "xml2", "mutatiesFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadMutatiesFactuur2XmlWithLoader4() {
		this.loader4.load(Paths.get("src", "test", "resources", "xml", "xml2", "mutatiesFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadOfferte2XmlWithLoader1() {
		this.loader1.load(Paths.get("src", "test", "resources", "xml", "xml2", "offerteXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadOfferte2XmlWithLoader2() {
		this.loader2.load(Paths.get("src", "test", "resources", "xml", "xml2", "offerteXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertValue(Offerte.class)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testReadOfferte2XmlWithLoader3() {
		this.loader3.load(Paths.get("src", "test", "resources", "xml", "xml2", "offerteXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadOfferte2XmlWithLoader4() {
		this.loader4.load(Paths.get("src", "test", "resources", "xml", "xml2", "offerteXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadParticulierFactuur2XmlWithLoader1() {
		this.loader1.load(Paths.get("src", "test", "resources", "xml", "xml2", "particulierFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadParticulierFactuur2XmlWithLoader2() {
		this.loader2.load(Paths.get("src", "test", "resources", "xml", "xml2", "particulierFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertValue(ParticulierFactuur.class)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testReadParticulierFactuur2XmlWithLoader3() {
		this.loader3.load(Paths.get("src", "test", "resources", "xml", "xml2", "particulierFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadParticulierFactuur2XmlWithLoader4() {
		this.loader4.load(Paths.get("src", "test", "resources", "xml", "xml2", "particulierFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadReparatiesFactuur2XmlWithLoader1() {
		this.loader1.load(Paths.get("src", "test", "resources", "xml", "xml2", "reparatiesFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadReparatiesFactuur2XmlWithLoader2() {
		this.loader2.load(Paths.get("src", "test", "resources", "xml", "xml2", "reparatiesFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertValue(ReparatiesFactuur.class)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testReadReparatiesFactuur2XmlWithLoader3() {
		this.loader3.load(Paths.get("src", "test", "resources", "xml", "xml2", "reparatiesFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadReparatiesFactuur2XmlWithLoader4() {
		this.loader4.load(Paths.get("src", "test", "resources", "xml", "xml2", "reparatiesFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadAangenomenFactuur3XmlWithLoader1() {
		this.loader1.load(Paths.get("src", "test", "resources", "xml", "xml3", "aangenomenFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadAangenomenFactuur3XmlWithLoader2() {
		this.loader2.load(Paths.get("src", "test", "resources", "xml", "xml3", "aangenomenFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadAangenomenFactuur3XmlWithLoader3() {
		this.loader3.load(Paths.get("src", "test", "resources", "xml", "xml3", "aangenomenFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertValue(ParticulierFactuur.class)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testReadAangenomenFactuur3XmlWithLoader4() {
		this.loader3.load(Paths.get("src", "test", "resources", "xml", "xml3", "aangenomenFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertValue(ParticulierFactuur.class)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testReadMutatiesFactuur3XmlWithLoader1() {
		this.loader1.load(Paths.get("src", "test", "resources", "xml", "xml3", "mutatiesFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadMutatiesFactuur3XmlWithLoader2() {
		this.loader2.load(Paths.get("src", "test", "resources", "xml", "xml3", "mutatiesFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertValue(MutatiesFactuur.class)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testReadMutatiesFactuur3XmlWithLoader3() {
		this.loader3.load(Paths.get("src", "test", "resources", "xml", "xml3", "mutatiesFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertValue(MutatiesFactuur.class)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testReadMutatiesFactuur3XmlWithLoader4() {
		this.loader4.load(Paths.get("src", "test", "resources", "xml", "xml3", "mutatiesFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadOfferteFactuur3XmlWithLoader1() {
		this.loader1.load(Paths.get("src", "test", "resources", "xml", "xml3", "offerteXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadOfferteFactuur3XmlWithLoader2() {
		this.loader2.load(Paths.get("src", "test", "resources", "xml", "xml3", "offerteXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertValue(Offerte.class)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testReadOfferteFactuur3XmlWithLoader3() {
		this.loader3.load(Paths.get("src", "test", "resources", "xml", "xml3", "offerteXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertValue(Offerte.class)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testReadOfferteFactuur3XmlWithLoader4() {
		this.loader4.load(Paths.get("src", "test", "resources", "xml", "xml3", "offerteXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadParticulierFactuur3XmlWithLoader1() {
		this.loader1.load(Paths.get("src", "test", "resources", "xml", "xml3", "particulierFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadParticulierFactuur3XmlWithLoader2() {
		this.loader2.load(Paths.get("src", "test", "resources", "xml", "xml3", "particulierFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadParticulierFactuur3XmlWithLoader3() {
		this.loader3.load(Paths.get("src", "test", "resources", "xml", "xml3", "particulierFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertValue(ParticulierFactuur.class)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testReadParticulierFactuur3XmlWithLoader4() {
		this.loader4.load(Paths.get("src", "test", "resources", "xml", "xml3", "particulierFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadReparatiesFactuur3XmlWithLoader1() {
		this.loader1.load(Paths.get("src", "test", "resources", "xml", "xml3", "reparatiesFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadReparatiesFactuur3XmlWithLoader2() {
		this.loader2.load(Paths.get("src", "test", "resources", "xml", "xml3", "reparatiesFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertValue(ReparatiesFactuur.class)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testReadReparatiesFactuur3XmlWithLoader3() {
		this.loader3.load(Paths.get("src", "test", "resources", "xml", "xml3", "reparatiesFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertValue(ReparatiesFactuur.class)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testReadReparatiesFactuur3XmlWithLoader4() {
		this.loader4.load(Paths.get("src", "test", "resources", "xml", "xml3", "reparatiesFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadMutatiesFactuur4XmlWithLoader1() {
		this.loader1.load(Paths.get("src", "test", "resources", "xml", "xml4", "mutatiesFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadMutatiesFactuur4XmlWithLoader2() {
		this.loader2.load(Paths.get("src", "test", "resources", "xml", "xml4", "mutatiesFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadMutatiesFactuur4XmlWithLoader3() {
		this.loader3.load(Paths.get("src", "test", "resources", "xml", "xml4", "mutatiesFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadMutatiesFactuur4XmlWithLoader4() {
		this.loader4.load(Paths.get("src", "test", "resources", "xml", "xml4", "mutatiesFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertValue(MutatiesFactuur.class)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testReadOfferteFactuur4XmlWithLoader1() {
		this.loader1.load(Paths.get("src", "test", "resources", "xml", "xml4", "offerteXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadOfferteFactuur4XmlWithLoader2() {
		this.loader2.load(Paths.get("src", "test", "resources", "xml", "xml4", "offerteXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadOfferteFactuur4XmlWithLoader3() {
		this.loader3.load(Paths.get("src", "test", "resources", "xml", "xml4", "offerteXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadOfferteFactuur4XmlWithLoader4() {
		this.loader4.load(Paths.get("src", "test", "resources", "xml", "xml4", "offerteXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertValue(Offerte.class)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testReadParticulierFactuur4XmlWithLoader1() {
		this.loader1.load(Paths.get("src", "test", "resources", "xml", "xml4", "particulierFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadParticulierFactuur4XmlWithLoader2() {
		this.loader2.load(Paths.get("src", "test", "resources", "xml", "xml4", "particulierFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadParticulierFactuur4XmlWithLoader3() {
		this.loader3.load(Paths.get("src", "test", "resources", "xml", "xml4", "particulierFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadParticulierFactuur4XmlWithLoader4() {
		this.loader4.load(Paths.get("src", "test", "resources", "xml", "xml4", "particulierFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertValue(ParticulierFactuur.class)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testReadReparatiesFactuur4XmlWithLoader1() {
		this.loader1.load(Paths.get("src", "test", "resources", "xml", "xml4", "reparatiesFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadReparatiesFactuur4XmlWithLoader2() {
		this.loader2.load(Paths.get("src", "test", "resources", "xml", "xml4", "reparatiesFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadReparatiesFactuur4XmlWithLoader3() {
		this.loader3.load(Paths.get("src", "test", "resources", "xml", "xml4", "reparatiesFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.test()
			.assertNoValues()
			.assertError(XmlParseException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadReparatiesFactuur4XmlWithLoader4() {
		this.loader4.load(Paths.get("src", "test", "resources", "xml", "xml4", "reparatiesFactuurXMLTest.xml").toAbsolutePath())
			.map(AbstractRekening::getClass)
			.cast(Class.class)
			.test()
			.assertValue(ReparatiesFactuur.class)
			.assertNoErrors()
			.assertComplete();
	}
}

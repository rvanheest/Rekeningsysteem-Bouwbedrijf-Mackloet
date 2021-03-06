package org.rekeningsysteem.test.integration;

import java.io.File;

import javax.management.modelmbean.XMLParseException;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.io.FactuurLoader;
import org.rekeningsysteem.io.xml.XmlReader;
import org.rekeningsysteem.io.xml.XmlReader1;
import org.rekeningsysteem.io.xml.XmlReader2;
import org.rekeningsysteem.io.xml.XmlReader3;

import rx.observers.TestSubscriber;

@RunWith(MockitoJUnitRunner.class)
public class XmlReaderIntegrationTest {

	private FactuurLoader loader1;
	private FactuurLoader loader2;
	private FactuurLoader loader3;
	private FactuurLoader loader4;
	private TestSubscriber<Class<? extends AbstractRekening>> testObserver;
	@Mock private Logger logger;

	@Before
	public void setUp() {
		this.loader1 = new XmlReader1(this.logger);
		this.loader2 = new XmlReader2(this.logger);
		this.loader3 = new XmlReader3(this.logger);
		this.loader4 = new XmlReader(this.logger);
		this.testObserver = new TestSubscriber<>();
	}

	@Test
	public void testReadMutatiesFactuur1XmlWithLoader1() {
		this.loader1.load(new File("src\\test\\resources\\xml\\xml1\\MutatiesFactuur.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(this.testObserver);

		this.testObserver.assertValue(MutatiesFactuur.class);
		this.testObserver.assertNoErrors();
		this.testObserver.assertCompleted();
	}

	@Test
	public void testReadMutatiesFactuur1XmlWithLoader2() {
		this.loader2.load(new File("src\\test\\resources\\xml\\xml1\\MutatiesFactuur.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(this.testObserver);
		
		this.testObserver.assertNoValues();
		this.testObserver.assertError(XMLParseException.class);
		this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadMutatiesFactuur1XmlWithLoader3() {
		this.loader3.load(new File("src\\test\\resources\\xml\\xml1\\MutatiesFactuur.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(this.testObserver);
		
		this.testObserver.assertNoValues();
		this.testObserver.assertError(XMLParseException.class);
		this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadMutatiesFactuur1XmlWithLoader4() {
		this.loader4.load(new File("src\\test\\resources\\xml\\xml1\\MutatiesFactuur.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(this.testObserver);
		
		this.testObserver.assertNoValues();
		this.testObserver.assertError(IllegalArgumentException.class);
		this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadOfferte1XmlWithLoader1() {
		this.loader1.load(new File("src\\test\\resources\\xml\\xml1\\Offerte.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertValue(Offerte.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}

	@Test
	public void testReadOfferte1XmlWithLoader2() {
		this.loader2.load(new File("src\\test\\resources\\xml\\xml1\\Offerte.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadOfferte1XmlWithLoader3() {
		this.loader3.load(new File("src\\test\\resources\\xml\\xml1\\Offerte.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadOfferte1XmlWithLoader4() {
		this.loader4.load(new File("src\\test\\resources\\xml\\xml1\\Offerte.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(IllegalArgumentException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadPartFactuur1XmlWithLoader1() {
		this.loader1.load(new File("src\\test\\resources\\xml\\xml1\\PartFactuur.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertValue(ParticulierFactuur.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}

	@Test
	public void testReadPartFactuur1XmlWithLoader2() {
		this.loader2.load(new File("src\\test\\resources\\xml\\xml1\\PartFactuur.xml"))
				.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadPartFactuur1XmlWithLoader3() {
		this.loader3.load(new File("src\\test\\resources\\xml\\xml1\\PartFactuur.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadPartFactuur1XmlWithLoader4() {
		this.loader4.load(new File("src\\test\\resources\\xml\\xml1\\PartFactuur.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(IllegalArgumentException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testRead1ParticulierFactuur1XmlWithLoader1() {
		this.loader1.load(new File("src\\test\\resources\\xml\\xml1\\ParticulierFactuur1.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertValue(ParticulierFactuur.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}

	@Test
	public void testRead1ParticulierFactuur1XmlWithLoader2() {
		this.loader2.load(new File("src\\test\\resources\\xml\\xml1\\ParticulierFactuur1.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testRead1ParticulierFactuur1XmlWithLoader3() {
		this.loader3.load(new File("src\\test\\resources\\xml\\xml1\\ParticulierFactuur1.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testRead1ParticulierFactuur1XmlWithLoader4() {
		this.loader4.load(new File("src\\test\\resources\\xml\\xml1\\ParticulierFactuur1.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(IllegalArgumentException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testRead1ParticulierFactuur2XmlWithLoader1() {
		this.loader1.load(new File("src\\test\\resources\\xml\\xml1\\ParticulierFactuur2.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertValue(ParticulierFactuur.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}

	@Test
	public void testRead1ParticulierFactuur2XmlWithLoader2() {
		this.loader2.load(new File("src\\test\\resources\\xml\\xml1\\ParticulierFactuur2.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testRead1ParticulierFactuur2XmlWithLoader3() {
		this.loader3.load(new File("src\\test\\resources\\xml\\xml1\\ParticulierFactuur2.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testRead1ParticulierFactuur2XmlWithLoader4() {
		this.loader4.load(new File("src\\test\\resources\\xml\\xml1\\ParticulierFactuur2.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(IllegalArgumentException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadReparatiesFactuur1XmlWithLoader1() {
		this.loader1.load(new File("src\\test\\resources\\xml\\xml1\\ReparatiesFactuur.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertValue(ReparatiesFactuur.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}

	@Test
	public void testReadReparatiesFactuur1XmlWithNewLoader2() {
		this.loader2.load(new File("src\\test\\resources\\xml\\xml1\\ReparatiesFactuur.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadReparatiesFactuur1XmlWithNewLoader3() {
		this.loader3.load(new File("src\\test\\resources\\xml\\xml1\\ReparatiesFactuur.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadReparatiesFactuur1XmlWithNewLoader4() {
		this.loader4.load(new File("src\\test\\resources\\xml\\xml1\\ReparatiesFactuur.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(IllegalArgumentException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadAangenomenFactuur2XmlWithLoader1() {
		this.loader1.load(new File("src\\test\\resources\\xml\\xml2\\aangenomenFactuurXMLTest.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(this.testObserver);
		
		this.testObserver.assertNoValues();
		this.testObserver.assertError(XMLParseException.class);
		this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadAangenomenFactuur2XmlWithLoader2() {
		this.loader2.load(new File("src\\test\\resources\\xml\\xml2\\aangenomenFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);

		this.testObserver.assertValue(ParticulierFactuur.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}

	@Test
	public void testReadAangenomenFactuur2XmlWithLoader3() {
		this.loader3.load(new File("src\\test\\resources\\xml\\xml2\\aangenomenFactuurXMLTest.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(this.testObserver);
		
		this.testObserver.assertNoValues();
		this.testObserver.assertError(XMLParseException.class);
		this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadAangenomenFactuur2XmlWithLoader4() {
		this.loader4.load(new File("src\\test\\resources\\xml\\xml2\\aangenomenFactuurXMLTest.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(this.testObserver);
		
		this.testObserver.assertNoValues();
		this.testObserver.assertError(IllegalArgumentException.class);
		this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadMutatiesFactuur2XmlWithLoader1() {
		this.loader1.load(new File("src\\test\\resources\\xml\\xml2\\mutatiesFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadMutatiesFactuur2XmlWithLoader2() {
		this.loader2.load(new File("src\\test\\resources\\xml\\xml2\\mutatiesFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertValue(MutatiesFactuur.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}

	@Test
	public void testReadMutatiesFactuur2XmlWithLoader3() {
		this.loader3.load(new File("src\\test\\resources\\xml\\xml2\\mutatiesFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadMutatiesFactuur2XmlWithLoader4() {
		this.loader4.load(new File("src\\test\\resources\\xml\\xml2\\mutatiesFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(IllegalArgumentException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadOfferte2XmlWithLoader1() {
		this.loader1.load(new File("src\\test\\resources\\xml\\xml2\\offerteXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadOfferte2XmlWithLoader2() {
		this.loader2.load(new File("src\\test\\resources\\xml\\xml2\\offerteXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertValue(Offerte.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}

	@Test
	public void testReadOfferte2XmlWithLoader3() {
		this.loader3.load(new File("src\\test\\resources\\xml\\xml2\\offerteXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadOfferte2XmlWithLoader4() {
		this.loader4.load(new File("src\\test\\resources\\xml\\xml2\\offerteXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(IllegalArgumentException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadParticulierFactuur2XmlWithLoader1() {
		this.loader1.load(new File("src\\test\\resources\\xml\\xml2\\particulierFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadParticulierFactuur2XmlWithLoader2() {
		this.loader2.load(new File("src\\test\\resources\\xml\\xml2\\particulierFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertValue(ParticulierFactuur.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}

	@Test
	public void testReadParticulierFactuur2XmlWithLoader3() {
		this.loader3.load(new File("src\\test\\resources\\xml\\xml2\\particulierFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadParticulierFactuur2XmlWithLoader4() {
		this.loader4.load(new File("src\\test\\resources\\xml\\xml2\\particulierFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(IllegalArgumentException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadReparatiesFactuur2XmlWithLoader1() {
		this.loader1.load(new File("src\\test\\resources\\xml\\xml2\\reparatiesFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadReparatiesFactuur2XmlWithLoader2() {
		this.loader2.load(new File("src\\test\\resources\\xml\\xml2\\reparatiesFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertValue(ReparatiesFactuur.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}

	@Test
	public void testReadReparatiesFactuur2XmlWithLoader3() {
		this.loader3.load(new File("src\\test\\resources\\xml\\xml2\\reparatiesFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadReparatiesFactuur2XmlWithLoader4() {
		this.loader4.load(new File("src\\test\\resources\\xml\\xml2\\reparatiesFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(IllegalArgumentException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadAangenomenFactuur3XmlWithLoader1() {
		this.loader1.load(new File("src\\test\\resources\\xml\\xml3\\aangenomenFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadAangenomenFactuur3XmlWithLoader2() {
		this.loader2.load(new File("src\\test\\resources\\xml\\xml3\\aangenomenFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(IllegalArgumentException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadAangenomenFactuur3XmlWithLoader3() {
		this.loader3.load(new File("src\\test\\resources\\xml\\xml3\\aangenomenFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertValue(ParticulierFactuur.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}

	@Test
	public void testReadAangenomenFactuur3XmlWithLoader4() {
		this.loader3.load(new File("src\\test\\resources\\xml\\xml3\\aangenomenFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertValue(ParticulierFactuur.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}

	@Test
	public void testReadMutatiesFactuur3XmlWithLoader1() {
		this.loader1.load(new File("src\\test\\resources\\xml\\xml3\\mutatiesFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadMutatiesFactuur3XmlWithLoader2() {
		this.loader2.load(new File("src\\test\\resources\\xml\\xml3\\mutatiesFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertValue(MutatiesFactuur.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}

	@Test
	public void testReadMutatiesFactuur3XmlWithLoader3() {
		this.loader3.load(new File("src\\test\\resources\\xml\\xml3\\mutatiesFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertValue(MutatiesFactuur.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}

	@Test
	public void testReadMutatiesFactuur3XmlWithLoader4() {
		this.loader4.load(new File("src\\test\\resources\\xml\\xml3\\mutatiesFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(IllegalArgumentException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadOfferteFactuur3XmlWithLoader1() {
		this.loader1.load(new File("src\\test\\resources\\xml\\xml3\\offerteXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadOfferteFactuur3XmlWithLoader2() {
		this.loader2.load(new File("src\\test\\resources\\xml\\xml3\\offerteXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertValue(Offerte.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}

	@Test
	public void testReadOfferteFactuur3XmlWithLoader3() {
		this.loader3.load(new File("src\\test\\resources\\xml\\xml3\\offerteXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertValue(Offerte.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}

	@Test
	public void testReadOfferteFactuur3XmlWithLoader4() {
		this.loader4.load(new File("src\\test\\resources\\xml\\xml3\\offerteXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(IllegalArgumentException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadParticulierFactuur3XmlWithLoader1() {
		this.loader1.load(new File("src\\test\\resources\\xml\\xml3\\particulierFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadParticulierFactuur3XmlWithLoader2() {
		this.loader2.load(new File("src\\test\\resources\\xml\\xml3\\particulierFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(IllegalArgumentException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadParticulierFactuur3XmlWithLoader3() {
		this.loader3.load(new File("src\\test\\resources\\xml\\xml3\\particulierFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertValue(ParticulierFactuur.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}

	@Test
	public void testReadParticulierFactuur3XmlWithLoader4() {
		this.loader4.load(new File("src\\test\\resources\\xml\\xml3\\particulierFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(IllegalArgumentException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadReparatiesFactuur3XmlWithLoader1() {
		this.loader1.load(new File("src\\test\\resources\\xml\\xml3\\reparatiesFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadReparatiesFactuur3XmlWithLoader2() {
		this.loader2.load(new File("src\\test\\resources\\xml\\xml3\\reparatiesFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertValue(ReparatiesFactuur.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}

	@Test
	public void testReadReparatiesFactuur3XmlWithLoader3() {
		this.loader3.load(new File("src\\test\\resources\\xml\\xml3\\reparatiesFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertValue(ReparatiesFactuur.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}

	@Test
	public void testReadReparatiesFactuur3XmlWithLoader4() {
		this.loader4.load(new File("src\\test\\resources\\xml\\xml3\\reparatiesFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(IllegalArgumentException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadMutatiesFactuur4XmlWithLoader1() {
		this.loader1.load(new File("src\\test\\resources\\xml\\xml4\\mutatiesFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadMutatiesFactuur4XmlWithLoader2() {
		this.loader2.load(new File("src\\test\\resources\\xml\\xml4\\mutatiesFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertValue(MutatiesFactuur.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}

	@Test
	public void testReadMutatiesFactuur4XmlWithLoader3() {
		this.loader3.load(new File("src\\test\\resources\\xml\\xml4\\mutatiesFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadMutatiesFactuur4XmlWithLoader4() {
		this.loader4.load(new File("src\\test\\resources\\xml\\xml4\\mutatiesFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertValue(MutatiesFactuur.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}

	@Test
	public void testReadOfferteFactuur4XmlWithLoader1() {
		this.loader1.load(new File("src\\test\\resources\\xml\\xml4\\offerteXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadOfferteFactuur4XmlWithLoader2() {
		this.loader2.load(new File("src\\test\\resources\\xml\\xml4\\offerteXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertValue(Offerte.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}

	@Test
	public void testReadOfferteFactuur4XmlWithLoader3() {
		this.loader3.load(new File("src\\test\\resources\\xml\\xml4\\offerteXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadOfferteFactuur4XmlWithLoader4() {
		this.loader4.load(new File("src\\test\\resources\\xml\\xml4\\offerteXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertValue(Offerte.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}

	@Test
	public void testReadParticulierFactuur4XmlWithLoader1() {
		this.loader1.load(new File("src\\test\\resources\\xml\\xml4\\particulierFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadParticulierFactuur4XmlWithLoader2() {
		this.loader2.load(new File("src\\test\\resources\\xml\\xml4\\particulierFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(IllegalArgumentException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadParticulierFactuur4XmlWithLoader3() {
		this.loader3.load(new File("src\\test\\resources\\xml\\xml4\\particulierFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
		this.testObserver.assertNoValues();
		this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadParticulierFactuur4XmlWithLoader4() {
		this.loader4.load(new File("src\\test\\resources\\xml\\xml4\\particulierFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertValue(ParticulierFactuur.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}

	@Test
	public void testReadReparatiesFactuur4XmlWithLoader1() {
		this.loader1.load(new File("src\\test\\resources\\xml\\xml4\\reparatiesFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadReparatiesFactuur4XmlWithLoader2() {
		this.loader2.load(new File("src\\test\\resources\\xml\\xml4\\reparatiesFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertValue(ReparatiesFactuur.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}

	@Test
	public void testReadReparatiesFactuur4XmlWithLoader3() {
		this.loader3.load(new File("src\\test\\resources\\xml\\xml4\\reparatiesFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertNoValues();
        this.testObserver.assertError(XMLParseException.class);
        this.testObserver.assertNotCompleted();
	}

	@Test
	public void testReadReparatiesFactuur4XmlWithLoader4() {
		this.loader4.load(new File("src\\test\\resources\\xml\\xml4\\reparatiesFactuurXMLTest.xml"))
        		.map(AbstractRekening::getClass)
        		.subscribe(this.testObserver);
        
        this.testObserver.assertValue(ReparatiesFactuur.class);
        this.testObserver.assertNoErrors();
        this.testObserver.assertCompleted();
	}
}

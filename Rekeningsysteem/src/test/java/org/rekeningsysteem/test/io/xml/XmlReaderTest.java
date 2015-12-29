package org.rekeningsysteem.test.io.xml;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.soap.Node;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier2.ParticulierFactuur2;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.xml.XmlReader;
import org.rekeningsysteem.io.xml.root.MutatiesFactuurRoot;
import org.rekeningsysteem.io.xml.root.OfferteRoot;
import org.rekeningsysteem.io.xml.root.ParticulierFactuur2Root;
import org.rekeningsysteem.io.xml.root.ReparatiesFactuurRoot;
import org.rekeningsysteem.io.xml.root.Root;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import rx.observers.TestSubscriber;

@RunWith(MockitoJUnitRunner.class)
public class XmlReaderTest {

	private XmlReader reader;
	private TestSubscriber<? super AbstractRekening> testObserver;
	private final Map<Class<? extends Root<?>>, Unmarshaller> map = new HashMap<>();
	@Mock private Unmarshaller unmarshaller;
	@Mock private DocumentBuilder builder;

	@Before
	public void setUp() {
		this.reader = new XmlReader(this.map, this.builder);
		this.testObserver = new TestSubscriber<>();

		this.map.put(MutatiesFactuurRoot.class, this.unmarshaller);
		this.map.put(OfferteRoot.class, this.unmarshaller);
		this.map.put(ParticulierFactuur2Root.class, this.unmarshaller);
		this.map.put(ReparatiesFactuurRoot.class, this.unmarshaller);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testLoadMutatiesFactuur() throws SAXException, IOException, JAXBException {
		File file = mock(File.class);
		Document doc = mock(Document.class);
		Element docElem = mock(Element.class);
		NodeList nodeList = mock(NodeList.class);
		Element factuur = mock(Element.class);
		String type = "MutatiesFactuur";
		String version = "4";
		Root<MutatiesFactuur> root = mock(Root.class);
		MutatiesFactuur rekening = new MutatiesFactuur(new FactuurHeader(
				new Debiteur("", "", "", "", "", ""), LocalDate.now(), ""),
				Currency.getInstance(Locale.US), new ItemList<>());

		when(this.builder.parse((File) anyObject())).thenReturn(doc);
		when(doc.getDocumentElement()).thenReturn(docElem);
		when(doc.getElementsByTagName(anyString())).thenReturn(nodeList);
		when(nodeList.item(anyInt())).thenReturn(factuur);
		when(factuur.getAttribute(eq("type"))).thenReturn(type);
		when(factuur.getAttribute(eq("version"))).thenReturn(version);
		when(this.unmarshaller.unmarshal((Node) anyObject())).thenReturn(root);
		when(root.getRekening()).thenReturn(rekening);

		this.reader.load(file).subscribe(this.testObserver);

		this.testObserver.assertValue(rekening);
		this.testObserver.assertNoErrors();
		this.testObserver.assertCompleted();
		verify(this.unmarshaller).unmarshal(eq(doc));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testLoadOfferte() throws SAXException, IOException, JAXBException {
		File file = mock(File.class);
		Document doc = mock(Document.class);
		Element docElem = mock(Element.class);
		NodeList nodeList = mock(NodeList.class);
		Element factuur = mock(Element.class);
		String type = "Offerte";
		String version = "4";
		Root<Offerte> root = mock(Root.class);
		Offerte rekening = new Offerte(new FactuurHeader(new Debiteur("", "", "", "", "", ""),
				LocalDate.now(), ""), "", true);

		when(this.builder.parse((File) anyObject())).thenReturn(doc);
		when(doc.getDocumentElement()).thenReturn(docElem);
		when(doc.getElementsByTagName(anyString())).thenReturn(nodeList);
		when(nodeList.item(anyInt())).thenReturn(factuur);
		when(factuur.getAttribute(eq("type"))).thenReturn(type);
		when(factuur.getAttribute(eq("version"))).thenReturn(version);
		when(this.unmarshaller.unmarshal((Node) anyObject())).thenReturn(root);
		when(root.getRekening()).thenReturn(rekening);

		this.reader.load(file).subscribe(this.testObserver);

		this.testObserver.assertValue(rekening);
		this.testObserver.assertNoErrors();
		this.testObserver.assertCompleted();
		verify(this.unmarshaller).unmarshal(eq(doc));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testLoadParticulierFactuur() throws SAXException, IOException, JAXBException {
		File file = mock(File.class);
		Document doc = mock(Document.class);
		Element docElem = mock(Element.class);
		NodeList nodeList = mock(NodeList.class);
		Element factuur = mock(Element.class);
		String type = "ParticulierFactuur";
		String version = "4";
		Root<ParticulierFactuur2> root = mock(Root.class);
		ParticulierFactuur2 rekening = new ParticulierFactuur2(new OmschrFactuurHeader(
				new Debiteur("", "", "", "", "", ""), LocalDate.now(), "", ""),
				Currency.getInstance(Locale.US), new ItemList<>());

		when(this.builder.parse((File) anyObject())).thenReturn(doc);
		when(doc.getDocumentElement()).thenReturn(docElem);
		when(doc.getElementsByTagName(anyString())).thenReturn(nodeList);
		when(nodeList.item(anyInt())).thenReturn(factuur);
		when(factuur.getAttribute(eq("type"))).thenReturn(type);
		when(factuur.getAttribute(eq("version"))).thenReturn(version);
		when(this.unmarshaller.unmarshal((Node) anyObject())).thenReturn(root);
		when(root.getRekening()).thenReturn(rekening);

		this.reader.load(file).subscribe(this.testObserver);

		this.testObserver.assertValue(rekening);
		this.testObserver.assertNoErrors();
		this.testObserver.assertCompleted();
		verify(this.unmarshaller).unmarshal(eq(doc));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testLoadReparatiesFactuur() throws SAXException, IOException, JAXBException {
		File file = mock(File.class);
		Document doc = mock(Document.class);
		Element docElem = mock(Element.class);
		NodeList nodeList = mock(NodeList.class);
		Element factuur = mock(Element.class);
		String type = "ReparatiesFactuur";
		String version = "4";
		Root<ReparatiesFactuur> root = mock(Root.class);
		ReparatiesFactuur rekening = new ReparatiesFactuur(new FactuurHeader(
				new Debiteur("", "", "", "", "", ""), LocalDate.now(), ""),
				Currency.getInstance(Locale.US), new ItemList<>());

		when(this.builder.parse((File) anyObject())).thenReturn(doc);
		when(doc.getDocumentElement()).thenReturn(docElem);
		when(doc.getElementsByTagName(anyString())).thenReturn(nodeList);
		when(nodeList.item(anyInt())).thenReturn(factuur);
		when(factuur.getAttribute(eq("type"))).thenReturn(type);
		when(factuur.getAttribute(eq("version"))).thenReturn(version);
		when(this.unmarshaller.unmarshal((Node) anyObject())).thenReturn(root);
		when(root.getRekening()).thenReturn(rekening);

		this.reader.load(file).subscribe(this.testObserver);

		this.testObserver.assertValue(rekening);
		this.testObserver.assertNoErrors();
		this.testObserver.assertCompleted();
		verify(this.unmarshaller).unmarshal(eq(doc));
	}

	@Test
	public void testLoadUnknownType() throws SAXException, IOException {
		File file = mock(File.class);
		Document doc = mock(Document.class);
		Element docElem = mock(Element.class);
		NodeList nodeList = mock(NodeList.class);
		Element factuur = mock(Element.class);
		String type = "anotherType";

		when(this.builder.parse((File) anyObject())).thenReturn(doc);
		when(doc.getDocumentElement()).thenReturn(docElem);
		when(doc.getElementsByTagName(anyString())).thenReturn(nodeList);
		when(nodeList.item(anyInt())).thenReturn(factuur);
		when(factuur.getAttribute(anyString())).thenReturn(type);

		this.reader.load(file).subscribe(this.testObserver);

		this.testObserver.assertNoValues();
		this.testObserver.assertError(IllegalArgumentException.class);
		this.testObserver.assertNotCompleted();
	}

	@Test
	public void testLoadParseSAXException() throws SAXException, IOException {
		File file = mock(File.class);

		when(this.builder.parse((File) anyObject())).thenThrow(new SAXException());

		this.reader.load(file).subscribe(this.testObserver);

		this.testObserver.assertNoValues();
		this.testObserver.assertError(SAXException.class);
		this.testObserver.assertNotCompleted();
	}

	@Test
	public void testLoadParseIOException() throws SAXException, IOException {
		File file = mock(File.class);

		when(this.builder.parse((File) anyObject())).thenThrow(new IOException());

		this.reader.load(file).subscribe(this.testObserver);

		this.testObserver.assertNoValues();
		this.testObserver.assertError(IOException.class);
		this.testObserver.assertNotCompleted();
	}

	@Test
	public void testLoadJAXBException() throws SAXException, IOException, JAXBException {
		File file = mock(File.class);
		Document doc = mock(Document.class);
		Element docElem = mock(Element.class);
		NodeList nodeList = mock(NodeList.class);
		Element factuur = mock(Element.class);
		String type = "ReparatiesFactuur";
		String version = "4";

		when(this.builder.parse((File) anyObject())).thenReturn(doc);
		when(doc.getDocumentElement()).thenReturn(docElem);
		when(doc.getElementsByTagName(anyString())).thenReturn(nodeList);
		when(nodeList.item(anyInt())).thenReturn(factuur);
		when(factuur.getAttribute(eq("type"))).thenReturn(type);
		when(factuur.getAttribute(eq("version"))).thenReturn(version);
		when(this.unmarshaller.unmarshal((Node) anyObject())).thenThrow(new JAXBException("foo"));

		this.reader.load(file).subscribe(this.testObserver);

		this.testObserver.assertNoValues();
		this.testObserver.assertError(JAXBException.class);
		this.testObserver.assertNotCompleted();
	}
}

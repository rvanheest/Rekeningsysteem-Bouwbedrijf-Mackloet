package org.rekeningsysteem.test.io.xml;

import static org.junit.Assert.*;
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
import java.util.HashMap;
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
import org.rekeningsysteem.data.aangenomen.AangenomenFactuur;
import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.io.xml.XmlReader;
import org.rekeningsysteem.io.xml.root.AangenomenFactuurRoot;
import org.rekeningsysteem.io.xml.root.MutatiesFactuurRoot;
import org.rekeningsysteem.io.xml.root.OfferteRoot;
import org.rekeningsysteem.io.xml.root.ParticulierFactuurRoot;
import org.rekeningsysteem.io.xml.root.ReparatiesFactuurRoot;
import org.rekeningsysteem.io.xml.root.Root;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@RunWith(MockitoJUnitRunner.class)
public class XmlReaderTest {

	private XmlReader reader;
	private final Map<Class<? extends Root<?>>, Unmarshaller> map = new HashMap<>();
	@Mock private Unmarshaller unmarshaller;
	@Mock private DocumentBuilder builder;

	@Before
	public void setUp() {
		this.reader = new XmlReader(this.map, this.builder);

		this.map.put(AangenomenFactuurRoot.class, this.unmarshaller);
		this.map.put(MutatiesFactuurRoot.class, this.unmarshaller);
		this.map.put(OfferteRoot.class, this.unmarshaller);
		this.map.put(ParticulierFactuurRoot.class, this.unmarshaller);
		this.map.put(ReparatiesFactuurRoot.class, this.unmarshaller);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testLoadAangenomenFactuur() throws SAXException, IOException, JAXBException {
		File file = mock(File.class);
		Document doc = mock(Document.class);
		Element docElem = mock(Element.class);
		NodeList nodeList = mock(NodeList.class);
		Element factuur = mock(Element.class);
		String type = "AangenomenFactuur";
		Root<AangenomenFactuur> root = mock(Root.class);
		AangenomenFactuur rekening = new AangenomenFactuur(new OmschrFactuurHeader(
				new Debiteur("", "", "", "", "", ""), LocalDate.now(), "", ""), "",
				new ItemList<AangenomenListItem>(), new BtwPercentage(0.0, 0.0));

		when(this.builder.parse((File) anyObject())).thenReturn(doc);
		when(doc.getDocumentElement()).thenReturn(docElem);
		when(doc.getElementsByTagName(anyString())).thenReturn(nodeList);
		when(nodeList.item(anyInt())).thenReturn(factuur);
		when(factuur.getAttribute(anyString())).thenReturn(type);
		when(this.unmarshaller.unmarshal((Node) anyObject())).thenReturn(root);
		when(root.getRekening()).thenReturn(rekening);

		assertEquals(rekening, this.reader.load(file).get());
		verify(this.unmarshaller).unmarshal(eq(doc));
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
		Root<MutatiesFactuur> root = mock(Root.class);
		MutatiesFactuur rekening = new MutatiesFactuur(new FactuurHeader(
				new Debiteur("", "", "", "", "", ""), LocalDate.now(), ""), "",
				new ItemList<MutatiesBon>(), new BtwPercentage(0.0, 0.0));

		when(this.builder.parse((File) anyObject())).thenReturn(doc);
		when(doc.getDocumentElement()).thenReturn(docElem);
		when(doc.getElementsByTagName(anyString())).thenReturn(nodeList);
		when(nodeList.item(anyInt())).thenReturn(factuur);
		when(factuur.getAttribute(anyString())).thenReturn(type);
		when(this.unmarshaller.unmarshal((Node) anyObject())).thenReturn(root);
		when(root.getRekening()).thenReturn(rekening);

		assertEquals(rekening, this.reader.load(file).get());
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
		Root<Offerte> root = mock(Root.class);
		Offerte rekening = new Offerte(new FactuurHeader(
				new Debiteur("", "", "", "", "", ""), LocalDate.now(), ""), "", true);

		when(this.builder.parse((File) anyObject())).thenReturn(doc);
		when(doc.getDocumentElement()).thenReturn(docElem);
		when(doc.getElementsByTagName(anyString())).thenReturn(nodeList);
		when(nodeList.item(anyInt())).thenReturn(factuur);
		when(factuur.getAttribute(anyString())).thenReturn(type);
		when(this.unmarshaller.unmarshal((Node) anyObject())).thenReturn(root);
		when(root.getRekening()).thenReturn(rekening);

		assertEquals(rekening, this.reader.load(file).get());
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
		Root<ParticulierFactuur> root = mock(Root.class);
		ParticulierFactuur rekening = new ParticulierFactuur(new OmschrFactuurHeader(
				new Debiteur("", "", "", "", "", ""), LocalDate.now(), "", ""), "",
				new ItemList<ParticulierArtikel>(),	new ItemList<AbstractLoon>(),
				new BtwPercentage(0.0, 0.0));

		when(this.builder.parse((File) anyObject())).thenReturn(doc);
		when(doc.getDocumentElement()).thenReturn(docElem);
		when(doc.getElementsByTagName(anyString())).thenReturn(nodeList);
		when(nodeList.item(anyInt())).thenReturn(factuur);
		when(factuur.getAttribute(anyString())).thenReturn(type);
		when(this.unmarshaller.unmarshal((Node) anyObject())).thenReturn(root);
		when(root.getRekening()).thenReturn(rekening);

		assertEquals(rekening, this.reader.load(file).get());
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
		Root<ReparatiesFactuur> root = mock(Root.class);
		ReparatiesFactuur rekening = new ReparatiesFactuur(new FactuurHeader(
				new Debiteur("", "", "", "", "", ""), LocalDate.now(), ""), "",
				new ItemList<ReparatiesBon>(), new BtwPercentage(0.0, 0.0));

		when(this.builder.parse((File) anyObject())).thenReturn(doc);
		when(doc.getDocumentElement()).thenReturn(docElem);
		when(doc.getElementsByTagName(anyString())).thenReturn(nodeList);
		when(nodeList.item(anyInt())).thenReturn(factuur);
		when(factuur.getAttribute(anyString())).thenReturn(type);
		when(this.unmarshaller.unmarshal((Node) anyObject())).thenReturn(root);
		when(root.getRekening()).thenReturn(rekening);

		assertEquals(rekening, this.reader.load(file).get());
		verify(this.unmarshaller).unmarshal(eq(doc));
	}

	@Test (expected = IllegalStateException.class)
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

		this.reader.load(file).get();
	}

	@Test (expected = IllegalStateException.class)
	public void testLoadParseSAXException() throws SAXException, IOException {
		File file = mock(File.class);

		when(this.builder.parse((File) anyObject())).thenThrow(new SAXException());

		this.reader.load(file).get();
	}

	@Test (expected = IllegalStateException.class)
	public void testLoadParseIOException() throws SAXException, IOException {
		File file = mock(File.class);

		when(this.builder.parse((File) anyObject())).thenThrow(new IOException());

		this.reader.load(file).get();
	}

	@Test (expected = IllegalStateException.class)
	public void testLoadJAXBException() throws SAXException, IOException, JAXBException {
		File file = mock(File.class);
		Document doc = mock(Document.class);
		Element docElem = mock(Element.class);
		NodeList nodeList = mock(NodeList.class);
		Element factuur = mock(Element.class);
		String type = "ReparatiesFactuur";

		when(this.builder.parse((File) anyObject())).thenReturn(doc);
		when(doc.getDocumentElement()).thenReturn(docElem);
		when(doc.getElementsByTagName(anyString())).thenReturn(nodeList);
		when(nodeList.item(anyInt())).thenReturn(factuur);
		when(factuur.getAttribute(anyString())).thenReturn(type);
		when(this.unmarshaller.unmarshal((Node) anyObject())).thenThrow(new JAXBException("foo"));

		this.reader.load(file).get();
	}
}

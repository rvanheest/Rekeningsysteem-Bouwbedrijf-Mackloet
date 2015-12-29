package org.rekeningsysteem.test.io.xml;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.time.LocalDate;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.bind.Marshaller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier2.ParticulierFactuur2;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.xml.XmlMakerVisitor;
import org.rekeningsysteem.io.xml.root.MutatiesFactuurRoot;
import org.rekeningsysteem.io.xml.root.OfferteRoot;
import org.rekeningsysteem.io.xml.root.ParticulierFactuur2Root;
import org.rekeningsysteem.io.xml.root.ReparatiesFactuurRoot;
import org.rekeningsysteem.io.xml.root.Root;

@RunWith(MockitoJUnitRunner.class)
public class XmlMakerVisitorTest {

	private XmlMakerVisitor visitor;
	@Mock private File mockedFile;
	private final Map<Class<? extends Root<?>>, Marshaller> marshallerMap = new HashMap<>();
	@Mock private Marshaller mockedMarshaller;

	private final FactuurHeader testHeader = new FactuurHeader(
			new Debiteur("", "", "", "", "", ""), LocalDate.now(), "");
	private final OmschrFactuurHeader testOmschrHeader = new OmschrFactuurHeader(
			new Debiteur("", "", "", "", "", ""), LocalDate.now(), "");

	@Before
	public void setUp() {
		this.visitor = new XmlMakerVisitor(this.marshallerMap);

		this.marshallerMap.put(MutatiesFactuurRoot.class, this.mockedMarshaller);
		this.marshallerMap.put(OfferteRoot.class, this.mockedMarshaller);
		this.marshallerMap.put(ParticulierFactuur2Root.class, this.mockedMarshaller);
		this.marshallerMap.put(ReparatiesFactuurRoot.class, this.mockedMarshaller);

		this.visitor.setSaveLocation(this.mockedFile);
	}

	@Test
	public void testSetGetSaveLocation() {
		File file = mock(File.class);
		this.visitor.setSaveLocation(file);
		assertEquals(file, this.visitor.getSaveLocation());
	}

	@Test
	public void testVisitMutatiesFactuur() throws Exception {
		MutatiesFactuur factuur = new MutatiesFactuur(this.testHeader,
				Currency.getInstance(Locale.US), new ItemList<>());
		this.visitor.visit(factuur);

		verify(this.mockedMarshaller).setProperty(anyString(), anyObject());
		verify(this.mockedMarshaller).marshal(anyObject(), eq(this.mockedFile));
	}

	@Test
	public void testVisitOfferte() throws Exception {
		Offerte factuur = new Offerte(this.testHeader, "", true);
		this.visitor.visit(factuur);

		verify(this.mockedMarshaller).setProperty(anyString(), anyObject());
		verify(this.mockedMarshaller).marshal(anyObject(), eq(this.mockedFile));
	}

	@Test
	public void testVisitParticulierFactuur() throws Exception {
		ParticulierFactuur2 factuur = new ParticulierFactuur2(this.testOmschrHeader,
				Currency.getInstance(Locale.US), new ItemList<>());
		this.visitor.visit(factuur);

		verify(this.mockedMarshaller).setProperty(anyString(), anyObject());
		verify(this.mockedMarshaller).marshal(anyObject(), eq(this.mockedFile));
	}

	@Test
	public void testVisitReparatiesFactuur() throws Exception {
		ReparatiesFactuur factuur = new ReparatiesFactuur(this.testHeader,
				Currency.getInstance(Locale.US), new ItemList<>());
		this.visitor.visit(factuur);

		verify(this.mockedMarshaller).setProperty(anyString(), anyObject());
		verify(this.mockedMarshaller).marshal(anyObject(), eq(this.mockedFile));
	}
}

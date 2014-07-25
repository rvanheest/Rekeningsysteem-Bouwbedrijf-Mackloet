package org.rekeningsysteem.test.io.xml;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.Marshaller;

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
import org.rekeningsysteem.io.xml.XmlMakerVisitor;
import org.rekeningsysteem.io.xml.root.AangenomenFactuurRoot;
import org.rekeningsysteem.io.xml.root.MutatiesFactuurRoot;
import org.rekeningsysteem.io.xml.root.OfferteRoot;
import org.rekeningsysteem.io.xml.root.ParticulierFactuurRoot;
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
	private final BtwPercentage btwPercentage = new BtwPercentage(0.0, 0.0);

	@Before
	public void setUp() {
		this.visitor = new XmlMakerVisitor(this.marshallerMap);

		this.marshallerMap.put(AangenomenFactuurRoot.class, this.mockedMarshaller);
		this.marshallerMap.put(MutatiesFactuurRoot.class, this.mockedMarshaller);
		this.marshallerMap.put(OfferteRoot.class, this.mockedMarshaller);
		this.marshallerMap.put(ParticulierFactuurRoot.class, this.mockedMarshaller);
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
	public void testVisitAangenomenFactuur() throws Exception {
		AangenomenFactuur factuur = new AangenomenFactuur(this.testOmschrHeader, "",
				new ItemList<AangenomenListItem>(), this.btwPercentage);
		this.visitor.visit(factuur);

		verify(this.mockedMarshaller).setProperty(anyString(), anyObject());
		verify(this.mockedMarshaller).marshal(anyObject(), eq(this.mockedFile));
	}

	@Test
	public void testVisitMutatiesFactuur() throws Exception {
		MutatiesFactuur factuur = new MutatiesFactuur(this.testHeader, "",
				new ItemList<MutatiesBon>(), this.btwPercentage);
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
		ParticulierFactuur factuur = new ParticulierFactuur(this.testOmschrHeader, "",
				new ItemList<ParticulierArtikel>(), new ItemList<AbstractLoon>(),
				this.btwPercentage);
		this.visitor.visit(factuur);

		verify(this.mockedMarshaller).setProperty(anyString(), anyObject());
		verify(this.mockedMarshaller).marshal(anyObject(), eq(this.mockedFile));
	}

	@Test
	public void testVisitReparatiesFactuur() throws Exception {
		ReparatiesFactuur factuur = new ReparatiesFactuur(this.testHeader, "",
				new ItemList<ReparatiesBon>(), this.btwPercentage);
		this.visitor.visit(factuur);

		verify(this.mockedMarshaller).setProperty(anyString(), anyObject());
		verify(this.mockedMarshaller).marshal(anyObject(), eq(this.mockedFile));
	}
}

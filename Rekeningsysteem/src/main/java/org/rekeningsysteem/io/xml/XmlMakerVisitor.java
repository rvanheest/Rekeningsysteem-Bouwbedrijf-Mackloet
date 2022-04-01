package org.rekeningsysteem.io.xml;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.logging.log4j.core.Logger;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.visitor.RekeningVoidVisitor;
import org.rekeningsysteem.io.xml.root.MutatiesFactuurRoot;
import org.rekeningsysteem.io.xml.root.OfferteRoot;
import org.rekeningsysteem.io.xml.root.ParticulierFactuurRoot;
import org.rekeningsysteem.io.xml.root.ReparatiesFactuurRoot;
import org.rekeningsysteem.io.xml.root.Root;

public class XmlMakerVisitor implements RekeningVoidVisitor {

	private File saveLocation;
	private final Map<Class<? extends Root<?>>, Marshaller> map;

	public XmlMakerVisitor(Logger logger) {
		this.map = new HashMap<>();

		try {
			this.map.put(MutatiesFactuurRoot.class,
					JAXBContext.newInstance(MutatiesFactuurRoot.class).createMarshaller());
			this.map.put(OfferteRoot.class,
					JAXBContext.newInstance(OfferteRoot.class).createMarshaller());
			this.map.put(ParticulierFactuurRoot.class,
					JAXBContext.newInstance(ParticulierFactuurRoot.class).createMarshaller());
			this.map.put(ReparatiesFactuurRoot.class,
					JAXBContext.newInstance(ReparatiesFactuurRoot.class).createMarshaller());
		}
		catch (JAXBException e) {
			// Should not happen
			logger.fatal("JAXBContext or Marshaller could not be "
					+ "made. (should not happen)", e);
		}
	}

	public XmlMakerVisitor(Map<Class<? extends Root<?>>, Marshaller> map) {
		this.map = map;
	}

	public File getSaveLocation() {
		return this.saveLocation;
	}

	public void setSaveLocation(File saveLocation) {
		this.saveLocation = saveLocation;
	}

	@Override
	public void visit(MutatiesFactuur factuur) throws Exception {
		Marshaller marshaller = this.map.get(MutatiesFactuurRoot.class);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(MutatiesFactuurRoot.build(a -> a.setRekening(factuur)), this.saveLocation);
	}

	@Override
	public void visit(Offerte offerte) throws Exception {
		Marshaller marshaller = this.map.get(OfferteRoot.class);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(OfferteRoot.build(a -> a.setRekening(offerte)), this.saveLocation);
	}

	@Override
	public void visit(ParticulierFactuur factuur) throws Exception {
		Marshaller marshaller = this.map.get(ParticulierFactuurRoot.class);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(ParticulierFactuurRoot.build(a -> a.setRekening(factuur)), this.saveLocation);
	}

	@Override
	public void visit(ReparatiesFactuur factuur) throws Exception {
		Marshaller marshaller = this.map.get(ReparatiesFactuurRoot.class);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(ReparatiesFactuurRoot.build(a -> a.setRekening(factuur)), this.saveLocation);
	}
}

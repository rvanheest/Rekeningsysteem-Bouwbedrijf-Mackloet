package org.rekeningsysteem.io.xml;

import java.io.File;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.rekeningsysteem.data.aangenomen.AangenomenFactuur;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;
import org.rekeningsysteem.io.xml.guice.MarshallerMap;
import org.rekeningsysteem.io.xml.root.AangenomenFactuurRoot;
import org.rekeningsysteem.io.xml.root.MutatiesFactuurRoot;
import org.rekeningsysteem.io.xml.root.OfferteRoot;
import org.rekeningsysteem.io.xml.root.ParticulierFactuurRoot;
import org.rekeningsysteem.io.xml.root.ReparatiesFactuurRoot;
import org.rekeningsysteem.io.xml.root.Root;

import com.google.inject.Inject;

public class XmlMakerVisitor implements RekeningVisitor {

	private File saveLocation;
	private final Map<Class<? extends Root<?>>, Marshaller> map;

	@Inject
	public XmlMakerVisitor(@MarshallerMap Map<Class<? extends Root<?>>, Marshaller> map) {
		this.map = map;
	}

	public File getSaveLocation() {
		return this.saveLocation;
	}

	public void setSaveLocation(File saveLocation) {
		this.saveLocation = saveLocation;
	}

	@Override
	public void visit(AangenomenFactuur factuur) throws Exception {
		Marshaller marshaller = this.map.get(AangenomenFactuurRoot.class);
		this.save(marshaller, new AangenomenFactuurRoot(), factuur);
	}

	@Override
	public void visit(MutatiesFactuur factuur) throws Exception {
		Marshaller marshaller = this.map.get(MutatiesFactuurRoot.class);
		this.save(marshaller, new MutatiesFactuurRoot(), factuur);
	}

	@Override
	public void visit(Offerte offerte) throws Exception {
		Marshaller marshaller = this.map.get(OfferteRoot.class);
		this.save(marshaller, new OfferteRoot(), offerte);
	}

	@Override
	public void visit(ParticulierFactuur factuur) throws Exception {
		Marshaller marshaller = this.map.get(ParticulierFactuurRoot.class);
		this.save(marshaller, new ParticulierFactuurRoot(), factuur);
	}

	@Override
	public void visit(ReparatiesFactuur factuur) throws Exception {
		Marshaller marshaller = this.map.get(ReparatiesFactuurRoot.class);
		this.save(marshaller, new ReparatiesFactuurRoot(), factuur);
	}

	private <T extends AbstractRekening> void save(Marshaller marshaller, Root<T> root, T rekening)
			throws JAXBException {
		root.setRekening(rekening);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(root, this.saveLocation);
	}
}

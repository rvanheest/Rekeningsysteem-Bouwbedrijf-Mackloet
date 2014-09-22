package org.rekeningsysteem.io.xml;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;

import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.io.FactuurLoader;
import org.rekeningsysteem.io.xml.guice.UnmarshallerMap;
import org.rekeningsysteem.io.xml.root.AangenomenFactuurRoot;
import org.rekeningsysteem.io.xml.root.MutatiesFactuurRoot;
import org.rekeningsysteem.io.xml.root.OfferteRoot;
import org.rekeningsysteem.io.xml.root.ParticulierFactuurRoot;
import org.rekeningsysteem.io.xml.root.ReparatiesFactuurRoot;
import org.rekeningsysteem.io.xml.root.Root;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import rx.Observable;

import com.google.inject.Inject;

public class XmlReader implements FactuurLoader {

	private final Map<Class<? extends Root<?>>, Unmarshaller> map;
	private DocumentBuilder builder;

	@Inject
	public XmlReader(@UnmarshallerMap Map<Class<? extends Root<?>>, Unmarshaller> map,
			DocumentBuilder builder) {
		this.map = map;
		this.builder = builder;
	}

	@Override
	public Observable<AbstractRekening> load(File file) {
		try {
			Document doc = this.builder.parse(file);
			doc.getDocumentElement().normalize();
			Node factuur = doc.getElementsByTagName("bestand").item(0);
			String type = ((Element) factuur).getAttribute("type");

			switch (type) {
				case "AangenomenFactuur": {
					Unmarshaller unmarshaller = this.map.get(AangenomenFactuurRoot.class);
					return this.readXML(unmarshaller, doc).map(Root::getRekening);
				}
				case "MutatiesFactuur": {
					Unmarshaller unmarshaller = this.map.get(MutatiesFactuurRoot.class);
					return this.readXML(unmarshaller, doc).map(Root::getRekening);
				}
				case "Offerte": {
					Unmarshaller unmarshaller = this.map.get(OfferteRoot.class);
					return this.readXML(unmarshaller, doc).map(Root::getRekening);
				}
				case "ParticulierFactuur": {
					Unmarshaller unmarshaller = this.map.get(ParticulierFactuurRoot.class);
					return this.readXML(unmarshaller, doc).map(Root::getRekening);
				}
				case "ReparatiesFactuur": {
					Unmarshaller unmarshaller = this.map.get(ReparatiesFactuurRoot.class);
					return this.readXML(unmarshaller, doc).map(Root::getRekening);
				}
				default:
					return Observable.error(new IllegalArgumentException("This typ (" + type
							+ ") can't be used"));
			}
		}
		catch (SAXException | IOException e) {
			return Observable.error(e);
		}
	}

	private Observable<Root<?>> readXML(Unmarshaller unmarshaller, Node node) {
		try {
			return Observable.from((Root<?>) unmarshaller.unmarshal(node));
		}
		catch (JAXBException e) {
			return Observable.error(e);
		}
	}
}

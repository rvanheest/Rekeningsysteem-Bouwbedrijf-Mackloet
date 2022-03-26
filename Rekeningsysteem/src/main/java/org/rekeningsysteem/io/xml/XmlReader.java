package org.rekeningsysteem.io.xml;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import io.reactivex.rxjava3.core.Single;
import org.apache.logging.log4j.core.Logger;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.io.FactuurLoader;
import org.rekeningsysteem.io.xml.root.MutatiesFactuurRoot;
import org.rekeningsysteem.io.xml.root.OfferteRoot;
import org.rekeningsysteem.io.xml.root.ParticulierFactuurRoot;
import org.rekeningsysteem.io.xml.root.ReparatiesFactuurRoot;
import org.rekeningsysteem.io.xml.root.Root;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XmlReader implements FactuurLoader {

	private final Map<Class<? extends Root<?>>, Unmarshaller> map;
	private DocumentBuilder builder;

	public XmlReader(Logger logger) {
		this.map = new HashMap<>();

		try {
			this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			this.map.put(MutatiesFactuurRoot.class,
				JAXBContext.newInstance(MutatiesFactuurRoot.class).createUnmarshaller());
			this.map.put(OfferteRoot.class,
				JAXBContext.newInstance(OfferteRoot.class).createUnmarshaller());
			this.map.put(ParticulierFactuurRoot.class,
				JAXBContext.newInstance(ParticulierFactuurRoot.class).createUnmarshaller());
			this.map.put(ReparatiesFactuurRoot.class,
				JAXBContext.newInstance(ReparatiesFactuurRoot.class).createUnmarshaller());
		}
		catch (JAXBException e) {
			// Should not happen
			logger.fatal("JAXBContext or Unmarshaller could not be "
				+ "made. (should not happen)", e);
		}
		catch (ParserConfigurationException e) {
			// Should not happen
			logger.fatal("DocumentBuilder could not be made. "
				+ "(should not happen)", e);
		}
	}

	public XmlReader(Map<Class<? extends Root<?>>, Unmarshaller> map, DocumentBuilder builder) {
		this.map = map;
		this.builder = builder;
	}

	@Override
	public Single<AbstractRekening> load(File file) {
		try {
			Document doc = this.builder.parse(file);
			doc.getDocumentElement().normalize();
			Node factuur = doc.getElementsByTagName("bestand").item(0);
			String type = ((Element) factuur).getAttribute("type");
			String version = ((Element) factuur).getAttribute("version");

			if ("4".equals(version)) {
				switch (type) {
					case "MutatiesFactuur": {
						return this.readXML(doc, MutatiesFactuurRoot.class).map(Root::getRekening);
					}
					case "Offerte": {
						return this.readXML(doc, OfferteRoot.class).map(Root::getRekening);
					}
					case "ParticulierFactuur": {
						return this.readXML(doc, ParticulierFactuurRoot.class).map(Root::getRekening);
					}
					case "ReparatiesFactuur": {
						return this.readXML(doc, ReparatiesFactuurRoot.class).map(Root::getRekening);
					}
					default:
						return Single.error(new IllegalArgumentException("This type (" + type + ") can't be used"));
				}
			}
			else {
				return Single.error(new IllegalArgumentException("The version (" + version + ") is not supported by this parser"));
			}
		}
		catch (SAXException | IOException e) {
			return Single.error(e);
		}
	}

	private Single<Root<?>> readXML(Node node, Class<? extends Root<?>> rootClass) {
		return Single.fromCallable(() -> (Root<?>) this.map.get(rootClass).unmarshal(node));
	}
}

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

import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.io.FactuurLoader;
import org.rekeningsysteem.io.xml.root.MutatiesFactuurRoot;
import org.rekeningsysteem.io.xml.root.OfferteRoot;
import org.rekeningsysteem.io.xml.root.ParticulierFactuurRoot;
import org.rekeningsysteem.io.xml.root.ReparatiesFactuurRoot;
import org.rekeningsysteem.io.xml.root.Root;
import org.rekeningsysteem.logging.ApplicationLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import rx.Observable;

public class XmlReader implements FactuurLoader {

	private final Map<Class<? extends Root<?>>, Unmarshaller> map;
	private DocumentBuilder builder;

	public XmlReader() {
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
			ApplicationLogger.getInstance().fatal("JAXBContext or Unmarshaller could not be "
					+ "made. (should not happen)", e);
		}
		catch (ParserConfigurationException e) {
			// Should not happen
			ApplicationLogger.getInstance().fatal("DocumentBuilder could not be made. "
					+ "(should not happen)", e);
		}
	}

	public XmlReader(Map<Class<? extends Root<?>>, Unmarshaller> map, DocumentBuilder builder) {
		this.map = map;
		this.builder = builder;
	}

	@Override
	public Observable<? extends AbstractRekening> load(File file) {
		try {
			Document doc = this.builder.parse(file);
			doc.getDocumentElement().normalize();
			Node factuur = doc.getElementsByTagName("bestand").item(0);
			String type = ((Element) factuur).getAttribute("type");
			String version = ((Element) factuur).getAttribute("version");

			if ("4".equals(version)) {
    			switch (type) {
    				case "MutatiesFactuur": {
    					Unmarshaller unmarshaller = this.map.get(MutatiesFactuurRoot.class);
    					Observable<? extends AbstractRekening> result = this.readXML(unmarshaller, doc).map(Root::getRekening);
    					return result;
    				}
    				case "Offerte": {
    					Unmarshaller unmarshaller = this.map.get(OfferteRoot.class);
    					Observable<? extends AbstractRekening> result = this.readXML(unmarshaller, doc).map(Root::getRekening);
    					return result;
    				}
    				case "ParticulierFactuur": {
    					Unmarshaller unmarshaller = this.map.get(ParticulierFactuurRoot.class);
    					Observable<? extends AbstractRekening> result = this.readXML(unmarshaller, doc).map(Root::getRekening);
    					return result;
    				}
    				case "ReparatiesFactuur": {
    					Unmarshaller unmarshaller = this.map.get(ReparatiesFactuurRoot.class);
    					Observable<? extends AbstractRekening> result = this.readXML(unmarshaller, doc).map(Root::getRekening);
    					return result;
    				}
    				default:
    					return Observable.error(new IllegalArgumentException("This type (" + type + ") can't be used"));
    			}
			}
			else {
				return Observable.error(new IllegalArgumentException("The version (" + version + ") is not supported by this parser"));
			}
		}
		catch (SAXException | IOException e) {
			return Observable.error(e);
		}
	}

	private Observable<Root<?>> readXML(Unmarshaller unmarshaller, Node node) {
		try {
			return Observable.just((Root<?>) unmarshaller.unmarshal(node));
		}
		catch (JAXBException e) {
			return Observable.error(e);
		}
	}
}

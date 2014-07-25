package org.rekeningsysteem.io.xml.guice;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.rekeningsysteem.io.FactuurLoader;
import org.rekeningsysteem.io.xml.XmlReader;
import org.rekeningsysteem.io.xml.root.AangenomenFactuurRoot;
import org.rekeningsysteem.io.xml.root.MutatiesFactuurRoot;
import org.rekeningsysteem.io.xml.root.OfferteRoot;
import org.rekeningsysteem.io.xml.root.ParticulierFactuurRoot;
import org.rekeningsysteem.io.xml.root.ReparatiesFactuurRoot;
import org.rekeningsysteem.io.xml.root.Root;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class XmlReaderModule extends AbstractModule {

	@Override
	protected void configure() {
		this.bind(FactuurLoader.class).to(XmlReader.class);
	}

	@Provides
	@Singleton
	@UnmarshallerMap
	public Map<Class<? extends Root<?>>, Unmarshaller> provideUnmarshallerMap()
			throws JAXBException {
		Map<Class<? extends Root<?>>, Unmarshaller> map = new HashMap<>();

		map.put(AangenomenFactuurRoot.class,
				JAXBContext.newInstance(AangenomenFactuurRoot.class).createUnmarshaller());
		map.put(MutatiesFactuurRoot.class,
				JAXBContext.newInstance(MutatiesFactuurRoot.class).createUnmarshaller());
		map.put(OfferteRoot.class,
				JAXBContext.newInstance(OfferteRoot.class).createUnmarshaller());
		map.put(ParticulierFactuurRoot.class,
				JAXBContext.newInstance(ParticulierFactuurRoot.class).createUnmarshaller());
		map.put(ReparatiesFactuurRoot.class,
				JAXBContext.newInstance(ReparatiesFactuurRoot.class).createUnmarshaller());

		return map;
	}

	@Provides
	public DocumentBuilder provideDocumentBuilder() throws ParserConfigurationException {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder();
	}
}

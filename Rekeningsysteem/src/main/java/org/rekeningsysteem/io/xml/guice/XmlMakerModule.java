package org.rekeningsysteem.io.xml.guice;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.rekeningsysteem.io.FactuurSaver;
import org.rekeningsysteem.io.xml.XmlMaker;
import org.rekeningsysteem.io.xml.root.AangenomenFactuurRoot;
import org.rekeningsysteem.io.xml.root.MutatiesFactuurRoot;
import org.rekeningsysteem.io.xml.root.OfferteRoot;
import org.rekeningsysteem.io.xml.root.ParticulierFactuurRoot;
import org.rekeningsysteem.io.xml.root.ReparatiesFactuurRoot;
import org.rekeningsysteem.io.xml.root.Root;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class XmlMakerModule extends AbstractModule {

	@Override
	protected void configure() {
		this.bind(FactuurSaver.class).to(XmlMaker.class);
	}

	@Provides
	@Singleton
	@MarshallerMap
	public Map<Class<? extends Root<?>>, Marshaller> provideMarshallerMap() throws JAXBException {
		Map<Class<? extends Root<?>>, Marshaller> map = new HashMap<>();

		map.put(AangenomenFactuurRoot.class,
				JAXBContext.newInstance(AangenomenFactuurRoot.class).createMarshaller());
		map.put(MutatiesFactuurRoot.class,
				JAXBContext.newInstance(MutatiesFactuurRoot.class).createMarshaller());
		map.put(OfferteRoot.class,
				JAXBContext.newInstance(OfferteRoot.class).createMarshaller());
		map.put(ParticulierFactuurRoot.class,
				JAXBContext.newInstance(ParticulierFactuurRoot.class).createMarshaller());
		map.put(ReparatiesFactuurRoot.class,
				JAXBContext.newInstance(ReparatiesFactuurRoot.class).createMarshaller());

		return map;
	}
}

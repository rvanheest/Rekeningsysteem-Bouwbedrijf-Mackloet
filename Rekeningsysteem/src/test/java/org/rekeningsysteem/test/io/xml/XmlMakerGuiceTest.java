package org.rekeningsysteem.test.io.xml;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.io.xml.XmlMaker;
import org.rekeningsysteem.io.xml.guice.XmlMakerModule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

@RunWith(MockitoJUnitRunner.class)
public class XmlMakerGuiceTest {

	private Injector injector;
	@Mock private Logger logger;

	@Before
	public void setUp() {
		this.injector = Guice.createInjector(new XmlMakerModule(), new AbstractModule() {

			@Override
			protected void configure() {
				this.bind(Logger.class).toInstance(XmlMakerGuiceTest.this.logger);
			}
		});
	}

	@Test
	public void testMakeXmlMaker() {
		this.injector.getInstance(XmlMaker.class);
	}
}

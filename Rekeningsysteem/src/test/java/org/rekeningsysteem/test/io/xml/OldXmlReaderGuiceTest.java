package org.rekeningsysteem.test.io.xml;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.io.xml.OldXmlReader;
import org.rekeningsysteem.io.xml.guice.XmlReaderModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

@RunWith(MockitoJUnitRunner.class)
public class OldXmlReaderGuiceTest {

	private Injector injector;

	@Before
	public void setUp() {
		this.injector = Guice.createInjector(new XmlReaderModule());
	}

	@Test
	public void testMakeOldXmlReader() {
		this.injector.getInstance(OldXmlReader.class);
	}
}

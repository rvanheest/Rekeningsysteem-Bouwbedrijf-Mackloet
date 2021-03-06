package org.rekeningsysteem.test.io.xml;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.io.File;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.io.xml.XmlMaker;
import org.rekeningsysteem.io.xml.XmlMakerVisitor;

@RunWith(MockitoJUnitRunner.class)
public class XmlMakerTest {

	private XmlMaker xmlMaker;
	@Mock private XmlMakerVisitor visitor;
	@Mock private Logger logger;

	@Before
	public void setUp() {
		this.xmlMaker = new XmlMaker(this.visitor, this.logger);
	}

	@Test
	public void testSave() throws Exception {
		AbstractRekening mockedRekening = mock(AbstractRekening.class);
		File mockedFile = mock(File.class);

		this.xmlMaker.save(mockedRekening, mockedFile);

		verify(this.visitor).setSaveLocation(eq(mockedFile));
		verify(mockedRekening).accept(eq(this.visitor));
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testSaveWithException() throws Exception {
		AbstractRekening mockedRekening = mock(AbstractRekening.class);
		File mockedFile = mock(File.class);

		doThrow(new Exception()).when(mockedRekening).accept(eq(this.visitor));

		this.xmlMaker.save(mockedRekening, mockedFile);

		verify(this.visitor).setSaveLocation(eq(mockedFile));
		verify(mockedRekening).accept(eq(this.visitor));
		verify(this.logger).error(anyString(), (Throwable) anyObject());
	}
}

package org.rekeningsysteem.test.integration.rekening;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.io.FactuurLoader;
import org.rekeningsysteem.io.FactuurSaver;
import org.rekeningsysteem.io.xml.XmlMaker;
import org.rekeningsysteem.io.xml.XmlReader;

import rx.observers.TestSubscriber;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractRekeningIntegrationTest {

	private FactuurSaver saver;
	private FactuurLoader loader;
	private AbstractRekening rekening;
	private File file;
	@Mock private Logger logger;

	protected abstract AbstractRekening makeRekening();

	protected abstract File makeFile();

	@Before
	public void setUp() {
		this.saver = new XmlMaker(this.logger);
		this.loader = new XmlReader(this.logger);
		this.rekening = this.makeRekening();
		this.file = this.makeFile();
	}

	@After
	public void tearDown() {
		if (this.file.exists()) {
			this.file.delete();
		}
		assertFalse(this.file.exists());
	}

	@Test
	public void testXML() {
		this.saver.save(this.rekening, this.file);

		TestSubscriber<AbstractRekening> testObserver = new TestSubscriber<>();
		this.loader.load(this.file).subscribe(testObserver);
		
		testObserver.assertValue(this.rekening);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
	}

	@Test
	public void testXmlWithException() throws Exception {
		AbstractRekening rekening = mock(AbstractRekening.class);
		File file = mock(File.class);

		doThrow(Exception.class).when(rekening).accept(anyObject());

		this.saver.save(rekening, file);

		verify(this.logger).error(anyString(), any(Exception.class));
	}
}

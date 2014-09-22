package org.rekeningsysteem.test.integration.rekening;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.io.FactuurLoader;
import org.rekeningsysteem.io.FactuurSaver;
import org.rekeningsysteem.io.xml.guice.XmlMakerModule;
import org.rekeningsysteem.io.xml.guice.XmlReaderModule;
import org.rekeningsysteem.logging.ConsoleLoggerModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public abstract class AbstractRekeningIntegrationTest {

	private AbstractRekening rekening;
	private File file;

	protected abstract AbstractRekening makeRekening();

	protected AbstractRekening getRekening() {
		return this.rekening;
	}

	protected abstract File makeFile();

	protected File getFile() {
		return this.file;
	}

	@Before
	public void setUp() {
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
		Injector injector = Guice.createInjector(new XmlMakerModule(), new XmlReaderModule(),
				new ConsoleLoggerModule());

		FactuurSaver maker = injector.getInstance(FactuurSaver.class);
		FactuurLoader reader = injector.getInstance(FactuurLoader.class);

		maker.save(this.rekening, this.file);

		reader.load(this.file).forEach(rek -> assertEquals(this.rekening, rek));
	}
}

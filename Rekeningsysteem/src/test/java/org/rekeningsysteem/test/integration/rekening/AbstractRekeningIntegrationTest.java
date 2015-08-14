package org.rekeningsysteem.test.integration.rekening;

import static org.junit.Assert.assertFalse;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.io.FactuurLoader;
import org.rekeningsysteem.io.FactuurSaver;
import org.rekeningsysteem.io.xml.XmlMaker;
import org.rekeningsysteem.io.xml.XmlReader;

import rx.observers.TestSubscriber;

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
		FactuurSaver maker = new XmlMaker();
		FactuurLoader reader = new XmlReader();

		maker.save(this.rekening, this.file);

		TestSubscriber<AbstractRekening> testObserver = new TestSubscriber<>();
		reader.load(this.file).subscribe(testObserver);
		
		testObserver.assertValue(this.rekening);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
	}
}

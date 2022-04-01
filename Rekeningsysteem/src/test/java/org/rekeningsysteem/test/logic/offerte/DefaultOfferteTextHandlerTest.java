package org.rekeningsysteem.test.logic.offerte;

import java.io.File;
import java.io.IOException;

import io.reactivex.rxjava3.core.Observable;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.rekeningsysteem.exception.NoSuchFileException;
import org.rekeningsysteem.io.FileIO;
import org.rekeningsysteem.logic.offerte.DefaultOfferteTextHandler;

public class DefaultOfferteTextHandlerTest {

	private DefaultOfferteTextHandler handler;
	@Rule public TemporaryFolder folder = new TemporaryFolder();
	private File file;
	private final FileIO io = new FileIO();

	@Before
	public void setUp() throws IOException {
		this.file = this.folder.newFile();
		this.handler = new DefaultOfferteTextHandler(this.file, this.io);
	}

	@Test
	public void testSetDefaultText() throws InterruptedException {
		this.handler.setDefaultText(Observable.just("abcdef"))
			.test()
			.await()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testGetDefaultText() throws InterruptedException {
		this.testSetDefaultText();

		this.handler.getDefaultText()
			.test()
			.await()
			.assertValue("abcdef")
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testSetDefaultTextWithNoFile() {
		this.file = null;
		this.handler = new DefaultOfferteTextHandler(this.file, this.io);

		this.handler.setDefaultText(Observable.just("abcdef"))
			.test()
			.assertNoValues()
			.assertError(NoSuchFileException.class)
			.assertNotComplete();
	}
}

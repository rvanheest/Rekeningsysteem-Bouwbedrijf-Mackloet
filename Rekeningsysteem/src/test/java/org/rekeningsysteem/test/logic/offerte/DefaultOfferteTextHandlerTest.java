package org.rekeningsysteem.test.logic.offerte;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.rekeningsysteem.io.FileIO;
import org.rekeningsysteem.logic.offerte.DefaultOfferteTextHandler;

import rx.Observable;
import rx.observers.TestSubscriber;

public class DefaultOfferteTextHandlerTest {

	private DefaultOfferteTextHandler handler;
	@Rule public TemporaryFolder folder = new TemporaryFolder();
	private File file;
	private FileIO io = new FileIO();

	@Before
	public void setUp() throws IOException {
		this.file = this.folder.newFile();
		this.handler = new DefaultOfferteTextHandler(this.file, this.io);
	}

	@Test
	public void testSetDefaultText() {
		TestSubscriber<Void> observer = new TestSubscriber<>();
		this.handler.setDefaultText(Observable.just("abcdef"))
				.subscribe(observer);

		observer.awaitTerminalEvent();

		observer.assertNoValues();
		observer.assertNoErrors();
		observer.assertCompleted();
	}

	@Test
	public void testGetDefaultText() {
		this.testSetDefaultText();

		TestSubscriber<String> observer = new TestSubscriber<>();
		this.handler.getDefaultText()
				.subscribe(observer);

		observer.awaitTerminalEvent();

		observer.assertValue("\n\nabcdef");
		observer.assertNoErrors();
		observer.assertCompleted();
	}
}

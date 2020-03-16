package org.rekeningsysteem.test.io;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.FileIO;

import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

public class FileIOTest {

	private FileIO io;
	@Rule public TemporaryFolder folder = new TemporaryFolder();
	private File file;

	@Before
	public void setUp() throws IOException {
		this.file = this.folder.newFile();
		this.io = new FileIO();
	}

	@Test
	public void testReadCSV() throws IOException {
		FileUtils.writeLines(this.file, Arrays.asList(
			"artikelnummer;omschrijving;prijsPer;Eenheid;verkoopprijs",
			"6030001186;PPB vloerbalk T2 183 503cm *;1;m1;13,20",
			"6030001187;PPB vloerbalk T2 183 363cm;1;m1;13,20",
			"6030001188;PPB vloerbalk T2 183 223cm;1;m1;13,20",
			"6030001189;PPB vloerbalk T2 183 373cm;1;m1;13,20",
			"6030001190;PPB vloerbalk T3 184;1;m1;13,80",
			"6030001223;PPB vulelement 640-3.0;1;Stuks;10,75",
			"6030001224;PPB vulelement 640-3.5 *;1;Stuks;12,22",
			"6030001225;PPB vulelement 640-4.0;1;Stuks;15,11"
		));

		TestSubscriber<EsselinkArtikel> observer = new TestSubscriber<>();
		this.io.readCSV(this.file).subscribe(observer);

		observer.assertValues(
			new EsselinkArtikel("6030001186", "PPB vloerbalk T2 183 503cm *", 1, "m1", new Geld(13.20)),
			new EsselinkArtikel("6030001187", "PPB vloerbalk T2 183 363cm", 1, "m1", new Geld(13.20)),
			new EsselinkArtikel("6030001188", "PPB vloerbalk T2 183 223cm", 1, "m1", new Geld(13.20)),
			new EsselinkArtikel("6030001189", "PPB vloerbalk T2 183 373cm", 1, "m1", new Geld(13.20)),
			new EsselinkArtikel("6030001190", "PPB vloerbalk T3 184", 1, "m1", new Geld(13.80)),
			new EsselinkArtikel("6030001223", "PPB vulelement 640-3.0", 1, "Stuks", new Geld(10.75)),
			new EsselinkArtikel("6030001224", "PPB vulelement 640-3.5 *", 1, "Stuks", new Geld(12.22)),
			new EsselinkArtikel("6030001225", "PPB vulelement 640-4.0", 1, "Stuks", new Geld(15.11))
		);
		observer.assertNoErrors();
		observer.assertCompleted();
	}

	@Test
	public void testNonExistingFile() {
		TestSubscriber<EsselinkArtikel> observer = new TestSubscriber<>();
		this.io.readCSV(new File("does-not-exist.csv")).subscribe(observer);

		observer.assertNoValues();
		observer.assertError(FileNotFoundException.class);
		observer.assertNotCompleted();
	}

	@Test
	public void testReadFile() {
		this.testWriteFile();

		TestSubscriber<String> observer = new TestSubscriber<>();
		this.io.readFile(this.file)
				.subscribe(observer);

		observer.awaitTerminalEvent();

		observer.assertValue("abcdef");
		observer.assertNoErrors();
		observer.assertCompleted();
	}

	@Test
	public void testWriteFile() {
		TestSubscriber<Void> writeObserver = new TestSubscriber<>();
		Observable.just("abc", "def")
				.compose(this.io.writeToFile(this.file, true))
				.subscribe(writeObserver);

		writeObserver.awaitTerminalEvent();

		writeObserver.assertNoValues();
		writeObserver.assertNoErrors();
		writeObserver.assertCompleted();

		TestSubscriber<String> readObserver = new TestSubscriber<>();
		this.io.readFile(this.file)
				.subscribe(readObserver);

		readObserver.awaitTerminalEvent();

		readObserver.assertValue("abcdef");
		readObserver.assertNoErrors();
		readObserver.assertCompleted();
	}

	@Test
	public void testWriteFileWithWriter() throws IOException {
		Writer writer = mock(Writer.class);

		TestSubscriber<Void> observer = new TestSubscriber<>();
		Observable.just("abc", "def")
				.compose(this.io.writeToFile(writer))
				.subscribe(observer);

		observer.awaitTerminalEvent();

		observer.assertNoValues();
		observer.assertNoErrors();
		observer.assertCompleted();
		verify(writer, times(2)).write(anyString());
	}

	@Test
	public void testWriteFileWithWriterOnIOThread() throws IOException {
		Writer writer = mock(Writer.class);

		TestSubscriber<Void> observer = new TestSubscriber<>();
		Observable.just("abc", "def")
				.observeOn(Schedulers.io())
				.compose(this.io.writeToFile(writer))
				.subscribe(observer);

		observer.awaitTerminalEvent();

		observer.assertNoValues();
		observer.assertNoErrors();
		observer.assertCompleted();
		verify(writer, times(2)).write(anyString());
	}

	@Test
	public void testWriteFileWithWriterAndExceptionInWrite() throws IOException {
		Writer writer = mock(Writer.class);
		doThrow(Exception.class).when(writer).write(anyString());

		TestSubscriber<Void> observer = new TestSubscriber<>();
		Observable.just("abc", "def")
				.compose(this.io.writeToFile(writer))
				.subscribe(observer);

		observer.awaitTerminalEvent();

		observer.assertNoValues();
		observer.assertError(Exception.class);
		observer.assertNotCompleted();
		verify(writer).write(anyString());
	}

	@Test
	public void testWriteFileWithWriterAndExceptionInClose() throws IOException {
		Writer writer = mock(Writer.class);
		doThrow(Exception.class).when(writer).close();

		TestSubscriber<Void> observer = new TestSubscriber<>();
		Observable.just("abc", "def")
				.compose(this.io.writeToFile(writer))
				.subscribe(observer);

		observer.awaitTerminalEvent();

		observer.assertNoValues();
		observer.assertNoErrors();
		observer.assertCompleted();
		verify(writer, times(2)).write(anyString());
	}

	@Test
	public void testWriteFileWithWriterAndExceptions() throws IOException {
		Writer writer = mock(Writer.class);
		doThrow(Exception.class).when(writer).write(anyString());
		doThrow(Exception.class).when(writer).close();

		TestSubscriber<Void> observer = new TestSubscriber<>();
		Observable.just("abc", "def")
				.compose(this.io.writeToFile(writer))
				.subscribe(observer);

		observer.awaitTerminalEvent();

		observer.assertNoValues();
		observer.assertError(Exception.class);
		observer.assertNotCompleted();
		verify(writer).write(anyString());
	}

	@Test
	public void testWriteFileWithWriterOnIOThreadNoMock() throws IOException {
		try (OutputStream out = new FileOutputStream(this.file, false);
				Writer writer = new OutputStreamWriter(out)) {
			TestSubscriber<Void> writeObserver = new TestSubscriber<>();
			Observable.just("abcdef")
					.observeOn(Schedulers.io())
					.compose(this.io.writeToFile(writer))
					.subscribe(writeObserver);

			writeObserver.awaitTerminalEvent();
			writeObserver.assertNoValues();
			writeObserver.assertNoErrors();
			writeObserver.assertCompleted();
		}

		TestSubscriber<String> readObserver = new TestSubscriber<>();
		this.io.readFile(this.file)
				.subscribe(readObserver);
		
		readObserver.awaitTerminalEvent();
		readObserver.assertValue("abcdef");
		readObserver.assertNoErrors();
		readObserver.assertCompleted();
	}

	@Test
	public void testWriteFileWithFileOnIOThreadNoMock() {
		TestSubscriber<Void> writeObserver = new TestSubscriber<>();
		Observable.just("abcdef")
				.observeOn(Schedulers.io())
				.compose(this.io.writeToFile(this.file, false))
				.subscribe(writeObserver);

		writeObserver.awaitTerminalEvent();
		writeObserver.assertNoValues();
		writeObserver.assertNoErrors();
		writeObserver.assertCompleted();

		TestSubscriber<String> readObserver = new TestSubscriber<>();
		this.io.readFile(this.file)
				.subscribe(readObserver);
		
		readObserver.awaitTerminalEvent();
		readObserver.assertValue("abcdef");
		readObserver.assertNoErrors();
		readObserver.assertCompleted();
	}

	@Test
	public void testAppendFileWithFileOnIOThreadNoMock() {
		TestSubscriber<Void> writeObserver1 = new TestSubscriber<>();
		Observable.just("abc", "def")
				.observeOn(Schedulers.io())
				.compose(this.io.writeToFile(this.file, false))
				.subscribe(writeObserver1);

		writeObserver1.awaitTerminalEvent();
		writeObserver1.assertNoValues();
		writeObserver1.assertNoErrors();
		writeObserver1.assertCompleted();

		TestSubscriber<Void> writeObserver2 = new TestSubscriber<>();
		Observable.just("ghi", "jkl")
				.observeOn(Schedulers.io())
				.compose(this.io.writeToFile(this.file, true))
				.subscribe(writeObserver2);

		writeObserver2.awaitTerminalEvent();
		writeObserver2.assertNoValues();
		writeObserver2.assertNoErrors();
		writeObserver2.assertCompleted();

		TestSubscriber<String> readObserver = new TestSubscriber<>();
		this.io.readFile(this.file)
				.subscribe(readObserver);
		
		readObserver.awaitTerminalEvent();
		readObserver.assertValue("defghijkl");
		readObserver.assertNoErrors();
		readObserver.assertCompleted();
	}
}

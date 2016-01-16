package org.rekeningsysteem.test.io;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.rekeningsysteem.io.FileIO;

import rx.Observable;
import rx.observers.TestSubscriber;

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
	public void testWriteFile() {
		TestSubscriber<Void> observer = new TestSubscriber<>();
		Observable.just("abc", "def")
				.compose(this.io.writeToFile(this.file, true))
				.subscribe(observer);

		observer.awaitTerminalEvent();

		observer.assertNoValues();
		observer.assertNoErrors();
		observer.assertCompleted();
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
		verify(writer).close();
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
		verify(writer).close();
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
		verify(writer).close();
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
		verify(writer).close();
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
}

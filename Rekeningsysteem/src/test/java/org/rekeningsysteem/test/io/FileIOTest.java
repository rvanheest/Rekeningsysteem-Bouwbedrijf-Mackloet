package org.rekeningsysteem.test.io;

import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.CountDownLatch;

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
	public void testWriteFile() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);

		TestSubscriber<Void> observer = new TestSubscriber<>();
		Observable.just("abc", "def")
				.compose(this.io.writeToFile(this.file, true))
				.subscribe(observer::onNext,
						e -> { observer.onError(e); latch.countDown(); },
						() -> { observer.onCompleted(); latch.countDown(); });

		latch.await();

		observer.assertNoValues();
		observer.assertNoErrors();
		observer.assertCompleted();
	}

	@Test
	public void testWriteFileWithWriter() throws InterruptedException, IOException {
		CountDownLatch latch = new CountDownLatch(1);

		Writer writer = mock(Writer.class);

		TestSubscriber<Void> observer = new TestSubscriber<>();
		Observable.just("abc", "def")
				.compose(this.io.writeToFile(writer))
				.subscribe(observer::onNext,
						e -> { observer.onError(e); latch.countDown(); },
						() -> { observer.onCompleted(); latch.countDown(); });

		latch.await();

		observer.assertNoValues();
		observer.assertNoErrors();
		observer.assertCompleted();
		verify(writer, times(2)).write(anyString());
		verify(writer).close();
	}

	@Test
	public void testWriteFileWithWriterAndExceptionInWrite() throws InterruptedException, IOException {
		CountDownLatch latch = new CountDownLatch(1);

		Writer writer = mock(Writer.class);
		doThrow(Exception.class).when(writer).write(anyString());

		TestSubscriber<Void> observer = new TestSubscriber<>();
		Observable.just("abc", "def")
				.compose(this.io.writeToFile(writer))
				.subscribe(observer::onNext,
						e -> { observer.onError(e); latch.countDown(); },
						() -> { observer.onCompleted(); latch.countDown(); });

		latch.await();

		observer.assertNoValues();
		observer.assertError(Exception.class);
		observer.assertNotCompleted();
		verify(writer).write(anyString());
		verify(writer).close();
	}

	@Test
	public void testWriteFileWithWriterAndExceptionInClose() throws InterruptedException, IOException {
		CountDownLatch latch = new CountDownLatch(1);

		Writer writer = mock(Writer.class);
		doThrow(Exception.class).when(writer).close();

		TestSubscriber<Void> observer = new TestSubscriber<>();
		Observable.just("abc", "def")
				.compose(this.io.writeToFile(writer))
				.subscribe(observer::onNext,
						e -> { observer.onError(e); latch.countDown(); },
						() -> { observer.onCompleted(); latch.countDown(); });

		latch.await();

		observer.assertNoValues();
		observer.assertNoErrors();
		observer.assertCompleted();
		verify(writer, times(2)).write(anyString());
		verify(writer).close();
	}

	@Test
	public void testWriteFileWithWriterAndExceptions() throws InterruptedException, IOException {
		CountDownLatch latch = new CountDownLatch(1);

		Writer writer = mock(Writer.class);
		doThrow(Exception.class).when(writer).write(anyString());
		doThrow(Exception.class).when(writer).close();

		TestSubscriber<Void> observer = new TestSubscriber<>();
		Observable.just("abc", "def")
				.compose(this.io.writeToFile(writer))
				.subscribe(observer::onNext,
						e -> { observer.onError(e); latch.countDown(); },
						() -> { observer.onCompleted(); latch.countDown(); });

		latch.await();

		observer.assertNoValues();
		observer.assertError(Exception.class);
		observer.assertNotCompleted();
		verify(writer).write(anyString());
		verify(writer).close();
	}

	@Test
	public void testReadFile() throws InterruptedException {
		this.testWriteFile();

		CountDownLatch latch = new CountDownLatch(1);

		TestSubscriber<String> observer = new TestSubscriber<>();
		this.io.readFile(this.file)
				.subscribe(observer::onNext,
						e -> { observer.onError(e); latch.countDown(); },
						() -> { observer.onCompleted(); latch.countDown(); });

		latch.await();

		observer.assertValue("abcdef");
		observer.assertNoErrors();
		observer.assertCompleted();
	}
}

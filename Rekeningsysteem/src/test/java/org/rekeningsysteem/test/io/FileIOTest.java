package org.rekeningsysteem.test.io;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.FileIO;

public class FileIOTest {

	private FileIO io;
	@Rule public TemporaryFolder folder = new TemporaryFolder();
	private Path path;

	@Before
	public void setUp() throws IOException {
		this.path = this.folder.newFile().toPath();
		this.io = new FileIO();
	}

	@Test
	public void testReadCSV() throws IOException {
		FileUtils.writeLines(this.path.toFile(), Arrays.asList(
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

		this.io.readCSV(this.path)
			.test()
			.assertValues(
				new EsselinkArtikel("6030001186", "PPB vloerbalk T2 183 503cm *", 1, "m1", new Geld(13.20)),
				new EsselinkArtikel("6030001187", "PPB vloerbalk T2 183 363cm", 1, "m1", new Geld(13.20)),
				new EsselinkArtikel("6030001188", "PPB vloerbalk T2 183 223cm", 1, "m1", new Geld(13.20)),
				new EsselinkArtikel("6030001189", "PPB vloerbalk T2 183 373cm", 1, "m1", new Geld(13.20)),
				new EsselinkArtikel("6030001190", "PPB vloerbalk T3 184", 1, "m1", new Geld(13.80)),
				new EsselinkArtikel("6030001223", "PPB vulelement 640-3.0", 1, "Stuks", new Geld(10.75)),
				new EsselinkArtikel("6030001224", "PPB vulelement 640-3.5 *", 1, "Stuks", new Geld(12.22)),
				new EsselinkArtikel("6030001225", "PPB vulelement 640-4.0", 1, "Stuks", new Geld(15.11))
			)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testNonExistingFile() {
		this.io.readCSV(Paths.get("does-not-exist.csv"))
			.test()
			.assertNoValues()
			.assertError(NoSuchFileException.class)
			.assertNotComplete();
	}

	@Test
	public void testReadFile() {
		this.testWriteFile();

		this.io.readFile(this.path)
			.test()
			.assertValue("abcdef")
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testWriteFile() {
		this.io.writeToFile(this.path, true, Observable.just("abc", "def"))
			.test()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();

		this.io.readFile(this.path)
			.test()
			.assertValue("abcdef")
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testWriteFileWithWriter() throws IOException {
		Writer writer = mock(Writer.class);

		Observable.just("abc", "def")
			.compose(this.io.writeToFile(writer))
			.test()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();

		verify(writer, times(2)).write(anyString());
	}

	@Test
	public void testWriteFileWithWriterOnIOThread() throws IOException, InterruptedException {
		Writer writer = mock(Writer.class);

		Observable.just("abc", "def")
			.observeOn(Schedulers.io())
			.compose(this.io.writeToFile(writer))
			.test()
			.await()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();

		verify(writer, times(2)).write(anyString());
	}

	@Test
	public void testWriteFileWithWriterAndExceptionInWrite() throws IOException {
		Writer writer = mock(Writer.class);
		doThrow(IOException.class).when(writer).write(anyString());

		Observable.just("abc", "def")
			.compose(this.io.writeToFile(writer))
			.test()
			.assertNoValues()
			.assertError(Exception.class)
			.assertNotComplete();

		verify(writer).write(anyString());
	}

	@Test
	public void testWriteFileWithWriterAndExceptionInClose() throws IOException {
		Writer writer = mock(Writer.class);
		doThrow(IOException.class).when(writer).close();

		Observable.just("abc", "def")
			.compose(this.io.writeToFile(writer))
			.test()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();

		verify(writer, times(2)).write(anyString());
	}

	@Test
	public void testWriteFileWithWriterAndExceptions() throws IOException {
		Writer writer = mock(Writer.class);
		doThrow(IOException.class).when(writer).write(anyString());
		doThrow(IOException.class).when(writer).close();

		Observable.just("abc", "def")
			.compose(this.io.writeToFile(writer))
			.test()
			.assertNoValues()
			.assertError(Exception.class)
			.assertNotComplete();

		verify(writer).write(anyString());
	}

	@Test
	public void testWriteFileWithWriterOnIOThreadNoMock() throws IOException, InterruptedException {
		try (OutputStream out = new FileOutputStream(this.path.toFile(), false);
			Writer writer = new OutputStreamWriter(out)) {

			Observable.just("abcdef")
				.observeOn(Schedulers.io())
				.compose(this.io.writeToFile(writer))
				.test()
				.await()
				.assertNoValues()
				.assertNoErrors()
				.assertComplete();
		}

		this.io.readFile(this.path)
			.test()
			.assertValue("abcdef")
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testWriteFileWithFileOnIOThreadNoMock() throws InterruptedException {
		this.io.writeToFile(this.path, false, Observable.just("abcdef").observeOn(Schedulers.io()))
			.test()
			.await()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();

		this.io.readFile(this.path)
			.test()
			.assertValue("abcdef")
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testAppendFileWithFileOnIOThreadNoMock() throws InterruptedException {
		this.io.writeToFile(this.path, false, Observable.just("abc", "def").observeOn(Schedulers.io()))
			.test()
			.await()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();

		this.io.writeToFile(this.path, true, Observable.just("ghi", "jkl").observeOn(Schedulers.io()))
			.test()
			.await()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();

		this.io.readFile(this.path)
			.test()
			.assertValue("defghijkl")
			.assertNoErrors()
			.assertComplete();
	}
}

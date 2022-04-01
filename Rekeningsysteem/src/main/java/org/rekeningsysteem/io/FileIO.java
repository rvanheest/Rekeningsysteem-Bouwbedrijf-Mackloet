package org.rekeningsysteem.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.functions.Function;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.exception.GeldParseException;

public class FileIO {

	public Observable<EsselinkArtikel> readCSV(File csv) {
		try {
			return Observable.fromIterable(CSVParser.parse(csv, StandardCharsets.UTF_8, CSVFormat.DEFAULT.builder().setDelimiter(';').build()))
				.skip(1)
				.flatMap(list -> {
					try {
						return Observable.just(
							new EsselinkArtikel(list.get(0), list.get(1), Integer.parseInt(list.get(2)), list.get(3),
								new Geld(list.get(4))));
					}
					catch (GeldParseException e) {
						return Observable.error(e);
					}
				});
		}
		catch (IOException e) {
			return Observable.error(e);
		}
	}

	public Observable<String> readFile(File file) {
		char[] buffer = new char[8 * 1024];
		return Observable.using(
			() -> new FileReader(file),
			reader -> Observable.generate(
				() -> reader.read(buffer),
				(n, emitter) -> {
					if (n == -1) {
						emitter.onComplete();
						return -1;
					}
					else {
						emitter.onNext(new String(buffer, 0, n));
						return reader.read(buffer);
					}
				}
			),
			InputStreamReader::close
		);
	}

	public ObservableTransformer<String, Void> writeToFile(Writer writer) {
		return strings -> strings.flatMap(string -> Observable.<Void> create(emitter -> {
			try {
				writer.write(string);
				emitter.onComplete();
			}
			catch (Exception e) {
				emitter.onError(e);
			}
		}));
	}

	public Completable writeToFile(File file, boolean append, Observable<String> strings) {
		return strings.flatMapCompletable(string -> Completable.using(
			() -> new FileOutputStream(file, append),
			out -> Completable.using(
				() -> new OutputStreamWriter(out),
				writer -> Completable.fromAction(() -> writer.write(string)),
				OutputStreamWriter::close
			),
			FileOutputStream::close
		));
	}
}

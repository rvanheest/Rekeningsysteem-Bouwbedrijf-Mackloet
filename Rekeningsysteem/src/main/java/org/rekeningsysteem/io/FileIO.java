package org.rekeningsysteem.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import rx.Observable;
import rx.Observable.Transformer;

public class FileIO {

	public Observable<String> readFile(File file) {
		return Observable.create(subscriber -> {
			char[] buffer = new char[8 * 1024];
			try (Reader reader = new FileReader(file)) {
				if (!subscriber.isUnsubscribed()) {
					int n = reader.read(buffer);
					while (n != -1 && !subscriber.isUnsubscribed()) {
						subscriber.onNext(new String(buffer, 0, n));
						n = reader.read(buffer);
					}
				}
			}
			catch (IOException e) {
				subscriber.onError(e);
			}
			if (!subscriber.isUnsubscribed()) {
				subscriber.onCompleted();
			}
		});
	}

	public Transformer<String, Void> writeToFile(Writer writer) {
		return strings -> strings.flatMap(string -> Observable.<Void> create(subscriber -> {
			try {
				writer.write(string);
				subscriber.onCompleted();
			}
			catch (Exception e) {
				subscriber.onError(e);
			}
		}));
	}

	public Transformer<String, Void> writeToFile(File file, boolean append) {
		return strings -> strings.flatMap(string -> Observable.create(subscriber -> {
			try (OutputStream out = new FileOutputStream(file, append);
					Writer writer = new OutputStreamWriter(out)) {
				writer.write(string);
				subscriber.onCompleted();
			}
			catch (IOException e) {
				subscriber.onError(e);
			}
		}));
	}
}

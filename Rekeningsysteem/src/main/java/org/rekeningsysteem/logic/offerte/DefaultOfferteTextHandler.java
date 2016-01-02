package org.rekeningsysteem.logic.offerte;

import java.io.File;
import java.util.Optional;

import org.rekeningsysteem.io.FileIO;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;

import rx.Observable;
import rx.schedulers.Schedulers;

public class DefaultOfferteTextHandler {

	private final Optional<File> file;
	private final FileIO io;

	public DefaultOfferteTextHandler() {
		this.file = PropertiesWorker.getInstance()
				.getProperty(PropertyModelEnum.OFFERTE_DEFAULT_TEXT_LOCATION)
				.map(File::new);
		this.io = new FileIO();
	}

	public DefaultOfferteTextHandler(File file, FileIO io) {
		this.file = Optional.ofNullable(file);
		this.io = io;
	}

	public Observable<String> getDefaultText() {
		return this.file.map(file -> this.io.readFile(file)
				.subscribeOn(Schedulers.io())
				.reduce("\n\n", (cum, string) -> cum + string))
				.orElseGet(() -> Observable.empty());
	}

	public Observable<Void> setDefaultText(Observable<String> text) {
		return this.file
				.map(file -> text.observeOn(Schedulers.io())
						.compose(this.io.writeToFile(file, false)))
				.orElseGet(() -> Observable.empty());
	}
}

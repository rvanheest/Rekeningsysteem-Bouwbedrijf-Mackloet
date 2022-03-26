package org.rekeningsysteem.logic.offerte;

import java.io.File;
import java.util.Optional;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.rekeningsysteem.exception.NoSuchFileException;
import org.rekeningsysteem.io.FileIO;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;

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

	public Maybe<String> getDefaultText() {
		return this.file
			.map(file -> this.io.readFile(file)
				.subscribeOn(Schedulers.io())
				.reduce(String::concat)
			)
			.orElseGet(Maybe::empty);
	}

	public Completable setDefaultText(Observable<String> text) {
		return this.file
			.map(file -> this.io.writeToFile(file, false, text.observeOn(Schedulers.io())))
			.orElseGet(() -> Completable.error(new NoSuchFileException("Er bestaat geen file waarin deze tekst kan worden opgeslagen.")));
	}
}

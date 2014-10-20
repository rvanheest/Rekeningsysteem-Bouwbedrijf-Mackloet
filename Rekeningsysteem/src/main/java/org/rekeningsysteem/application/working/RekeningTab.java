package org.rekeningsysteem.application.working;

import java.io.File;
import java.util.Optional;

import javafx.scene.control.Tab;

import org.rekeningsysteem.application.guice.TabName;
import org.rekeningsysteem.application.io.IOWorker;
import org.rekeningsysteem.data.util.AbstractRekening;

import rx.Observable;
import rx.subjects.PublishSubject;

import com.google.inject.Inject;

public class RekeningTab extends Tab {

	private final AbstractRekeningController controller;
	private final PublishSubject<Boolean> modified = PublishSubject.create();
	private Optional<File> saveFile;
	
	private final IOWorker ioWorker;

	@Inject
	public RekeningTab(@TabName String name, AbstractRekeningController controller, IOWorker ioWorker) {
		this(name, controller, ioWorker, null);
	}

	public RekeningTab(@TabName String name, AbstractRekeningController controller, IOWorker ioWorker, File file) {
		super(name);
		this.controller = controller;
		this.saveFile = Optional.ofNullable(file);
		this.ioWorker = ioWorker;
		
		this.setContent(this.controller.getUI());

		this.controller.getModel()
				.buffer(2, 1) // fired at first on the 2nd onNext!
				.map(list -> list.get(0).equals(list.get(1))) // will of course return false
				.scan((saved, equals) -> saved && equals)
				.map(b -> !b)
				.subscribe(this.modified::onNext);

		this.modified.filter(b -> b == true)
				.map(b -> this.getText())
				.filter(s -> !s.endsWith("*"))
				.map(s -> s + "*")
				.subscribe(this::setText);
	}

	public Observable<? extends AbstractRekening> getModel() {
		return this.controller.getModel();
	}

	public Optional<File> getSaveFile() {
		return this.saveFile;
	}

	public void setSaveFile(File file) {
		this.saveFile = Optional.ofNullable(file);
		this.saveFile.map(File::getName).ifPresent(this::setText);
	}

	public void save() {
		this.getModel()
				.doOnNext(factuur -> this.saveFile.ifPresent(file -> this.ioWorker.save(factuur, file)))
				.map(factuur -> this.getText())
				.filter(s -> s.endsWith("*"))
				.map(s -> s.substring(0, s.length() - 1))
				.subscribe(this::setText)
				.unsubscribe();
		this.modified.onNext(false);
	}

	public void export(File file) {
		if (file != null) {
    		this.getModel().doOnNext(factuur -> this.ioWorker.export(factuur, file))
    				.subscribe(factuur -> {},
    						e -> e.printStackTrace())
    				.unsubscribe();
		}
	}
}

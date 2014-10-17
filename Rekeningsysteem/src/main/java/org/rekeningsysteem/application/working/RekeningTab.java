package org.rekeningsysteem.application.working;

import java.io.File;
import java.util.Optional;

import javafx.scene.control.Tab;

import org.rekeningsysteem.application.guice.TabName;
import org.rekeningsysteem.data.util.AbstractRekening;

import rx.Observable;
import rx.subjects.PublishSubject;

import com.google.inject.Inject;

public class RekeningTab extends Tab {

	private final AbstractRekeningController controller;
	private final PublishSubject<Boolean> modified = PublishSubject.create();
	private Optional<File> saveFile;

	@Inject
	public RekeningTab(@TabName String name, AbstractRekeningController controller) {
		this(name, controller, null);
	}

	public RekeningTab(@TabName String name, AbstractRekeningController controller, File file) {
		super(name);
		this.controller = controller;
		this.saveFile = Optional.ofNullable(file);
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
}

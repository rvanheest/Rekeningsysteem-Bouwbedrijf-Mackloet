package org.rekeningsysteem.application.working;

import java.io.File;
import java.util.Optional;

import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;

import org.rekeningsysteem.data.aangenomen.AangenomenFactuur;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.ui.AbstractRekeningController;
import org.rekeningsysteem.ui.aangenomen.AangenomenController;
import org.rekeningsysteem.ui.mutaties.MutatiesController;
import org.rekeningsysteem.ui.offerte.OfferteController;

import rx.Observable;
import rx.subjects.PublishSubject;

public class RekeningTab extends Tab {

	private static final IOWorker ioWorker = new IOWorker();
	private final AbstractRekeningController controller;
	private final PublishSubject<Boolean> modified = PublishSubject.create();
	private Optional<File> saveFile;

	@Deprecated
	public RekeningTab(String name) {
		super(name);
		this.controller = null;
		this.setContent(new SplitPane());
	}

	public RekeningTab(String name, AbstractRekeningController controller) {
		this(name, controller, null);
	}

	public RekeningTab(String name, AbstractRekeningController controller, File file) {
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

	public void initFactuurnummer() {
		this.controller.initFactuurnummer();
	}

	public static Observable<RekeningTab> openFile(File file) {
		// TODO remove GUICE
		Observable<? extends AbstractRekening> factuur = ioWorker.load(file);
		factuur.subscribe(System.out::println);

		Observable<AangenomenController> aangenomen = factuur
				.filter(a -> a instanceof AangenomenFactuur)
				.cast(AangenomenFactuur.class)
				.map(AangenomenController::new);
		Observable<MutatiesController> mutaties = factuur
				.filter(m -> m instanceof MutatiesFactuur)
				.cast(MutatiesFactuur.class)
				.map(MutatiesController::new);
		Observable<OfferteController> offerte = factuur
				.filter(o -> o instanceof Offerte)
				.cast(Offerte.class)
				.map(OfferteController::new);

		return Observable.merge(aangenomen, mutaties, offerte)
				.map(c -> new RekeningTab(file.getName(), c, file));
	}

	public void save() {
		this.getModel()
				.first()
				.doOnNext(factuur -> this.saveFile.ifPresent(file -> ioWorker.save(factuur, file)))
				.map(factuur -> this.getText())
				.filter(s -> s.endsWith("*"))
				.map(s -> s.substring(0, s.length() - 1))
				.subscribe(this::setText);
		this.modified.onNext(false);
	}

	public void export(File file) {
		this.getModel()
				.first()
				.subscribe(factuur -> ioWorker.export(factuur, file),
						e -> e.printStackTrace());
	}
}

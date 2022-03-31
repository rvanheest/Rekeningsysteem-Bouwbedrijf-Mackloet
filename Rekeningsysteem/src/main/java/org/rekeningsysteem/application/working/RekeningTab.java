package org.rekeningsysteem.application.working;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;

import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.exception.PdfException;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.io.xml.IOWorker;
import org.rekeningsysteem.logging.ApplicationLogger;
import org.rekeningsysteem.logic.database.DebiteurDBInteraction;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.ui.AbstractRekeningController;
import org.rekeningsysteem.ui.mutaties.MutatiesController;
import org.rekeningsysteem.ui.offerte.OfferteController;
import org.rekeningsysteem.ui.particulier.ParticulierController;
import org.rekeningsysteem.ui.reparaties.ReparatiesController;

import de.nixosoft.jlr.JLROpener;

public class RekeningTab extends Tab implements Disposable {

	private static final IOWorker ioWorker = new IOWorker(ApplicationLogger.getInstance());

	private final PublishSubject<Boolean> modified = PublishSubject.create();
	private final BehaviorSubject<AbstractRekening> latest = BehaviorSubject.create();
	private final AbstractRekeningController<? extends AbstractRekening> controller;
	private Optional<File> saveFile;
	private final DebiteurDBInteraction debiteurDB;
	private final CompositeDisposable disposable = new CompositeDisposable();

	public RekeningTab(String name, AbstractRekeningController<? extends AbstractRekening> controller, Database database) {
		this(name, controller, Optional.empty(), database);
	}

	public RekeningTab(String name, AbstractRekeningController<? extends AbstractRekening> controller, Optional<File> file, Database database) {
		super(name);
		this.controller = controller;
		this.saveFile = file;
		this.debiteurDB = new DebiteurDBInteraction(database);

		this.setContent(this.controller.getUI());

		this.disposable.addAll(
			this.getModel().subscribe(this.latest::onNext, this.latest::onError, this.latest::onComplete),

			this.getModel().skip(1).subscribe(ar -> this.modified.onNext(true)),

			this.modified.filter(b -> b)
				.map(b -> this.getText())
				.filter(s -> !s.endsWith("*"))
				.map(s -> s + "*")
				.subscribe(this::setText),

			this.controller
		);
		
		this.setOnClosed(evt -> this.dispose());
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

	public static Maybe<RekeningTab> openFile(File file, Database database) {
		if (file.getName().endsWith(".pdf")) {
			return Maybe.fromAction(() -> JLROpener.open(file));
		}

		if (file.getName().endsWith(".xml")) {
			return ioWorker.load(file)
				.doOnError(error -> {
					// TODO move this to a separate class (as well as all other popup windows)
					String alertText = "Deze factuur kon niet worden geopend. De file is "
						+ "waarschijnlijk corrupt. Raadpleeg de programmeur om dit probleem "
						+ "op te lossen.";
					ButtonType close = new ButtonType("Sluit", ButtonData.CANCEL_CLOSE);
					Alert alert = new Alert(AlertType.NONE, alertText, close);
					alert.setHeaderText("Fout bij inladen factuur");
					alert.show();
				})
				.toObservable()
				.publish(f -> Observable.merge(
					f.ofType(MutatiesFactuur.class).map(fact -> new MutatiesController(fact, database)),
					f.ofType(Offerte.class).map(fact -> new OfferteController(fact, database)),
					f.ofType(ParticulierFactuur.class).map(fact -> new ParticulierController(fact, PropertiesWorker.getInstance(), database)),
					f.ofType(ReparatiesFactuur.class).map(fact -> new ReparatiesController(fact, database))
				))
				.map(c -> new RekeningTab(file.getName(), c, Optional.of(file), database))
				.singleElement();
		}

		return Maybe.empty();
	}

	public void save() {
		AbstractRekening rekening = this.latest.getValue();

		this.saveFile.ifPresent(file -> ioWorker.save(rekening, file));

		if (this.controller.getSaveSelected().blockingGet()) {
			this.debiteurDB.addDebiteur(rekening.getFactuurHeader().getDebiteur()).blockingAwait();
		}
		
		String text = this.getText();
		if (text.endsWith("*")) {
			this.setText(text.substring(0, text.length() - 1));
		}

		this.modified.onNext(false);
	}

	public void export(File file) {
		final AtomicReference<PdfException> e = new AtomicReference<>();

		AbstractRekening rekening = this.latest.getValue();

		try {
			ioWorker.export(rekening, file);
		}
		catch (PdfException ex) {
			e.set(ex);
		}

		if (e.get() != null) {
			throw e.get();
		}
	}

	@Override
	public boolean isDisposed() {
		return this.disposable.isDisposed();
	}

	@Override
	public void dispose() {
		this.disposable.dispose();
	}
}

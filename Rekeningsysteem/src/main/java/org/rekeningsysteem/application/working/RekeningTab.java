package org.rekeningsysteem.application.working;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javafx.scene.control.Tab;

import org.rekeningsysteem.data.aangenomen.AangenomenFactuur;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.logic.database.DebiteurDBInteraction;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.ui.AbstractRekeningController;
import org.rekeningsysteem.ui.aangenomen.AangenomenController;
import org.rekeningsysteem.ui.mutaties.MutatiesController;
import org.rekeningsysteem.ui.offerte.OfferteController;
import org.rekeningsysteem.ui.particulier.ParticulierController;
import org.rekeningsysteem.ui.reparaties.ReparatiesController;

import de.nixosoft.jlr.JLROpener;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

public class RekeningTab extends Tab {

	private static final IOWorker ioWorker = new IOWorker();

	private final PublishSubject<Boolean> modified = PublishSubject.create();
	private final BehaviorSubject<AbstractRekening> latest = BehaviorSubject.create();
	private final AbstractRekeningController<? extends AbstractRekening> controller;
	private Optional<File> saveFile;
	private final DebiteurDBInteraction debiteurDB;

	public RekeningTab(String name,
			AbstractRekeningController<? extends AbstractRekening> controller, Database database) {
		this(name, controller, null, database);
	}

	public RekeningTab(String name,
			AbstractRekeningController<? extends AbstractRekening> controller,
			File file, Database database) {
		super(name);
		this.controller = controller;
		this.saveFile = Optional.ofNullable(file);
		this.debiteurDB = new DebiteurDBInteraction(database);

		this.setContent(this.controller.getUI());

		this.getModel().subscribe(this.latest);
		this.getModel().skip(1).subscribe(ar -> this.modified.onNext(true));

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

	public static Observable<RekeningTab> openFile(File file, Database database) {
		if (file.getName().endsWith(".pdf")) {
			try {
				JLROpener.open(file);
				return Observable.empty();
			}
			catch (IOException e) {
				return Observable.error(e);
			}
		}
		else if (file.getName().endsWith(".xml")) {
			PropertiesWorker properties = PropertiesWorker.getInstance();

			return ioWorker.load(file).publish(f -> {
				Observable<AangenomenController> aangenomen = f.ofType(AangenomenFactuur.class)
						.map(fact -> new AangenomenController(fact, properties, database));
				Observable<MutatiesController> mutaties = f.ofType(MutatiesFactuur.class)
						.map(fact -> new MutatiesController(fact, database));
				Observable<OfferteController> offerte = f.ofType(Offerte.class)
						.map(fact -> new OfferteController(fact, database));
				Observable<ParticulierController> particulier = f.ofType(ParticulierFactuur.class)
						.map(fact -> new ParticulierController(fact, properties, database));
				Observable<ReparatiesController> reparaties = f.ofType(ReparatiesFactuur.class)
						.map(fact -> new ReparatiesController(fact, database));

				return Observable.merge(aangenomen, mutaties, offerte, particulier, reparaties);
			}).map(c -> new RekeningTab(file.getName(), c, file, database)).single();
		}
		return Observable.empty();
	}

	public void save() {
		this.latest.first()
				.doOnNext(factuur -> this.saveFile.ifPresent(file -> ioWorker.save(factuur, file)))
				.flatMap(rekening -> this.controller.getSaveSelected().flatMap(select -> {
					if (select) {
						Debiteur debiteur = rekening.getFactuurHeader().getDebiteur();
						// TODO on which scheduler should the following line be? ioScheduler?
						return this.debiteurDB.addDebiteur(debiteur);
					}
					return Observable.just(-1);
				}))
				.map(i -> this.getText())
				.filter(s -> s.endsWith("*"))
				.map(s -> s.substring(0, s.length() - 1))
				.subscribe(this::setText);
		this.modified.onNext(false);
	}

	public void export(File file) {
		this.latest.first().subscribe(factuur -> ioWorker.export(factuur, file));
	}
}

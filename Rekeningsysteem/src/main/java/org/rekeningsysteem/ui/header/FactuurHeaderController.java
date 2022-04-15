package org.rekeningsysteem.ui.header;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.logic.database.DebiteurDBInteraction;
import org.rekeningsysteem.ui.header.FactuurnummerPane.FactuurnummerType;

import java.util.Optional;

public class FactuurHeaderController implements Disposable {

	private final Observable<FactuurHeader> model;
	private final FactuurHeaderPane ui;

	// subcontrollers
	private final DebiteurController debiteur;
	private final FactuurnummerController factuurnummer;

	public FactuurHeaderController(FactuurHeader input, Database database) {
		this(
			new DebiteurController(input.debiteur(), new DebiteurDBInteraction(database)),
			new DatumController(input.datum()),
			new FactuurnummerController(FactuurnummerType.FACTUUR, input.factuurnummer())
		);
	}

	public FactuurHeaderController(DebiteurController debiteur, DatumController datum, FactuurnummerController factuurnummer) {
		this.debiteur = debiteur;
		this.factuurnummer = factuurnummer;
		this.model = Observable.combineLatest(debiteur.getModel(), datum.getModel(), factuurnummer.getModel(), FactuurHeader::new);
		this.ui = new FactuurHeaderPane(debiteur.getUI(), datum.getUI(), factuurnummer.getUI());
	}

	public Observable<FactuurHeader> getModel() {
		return this.model;
	}

	public FactuurHeaderPane getUI() {
		return this.ui;
	}

	public DebiteurController getDebiteurController() {
		return this.debiteur;
	}

	public void setFactuurnummer(Optional<String> factuurnummer) {
		this.factuurnummer.setFactuurnummer(factuurnummer);
	}

	@Override
	public boolean isDisposed() {
		return this.debiteur.isDisposed();
	}

	@Override
	public void dispose() {
		this.debiteur.dispose();
	}
}

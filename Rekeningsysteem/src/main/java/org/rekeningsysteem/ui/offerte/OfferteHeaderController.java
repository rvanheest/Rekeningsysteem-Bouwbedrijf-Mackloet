package org.rekeningsysteem.ui.offerte;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.logic.database.DebiteurDBInteraction;
import org.rekeningsysteem.ui.header.DatumController;
import org.rekeningsysteem.ui.header.DebiteurController;
import org.rekeningsysteem.ui.header.FactuurHeaderPane;
import org.rekeningsysteem.ui.header.FactuurnummerController;
import org.rekeningsysteem.ui.header.FactuurnummerPane.FactuurnummerType;

public class OfferteHeaderController implements Disposable {

	private final Observable<FactuurHeader> model;
	private final Observable<Boolean> ondertekenenModel;
	private final FactuurHeaderPane ui;

	// subcontrollers
	private final DebiteurController debiteur;
	private final FactuurnummerController offertenummer;
	private final OndertekenenController ondertekenen;

	public OfferteHeaderController(Database database) {
		this(
			new DebiteurController(new DebiteurDBInteraction(database)),
			new DatumController(),
			new FactuurnummerController(FactuurnummerType.OFFERTE),
			new OndertekenenController()
		);
	}

	public OfferteHeaderController(FactuurHeader headerInput, Boolean ondertekenenInput, Database database) {
		this(
			new DebiteurController(headerInput.debiteur(), new DebiteurDBInteraction(database)),
			new DatumController(headerInput.datum()),
			new FactuurnummerController(FactuurnummerType.OFFERTE, headerInput.factuurnummer()),
			new OndertekenenController(ondertekenenInput)
		);
	}

	public OfferteHeaderController(DebiteurController debiteur, DatumController datum, FactuurnummerController offertenummer, OndertekenenController ondertekenen) {
		this.debiteur = debiteur;
		this.offertenummer = offertenummer;
		this.ondertekenen = ondertekenen;

		this.model = Observable.combineLatest(debiteur.getModel(), datum.getModel(), offertenummer.getModel(), FactuurHeader::new);
		this.ondertekenenModel = ondertekenen.getModel();
		this.ui = new FactuurHeaderPane(debiteur.getUI(), datum.getUI(), offertenummer.getUI(), ondertekenen.getUI());
	}

	public Observable<FactuurHeader> getModel() {
		return this.model;
	}

	public Observable<Boolean> getOndertekenenModel() {
		return this.ondertekenenModel;
	}

	public FactuurHeaderPane getUI() {
		return this.ui;
	}

	public DebiteurController getDebiteurController() {
		return this.debiteur;
	}

	public FactuurnummerController getOffertenummerController() {
		return this.offertenummer;
	}

	public OndertekenenController getOndertekenen() {
		return this.ondertekenen;
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

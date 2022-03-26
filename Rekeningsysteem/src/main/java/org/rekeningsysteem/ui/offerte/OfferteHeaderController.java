package org.rekeningsysteem.ui.offerte;

import io.reactivex.rxjava3.core.Observable;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.logic.database.DebiteurDBInteraction;
import org.rekeningsysteem.ui.WorkingPaneController;
import org.rekeningsysteem.ui.header.DatumController;
import org.rekeningsysteem.ui.header.DebiteurController;
import org.rekeningsysteem.ui.header.FactuurHeaderPane;
import org.rekeningsysteem.ui.header.FactuurnummerController;
import org.rekeningsysteem.ui.header.FactuurnummerPane.FactuurnummerType;

public class OfferteHeaderController extends WorkingPaneController {

	private final Observable<FactuurHeader> model;
	private final Observable<Boolean> ondertekenenModel;

	// subcontrollers
	private final DebiteurController debiteur;
	private final DatumController datum;
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
			new DebiteurController(headerInput.getDebiteur(), new DebiteurDBInteraction(database)),
			new DatumController(headerInput.getDatum()),
			new FactuurnummerController(FactuurnummerType.OFFERTE, headerInput.getFactuurnummer()),
			new OndertekenenController(ondertekenenInput)
		);
	}

	public OfferteHeaderController(DebiteurController debiteur, DatumController datum, FactuurnummerController offertenummer, OndertekenenController ondertekenen) {
		super(new FactuurHeaderPane(debiteur.getUI(), datum.getUI(), offertenummer.getUI(), ondertekenen.getUI()));
		this.debiteur = debiteur;
		this.datum = datum;
		this.offertenummer = offertenummer;
		this.ondertekenen = ondertekenen;

		this.model = Observable.combineLatest(debiteur.getModel(), datum.getModel(),
			offertenummer.getModel(), FactuurHeader::new);
		this.ondertekenenModel = ondertekenen.getModel();
	}

	public Observable<FactuurHeader> getModel() {
		return this.model;
	}

	public Observable<Boolean> getOndertekenenModel() {
		return this.ondertekenenModel;
	}

	public DebiteurController getDebiteurController() {
		return this.debiteur;
	}

	public DatumController getDatumController() {
		return this.datum;
	}

	public FactuurnummerController getOffertenummerController() {
		return this.offertenummer;
	}

	public OndertekenenController getOndertekenen() {
		return this.ondertekenen;
	}
}

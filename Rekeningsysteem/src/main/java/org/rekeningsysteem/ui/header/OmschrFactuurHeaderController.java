package org.rekeningsysteem.ui.header;

import io.reactivex.rxjava3.core.Observable;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.logic.database.DebiteurDBInteraction;
import org.rekeningsysteem.ui.WorkingPaneController;
import org.rekeningsysteem.ui.header.FactuurnummerPane.FactuurnummerType;

public class OmschrFactuurHeaderController extends WorkingPaneController {

	private final Observable<OmschrFactuurHeader> model;

	// subcontrollers
	private final DebiteurController debiteur;
	private final DatumController datum;
	private final FactuurnummerController factuurnummer;
	private final OmschrijvingController omschrijving;

	public OmschrFactuurHeaderController(Database database) {
		this(
			new DebiteurController(new DebiteurDBInteraction(database)),
			new DatumController(),
			new FactuurnummerController(FactuurnummerType.FACTUUR),
			new OmschrijvingController()
		);
	}

	public OmschrFactuurHeaderController(OmschrFactuurHeader input, Database database) {
		this(
			new DebiteurController(input.getDebiteur(), new DebiteurDBInteraction(database)),
			new DatumController(input.getDatum()),
			new FactuurnummerController(FactuurnummerType.FACTUUR, input.getFactuurnummer()),
			new OmschrijvingController(input.getOmschrijving())
		);
	}

	public OmschrFactuurHeaderController(DebiteurController debiteur, DatumController datum,
		FactuurnummerController factuurnummer, OmschrijvingController omschrijving) {
		super(new FactuurHeaderPane(debiteur.getUI(), datum.getUI(), factuurnummer.getUI(), omschrijving.getUI()));
		this.debiteur = debiteur;
		this.datum = datum;
		this.factuurnummer = factuurnummer;
		this.omschrijving = omschrijving;

		this.model = Observable.combineLatest(
			debiteur.getModel(),
			datum.getModel(),
			factuurnummer.getModel(),
			omschrijving.getModel(),
			OmschrFactuurHeader::new
		);
	}

	public Observable<OmschrFactuurHeader> getModel() {
		return this.model;
	}

	public DebiteurController getDebiteurController() {
		return this.debiteur;
	}

	public DatumController getDatumController() {
		return this.datum;
	}

	public FactuurnummerController getFactuurnummerController() {
		return this.factuurnummer;
	}

	public OmschrijvingController getOmschrijvingController() {
		return this.omschrijving;
	}
}

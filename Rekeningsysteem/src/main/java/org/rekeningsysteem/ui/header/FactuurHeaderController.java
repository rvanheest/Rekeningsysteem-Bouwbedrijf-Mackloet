package org.rekeningsysteem.ui.header;

import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.logic.database.DebiteurDBInteraction;
import org.rekeningsysteem.ui.WorkingPaneController;
import org.rekeningsysteem.ui.header.FactuurnummerPane.FactuurnummerType;

import rx.Observable;

public class FactuurHeaderController extends WorkingPaneController {

	private final Observable<FactuurHeader> model;
	
	// subcontrollers
	private final DebiteurController debiteur;
	private final DatumController datum;
	private final FactuurnummerController factuurnummer;

	public FactuurHeaderController(Database database) {
		this(new DebiteurController(new DebiteurDBInteraction(database)), new DatumController(),
				new FactuurnummerController(FactuurnummerType.FACTUUR));
	}

	public FactuurHeaderController(FactuurHeader input, Database database) {
		this(new DebiteurController(input.getDebiteur(), new DebiteurDBInteraction(database)),
				new DatumController(input.getDatum()),
				new FactuurnummerController(FactuurnummerType.FACTUUR, input.getFactuurnummer()));
	}

	public FactuurHeaderController(DebiteurController debiteur, DatumController datum,
			FactuurnummerController factuurnummer) {
		super(new FactuurHeaderPane(debiteur.getUI(), datum.getUI(), factuurnummer.getUI()));
		this.debiteur = debiteur;
		this.datum = datum;
		this.factuurnummer = factuurnummer;
		
		this.model = Observable.combineLatest(debiteur.getModel(), datum.getModel(),
				factuurnummer.getModel(), FactuurHeader::new);
	}

	public Observable<FactuurHeader> getModel() {
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
}

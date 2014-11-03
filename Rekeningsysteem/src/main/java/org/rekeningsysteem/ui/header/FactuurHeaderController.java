package org.rekeningsysteem.ui.header;

import java.util.Optional;

import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.ui.WorkingPaneController;
import org.rekeningsysteem.ui.header.FactuurnummerPane.FactuurnummerType;

import rx.Observable;

public class FactuurHeaderController extends WorkingPaneController {

	private final Observable<FactuurHeader> model;
	private final FactuurnummerController factuurnummerController;

	public FactuurHeaderController() {
		this(new DebiteurController(), new DatumController(),
				new FactuurnummerController(FactuurnummerType.FACTUUR));
	}

	public FactuurHeaderController(FactuurHeader input) {
		this(new DebiteurController(input.getDebiteur()), new DatumController(input.getDatum()),
				new FactuurnummerController(FactuurnummerType.FACTUUR, input.getFactuurnummer()));
	}

	public FactuurHeaderController(DebiteurController debiteur, DatumController datum,
			FactuurnummerController factuurnummer) {
		super(new FactuurHeaderPane(debiteur.getUI(), datum.getUI(), factuurnummer.getUI()));
		this.model = Observable.combineLatest(debiteur.getModel(), datum.getModel(),
				factuurnummer.getModel(), FactuurHeader::new);
		this.factuurnummerController = factuurnummer;
	}

	public Observable<FactuurHeader> getModel() {
		return this.model;
	}

	public void initFactuurnummer(Optional<String> factuurnummer) {
		this.factuurnummerController.getUI().setFactuurnummer(factuurnummer);
	}
}

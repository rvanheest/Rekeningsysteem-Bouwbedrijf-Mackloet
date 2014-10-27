package org.rekeningsysteem.ui.header;

import java.util.Optional;

import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.ui.WorkingPaneController;
import org.rekeningsysteem.ui.header.FactuurnummerPane.FactuurnummerType;

import rx.Observable;

public class FactuurHeaderController extends WorkingPaneController {

	private final Observable<FactuurHeader> model;
	
	public FactuurHeaderController() {
		this(new DebiteurController(), new DatumController(),
				new FactuurnummerController(FactuurnummerType.FACTUUR));
	}

	public FactuurHeaderController(Observable<FactuurHeader> input) {
		this(new DebiteurController(), new DatumController(),
				new FactuurnummerController(FactuurnummerType.FACTUUR), input);
	}

	public FactuurHeaderController(DebiteurController debiteur, DatumController datum,
			FactuurnummerController factuurnummer) {
		this(debiteur, datum, factuurnummer, Observable.empty());
	}

	public FactuurHeaderController(DebiteurController debiteur, DatumController datum,
			FactuurnummerController factuurnummer, Observable<FactuurHeader> input) {
		super(new FactuurHeaderPane(debiteur.getUI(), datum.getUI(), factuurnummer.getUI()));
		this.model = Observable.combineLatest(debiteur.getModel(), datum.getModel(),
				factuurnummer.getModel(), FactuurHeader::new);

		input.map(FactuurHeader::getDebiteur).subscribe(debiteur);
		input.map(FactuurHeader::getDatum).subscribe(datum);
		input.map(FactuurHeader::getFactuurnummer)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.subscribe(factuurnummer);
	}

	public Observable<FactuurHeader> getModel() {
		return this.model;
	}
}

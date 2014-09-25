package org.rekeningsysteem.ui;

import java.util.Optional;

import org.rekeningsysteem.data.util.header.FactuurHeader;

import rx.Observable;

import com.google.inject.Inject;

public class FactuurHeaderController {

	private final FactuurHeaderPane ui;
	private final Observable<FactuurHeader> model;

	@Inject
	public FactuurHeaderController(DebiteurController debiteur, DatumController datum,
			FactuurnummerController factuurnummer) {
		this(debiteur, datum, factuurnummer, Observable.empty());
	}

	public FactuurHeaderController(DebiteurController debiteur, DatumController datum,
			FactuurnummerController factuurnummer, Observable<FactuurHeader> input) {
		this.ui = new FactuurHeaderPane(debiteur.getUI(), datum.getUI(), factuurnummer.getUI());
		this.model = Observable.combineLatest(debiteur.getModel(), datum.getModel(),
				factuurnummer.getModel(), FactuurHeader::new);

		input.map(FactuurHeader::getDebiteur).subscribe(debiteur);
		input.map(FactuurHeader::getDatum).subscribe(datum);
		input.map(FactuurHeader::getFactuurnummer)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.subscribe(factuurnummer);
	}

	public FactuurHeaderPane getUI() {
		return this.ui;
	}

	public Observable<FactuurHeader> getModel() {
		return this.model;
	}
}

package org.rekeningsysteem.ui.offerte;

import java.util.Optional;

import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.ui.DatumController;
import org.rekeningsysteem.ui.DebiteurController;
import org.rekeningsysteem.ui.FactuurHeaderPane;
import org.rekeningsysteem.ui.FactuurnummerController;

import rx.Observable;

import com.google.inject.Inject;

public class OfferteHeaderController {

	private final FactuurHeaderPane ui;
	private final Observable<FactuurHeader> model;
	private final Observable<Boolean> ondertekenen;

	@Inject
	public OfferteHeaderController(DebiteurController debiteur, DatumController datum,
			FactuurnummerController offertenummer, OndertekenenController ondertekenen) {
		this(debiteur, datum, offertenummer, ondertekenen, Observable.empty(), Observable.empty());
	}

	public OfferteHeaderController(DebiteurController debiteur, DatumController datum,
			FactuurnummerController offertenummer, OndertekenenController ondertekenen,
			Observable<FactuurHeader> input, Observable<Boolean> ondertekenenInput) {
		this.ui = new FactuurHeaderPane(debiteur.getUI(), datum.getUI(),
				offertenummer.getUI(), ondertekenen.getUI());
		this.model = Observable.combineLatest(debiteur.getModel(), datum.getModel(),
				offertenummer.getModel(), FactuurHeader::new);
		this.ondertekenen = ondertekenen.getModel();

		input.map(FactuurHeader::getDebiteur).subscribe(debiteur);
		input.map(FactuurHeader::getDatum).subscribe(datum);
		input.map(FactuurHeader::getFactuurnummer)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.subscribe(offertenummer);
		ondertekenenInput.subscribe(ondertekenen);
	}

	public FactuurHeaderPane getUI() {
		return this.ui;
	}

	public Observable<FactuurHeader> getModel() {
		return this.model;
	}

	public Observable<Boolean> getOndertekenen() {
		return this.ondertekenen;
	}
}

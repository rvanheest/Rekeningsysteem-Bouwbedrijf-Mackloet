package org.rekeningsysteem.ui.offerte;

import java.util.Optional;

import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.ui.WorkingPaneController;
import org.rekeningsysteem.ui.header.DatumController;
import org.rekeningsysteem.ui.header.DebiteurController;
import org.rekeningsysteem.ui.header.FactuurHeaderPane;
import org.rekeningsysteem.ui.header.FactuurnummerController;
import org.rekeningsysteem.ui.header.FactuurnummerPane.FactuurnummerType;

import rx.Observable;
import rx.Subscription;

public class OfferteHeaderController extends WorkingPaneController {

	private final Observable<FactuurHeader> model;
	private final Observable<Boolean> ondertekenen;
	private final FactuurnummerController offertenummerController;

	public OfferteHeaderController() {
		this(new DebiteurController(), new DatumController(),
				new FactuurnummerController(FactuurnummerType.OFFERTE),
				new OndertekenenController());
	}

	public OfferteHeaderController(Observable<FactuurHeader> input, Observable<Boolean> ondertekenenInput) {
		this(new DebiteurController(), new DatumController(),
				new FactuurnummerController(FactuurnummerType.OFFERTE),
				new OndertekenenController(), input, ondertekenenInput);
	}

	public OfferteHeaderController(DebiteurController debiteur, DatumController datum,
			FactuurnummerController offertenummer, OndertekenenController ondertekenen) {
		this(debiteur, datum, offertenummer, ondertekenen, Observable.empty(), Observable.empty());
	}

	public OfferteHeaderController(DebiteurController debiteur, DatumController datum,
			FactuurnummerController offertenummer, OndertekenenController ondertekenen,
			Observable<FactuurHeader> input, Observable<Boolean> ondertekenenInput) {
		super(new FactuurHeaderPane(debiteur.getUI(), datum.getUI(),
				offertenummer.getUI(), ondertekenen.getUI()));
		this.model = Observable.combineLatest(debiteur.getModel(), datum.getModel(),
				offertenummer.getModel(), FactuurHeader::new);
		this.ondertekenen = ondertekenen.getModel();
		this.offertenummerController = offertenummer;

		input.map(FactuurHeader::getDebiteur).subscribe(debiteur);
		input.map(FactuurHeader::getDatum).subscribe(datum);
		input.map(FactuurHeader::getFactuurnummer)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.subscribe(offertenummer);
		ondertekenenInput.subscribe(ondertekenen);
	}

	public Observable<FactuurHeader> getModel() {
		return this.model;
	}

	public Observable<Boolean> getOndertekenen() {
		return this.ondertekenen;
	}

	public Subscription initFactuurnummer(Observable<String> factuurnummer) {
		return factuurnummer.subscribe(this.offertenummerController);
	}
}

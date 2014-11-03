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

public class OfferteHeaderController extends WorkingPaneController {

	private final Observable<FactuurHeader> model;
	private final Observable<Boolean> ondertekenen;
	private final FactuurnummerController offertenummerController;

	public OfferteHeaderController() {
		this(new DebiteurController(), new DatumController(),
				new FactuurnummerController(FactuurnummerType.OFFERTE),
				new OndertekenenController());
	}

	public OfferteHeaderController(FactuurHeader headerInput, Boolean ondertekenenInput) {
		this(new DebiteurController(headerInput.getDebiteur()), new DatumController(headerInput.getDatum()),
				new FactuurnummerController(FactuurnummerType.OFFERTE, headerInput.getFactuurnummer()),
				new OndertekenenController(ondertekenenInput));
	}

	public OfferteHeaderController(DebiteurController debiteur, DatumController datum,
			FactuurnummerController offertenummer, OndertekenenController ondertekenen) {
		super(new FactuurHeaderPane(debiteur.getUI(), datum.getUI(),
				offertenummer.getUI(), ondertekenen.getUI()));
		this.model = Observable.combineLatest(debiteur.getModel(), datum.getModel(),
				offertenummer.getModel(), FactuurHeader::new);
		this.ondertekenen = ondertekenen.getModel();
		this.offertenummerController = offertenummer;
	}

	public Observable<FactuurHeader> getModel() {
		return this.model;
	}

	public Observable<Boolean> getOndertekenen() {
		return this.ondertekenen;
	}

	public void initFactuurnummer(Optional<String> offertenummer) {
		this.offertenummerController.getUI().setFactuurnummer(offertenummer);
	}
}

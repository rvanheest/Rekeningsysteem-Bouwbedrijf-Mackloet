package org.rekeningsysteem.ui.header;

import java.util.Optional;

import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.ui.WorkingPaneController;
import org.rekeningsysteem.ui.header.FactuurnummerPane.FactuurnummerType;

import rx.Observable;

public class OmschrFactuurHeaderController extends WorkingPaneController {

	private final Observable<OmschrFactuurHeader> model;
	private final FactuurnummerController factuurnummerController;

	public OmschrFactuurHeaderController() {
		this(new DebiteurController(), new DatumController(),
				new FactuurnummerController(FactuurnummerType.FACTUUR),
				new OmschrijvingController());
	}

	public OmschrFactuurHeaderController(OmschrFactuurHeader input) {
		this(new DebiteurController(input.getDebiteur()), new DatumController(input.getDatum()),
				new FactuurnummerController(FactuurnummerType.FACTUUR, input.getFactuurnummer()),
				new OmschrijvingController(input.getOmschrijving()));
	}

	public OmschrFactuurHeaderController(DebiteurController debiteur, DatumController datum,
			FactuurnummerController factuurnummer, OmschrijvingController omschrijving) {
		super(new FactuurHeaderPane(debiteur.getUI(), datum.getUI(),
				factuurnummer.getUI(), omschrijving.getUI()));
		this.model = Observable.combineLatest(debiteur.getModel(), datum.getModel(),
				factuurnummer.getModel(), omschrijving.getModel(), OmschrFactuurHeader::new);
		this.factuurnummerController = factuurnummer;
	}

//	public OmschrFactuurHeaderController(DebiteurController debiteur, DatumController datum,
//			FactuurnummerController factuurnummer, OmschrijvingController omschrijving,
//			Observable<OmschrFactuurHeader> input) {
//		super(new FactuurHeaderPane(debiteur.getUI(), datum.getUI(),
//				factuurnummer.getUI(), omschrijving.getUI()));
//		this.model = Observable.combineLatest(debiteur.getModel(), datum.getModel(),
//				factuurnummer.getModel(), omschrijving.getModel(), OmschrFactuurHeader::new);
//		this.factuurnummerController = factuurnummer;
//
//		input.map(FactuurHeader::getDebiteur).subscribe(debiteur);
//		input.map(FactuurHeader::getDatum).subscribe(datum);
//		input.map(FactuurHeader::getFactuurnummer)
//				.filter(Optional::isPresent)
//				.map(Optional::get)
//				.subscribe(factuurnummer);
//		input.map(OmschrFactuurHeader::getOmschrijving).subscribe(omschrijving);
//	}

	public Observable<OmschrFactuurHeader> getModel() {
		return this.model;
	}

	public void initFactuurnummer(Optional<String> factuurnummer) {
		this.factuurnummerController.getUI().setFactuurnummer(factuurnummer);
	}
}

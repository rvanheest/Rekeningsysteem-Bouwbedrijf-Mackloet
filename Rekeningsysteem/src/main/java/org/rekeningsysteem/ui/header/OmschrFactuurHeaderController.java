package org.rekeningsysteem.ui.header;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.logic.database.DebiteurDBInteraction;
import org.rekeningsysteem.ui.header.FactuurnummerPane.FactuurnummerType;

import java.util.Optional;

public class OmschrFactuurHeaderController implements Disposable {

	private final Observable<Pair<FactuurHeader, String>> model;
	private final FactuurHeaderPane ui;

	// subcontrollers
	private final DebiteurController debiteur;
	private final FactuurnummerController factuurnummer;

	public OmschrFactuurHeaderController(Database database) {
		this(
			new DebiteurController(new DebiteurDBInteraction(database)),
			new DatumController(),
			new FactuurnummerController(FactuurnummerType.FACTUUR),
			new OmschrijvingController()
		);
	}

	public OmschrFactuurHeaderController(FactuurHeader factuurHeader, String omschrijving, Database database) {
		this(
			new DebiteurController(factuurHeader.debiteur(), new DebiteurDBInteraction(database)),
			new DatumController(factuurHeader.datum()),
			new FactuurnummerController(FactuurnummerType.FACTUUR, factuurHeader.factuurnummer()),
			new OmschrijvingController(omschrijving)
		);
	}

	private OmschrFactuurHeaderController(
		DebiteurController debiteur,
		DatumController datum,
		FactuurnummerController factuurnummer,
		OmschrijvingController omschrijving
	) {
		this.debiteur = debiteur;
		this.factuurnummer = factuurnummer;

		this.model = Observable.combineLatest(
			debiteur.getModel(),
			datum.getModel(),
			factuurnummer.getModel(),
			omschrijving.getModel(),
				(deb, dat, fac, omschr) -> new ImmutablePair<>(new FactuurHeader(deb, dat, fac), omschr)
		);
		this.ui = new FactuurHeaderPane(debiteur.getUI(), datum.getUI(), factuurnummer.getUI(), omschrijving.getUI());
	}

	public Observable<Pair<FactuurHeader, String>> getModel() {
		return this.model;
	}

	public FactuurHeaderPane getUI() {
		return this.ui;
	}

	public Observable<Boolean> isDebiteurSaveSelected() {
		return this.debiteur.isSaveSelected();
	}

	public void setFactuurnummer(Optional<String> factuurnummer) {
		this.factuurnummer.setFactuurnummer(factuurnummer);
	}

	@Override
	public boolean isDisposed() {
		return this.debiteur.isDisposed();
	}

	@Override
	public void dispose() {
		this.debiteur.dispose();
	}
}

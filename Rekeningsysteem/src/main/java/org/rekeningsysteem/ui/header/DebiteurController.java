package org.rekeningsysteem.ui.header;

import org.rekeningsysteem.data.util.header.Debiteur;

import rx.Observable;
import rx.Observer;

public class DebiteurController implements Observer<Debiteur> {

	private final DebiteurPane ui;
	private final Observable<Debiteur> model;

	public DebiteurController() {
		this(new DebiteurPane());
	}

	public DebiteurController(DebiteurPane ui) {
		this.ui = ui;
		this.model = Observable.combineLatest(this.ui.getNaam(), this.ui.getStraat(),
				this.ui.getNummer(), this.ui.getPostcode(), this.ui.getPlaats(),
				this.ui.getBtwnummer(), Debiteur::new);
	}

	public DebiteurPane getUI() {
		return this.ui;
	}

	public Observable<Debiteur> getModel() {
		return this.model;
	}

	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
		e.printStackTrace();
	}

	@Override
	public void onNext(Debiteur debiteur) {
		this.ui.setNaam(debiteur.getNaam());
		this.ui.setStraat(debiteur.getStraat());
		this.ui.setNummer(debiteur.getNummer());
		this.ui.setPostcode(debiteur.getPostcode());
		this.ui.setPlaats(debiteur.getPlaats());
		this.ui.setBtwNummer(debiteur.getBtwNummer().orElse(""));
	}
}

package org.rekeningsysteem.application.settings.debiteur;

import io.reactivex.rxjava3.core.Observable;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.ui.list.AbstractListItemController;

import java.util.Optional;

public class DebiteurItemPaneController extends AbstractListItemController<Debiteur, DebiteurItemPane> {

	public DebiteurItemPaneController() {
		this(Optional.empty(), new DebiteurItemPane());
	}

	public DebiteurItemPaneController(Debiteur debiteur) {
		this(debiteur.getDebiteurID(), new DebiteurItemPane());
		setDebiteur(debiteur, this.getUI());
	}

	public DebiteurItemPaneController(Optional<Integer> debiteurID, DebiteurItemPane ui) {
		super(ui, getDebiteur(debiteurID, ui));
	}

	private static Observable<Debiteur> getDebiteur(Optional<Integer> debiteurID, DebiteurItemPane ui) {
		return Observable.combineLatest(
			ui.getNaam(),
			ui.getStraat(),
			ui.getNummer(),
			ui.getPostcode(),
			ui.getPlaats(),
			ui.getBtwnummer(),
			(naam, straat, nummer, postcode, plaats, btw) -> new Debiteur(debiteurID, naam, straat, nummer, postcode, plaats, btw)
		);
	}

	private static void setDebiteur(Debiteur debiteur, DebiteurItemPane ui) {
		ui.setNaam(debiteur.getNaam());
		ui.setStraat(debiteur.getStraat());
		ui.setNummer(debiteur.getNummer());
		ui.setPostcode(debiteur.getPostcode());
		ui.setPlaats(debiteur.getPlaats());
		ui.setBtwNummer(debiteur.getBtwNummer().orElse(""));
		ui.setAsUpdate();
	}
}

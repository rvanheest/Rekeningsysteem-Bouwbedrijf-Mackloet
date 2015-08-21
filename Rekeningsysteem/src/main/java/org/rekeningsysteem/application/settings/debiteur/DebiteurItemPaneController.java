package org.rekeningsysteem.application.settings.debiteur;

import java.util.Optional;

import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.ui.list.AbstractListItemController;

import rx.Observable;

public class DebiteurItemPaneController extends AbstractListItemController<Debiteur> {

	public DebiteurItemPaneController() {
		this(Optional.empty(), new DebiteurItemPane());
	}

	public DebiteurItemPaneController(Optional<Integer> debiteurID) {
		this(debiteurID, new DebiteurItemPane());
	}

	public DebiteurItemPaneController(Debiteur input) {
		this(input.getDebiteurID());
		this.getUI().setNaam(input.getNaam());
		this.getUI().setStraat(input.getStraat());
		this.getUI().setNummer(input.getNummer());
		this.getUI().setPostcode(input.getPostcode());
		this.getUI().setPlaats(input.getPlaats());
		this.getUI().setBtwNummer(input.getBtwNummer().orElse(""));
		this.getUI().setAsUpdate();
	}

	public DebiteurItemPaneController(Optional<Integer> debiteurID, DebiteurItemPane ui) {
		super(ui, Observable.combineLatest(ui.getNaam(), ui.getStraat(), ui.getNummer(),
				ui.getPostcode(), ui.getPlaats(), ui.getBtwnummer(),
				(naam, straat, nummer, postcode, plaats, btw) -> new Debiteur(debiteurID,
						naam, straat, nummer, postcode, plaats, btw))
				.sample(ui.getAddButtonEvent())
				.map(Optional::of)
				.mergeWith(ui.getCancelButtonEvent().map(event -> Optional.empty()))
				.first());
	}

	public DebiteurItemPane getUI() {
		return (DebiteurItemPane) super.getUI();
	}
}

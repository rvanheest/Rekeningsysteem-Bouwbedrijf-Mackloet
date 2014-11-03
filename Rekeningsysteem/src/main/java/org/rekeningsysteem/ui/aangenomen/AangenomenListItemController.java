package org.rekeningsysteem.ui.aangenomen;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.Geld;

import rx.Observable;

public class AangenomenListItemController {

	private AangenomenListItemPane ui;
	private Observable<Optional<AangenomenListItem>> model;

	public AangenomenListItemController(Currency currency) {
		this(new AangenomenListItemPane(currency));
	}

	public AangenomenListItemController(Currency currency, AangenomenListItem input) {
		this(currency);
		this.ui.setOmschrijving(input.getOmschrijving());
		this.ui.setLoon(input.getLoon().getBedrag());
		this.ui.setMateriaal(input.getMateriaal().getBedrag());
	}

	public AangenomenListItemController(AangenomenListItemPane ui) {
		this.ui = ui;

		Observable<Optional<AangenomenListItem>> item = Observable.combineLatest(
				this.ui.getOmschrijving(), this.ui.getLoon().map(Geld::new),
				this.ui.getMateriaal().map(Geld::new), AangenomenListItem::new)
				.sample(this.ui.getAddButtonEvent())
				.map(Optional::of);
		Observable<Optional<AangenomenListItem>> cancel = this.ui.getCancelButtonEvent()
				.map(event -> Optional.empty());

		this.model = Observable.merge(item, cancel).first();
	}

	public AangenomenListItemPane getUI() {
		return this.ui;
	}

	public Observable<Optional<AangenomenListItem>> getModel() {
		return this.model;
	}
}

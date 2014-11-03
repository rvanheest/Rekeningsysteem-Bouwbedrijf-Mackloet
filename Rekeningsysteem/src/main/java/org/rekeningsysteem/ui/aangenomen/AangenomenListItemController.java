package org.rekeningsysteem.ui.aangenomen;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.ui.list.AbstractListItemController;

import rx.Observable;

public class AangenomenListItemController extends AbstractListItemController<AangenomenListItem> {

	public AangenomenListItemController(Currency currency) {
		this(new AangenomenListItemPane(currency));
	}

	public AangenomenListItemController(Currency currency, AangenomenListItem input) {
		this(currency);
		this.getUI().setOmschrijving(input.getOmschrijving());
		this.getUI().setLoon(input.getLoon().getBedrag());
		this.getUI().setMateriaal(input.getMateriaal().getBedrag());
	}

	public AangenomenListItemController(AangenomenListItemPane ui) {
		super(ui, Observable.merge(
				Observable.combineLatest(ui.getOmschrijving(),
						ui.getLoon().map(Geld::new),
						ui.getMateriaal().map(Geld::new), AangenomenListItem::new)
						.sample(ui.getAddButtonEvent())
						.map(Optional::of),
				ui.getCancelButtonEvent()
						.<Optional<AangenomenListItem>> map(event -> Optional.empty()))
				.first());
	}

	public AangenomenListItemPane getUI() {
		return (AangenomenListItemPane) super.getUI();
	}
}

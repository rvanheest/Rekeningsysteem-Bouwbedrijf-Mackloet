package org.rekeningsysteem.ui.aangenomen;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.ui.list.AbstractListItemController;

import rx.Observable;

@Deprecated
public class AangenomenListItemController extends AbstractListItemController<AangenomenListItem> {

	public AangenomenListItemController(Currency currency, BtwPercentage defaultBtw) {
		this(new AangenomenListItemPane(currency), defaultBtw);
	}

	public AangenomenListItemController(Currency currency, BtwPercentage defaultBtw,
			AangenomenListItem input) {
		this(currency, defaultBtw);
		this.getUI().setOmschrijving(input.getOmschrijving());
		this.getUI().setLoon(input.getLoon().getBedrag());
		this.getUI().setMateriaal(input.getMateriaal().getBedrag());
	}

	public AangenomenListItemController(AangenomenListItemPane ui, BtwPercentage defaultBtw) {
		super(ui, Observable.combineLatest(ui.getOmschrijving(),
				ui.getLoon().map(Geld::new), ui.getLoonBtwPercentage(),
				ui.getMateriaal().map(Geld::new), ui.getMateriaalBtwPercentage(),
				AangenomenListItem::new)
				.sample(ui.getAddButtonEvent())
				.map(Optional::of)
				.mergeWith(ui.getCancelButtonEvent()
						.map(event -> Optional.empty()))
				.first());
		ui.setLoonBtwPercentage(defaultBtw.getLoonPercentage());
		ui.setMateriaalBtwPercentage(defaultBtw.getMateriaalPercentage());
	}

	public AangenomenListItemPane getUI() {
		return (AangenomenListItemPane) super.getUI();
	}
}

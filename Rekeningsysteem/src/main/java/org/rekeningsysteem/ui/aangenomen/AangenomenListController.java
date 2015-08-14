package org.rekeningsysteem.ui.aangenomen;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.aangenomen.AangenomenListPane.AangenomenModel;
import org.rekeningsysteem.ui.list.AbstractListController;

public class AangenomenListController extends AbstractListController<AangenomenListItem, AangenomenModel> {

	public AangenomenListController(Currency currency, BtwPercentage defaultBtw) {
		this(currency, defaultBtw, new AangenomenListPane());
	}

	public AangenomenListController(Currency currency, BtwPercentage defaultBtw,
			List<AangenomenListItem> input) {
		this(currency, defaultBtw);
		this.getUI().setData(modelToUI(input));
	}

	public AangenomenListController(Currency currency, BtwPercentage defaultBtw,
			AangenomenListPane ui) {
		super(currency, defaultBtw, ui, AangenomenListItemController::new);
	}

	@Override
	protected List<AangenomenModel> modelToUI(List<AangenomenListItem> list) {
		return list.stream().map(item -> new AangenomenModel(item.getOmschrijving(),
				item.getLoon().getBedrag(), item.getLoonBtwPercentage(),
				item.getMateriaal().getBedrag(), item.getMateriaalBtwPercentage()))
				.collect(Collectors.toList());
	}

	@Override
	protected ItemList<AangenomenListItem> uiToModel(List<? extends AangenomenModel> list) {
		return list.stream().map(item -> new AangenomenListItem(item.getOmschrijving(),
				new Geld(item.getLoon()), item.getLoonBtwPercentage(),
				new Geld(item.getMateriaal()), item.getMateriaalBtwPercentage()))
				.collect(Collectors.toCollection(ItemList::new));
	}
}

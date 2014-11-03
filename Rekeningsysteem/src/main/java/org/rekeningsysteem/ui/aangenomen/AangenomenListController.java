package org.rekeningsysteem.ui.aangenomen;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.aangenomen.AangenomenListPane.AangenomenModel;
import org.rekeningsysteem.ui.list.AbstractListController;

public class AangenomenListController extends AbstractListController<AangenomenListItem, AangenomenModel> {

	public AangenomenListController(Currency currency) {
		this(currency, new AangenomenListPane());
	}

	public AangenomenListController(Currency currency, List<AangenomenListItem> input) {
		this(currency);
		this.getUI().setData(modelToUI(input));
	}

	public AangenomenListController(Currency currency, AangenomenListPane ui) {
		super(currency, ui, AangenomenListItemController::new);
	}

	@Override
	protected List<AangenomenModel> modelToUI(List<AangenomenListItem> list) {
		return list.stream().map(item -> new AangenomenModel(item.getOmschrijving(),
				item.getLoon().getBedrag(), item.getMateriaal().getBedrag()))
				.collect(Collectors.toList());
	}

	@Override
	protected ItemList<AangenomenListItem> uiToModel(List<? extends AangenomenModel> list) {
		return list.stream().map(item -> new AangenomenListItem(item.getOmschrijving(),
				new Geld(item.getLoon()), new Geld(item.getMateriaal())))
				.collect(Collectors.toCollection(ItemList::new));
	}
}

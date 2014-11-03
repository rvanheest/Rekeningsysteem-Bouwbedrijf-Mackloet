package org.rekeningsysteem.ui.reparaties;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.list.AbstractListController;
import org.rekeningsysteem.ui.reparaties.ReparatiesListPane.ReparatiesModel;

public class ReparatiesListController extends
		AbstractListController<ReparatiesBon, ReparatiesModel> {

	public ReparatiesListController(Currency currency) {
		this(currency, new ReparatiesListPane());
	}

	public ReparatiesListController(Currency currency, List<ReparatiesBon> input) {
		this(currency);
		this.getUI().setData(modelToUI(input));
	}

	public ReparatiesListController(Currency currency, ReparatiesListPane ui) {
		super(currency, ui, ReparatiesBonController::new);
	}

	@Override
	protected List<ReparatiesModel> modelToUI(List<ReparatiesBon> list) {
		return list.stream().map(item -> new ReparatiesModel(item.getOmschrijving(),
				item.getBonnummer(), item.getLoon().getBedrag(), item.getMateriaal().getBedrag()))
				.collect(Collectors.toList());
	}

	@Override
	protected ItemList<ReparatiesBon> uiToModel(List<? extends ReparatiesModel> list) {
		return list.stream().map(item -> new ReparatiesBon(item.getOmschrijving(),
				item.getBonnummer(), new Geld(item.getLoon()), new Geld(item.getMateriaal())))
				.collect(Collectors.toCollection(ItemList::new));
	}
}

package org.rekeningsysteem.ui.mutaties;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.list.AbstractListController;
import org.rekeningsysteem.ui.mutaties.MutatiesListPane.MutatiesModel;

public class MutatiesListController extends AbstractListController<MutatiesBon, MutatiesModel> {

	public MutatiesListController(Currency currency) {
		this(currency, new MutatiesListPane());
	}

	public MutatiesListController(Currency currency, List<MutatiesBon> input) {
		this(currency);
		this.getUI().setData(modelToUI(input));
	}

	public MutatiesListController(Currency currency, MutatiesListPane ui) {
		super(currency, ui, MutatiesBonController::new);
	}

	@Override
	protected List<MutatiesModel> modelToUI(List<MutatiesBon> list) {
		return list.stream()
				.map(item -> new MutatiesModel(item.getOmschrijving(),
						item.getBonnummer(), item.getMateriaal().getBedrag()))
				.collect(Collectors.toList());
	}

	@Override
	protected ItemList<MutatiesBon> uiToModel(List<? extends MutatiesModel> list) {
		return list.stream()
				.map(item -> new MutatiesBon(item.getOmschrijving(),
						item.getBonnummer(), new Geld(item.getPrijs())))
				.collect(Collectors.toCollection(ItemList::new));
	}
}

package org.rekeningsysteem.ui.mutaties;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.list.AbstractListController;
import org.rekeningsysteem.ui.mutaties.MutatiesListPane.MutatiesModel;

public class MutatiesListController extends AbstractListController<MutatiesInkoopOrder, MutatiesModel> {

	public MutatiesListController(Currency currency) {
		this(currency, new MutatiesListPane());
	}

	public MutatiesListController(Currency currency, List<MutatiesInkoopOrder> input) {
		this(currency);
		this.getUI().setData(modelToUI(input));
	}

	public MutatiesListController(Currency currency, MutatiesListPane ui) {
		super(currency, ui, MutatiesInkoopOrderController::new);
	}

	@Override
	protected List<MutatiesModel> modelToUI(List<MutatiesInkoopOrder> list) {
		return list.stream()
			.map(item -> new MutatiesModel(
				item.getOmschrijving(),
				item.getInkoopOrderNummer(),
				item.getMateriaal().getBedrag()
			))
			.collect(Collectors.toList());
	}

	@Override
	protected ItemList<MutatiesInkoopOrder> uiToModel(List<? extends MutatiesModel> list) {
		return list.stream()
			.map(item -> new MutatiesInkoopOrder(
				item.getOmschrijving(),
				item.getOrdernummer(),
				new Geld(item.getPrijs())
			))
			.collect(Collectors.toCollection(ItemList::new));
	}
}

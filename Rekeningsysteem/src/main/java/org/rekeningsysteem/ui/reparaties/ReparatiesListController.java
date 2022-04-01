package org.rekeningsysteem.ui.reparaties;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.list.AbstractListController;
import org.rekeningsysteem.ui.reparaties.ReparatiesListPane.ReparatiesModel;

public class ReparatiesListController extends AbstractListController<ReparatiesInkoopOrder, ReparatiesModel, ReparatiesInkoopOrderController> {

	public ReparatiesListController(Currency currency) {
		this(currency, new ReparatiesListPane());
	}

	public ReparatiesListController(Currency currency, List<ReparatiesInkoopOrder> input) {
		this(currency);
		this.setData(modelToUI(input));
	}

	public ReparatiesListController(Currency currency, ReparatiesListPane ui) {
		super(currency, ui, ReparatiesInkoopOrderController::new);
	}

	@Override
	protected List<ReparatiesModel> modelToUI(List<ReparatiesInkoopOrder> list) {
		return list.stream()
			.map(item -> new ReparatiesModel(
				item.getOmschrijving(),
				item.getInkoopOrderNummer(),
				item.getLoon().getBedrag(),
				item.getMateriaal().getBedrag()
			))
			.collect(Collectors.toList());
	}

	@Override
	protected ItemList<ReparatiesInkoopOrder> uiToModel(List<? extends ReparatiesModel> list) {
		return list.stream()
			.map(item -> new ReparatiesInkoopOrder(
				item.getOmschrijving(),
				item.getOrdernummer(),
				new Geld(item.getLoon()),
				new Geld(item.getMateriaal())
			))
			.collect(Collectors.toCollection(ItemList::new));
	}
}

package org.rekeningsysteem.ui.reparaties;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.list.AbstractListController;
import org.rekeningsysteem.ui.reparaties.ReparatiesListPane.ReparatiesModel;

public class ReparatiesListController extends AbstractListController<ReparatiesInkoopOrder, ReparatiesModel, ReparatiesInkoopOrderController> {

	public ReparatiesListController(Currency currency) {
		this(currency, new ReparatiesListPane());
	}

	public ReparatiesListController(ItemList<ReparatiesInkoopOrder> input) {
		this(input.getCurrency(), new ReparatiesListPane());
		this.setData(modelToUI(input.stream()));
	}

	public ReparatiesListController(Currency currency, ReparatiesListPane ui) {
		super(ui, () -> new ReparatiesInkoopOrderController(currency));
	}

	@Override
	protected List<ReparatiesModel> modelToUI(Stream<ReparatiesInkoopOrder> stream) {
		return stream
			.map(item -> new ReparatiesModel(
				item.omschrijving(),
				item.inkoopOrderNummer(),
				item.loon().bedrag(),
				item.materiaal().bedrag()
			))
			.collect(Collectors.toList());
	}

	@Override
	protected List<ReparatiesInkoopOrder> uiToModel(List<? extends ReparatiesModel> list) {
		return list.stream()
			.map(item -> new ReparatiesInkoopOrder(
				item.getOmschrijving(),
				item.getOrdernummer(),
				new Geld(item.getLoon()),
				new Geld(item.getMateriaal())
			))
			.collect(Collectors.toList());
	}
}

package org.rekeningsysteem.ui.mutaties;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.list.AbstractListController;
import org.rekeningsysteem.ui.mutaties.MutatiesListPane.MutatiesModel;

public class MutatiesListController extends AbstractListController<MutatiesInkoopOrder, MutatiesModel, MutatiesInkoopOrderController> {

	public MutatiesListController(Currency currency) {
		this(currency, new MutatiesListPane());
	}

	public MutatiesListController(ItemList<MutatiesInkoopOrder> input) {
		this(input.getCurrency(), new MutatiesListPane());
		this.setData(modelToUI(input.stream()));
	}

	public MutatiesListController(Currency currency, MutatiesListPane ui) {
		super(ui, () -> new MutatiesInkoopOrderController(currency));
	}

	@Override
	protected List<MutatiesModel> modelToUI(Stream<MutatiesInkoopOrder> stream) {
		return stream
			.map(item -> new MutatiesModel(
				item.omschrijving(),
				item.inkoopOrderNummer(),
				item.materiaal().bedrag()
			))
			.collect(Collectors.toList());
	}

	@Override
	protected List<MutatiesInkoopOrder> uiToModel(List<? extends MutatiesModel> list) {
		return list.stream()
			.map(item -> new MutatiesInkoopOrder(
				item.getOmschrijving(),
				item.getOrdernummer(),
				new Geld(item.getPrijs())
			))
			.collect(Collectors.toList());
	}
}

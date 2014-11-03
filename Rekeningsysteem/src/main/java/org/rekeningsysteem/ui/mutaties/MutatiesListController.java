package org.rekeningsysteem.ui.mutaties;

import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.rekeningsysteem.application.Main;
import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.mutaties.MutatiesListPane.MutatiesModel;

import rx.Observable;

public class MutatiesListController {

	private final MutatiesListPane ui;
	private final Observable<ItemList<MutatiesBon>> model;

	private static final List<MutatiesModel> modelToUI(List<MutatiesBon> list) {
		return list.stream().map(item -> new MutatiesModel(item.getOmschrijving(),
				item.getBonnummer(), item.getMateriaal().getBedrag()))
				.collect(Collectors.toList());
	}

	private static final ItemList<MutatiesBon> uiToModel(List<? extends MutatiesModel> list) {
		return list.stream().map(item -> new MutatiesBon(item.getOmschrijving(),
				item.getBonnummer(), new Geld(item.getPrijs())))
				.collect(Collectors.toCollection(ItemList::new));
	}

	public MutatiesListController(Currency currency) {
		this(currency, new MutatiesListPane());
	}

	public MutatiesListController(Currency currency, List<MutatiesBon> input) {
		this(currency);
		this.ui.setData(modelToUI(input));
	}

	public MutatiesListController(Currency currency, MutatiesListPane ui) {
		this.ui = ui;
		this.model = this.ui.getData().map(MutatiesListController::uiToModel);

		this.ui.getAddButtonEvent()
				.map(event -> currency)
				.map(MutatiesBonController::new)
				.doOnNext(controller -> Main.getMain().showModalMessage(controller.getUI()))
				.flatMap(controller -> controller.getModel())
				.doOnNext(optItem -> Main.getMain().hideModalMessage())
				.filter(Optional::isPresent)
				.flatMap(optItem -> this.model.first()
						.doOnNext(list -> optItem.map(list::add)))
				.map(MutatiesListController::modelToUI)
				.subscribe(this.ui::setData);

		this.ui.getUpButtonEvent()
				.flatMap(index -> this.model.first()
						.doOnNext(list -> Collections.swap(list, index, index - 1)))
				.map(MutatiesListController::modelToUI)
				.subscribe(this.ui::setData);

		this.ui.getDownButtonEvent()
				.flatMap(index -> this.model.first()
						.doOnNext(list -> Collections.swap(list, index, index + 1)))
				.map(MutatiesListController::modelToUI)
				.subscribe(this.ui::setData);
	}

	public MutatiesListPane getUI() {
		return this.ui;
	}

	public Observable<ItemList<MutatiesBon>> getModel() {
		return this.model;
	}
}

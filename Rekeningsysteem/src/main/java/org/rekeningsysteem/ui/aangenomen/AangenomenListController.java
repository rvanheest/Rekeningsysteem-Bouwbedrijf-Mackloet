package org.rekeningsysteem.ui.aangenomen;

import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.rekeningsysteem.application.Main;
import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.aangenomen.AangenomenListPane.AangenomenModel;

import rx.Observable;

public class AangenomenListController {

	private final AangenomenListPane ui;
	private final Observable<ItemList<AangenomenListItem>> model;

	private static final List<AangenomenModel> modelToUI(List<AangenomenListItem> list) {
		return list.stream().map(item -> new AangenomenModel(item.getOmschrijving(),
				item.getLoon().getBedrag(), item.getMateriaal().getBedrag()))
				.collect(Collectors.toList());
	}

	private static final ItemList<AangenomenListItem> uiToModel(List<? extends AangenomenModel> list) {
		return list.stream().map(item -> new AangenomenListItem(item.getOmschrijving(), new Geld(item.getLoon()), new Geld(item.getMateriaal()))).collect(Collectors.toCollection(ItemList::new));
	}

	public AangenomenListController(Currency currency) {
		this(currency, new AangenomenListPane());
	}

	public AangenomenListController(Currency currency, List<AangenomenListItem> input) {
		this(currency);
		this.ui.setData(modelToUI(input));
	}

	public AangenomenListController(Currency currency, AangenomenListPane ui) {
		this.ui = ui;
		this.model = this.ui.getData().map(AangenomenListController::uiToModel);
		
		this.ui.getAddButtonEvent()
				.map(event -> currency)
				.map(AangenomenListItemController::new)
				.doOnNext(controller -> Main.getMain().showModalMessage(controller.getUI()))
				.flatMap(controller -> controller.getModel())
				.doOnNext(optItem -> Main.getMain().hideModalMessage())
				.filter(Optional::isPresent)
				.flatMap(optItem -> this.model.first()
						.doOnNext(list -> optItem.map(list::add)))
				.map(AangenomenListController::modelToUI)
				.subscribe(this.ui::setData);

		this.ui.getUpButtonEvent()
				.flatMap(index -> this.model.first()
						.doOnNext(list -> Collections.swap(list, index, index - 1)))
				.map(AangenomenListController::modelToUI)
				.subscribe(this.ui::setData);

		this.ui.getDownButtonEvent()
				.flatMap(index -> this.model.first()
						.doOnNext(list -> Collections.swap(list, index, index + 1)))
				.map(AangenomenListController::modelToUI)
				.subscribe(this.ui::setData);
	}

	public AangenomenListPane getUI() {
		return this.ui;
	}

	public Observable<ItemList<AangenomenListItem>> getModel() {
		return this.model;
	}
}

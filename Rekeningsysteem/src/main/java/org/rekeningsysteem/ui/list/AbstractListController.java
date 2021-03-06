package org.rekeningsysteem.ui.list;

import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import org.rekeningsysteem.application.Main;
import org.rekeningsysteem.data.util.BtwPercentages;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.io.database.Database;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;

public abstract class AbstractListController<M extends ListItem, U> {

	private final AbstractListPane<U> ui;
	private final Observable<ItemList<M>> model;

	public AbstractListController(Currency currency, AbstractListPane<U> ui,
			Func1<Currency, AbstractListItemController<M>> func) {
		this(ui, ui.getAddButtonEvent().map(e -> func.call(currency)));
	}

	public AbstractListController(Currency currency, BtwPercentages defaultBtw,
			AbstractListPane<U> ui,
			Func2<Currency, BtwPercentages, AbstractListItemController<M>> func) {
		this(ui, ui.getAddButtonEvent().map(e -> func.call(currency, defaultBtw)));
	}

	public AbstractListController(Currency currency, Database db,
			BtwPercentages defaultBtw, AbstractListPane<U> ui,
			Func3<Currency, Database, BtwPercentages, AbstractListItemController<M>> func) {
		this(ui, ui.getAddButtonEvent().map(e -> func.call(currency, db, defaultBtw)));
	}

	private AbstractListController(AbstractListPane<U> ui,
			Observable<? extends AbstractListItemController<? extends M>> listItemController) {
		this.ui = ui;
		this.model = this.ui.getData().map(this::uiToModel);

		listItemController
				.doOnNext(controller -> Main.getMain().showModalMessage(controller.getUI()))
				.flatMap(controller -> controller.getModel())
				.doOnNext(optItem -> Main.getMain().hideModalMessage())
				.filter(Optional::isPresent)
				.flatMap(optItem -> this.getModel().first()
						.doOnNext(list -> optItem.map(list::add)))
				.map(this::modelToUI)
				.subscribe(this.ui::setData);

		this.ui.getUpButtonEvent()
				.flatMap(index -> this.model.first()
						.doOnNext(list -> Collections.swap(list, index, index - 1)))
				.map(this::modelToUI)
				.subscribe(this.ui::setData);

		this.ui.getDownButtonEvent()
				.flatMap(index -> this.model.first()
						.doOnNext(list -> Collections.swap(list, index, index + 1)))
				.map(this::modelToUI)
				.subscribe(this.ui::setData);
	}

	protected abstract List<U> modelToUI(List<M> list);

	protected abstract ItemList<M> uiToModel(List<? extends U> list);

	public AbstractListPane<U> getUI() {
		return this.ui;
	}

	public Observable<ItemList<M>> getModel() {
		return this.model;
	}
}

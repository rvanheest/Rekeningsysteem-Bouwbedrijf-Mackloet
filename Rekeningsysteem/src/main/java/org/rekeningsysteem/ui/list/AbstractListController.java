package org.rekeningsysteem.ui.list;

import java.util.Collections;
import java.util.Currency;
import java.util.List;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Function3;
import org.rekeningsysteem.application.Main;
import org.rekeningsysteem.data.util.BtwPercentages;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.io.database.Database;

public abstract class AbstractListController<M extends ListItem, U, C extends AbstractListItemController<M, ?>> implements Disposable {

	private final AbstractListPane<U> ui;
	private final Observable<ItemList<M>> model;
	private final CompositeDisposable disposable = new CompositeDisposable();

	public AbstractListController(
		Currency currency,
		AbstractListPane<U> ui,
		Function<Currency, C> func
	) {
		this(ui, ui.getAddButtonEvent().map(e -> func.apply(currency)));
	}

	public AbstractListController(
		Currency currency,
		Database db,
		BtwPercentages defaultBtw,
		AbstractListPane<U> ui,
		Function3<Currency, Database, BtwPercentages, C> func
	) {
		this(ui, ui.getAddButtonEvent().map(e -> func.apply(currency, db, defaultBtw)));
	}

	private AbstractListController(AbstractListPane<U> ui, Observable<? extends C> listItemController) {
		this.ui = ui;
		this.model = this.ui.getData().map(this::uiToModel);

		this.disposable.addAll(
			listItemController
				.doOnNext(C::showModalMessage)
				.flatMapMaybe(c -> c.getModel().doOnSuccess(m -> {
					c.dispose();
					Main.getMain().hideModalMessage();
				}))
				.flatMapMaybe(Maybe::fromOptional)
				.flatMapMaybe(model -> this.getModel().firstElement().doOnSuccess(list -> list.add(model)))
				.map(this::modelToUI)
				.subscribe(this.ui::setData),

			this.ui.getUpButtonEvent()
				.flatMapMaybe(index -> this.model.firstElement().doOnSuccess(list -> Collections.swap(list, index, index - 1)))
				.map(this::modelToUI)
				.subscribe(this.ui::setData),

			this.ui.getDownButtonEvent()
				.flatMapMaybe(index -> this.model.firstElement().doOnSuccess(list -> Collections.swap(list, index, index + 1)))
				.map(this::modelToUI)
				.subscribe(this.ui::setData),

			this.ui
		);
	}

	protected abstract List<U> modelToUI(List<M> list);

	protected abstract ItemList<M> uiToModel(List<? extends U> list);

	public AbstractListPane<U> getUI() {
		return this.ui;
	}

	public Observable<ItemList<M>> getModel() {
		return this.model;
	}

	protected void setData(List<U> us) {
		this.ui.setData(us);
	}

	@Override
	public boolean isDisposed() {
		return this.disposable.isDisposed();
	}

	@Override
	public void dispose() {
		this.disposable.dispose();
	}
}

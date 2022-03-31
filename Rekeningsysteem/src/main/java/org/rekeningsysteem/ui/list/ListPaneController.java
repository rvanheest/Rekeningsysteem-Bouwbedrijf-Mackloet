package org.rekeningsysteem.ui.list;

import java.util.Currency;
import java.util.function.Function;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.ListItem;

public class ListPaneController<M extends ListItem> implements Disposable {

	private final Currency currency;
	private final Observable<ItemList<M>> listModel;
	private final BtwListPane ui;

	public <U> ListPaneController(AbstractListController<M, U, ?> list, Currency currency) {
		this(list, BtwListPane::new, currency);
	}

	public <U> ListPaneController(
		AbstractListController<M, U, ?> subController,
		Function<AbstractListPane<U>, BtwListPane> uiFactory,
		Currency currency
	) {
		this.currency = currency;
		this.listModel = subController.getModel();
		this.ui = uiFactory.apply(subController.getUI());
	}

	public Currency getCurrency() {
		return this.currency;
	}

	public Observable<ItemList<M>> getListModel() {
		return this.listModel;
	}

	public BtwListPane getUI() {
		return this.ui;
	}

	@Override
	public boolean isDisposed() {
		return this.ui.isDisposed();
	}

	@Override
	public void dispose() {
		this.ui.dispose();
	}
}

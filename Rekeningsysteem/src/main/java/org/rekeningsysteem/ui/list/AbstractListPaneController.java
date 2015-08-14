package org.rekeningsysteem.ui.list;

import java.util.Currency;
import java.util.function.Function;

import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.ui.WorkingPane;
import org.rekeningsysteem.ui.WorkingPaneController;

import rx.Observable;

public abstract class AbstractListPaneController<M extends ListItem> extends WorkingPaneController {

	private final Currency currency;
	private final Observable<ItemList<M>> listModel;

	public AbstractListPaneController(AbstractListController<M, ?> list, Currency currency) {
		this(list, BtwListPane::new, currency);
	}

	public <U> AbstractListPaneController(AbstractListController<M, U> subController,
			Function<AbstractListPane<U>, WorkingPane> uiFactory, Currency currency) {
		super(uiFactory.apply(subController.getUI()));
		this.currency = currency;
		this.listModel = subController.getModel();
	}

	public Currency getCurrency() {
		return this.currency;
	}

	public Observable<ItemList<M>> getListModel() {
		return this.listModel;
	}
}

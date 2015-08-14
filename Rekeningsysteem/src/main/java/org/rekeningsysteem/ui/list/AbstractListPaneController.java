package org.rekeningsysteem.ui.list;

import java.util.Currency;

import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.ui.WorkingPaneController;

import rx.Observable;

public abstract class AbstractListPaneController<M extends ListItem> extends WorkingPaneController {

	private final Currency currency;
	private final Observable<ItemList<M>> listModel;

	public AbstractListPaneController(AbstractListController<M, ?> list, Currency currency) {
		super(new BtwListPane(list.getUI()));
		this.currency = currency;
		this.listModel = list.getModel();
	}

	public Currency getCurrency() {
		return this.currency;
	}

	public Observable<ItemList<M>> getListModel() {
		return this.listModel;
	}
}

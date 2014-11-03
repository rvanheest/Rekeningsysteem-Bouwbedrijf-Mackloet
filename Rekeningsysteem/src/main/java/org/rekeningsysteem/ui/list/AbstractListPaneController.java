package org.rekeningsysteem.ui.list;

import java.util.Currency;

import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.ui.WorkingPaneController;
import org.rekeningsysteem.ui.btw.BtwController;

import rx.Observable;

public abstract class AbstractListPaneController<M extends ListItem> extends WorkingPaneController {

	private final Currency currency;
	private final Observable<ItemList<M>> listModel;
	private final Observable<BtwPercentage> btwModel;

	public AbstractListPaneController(AbstractListController<M, ?> list, BtwController btw,
			Currency currency) {
		super(new ListPane(list.getUI(), btw.getUI()));
		this.currency = currency;
		this.listModel = list.getModel();
		this.btwModel = btw.getModel();
	}

	public Currency getCurrency() {
		return this.currency;
	}

	public Observable<ItemList<M>> getListModel() {
		return this.listModel;
	}

	public Observable<BtwPercentage> getBtwModel() {
		return this.btwModel;
	}
}

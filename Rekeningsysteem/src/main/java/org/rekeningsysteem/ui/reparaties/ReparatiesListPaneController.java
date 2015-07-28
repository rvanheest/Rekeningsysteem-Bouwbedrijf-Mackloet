package org.rekeningsysteem.ui.reparaties;

import java.util.Currency;

import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.WorkingPaneController;
import org.rekeningsysteem.ui.list.ListPane;

import rx.Observable;

public class ReparatiesListPaneController extends WorkingPaneController {

	private final Currency currency;
	private final Observable<ItemList<ReparatiesBon>> listModel;

	public ReparatiesListPaneController(Currency currency) {
		this(new ReparatiesListController(currency), currency);
	}

	public ReparatiesListPaneController(Currency currency, ItemList<ReparatiesBon> inputList) {
		this(new ReparatiesListController(currency, inputList), currency);
	}

	public ReparatiesListPaneController(ReparatiesListController list, Currency currency) {
		super(new ListPane(list.getUI()));
		this.currency = currency;
		this.listModel = list.getModel();
	}

	public Currency getCurrency() {
		return this.currency;
	}

	public Observable<ItemList<ReparatiesBon>> getListModel() {
		return this.listModel;
	}
}

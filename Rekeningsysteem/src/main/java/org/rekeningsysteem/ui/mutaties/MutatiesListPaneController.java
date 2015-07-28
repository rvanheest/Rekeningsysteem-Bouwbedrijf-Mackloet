package org.rekeningsysteem.ui.mutaties;

import java.util.Currency;

import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.WorkingPaneController;
import org.rekeningsysteem.ui.list.ListPane;

import rx.Observable;

public class MutatiesListPaneController extends WorkingPaneController {

	private final Currency currency;
	private final Observable<ItemList<MutatiesBon>> listModel;

	public MutatiesListPaneController(Currency currency) {
		this(new MutatiesListController(currency), currency);
	}

	public MutatiesListPaneController(Currency currency, ItemList<MutatiesBon> inputList) {
		this(new MutatiesListController(currency, inputList), currency);
	}

	public MutatiesListPaneController(MutatiesListController list, Currency currency) {
		super(new ListPane(list.getUI()));
		this.currency = currency;
		this.listModel = list.getModel();
	}

	public Currency getCurrency() {
		return this.currency;
	}

	public Observable<ItemList<MutatiesBon>> getListModel() {
		return this.listModel;
	}
}

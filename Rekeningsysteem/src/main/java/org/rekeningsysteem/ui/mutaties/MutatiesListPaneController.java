package org.rekeningsysteem.ui.mutaties;

import java.util.Currency;

import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.WorkingPaneController;
import org.rekeningsysteem.ui.btw.BtwController;
import org.rekeningsysteem.ui.list.ListPane;

import rx.Observable;

public class MutatiesListPaneController extends WorkingPaneController {

	private final Currency currency;
	private final Observable<ItemList<MutatiesBon>> listModel;
	private final Observable<BtwPercentage> btwModel;

	public MutatiesListPaneController(Currency currency, BtwPercentage btw) {
		this(new MutatiesListController(currency), new BtwController(btw), currency);
	}

	public MutatiesListPaneController(Currency currency, ItemList<MutatiesBon> inputList,
			BtwPercentage inputBtw) {
		this(new MutatiesListController(currency, inputList), new BtwController(inputBtw), currency);
	}

	public MutatiesListPaneController(MutatiesListController list, BtwController btw, Currency currency) {
		super(new ListPane(list.getUI(), btw.getUI()));
		this.currency = currency;
		this.listModel = list.getModel();
		this.btwModel = btw.getModel();
	}

	public Currency getCurrency() {
		return this.currency;
	}

	public Observable<ItemList<MutatiesBon>> getListModel() {
		return this.listModel;
	}

	public Observable<BtwPercentage> getBtwModel() {
		return this.btwModel;
	}
}

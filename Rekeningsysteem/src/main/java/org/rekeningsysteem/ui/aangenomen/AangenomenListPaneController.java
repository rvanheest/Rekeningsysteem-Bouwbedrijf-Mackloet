package org.rekeningsysteem.ui.aangenomen;

import java.util.Currency;

import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.WorkingPaneController;
import org.rekeningsysteem.ui.btw.BtwController;
import org.rekeningsysteem.ui.list.ListPane;

import rx.Observable;

public class AangenomenListPaneController extends WorkingPaneController {

	private final Currency currency;
	private final Observable<ItemList<AangenomenListItem>> listModel;
	private final Observable<BtwPercentage> btwModel;

	public AangenomenListPaneController(Currency currency, BtwPercentage btw) {
		this(new AangenomenListController(currency), new BtwController(btw), currency);
	}

	public AangenomenListPaneController(Currency currency, ItemList<AangenomenListItem> inputList,
			BtwPercentage inputBtw) {
		this(new AangenomenListController(currency, inputList), new BtwController(inputBtw), currency);
	}

	public AangenomenListPaneController(AangenomenListController list, BtwController btw, Currency currency) {
		super(new ListPane(list.getUI(), btw.getUI()));
		this.currency = currency;
		this.listModel = list.getModel();
		this.btwModel = btw.getModel();
	}

	public Currency getCurrency() {
		return this.currency;
	}

	public Observable<ItemList<AangenomenListItem>> getListModel() {
		return this.listModel;
	}

	public Observable<BtwPercentage> getBtwModel() {
		return this.btwModel;
	}
}

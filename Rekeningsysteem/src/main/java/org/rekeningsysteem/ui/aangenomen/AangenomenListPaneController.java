package org.rekeningsysteem.ui.aangenomen;

import java.util.Currency;

import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.WorkingPaneController;
import org.rekeningsysteem.ui.list.BtwListPane;

import rx.Observable;

public class AangenomenListPaneController extends WorkingPaneController {

	private final Currency currency;
	private final Observable<ItemList<AangenomenListItem>> listModel;

	public AangenomenListPaneController(Currency currency, BtwPercentage defaultBtw) {
		this(new AangenomenListController(currency, defaultBtw), currency);
	}

	public AangenomenListPaneController(Currency currency, BtwPercentage defaultBtw,
			ItemList<AangenomenListItem> inputList) {
		this(new AangenomenListController(currency, defaultBtw, inputList), currency);
	}

	public AangenomenListPaneController(AangenomenListController list, Currency currency) {
		super(new BtwListPane(list.getUI()));
		this.currency = currency;
		this.listModel = list.getModel();
	}

	public Currency getCurrency() {
		return this.currency;
	}

	public Observable<ItemList<AangenomenListItem>> getListModel() {
		return this.listModel;
	}
}

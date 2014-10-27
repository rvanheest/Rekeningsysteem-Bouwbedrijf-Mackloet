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

	private final Observable<Currency> currency;
	private final Observable<ItemList<AangenomenListItem>> listModel;
	private final Observable<BtwPercentage> btwModel;

	public AangenomenListPaneController(Observable<Currency> currency) {
		this(new AangenomenListController(currency), new BtwController(), currency);
	}

	public AangenomenListPaneController(Observable<Currency> currency,
			Observable<ItemList<AangenomenListItem>> inputList,
			Observable<BtwPercentage> inputBtw) {
		this(new AangenomenListController(currency), new BtwController(), currency, inputList, inputBtw);
	}

	public AangenomenListPaneController(AangenomenListController list, BtwController btw, Observable<Currency> currency) {
		this(list, btw, currency, Observable.empty(), Observable.empty());
	}

	public AangenomenListPaneController(AangenomenListController list, BtwController btw,
			Observable<Currency> currency,
			Observable<ItemList<AangenomenListItem>> inputList,
			Observable<BtwPercentage> inputBtw) {
		super(new ListPane(list.getUI(), btw.getUI()));
		this.currency = currency;
		this.listModel = list.getModel();
		this.btwModel = btw.getModel();

		inputList.subscribe(list);
		inputBtw.subscribe(btw);
	}

	public Observable<Currency> getCurrency() {
		return this.currency;
	}

	public Observable<ItemList<AangenomenListItem>> getListModel() {
		return this.listModel;
	}

	public Observable<BtwPercentage> getBtwModel() {
		return this.btwModel;
	}
}

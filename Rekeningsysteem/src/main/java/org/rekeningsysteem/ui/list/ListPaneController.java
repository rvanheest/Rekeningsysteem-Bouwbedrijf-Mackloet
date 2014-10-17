package org.rekeningsysteem.ui.list;

import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.WorkingPaneController;
import org.rekeningsysteem.ui.aangenomen.AangenomenListController;
import org.rekeningsysteem.ui.btw.BtwController;

import rx.Observable;

import com.google.inject.Inject;

public class ListPaneController extends WorkingPaneController {

	private final Observable<ItemList<AangenomenListItem>> listModel;
	private final Observable<BtwPercentage> btwModel;

	@Inject
	public ListPaneController(AangenomenListController list, BtwController btw) {
		this(list, btw, Observable.empty(), Observable.empty());
	}

	public ListPaneController(AangenomenListController list, BtwController btw,
			Observable<ItemList<AangenomenListItem>> inputList,
			Observable<BtwPercentage> inputBtw) {
		super(new ListPane(list.getUI(), btw.getUI()));
		this.listModel = list.getModel();
		this.btwModel = btw.getModel();
		
		inputList.subscribe(list);
		inputBtw.subscribe(btw);
	}

	public Observable<ItemList<AangenomenListItem>> getListModel() {
		return this.listModel;
	}

	public Observable<BtwPercentage> getBtwModel() {
		return this.btwModel;
	}
}

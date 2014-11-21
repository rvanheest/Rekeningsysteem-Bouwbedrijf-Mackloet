package org.rekeningsysteem.ui.particulier.tabpane;

import org.rekeningsysteem.rxjavafx.Observables;

import rx.Observable;
import rx.functions.Func1;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class ItemTabPane extends TabPane {

	private final Observable<ItemType> type;

	public ItemTabPane(Func1<String, ItemType> func) {
		super();
		this.type = Observables.fromProperty(this.getSelectionModel().selectedItemProperty())
				.map(Tab::getText)
				.map(func);
	}
	
	public void add(ItemType tabName, Node content) {
		Tab tab = new Tab(tabName.getTabName());
		tab.setContent(content);
		this.getTabs().add(tab);
	}
	
	public Observable<? extends ItemType> getType() {
		return this.type;
	}
}

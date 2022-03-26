package org.rekeningsysteem.ui.particulier.tabpane;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import org.rekeningsysteem.rxjavafx.Observables;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class ItemTabPane extends TabPane {

	private final Observable<ItemType> type;

	public ItemTabPane(Function<String, ItemType> func) {
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

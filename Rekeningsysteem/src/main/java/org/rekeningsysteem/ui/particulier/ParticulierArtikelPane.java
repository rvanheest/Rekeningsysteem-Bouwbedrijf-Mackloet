package org.rekeningsysteem.ui.particulier;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.control.TabPane.TabClosingPolicy;

import org.rekeningsysteem.ui.list.ItemPane;
import org.rekeningsysteem.ui.particulier.tabpane.ItemTabPane;

import rx.Observable;

public class ParticulierArtikelPane extends ItemPane {

	private final ItemTabPane content;

	private final Map<String, ParticulierArtikelType> lookupTable = new HashMap<>();
	private final Observable<ParticulierArtikelType> type;

	public ParticulierArtikelPane(Currency currency) {
		super("Nieuw particulier artikel");

		this.content = new ItemTabPane(this.lookupTable::get);
		this.content.setId("particulier-tabs");
		this.content.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		this.type = this.content.getType().cast(ParticulierArtikelType.class);

		this.getChildren().add(1, this.content);
	}

	public void addContent(ParticulierArtikelType tabName, Node content) {
		this.lookupTable.put(tabName.getTabName(), tabName);
		this.content.add(tabName, content);
	}

	public Observable<ParticulierArtikelType> getType() {
		return this.type;
	}
}

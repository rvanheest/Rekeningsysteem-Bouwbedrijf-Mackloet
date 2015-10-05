package org.rekeningsysteem.ui.particulier;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.control.TabPane.TabClosingPolicy;

import org.rekeningsysteem.ui.list.ItemPane;
import org.rekeningsysteem.ui.particulier.tabpane.ItemTabPane;

import rx.Observable;

@Deprecated
public class ParticulierArtikelPane extends ItemPane {

	private final ItemTabPane content;

	private Map<String, ParticulierArtikelType> lookupTable = new HashMap<>();
	private Observable<ParticulierArtikelType> type;

	public ParticulierArtikelPane(Currency currency) {
		super("Nieuw particulier artikel");
		this.content = this.getContent();

		this.getChildren().add(1, this.content);
	}

	private ItemTabPane getContent() {
		ItemTabPane content = new ItemTabPane(this.lookupTable::get);
		content.setId("particulier-tabs");
		content.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		this.type = content.getType().cast(ParticulierArtikelType.class);

		return content;
	}

	public void addContent(ParticulierArtikelType tabName, Node content) {
		this.lookupTable.put(tabName.getTabName(), tabName);
		this.content.add(tabName, content);
	}

	public Observable<ParticulierArtikelType> getType() {
		return this.type;
	}
}

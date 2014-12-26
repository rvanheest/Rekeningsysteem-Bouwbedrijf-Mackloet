package org.rekeningsysteem.ui.particulier;

import java.util.Currency;

import javafx.scene.Node;
import javafx.scene.control.TabPane.TabClosingPolicy;

import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.ui.list.ItemPane;
import org.rekeningsysteem.ui.particulier.tabpane.ItemTabPane;

import rx.Observable;
 
public class ParticulierArtikelPane extends ItemPane {

	private final AnderArtikelController anderController;
	private final GebruiktEsselinkArtikelController gebruiktController;
	
	private Observable<ParticulierArtikelType> type;

	public ParticulierArtikelPane(Currency currency) {
		super("Nieuw particulier artikel");
		this.anderController = new AnderArtikelController(currency);
		this.gebruiktController = new GebruiktEsselinkArtikelController(currency);
		
		this.getChildren().add(1, this.getContent());
	}

	private Node getContent() {
		ItemTabPane content = new ItemTabPane(text -> {
			if (text.equals(ParticulierArtikelType.ANDER.getTabName())) {
				return ParticulierArtikelType.ANDER;
			}
			else if (text.equals(ParticulierArtikelType.ESSELINK.getTabName())) {
				return ParticulierArtikelType.ESSELINK;
			}
			else {
				return null;
			}
		});
		
		content.setId("particulier-tabs");
		content.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		content.add(ParticulierArtikelType.ESSELINK, this.gebruiktController.getUI());
		content.add(ParticulierArtikelType.ANDER, this.anderController.getUI());
		
		this.type = content.getType().cast(ParticulierArtikelType.class);

		return content;
	}

	public Observable<ParticulierArtikelType> getType() {
		return this.type;
	}

	public Observable<AnderArtikel> getAnderArtikel() {
		return this.anderController.getModel();
	}
	
	public void setAnderArtikel(AnderArtikel ander) {
		this.anderController.getUI().setOmschrijving(ander.getOmschrijving());
		this.anderController.getUI().setPrijs(ander.getMateriaal().getBedrag());
	}

	public Observable<GebruiktEsselinkArtikel> getGebruiktEsselinkArtikel() {
		return this.gebruiktController.getModel();
	}
}

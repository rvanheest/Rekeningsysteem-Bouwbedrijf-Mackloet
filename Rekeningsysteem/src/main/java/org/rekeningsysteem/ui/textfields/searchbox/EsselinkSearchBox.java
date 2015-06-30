package org.rekeningsysteem.ui.textfields.searchbox;

import java.util.Currency;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import org.rekeningsysteem.data.particulier.EsselinkArtikel;

public class EsselinkSearchBox extends AbstractSearchBox<EsselinkArtikel> {

	private final Label infoOmschrijving = new Label();
	private final Label infoArtikelNummer = new Label();
	private final Label infoPrijsEenheid = new Label();
	
	private final Currency currency;

	public EsselinkSearchBox(Currency currency) {
		super("Zoek artikel...");
		this.currency = currency;
		this.setId("esselinkSearchBox");
		
		this.infoBox.setId("search-info-box");
		this.infoBox.setFillWidth(true);
		this.infoBox.setMinWidth(Region.USE_PREF_SIZE);
		this.infoBox.setPrefWidth(200);
		this.infoBox.getChildren().addAll(this.infoOmschrijving,
				this.infoArtikelNummer, this.infoPrijsEenheid);
		
		this.infoOmschrijving.setId("search-info-name");
		this.infoOmschrijving.setMinHeight(Region.USE_PREF_SIZE);
		this.infoOmschrijving.setPrefHeight(28);
		
		this.infoArtikelNummer.setId("search-info-description");
		this.infoArtikelNummer.setWrapText(true);
		this.infoArtikelNummer.setPrefWidth(this.infoBox.getPrefWidth() - 24);

		this.infoPrijsEenheid.setId("search-info-description");
		this.infoPrijsEenheid.setWrapText(true);
		this.infoPrijsEenheid.setPrefWidth(this.infoBox.getPrefWidth() - 24);
	}
	
	@Override
	void setTextfields(EsselinkArtikel ea) {
		this.infoOmschrijving.setText(ea.getOmschrijving());
		this.infoArtikelNummer.setText("Artikelnummer: " + ea.getArtikelNummer());
		this.infoPrijsEenheid.setText(this.currency.getSymbol() + " "
				+ ea.getVerkoopPrijs().getBedrag() + " per "
				+ ea.getPrijsPer() + " " + ea.getEenheid());
	}
	
	@Override
	HBox getHBox(EsselinkArtikel ea) {
		Label artNrLabel = new Label(ea.getArtikelNummer());
		artNrLabel.getStyleClass().add("artikelnummer-label");
        artNrLabel.setAlignment(Pos.CENTER_RIGHT);
        artNrLabel.setMinWidth(USE_PREF_SIZE);
        artNrLabel.setPrefWidth(70);
        
		Label omschrLabel = new Label(ea.getOmschrijving());
		
		return new HBox(artNrLabel, omschrLabel);
	}
}

package org.rekeningsysteem.ui.textfields.searchbox;

import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import org.rekeningsysteem.data.util.header.Debiteur;

public class DebiteurSearchBox extends AbstractSearchBox<Debiteur> {

	private final Label infoNaam = new Label();
	private final Label infoAdres = new Label();
	private final Label infoPostcodePlaats = new Label();
	private final Label infoBtwNummer = new Label();

	public DebiteurSearchBox() {
		super("Zoek debiteur...");

		this.infoBox.setId("search-info-box");
		this.infoBox.setFillWidth(true);
		this.infoBox.setMinWidth(Region.USE_PREF_SIZE);
		this.infoBox.setPrefWidth(200);
		this.infoBox.getChildren().addAll(this.infoNaam, this.infoAdres, this.infoPostcodePlaats);

		this.infoNaam.setId("search-info-name");
		this.infoNaam.setMinHeight(Region.USE_PREF_SIZE);
		this.infoNaam.setPrefHeight(28);

		this.infoAdres.setId("search-info-description");
		this.infoAdres.setWrapText(true);
		this.infoAdres.setPrefWidth(this.infoBox.getPrefWidth() - 24);

		this.infoPostcodePlaats.setId("search-info-description");
		this.infoPostcodePlaats.setWrapText(true);
		this.infoPostcodePlaats.setPrefWidth(this.infoBox.getPrefWidth() - 24);

		this.infoBtwNummer.setId("search-info-description");
		this.infoBtwNummer.setWrapText(true);
		this.infoBtwNummer.setPrefWidth(this.infoBox.getPrefWidth() - 24);
	}

	@Override
	void setTextfields(Debiteur debiteur) {
		Optional<String> btwNummer = debiteur.getBtwNummer().map(s -> "BTW nummer: " + s);

		this.infoNaam.setText(debiteur.getNaam());
		this.infoAdres.setText(debiteur.getStraat() + " " + debiteur.getNummer());
		this.infoPostcodePlaats.setText(debiteur.getPostcode() + "  " + debiteur.getPlaats().toUpperCase());
		this.infoBtwNummer.setText(btwNummer.orElse(""));

		ObservableList<Node> infoBoxChildren = this.infoBox.getChildren();
		if (btwNummer.isPresent()) {
			if (!infoBoxChildren.contains(this.infoBtwNummer)) {
				infoBoxChildren.add(this.infoBtwNummer);
			}
		}
		else {
			infoBoxChildren.remove(this.infoBtwNummer);
		}
	}

	@Override
	HBox getHBox(Debiteur debiteur) {
		return new HBox(new Label(debiteur.getNaam()));
	}
}

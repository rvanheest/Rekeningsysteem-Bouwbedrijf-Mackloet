package org.rekeningsysteem.ui.aangenomen;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.list.ItemPane;
import org.rekeningsysteem.ui.textfields.MoneyField;

import com.google.inject.Inject;

import rx.Observable;

public class AangenomenListItemPane extends ItemPane {

	private final TextField omschrTF = new TextField();
	private final MoneyField loonTF;
	private final MoneyField materiaalTF;

	private final Observable<Double> loon;
	private final Observable<Double> materiaal;

	@Inject
	public AangenomenListItemPane(Currency currency) {
		super("Nieuw artikel");
		this.loonTF = new MoneyField(currency);
		this.materiaalTF = new MoneyField(currency);
		
		this.loon = Observables
				.fromProperty(this.loonTF.valueProperty())
				.filter(Objects::nonNull)
				.map(BigDecimal::doubleValue);
		
		this.materiaal = Observables
				.fromProperty(this.materiaalTF.valueProperty())
				.filter(Objects::nonNull)
				.map(BigDecimal::doubleValue);

		this.getChildren().add(1, this.getContent());
	}

	private Node getContent() {
		GridPane content = new GridPane();
		content.setPadding(new Insets(8));
		content.setHgap(10);
		content.setVgap(5);
		content.setAlignment(Pos.CENTER);

		Label omschrL = new Label("Omschrijving");
		Label loonL = new Label("Loon");
		Label materiaalL = new Label("Materiaal");

		content.add(omschrL, 0, 0);
		content.add(loonL, 0, 1);
		content.add(materiaalL, 0, 2);

		content.add(this.omschrTF, 1, 0);
		content.add(this.loonTF, 1, 1);
		content.add(this.materiaalTF, 1, 2);

		return content;
	}

	public Observable<String> getOmschrijving() {
		return Observables.fromProperty(this.omschrTF.textProperty());
	}

	public void setOmschrijving(String omschrijving) {
		this.omschrTF.setText(omschrijving);
	}

	public Observable<Double> getLoon() {
		return this.loon;
	}

	public void setLoon(Double loon) {
		this.loonTF.setValue(BigDecimal.valueOf(loon));
	}

	public Observable<Double> getMateriaal() {
		return this.materiaal;
	}

	public void setMateriaal(Double materiaal) {
		this.materiaalTF.setValue(BigDecimal.valueOf(materiaal));
	}
}

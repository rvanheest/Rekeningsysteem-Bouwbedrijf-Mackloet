package org.rekeningsysteem.ui.reparaties;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

import io.reactivex.rxjava3.core.Observable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.list.ItemPane;
import org.rekeningsysteem.ui.textfields.MoneyField;

public class ReparatiesInkoopOrderPane extends ItemPane {

	private final TextField omschrTF = new TextField();
	private final TextField ordernrTF = new TextField();
	private final MoneyField loonTF;
	private final MoneyField materiaalTF;

	private final Observable<String> omschrijving;
	private final Observable<String> ordernummer;
	private final Observable<Double> loon;
	private final Observable<Double> materiaal;

	public ReparatiesInkoopOrderPane(Currency currency) {
		super("Nieuwe reparaties inkooporder");
		this.loonTF = new MoneyField(currency);
		this.materiaalTF = new MoneyField(currency);

		this.omschrijving = Observables.fromProperty(this.omschrTF.textProperty());
		this.ordernummer = Observables.fromProperty(this.ordernrTF.textProperty());
		this.loon = Observables.fromProperty(this.loonTF.valueProperty())
				.filter(Objects::nonNull)
				.map(BigDecimal::doubleValue);
		this.materiaal = Observables.fromProperty(this.materiaalTF.valueProperty())
				.filter(Objects::nonNull)
				.map(BigDecimal::doubleValue);
		
		this.omschrTF.setPrefColumnCount(20);

		this.getChildren().add(1, this.getContent());
	}

	private Node getContent() {
		GridPane content = new GridPane();
		content.setPadding(new Insets(8));
		content.setHgap(10);
		content.setVgap(5);
		content.setAlignment(Pos.CENTER);

		Label omschrL = new Label("Omschrijving");
		Label ordernrL = new Label("Ordernummer");
		Label loonL = new Label("Arbeid");
		Label materiaalL = new Label("Materiaal");

		content.add(omschrL, 0, 0);
		content.add(ordernrL, 0, 1);
		content.add(loonL, 0, 2);
		content.add(materiaalL, 0, 3);

		content.add(this.omschrTF, 1, 0);
		content.add(this.ordernrTF, 1, 1);
		content.add(this.loonTF, 1, 2);
		content.add(this.materiaalTF, 1, 3);

		return content;
	}

	public Observable<String> getOmschrijving() {
		return this.omschrijving;
	}

	public void setOmschrijving(String omschrijving) {
		this.omschrTF.setText(omschrijving);
	}

	public Observable<String> getOrdernummer() {
		return this.ordernummer;
	}

	public void setOrdernummer(String ordernummer) {
		this.ordernrTF.setText(ordernummer);
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

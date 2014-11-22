package org.rekeningsysteem.ui.mutaties;

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

import rx.Observable;

public class MutatiesBonPane extends ItemPane {

	private final TextField omschrTF = new TextField();
	private final TextField bonnrTF = new TextField();
	private final MoneyField prijsTF;

	private final Observable<String> omschrijving;
	private final Observable<String> bonnummer;
	private final Observable<Double> prijs;

	public MutatiesBonPane(Currency currency) {
		super("Nieuwe mutaties bon");
		this.prijsTF = new MoneyField(currency);
		
		this.omschrijving = Observables.fromProperty(this.omschrTF.textProperty());
		this.bonnummer = Observables.fromProperty(this.bonnrTF.textProperty());
		this.prijs = Observables.fromProperty(this.prijsTF.valueProperty())
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
		Label bonnrL = new Label("Bonnummer");
		Label prijsL = new Label("Prijs");

		content.add(omschrL, 0, 0);
		content.add(bonnrL, 0, 1);
		content.add(prijsL, 0, 2);

		content.add(this.omschrTF, 1, 0);
		content.add(this.bonnrTF, 1, 1);
		content.add(this.prijsTF, 1, 2);
		
		this.omschrTF.requestFocus();

		return content;
	}

	public Observable<String> getOmschrijving() {
		return this.omschrijving;
	}

	public void setOmschrijving(String omschrijving) {
		this.omschrTF.setText(omschrijving);
	}

	public Observable<String> getBonnummer() {
		return this.bonnummer;
	}

	public void setBonnummer(String bonnummer) {
		this.bonnrTF.setText(bonnummer);
	}

	public Observable<Double> getPrijs() {
		return this.prijs;
	}

	public void setPrijs(Double prijs) {
		this.prijsTF.setValue(BigDecimal.valueOf(prijs));
	}
}

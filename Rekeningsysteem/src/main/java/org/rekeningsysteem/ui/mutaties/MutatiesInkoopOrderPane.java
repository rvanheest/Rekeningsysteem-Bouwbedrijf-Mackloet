package org.rekeningsysteem.ui.mutaties;

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

public class MutatiesInkoopOrderPane extends ItemPane {

	private final TextField omschrTF = new TextField();
	private final TextField ordernrTF = new TextField();
	private final MoneyField prijsTF;

	private final Observable<String> omschrijving;
	private final Observable<String> ordernummer;
	private final Observable<Double> prijs;

	public MutatiesInkoopOrderPane(Currency currency) {
		super("Nieuwe mutaties inkooporder");
		this.prijsTF = new MoneyField(currency);

		this.omschrijving = Observables.fromProperty(this.omschrTF.textProperty());
		this.ordernummer = Observables.fromProperty(this.ordernrTF.textProperty());
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
		Label ordernrL = new Label("Ordernummer");
		Label prijsL = new Label("Prijs");

		content.add(omschrL, 0, 0);
		content.add(ordernrL, 0, 1);
		content.add(prijsL, 0, 2);

		content.add(this.omschrTF, 1, 0);
		content.add(this.ordernrTF, 1, 1);
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

	public Observable<String> getOrdernummer() {
		return this.ordernummer;
	}

	public void setOrdernummer(String ordernummer) {
		this.ordernrTF.setText(ordernummer);
	}

	public Observable<Double> getPrijs() {
		return this.prijs;
	}

	public void setPrijs(Double prijs) {
		this.prijsTF.setValue(BigDecimal.valueOf(prijs));
	}
}

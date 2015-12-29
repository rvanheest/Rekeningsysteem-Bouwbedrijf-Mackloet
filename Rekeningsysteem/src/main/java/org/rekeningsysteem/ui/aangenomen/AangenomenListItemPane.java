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
import org.rekeningsysteem.ui.textfields.PercentageField;

import rx.Observable;

@Deprecated
public class AangenomenListItemPane extends ItemPane {

	private final TextField omschrTF = new TextField();
	private final MoneyField loonTF;
	private final MoneyField materiaalTF;
	private final PercentageField loonBtwTF = new PercentageField();
	private final PercentageField materiaalBtwTF = new PercentageField();

	private final Observable<String> omschrijving;
	private final Observable<Double> loon;
	private final Observable<Double> loonBtwPercentage;
	private final Observable<Double> materiaal;
	private final Observable<Double> materiaalBtwPercentage;

	public AangenomenListItemPane(Currency currency) {
		super("Nieuw artikel");
		this.loonTF = new MoneyField(currency);
		this.materiaalTF = new MoneyField(currency);

		this.omschrijving = Observables.fromProperty(this.omschrTF.textProperty());
		this.loon = Observables
				.fromProperty(this.loonTF.valueProperty())
				.filter(Objects::nonNull)
				.map(BigDecimal::doubleValue);
		this.loonBtwPercentage = Observables.fromProperty(this.loonBtwTF.valueProperty())
				.map(n -> Objects.isNull(n) ? BigDecimal.ZERO : n)
				.map(BigDecimal::doubleValue);
		this.materiaal = Observables
				.fromProperty(this.materiaalTF.valueProperty())
				.filter(Objects::nonNull)
				.map(BigDecimal::doubleValue);
		this.materiaalBtwPercentage = Observables.fromProperty(this.materiaalBtwTF.valueProperty())
				.map(n -> Objects.isNull(n) ? BigDecimal.ZERO : n)
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
		Label loonL = new Label("Arbeid");
		Label materiaalL = new Label("Materiaal");
		Label loonBtwL = new Label("BTW arbeid");
		Label materiaalBtwL = new Label("BTW materiaal");

		content.add(omschrL, 0, 0);
		content.add(loonL, 0, 1);
		content.add(materiaalL, 0, 2);
		content.add(loonBtwL, 0, 3);
		content.add(materiaalBtwL, 0, 4);

		content.add(this.omschrTF, 1, 0);
		content.add(this.loonTF, 1, 1);
		content.add(this.materiaalTF, 1, 2);
		content.add(this.loonBtwTF, 1, 3);
		content.add(this.materiaalBtwTF, 1, 4);

		return content;
	}

	public Observable<String> getOmschrijving() {
		return this.omschrijving;
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

	public Observable<Double> getLoonBtwPercentage() {
		return this.loonBtwPercentage;
	}

	public void setLoonBtwPercentage(Double loonBtwPercentage) {
		this.loonBtwTF.setValue(BigDecimal.valueOf(loonBtwPercentage));
	}

	public Observable<Double> getMateriaalBtwPercentage() {
		return this.materiaalBtwPercentage;
	}

	public void setMateriaalBtwPercentage(Double materiaalBtwPercentage) {
		this.materiaalBtwTF.setValue(BigDecimal.valueOf(materiaalBtwPercentage));
	}
}

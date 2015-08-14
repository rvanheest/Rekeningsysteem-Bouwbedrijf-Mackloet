package org.rekeningsysteem.ui.btw;

import java.math.BigDecimal;
import java.util.Objects;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.Page;
import org.rekeningsysteem.ui.textfields.PercentageField;

import rx.Observable;

@Deprecated
public class BtwPane extends Page {

	private final GridPane grid = new GridPane();

	private final PercentageField loonTF;
	private final PercentageField materTF;

	private final Observable<Double> loon;
	private final Observable<Double> materiaal;

	public BtwPane() {
		super("BTW percentages");

		this.loonTF = new PercentageField();
		this.materTF = new PercentageField();
		
		this.loon = Observables.fromProperty(this.loonTF.valueProperty())
				.map(n -> Objects.isNull(n) ? BigDecimal.ZERO : n)
				.map(BigDecimal::doubleValue);
		this.materiaal = Observables.fromProperty(this.materTF.valueProperty())
				.map(n -> Objects.isNull(n) ? BigDecimal.ZERO : n)
				.map(BigDecimal::doubleValue);

		this.grid.setHgap(10);
		this.grid.setVgap(1);
		this.grid.setAlignment(Pos.TOP_CENTER);

		this.initLabels();
		this.initTextFields();
		
		this.getChildren().add(this.grid);
	}

	private void initLabels() {
		this.grid.add(new Label("Arbeid"), 0, 0);
		this.grid.add(new Label("Materiaal"), 0, 1);
	}

	private void initTextFields() {
		this.grid.add(this.loonTF, 1, 0);
		this.grid.add(this.materTF, 1, 1);
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
		this.materTF.setValue(BigDecimal.valueOf(materiaal));
	}
}

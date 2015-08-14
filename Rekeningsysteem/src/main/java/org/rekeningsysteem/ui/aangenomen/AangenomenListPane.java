package org.rekeningsysteem.ui.aangenomen;

import java.util.Arrays;
import java.util.List;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import org.rekeningsysteem.ui.aangenomen.AangenomenListPane.AangenomenModel;
import org.rekeningsysteem.ui.list.AbstractListPane;
import org.rekeningsysteem.ui.list.MoneyCell;

public class AangenomenListPane extends AbstractListPane<AangenomenModel> {

	public AangenomenListPane() {
		super("Factuurlijst");
	}

	@Override
	protected List<TableColumn<AangenomenModel, ?>> initTableColumns() {
		TableColumn<AangenomenModel, String> omschrCol = new TableColumn<>("Omschrijving");
		TableColumn<AangenomenModel, Double> loonCol = new TableColumn<>("Arbeid");
		TableColumn<AangenomenModel, Double> loonBtwCol = new TableColumn<>("BTW arbeid");
		TableColumn<AangenomenModel, Double> materiaalCol = new TableColumn<>("Materiaal");
		TableColumn<AangenomenModel, Double> materiaalBtwCol = new TableColumn<>("BTW materiaal");

		omschrCol.setMinWidth(300);
		loonCol.setMinWidth(100);
		loonBtwCol.setMinWidth(50);
		materiaalCol.setMinWidth(100);
		materiaalBtwCol.setMinWidth(50);

		omschrCol.setCellValueFactory(new PropertyValueFactory<>("omschrijving"));
		loonCol.setCellValueFactory(new PropertyValueFactory<>("loon"));
		loonBtwCol.setCellValueFactory(new PropertyValueFactory<>("loonBtwPercentage"));
		materiaalCol.setCellValueFactory(new PropertyValueFactory<>("materiaal"));
		materiaalBtwCol.setCellValueFactory(new PropertyValueFactory<>("materiaalBtwPercentage"));

		loonCol.setCellFactory(param -> new MoneyCell<>());
		materiaalCol.setCellFactory(param -> new MoneyCell<>());

		return Arrays.asList(omschrCol, loonCol, loonBtwCol, materiaalCol, materiaalBtwCol, this.getDeleteCol());
	}

	public static class AangenomenModel {

		private String omschrijving;
		private double loon;
		private double loonBtwPercentage;
		private double materiaal;
		private double materiaalBtwPercentage;

		public AangenomenModel(String omschrijving, double loon, double loonBtwPercentage,
				double materiaal, double materiaalBtwPercentage) {
			this.omschrijving = omschrijving;
			this.loon = loon;
			this.loonBtwPercentage = loonBtwPercentage;
			this.materiaal = materiaal;
			this.materiaalBtwPercentage = materiaalBtwPercentage;
		}

		public String getOmschrijving() {
			return this.omschrijving;
		}

		public void setOmschrijving(String omschrijving) {
			this.omschrijving = omschrijving;
		}

		public double getLoon() {
			return this.loon;
		}

		public void setLoon(double loon) {
			this.loon = loon;
		}

		public double getLoonBtwPercentage() {
			return this.loonBtwPercentage;
		}

		public void setLoonBtwPercentage(double loonBtwPercentage) {
			this.loonBtwPercentage = loonBtwPercentage;
		}

		public double getMateriaal() {
			return this.materiaal;
		}

		public void setMateriaal(double materiaal) {
			this.materiaal = materiaal;
		}

		public double getMateriaalBtwPercentage() {
			return this.materiaalBtwPercentage;
		}

		public void setMateriaalBtwPercentage(double materiaalBtwPercentage) {
			this.materiaalBtwPercentage = materiaalBtwPercentage;
		}
	}
}

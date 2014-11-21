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
		TableColumn<AangenomenModel, Double> materiaalCol = new TableColumn<>("Materiaal");

		omschrCol.setMinWidth(300);
		loonCol.setMinWidth(100);
		materiaalCol.setMinWidth(100);

		omschrCol.setCellValueFactory(new PropertyValueFactory<>("omschrijving"));
		loonCol.setCellValueFactory(new PropertyValueFactory<>("loon"));
		materiaalCol.setCellValueFactory(new PropertyValueFactory<>("materiaal"));

		loonCol.setCellFactory(param -> new MoneyCell<>());
		materiaalCol.setCellFactory(param -> new MoneyCell<>());

		return Arrays.asList(omschrCol, loonCol, materiaalCol, this.getDeleteCol());
	}

	public static class AangenomenModel {

		private String omschrijving;
		private double loon;
		private double materiaal;

		public AangenomenModel(String omschrijving, double loon, double materiaal) {
			this.omschrijving = omschrijving;
			this.loon = loon;
			this.materiaal = materiaal;
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

		public double getMateriaal() {
			return this.materiaal;
		}

		public void setMateriaal(double materiaal) {
			this.materiaal = materiaal;
		}
	}
}

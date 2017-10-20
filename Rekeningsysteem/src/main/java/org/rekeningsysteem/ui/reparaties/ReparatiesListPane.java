package org.rekeningsysteem.ui.reparaties;

import java.util.Arrays;
import java.util.List;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import org.rekeningsysteem.ui.list.AbstractListPane;
import org.rekeningsysteem.ui.list.MoneyCell;
import org.rekeningsysteem.ui.reparaties.ReparatiesListPane.ReparatiesModel;

public class ReparatiesListPane extends AbstractListPane<ReparatiesModel> {

	public ReparatiesListPane() {
		super("Factuurlijst");
	}

	@Override
	protected List<TableColumn<ReparatiesModel, ?>> initTableColumns() {
		TableColumn<ReparatiesModel, String> omschrCol = new TableColumn<>("Omschrijving");
		TableColumn<ReparatiesModel, String> ordernrCol = new TableColumn<>("Ordernummer");
		TableColumn<ReparatiesModel, Double> loonCol = new TableColumn<>("Arbeid");
		TableColumn<ReparatiesModel, Double> materiaalCol = new TableColumn<>("Materiaal");

		omschrCol.setMinWidth(300);
		ordernrCol.setMinWidth(100);
		loonCol.setMinWidth(100);
		materiaalCol.setMinWidth(100);

		omschrCol.setCellValueFactory(new PropertyValueFactory<>("omschrijving"));
		ordernrCol.setCellValueFactory(new PropertyValueFactory<>("ordernummer"));
		loonCol.setCellValueFactory(new PropertyValueFactory<>("loon"));
		materiaalCol.setCellValueFactory(new PropertyValueFactory<>("materiaal"));

		loonCol.setCellFactory(param -> new MoneyCell<>());
		materiaalCol.setCellFactory(param -> new MoneyCell<>());

		return Arrays.asList(omschrCol, ordernrCol, loonCol, materiaalCol, this.getDeleteCol());
	}

	public static class ReparatiesModel {

		private String omschrijving;
		private String ordernummer;
		private double loon;
		private double materiaal;

		public ReparatiesModel(String omschrijving, String ordernummer, double loon, double materiaal) {
			this.omschrijving = omschrijving;
			this.ordernummer = ordernummer;
			this.loon = loon;
			this.materiaal = materiaal;
		}

		public String getOmschrijving() {
			return this.omschrijving;
		}

		public void setOmschrijving(String omschrijving) {
			this.omschrijving = omschrijving;
		}

		public String getOrdernummer() {
			return this.ordernummer;
		}

		public void setOrdernummer(String ordernummer) {
			this.ordernummer = ordernummer;
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

package org.rekeningsysteem.ui.reparaties;

import java.util.Arrays;
import java.util.List;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import org.rekeningsysteem.ui.list.AbstractListPane;
import org.rekeningsysteem.ui.list.MoneyCell;
import org.rekeningsysteem.ui.reparaties.ReparatiesListPane.ReparatiesModel;

public class ReparatiesListPane extends AbstractListPane<ReparatiesModel> {

	@Override
	protected List<TableColumn<ReparatiesModel, ?>> initTableColumns() {
		TableColumn<ReparatiesModel, String> omschrCol = new TableColumn<>("Omschrijving");
		TableColumn<ReparatiesModel, String> bonnrCol = new TableColumn<>("Bonnummer");
		TableColumn<ReparatiesModel, Double> loonCol = new TableColumn<>("Loon");
		TableColumn<ReparatiesModel, Double> materiaalCol = new TableColumn<>("Materiaal");

		omschrCol.setMinWidth(300);
		bonnrCol.setMinWidth(100);
		loonCol.setMinWidth(100);
		materiaalCol.setMinWidth(100);

		omschrCol.setCellValueFactory(new PropertyValueFactory<>("omschrijving"));
		bonnrCol.setCellValueFactory(new PropertyValueFactory<>("bonnummer"));
		loonCol.setCellValueFactory(new PropertyValueFactory<>("loon"));
		materiaalCol.setCellValueFactory(new PropertyValueFactory<>("materiaal"));

		loonCol.setCellFactory(param -> new MoneyCell<>());
		materiaalCol.setCellFactory(param -> new MoneyCell<>());

		return Arrays.asList(omschrCol, bonnrCol, loonCol, materiaalCol, this.getDeleteCol());
	}

	public static class ReparatiesModel {

		private String omschrijving;
		private String bonnummer;
		private double loon;
		private double materiaal;

		public ReparatiesModel(String omschrijving, String bonnummer, double loon, double materiaal) {
			this.omschrijving = omschrijving;
			this.bonnummer = bonnummer;
			this.loon = loon;
			this.materiaal = materiaal;
		}

		public String getOmschrijving() {
			return this.omschrijving;
		}

		public void setOmschrijving(String omschrijving) {
			this.omschrijving = omschrijving;
		}

		public String getBonnummer() {
			return this.bonnummer;
		}

		public void setBonnummer(String bonnummer) {
			this.bonnummer = bonnummer;
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

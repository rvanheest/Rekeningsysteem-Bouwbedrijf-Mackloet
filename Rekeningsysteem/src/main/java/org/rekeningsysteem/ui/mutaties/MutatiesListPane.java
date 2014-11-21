package org.rekeningsysteem.ui.mutaties;

import java.util.Arrays;
import java.util.List;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import org.rekeningsysteem.ui.list.AbstractListPane;
import org.rekeningsysteem.ui.list.MoneyCell;
import org.rekeningsysteem.ui.mutaties.MutatiesListPane.MutatiesModel;

public class MutatiesListPane extends AbstractListPane<MutatiesModel> {

	public MutatiesListPane() {
		super("Factuurlijst");
	}

	@Override
	protected List<TableColumn<MutatiesModel, ?>> initTableColumns() {
		TableColumn<MutatiesModel, String> omschrCol = new TableColumn<>("Omschrijving");
		TableColumn<MutatiesModel, String> bonnrCol = new TableColumn<>("Bonnummer");
		TableColumn<MutatiesModel, Double> prijsCol = new TableColumn<>("Prijs");

		omschrCol.setMinWidth(300);
		bonnrCol.setMinWidth(100);
		prijsCol.setMinWidth(100);

		omschrCol.setCellValueFactory(new PropertyValueFactory<>("omschrijving"));
		bonnrCol.setCellValueFactory(new PropertyValueFactory<>("bonnummer"));
		prijsCol.setCellValueFactory(new PropertyValueFactory<>("prijs"));

		prijsCol.setCellFactory(param -> new MoneyCell<>());

		return Arrays.asList(omschrCol, bonnrCol, prijsCol, this.getDeleteCol());
	}

	public static class MutatiesModel {

		private String omschrijving;
		private String bonnummer;
		private double prijs;

		public MutatiesModel(String omschrijving, String bonnummer, double prijs) {
			this.omschrijving = omschrijving;
			this.bonnummer = bonnummer;
			this.prijs = prijs;
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

		public double getPrijs() {
			return this.prijs;
		}

		public void setPrijs(double prijs) {
			this.prijs = prijs;
		}
	}
}

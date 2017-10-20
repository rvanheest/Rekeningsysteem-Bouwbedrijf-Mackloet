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
		TableColumn<MutatiesModel, String> ordernrCol = new TableColumn<>("Ordernummer");
		TableColumn<MutatiesModel, Double> prijsCol = new TableColumn<>("Prijs");

		omschrCol.setMinWidth(300);
		ordernrCol.setMinWidth(100);
		prijsCol.setMinWidth(100);

		omschrCol.setCellValueFactory(new PropertyValueFactory<>("omschrijving"));
		ordernrCol.setCellValueFactory(new PropertyValueFactory<>("ordernummer"));
		prijsCol.setCellValueFactory(new PropertyValueFactory<>("prijs"));

		prijsCol.setCellFactory(param -> new MoneyCell<>());

		return Arrays.asList(omschrCol, ordernrCol, prijsCol, this.getDeleteCol());
	}

	public static class MutatiesModel {

		private String omschrijving;
		private String ordernummer;
		private double prijs;

		public MutatiesModel(String omschrijving, String ordernummer, double prijs) {
			this.omschrijving = omschrijving;
			this.ordernummer = ordernummer;
			this.prijs = prijs;
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

		public double getPrijs() {
			return this.prijs;
		}

		public void setPrijs(double prijs) {
			this.prijs = prijs;
		}
	}
}

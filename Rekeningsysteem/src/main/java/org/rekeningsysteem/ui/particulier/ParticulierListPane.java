package org.rekeningsysteem.ui.particulier;

import java.util.Arrays;
import java.util.List;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import org.rekeningsysteem.ui.list.AbstractListPane;
import org.rekeningsysteem.ui.list.MoneyCell;
import org.rekeningsysteem.ui.particulier.ParticulierListPane.ParticulierModel;

public class ParticulierListPane extends AbstractListPane<ParticulierModel> {

	public ParticulierListPane() {
		super("Factuurlijst");
	}

	@Override
	protected List<TableColumn<ParticulierModel, ?>> initTableColumns() {
		TableColumn<ParticulierModel, String> artNrCol = new TableColumn<>("Artikelnummer");
		TableColumn<ParticulierModel, String> omschrCol = new TableColumn<>("Omschrijving");
		TableColumn<ParticulierModel, Integer> prijsPerCol = new TableColumn<>("Prijs Per");
		TableColumn<ParticulierModel, String> eenheidCol = new TableColumn<>("Eenheid");
		TableColumn<ParticulierModel, Double> verkoopprijsCol = new TableColumn<>("Verkoopprijs");
		TableColumn<ParticulierModel, Double> aantalCol = new TableColumn<>("Aantal");
		
		artNrCol.setMinWidth(100);
		omschrCol.setMinWidth(200);
		prijsPerCol.setMinWidth(50);
		eenheidCol.setMinWidth(100);
		verkoopprijsCol.setMinWidth(90);
		aantalCol.setMinWidth(50);
		
		artNrCol.setCellValueFactory(new PropertyValueFactory<>("artikelNummer"));
		omschrCol.setCellValueFactory(new PropertyValueFactory<>("omschrijving"));
		prijsPerCol.setCellValueFactory(new PropertyValueFactory<>("prijsPer"));
		eenheidCol.setCellValueFactory(new PropertyValueFactory<>("eenheid"));
		verkoopprijsCol.setCellValueFactory(new PropertyValueFactory<>("verkoopPrijs"));
		aantalCol.setCellValueFactory(new PropertyValueFactory<>("aantal"));
		
		verkoopprijsCol.setCellFactory(param -> new MoneyCell<>());
		
		return Arrays.asList(artNrCol, omschrCol, prijsPerCol, eenheidCol, verkoopprijsCol,
				aantalCol, this.getDeleteCol());
	}

	public static class ParticulierModel {

		private final String artikelNummer;
		private final String omschrijving;
		private final String prijsPer;
		private final String eenheid;
		private final double verkoopPrijs;
		private final String aantal;

		public ParticulierModel(String artikelNummer, String omschrijving, String prijsPer,
				String eenheid, double verkoopPrijs, String aantal) {
			this.artikelNummer = artikelNummer;
			this.omschrijving = omschrijving;
			this.prijsPer = prijsPer;
			this.eenheid = eenheid;
			this.verkoopPrijs = verkoopPrijs;
			this.aantal = aantal;
		}

		public String getArtikelNummer() {
			return this.artikelNummer;
		}

		public String getOmschrijving() {
			return this.omschrijving;
		}

		public String getPrijsPer() {
			return this.prijsPer;
		}

		public String getEenheid() {
			return this.eenheid;
		}

		public double getVerkoopPrijs() {
			return this.verkoopPrijs;
		}

		public String getAantal() {
			return this.aantal;
		}
	}
}
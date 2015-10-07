package org.rekeningsysteem.ui.particulier2;

import java.util.Arrays;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.ui.list.AbstractListPane;
import org.rekeningsysteem.ui.list.MoneyCell;
import org.rekeningsysteem.ui.particulier2.ParticulierListPane2.ParticulierModel2;

// TODO ParticulierListPane
public class ParticulierListPane2 extends AbstractListPane<ParticulierModel2> {

	public ParticulierListPane2() {
		super("Factuurlijst");
	}

	@Override
	protected List<TableColumn<ParticulierModel2, ?>> initTableColumns() {
		TableColumn<ParticulierModel2, String> omschrCol = new TableColumn<>("Omschrijving");
		TableColumn<ParticulierModel2, Double> loonCol = new TableColumn<>("Arbeid");
		TableColumn<ParticulierModel2, Double> loonBtwCol = new TableColumn<>("BTW arbeid");
		TableColumn<ParticulierModel2, Double> materiaalCol = new TableColumn<>("Materiaal");
		TableColumn<ParticulierModel2, Double> materiaalBtwCol = new TableColumn<>("BTW materiaal");

		omschrCol.setMinWidth(300);
		loonCol.setMinWidth(75);
		loonBtwCol.setMinWidth(50);
		materiaalCol.setMinWidth(75);
		materiaalBtwCol.setMinWidth(100);

		omschrCol.setCellValueFactory(new PropertyValueFactory<>("omschrijving"));
		loonCol.setCellValueFactory(new PropertyValueFactory<>("loon"));
		loonBtwCol.setCellValueFactory(new PropertyValueFactory<>("loonBtwPercentage"));
		materiaalCol.setCellValueFactory(new PropertyValueFactory<>("materiaal"));
		materiaalBtwCol.setCellValueFactory(new PropertyValueFactory<>("materiaalBtwPercentage"));

		Callback<TableColumn<ParticulierModel2, Double>, TableCell<ParticulierModel2, Double>> loonBtwCellFactory = loonBtwCol
				.getCellFactory();
		Callback<TableColumn<ParticulierModel2, Double>, TableCell<ParticulierModel2, Double>> materiaalBtwCellFactory = materiaalBtwCol
				.getCellFactory();

		loonCol.setCellFactory(c -> new MoneyCell<>());
		loonBtwCol.setCellFactory(column -> {
			TableCell<ParticulierModel2, Double> cell = loonBtwCellFactory.call(column);
			cell.setAlignment(Pos.TOP_RIGHT);
			return cell;
		});
		materiaalCol.setCellFactory(c -> new MoneyCell<>());
		materiaalBtwCol.setCellFactory(column -> {
			TableCell<ParticulierModel2, Double> cell = materiaalBtwCellFactory.call(column);
			cell.setAlignment(Pos.TOP_RIGHT);
			return cell;
		});

		return Arrays.asList(omschrCol, loonCol, loonBtwCol, materiaalCol, materiaalBtwCol,
				this.getDeleteCol());
	}

	// TODO ParticulierModel
	public static class ParticulierModel2 {

		private final String omschrijving;
//		private final double loon;
//		private final double loonBtwPercentage;
		private final double materiaal;
		private final double materiaalBtwPercentage;
		private final double aantal;
		private final EsselinkArtikel artikel;

		public ParticulierModel2(String omschrijving, /*double loon, double loonBtwPercentage,*/
				double materiaal, double materiaalBtwPercentage) {
			this(omschrijving, materiaal, materiaalBtwPercentage, 0.0, null);
		}

		public ParticulierModel2(String omschrijving, /*double loon, double loonBtwPercentage,*/
				double materiaal, double materiaalBtwPercentage, double aantal, EsselinkArtikel artikel) {
			this.omschrijving = omschrijving;
//			this.loon = loon;
//			this.loonBtwPercentage = loonBtwPercentage;
			this.materiaal = materiaal;
			this.materiaalBtwPercentage = materiaalBtwPercentage;
			this.aantal = aantal;
			this.artikel = artikel;
		}

		public String getOmschrijving() {
			return this.omschrijving;
		}

//		public double getLoon() {
//			return this.loon;
//		}
//
//		public double getLoonBtwPercentage() {
//			return this.loonBtwPercentage;
//		}

		public double getMateriaal() {
			return this.materiaal;
		}

		public double getMateriaalBtwPercentage() {
			return this.materiaalBtwPercentage;
		}

		public double getAantal() {
			return this.aantal;
		}

		public EsselinkArtikel getArtikel() {
			return this.artikel;
		}
	}
}

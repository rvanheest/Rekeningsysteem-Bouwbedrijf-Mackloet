package org.rekeningsysteem.ui.aangenomen;

import java.util.Arrays;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

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
		loonCol.setMinWidth(75);
		loonBtwCol.setMinWidth(50);
		materiaalCol.setMinWidth(75);
		materiaalBtwCol.setMinWidth(100);

		omschrCol.setCellValueFactory(new PropertyValueFactory<>("omschrijving"));
		loonCol.setCellValueFactory(new PropertyValueFactory<>("loon"));
		loonBtwCol.setCellValueFactory(new PropertyValueFactory<>("loonBtwPercentage"));
		materiaalCol.setCellValueFactory(new PropertyValueFactory<>("materiaal"));
		materiaalBtwCol.setCellValueFactory(new PropertyValueFactory<>("materiaalBtwPercentage"));

		Callback<TableColumn<AangenomenModel, Double>, TableCell<AangenomenModel, Double>> loonBtwCellFactory = loonBtwCol
				.getCellFactory();
		Callback<TableColumn<AangenomenModel, Double>, TableCell<AangenomenModel, Double>> materiaalBtwCellFactory = materiaalBtwCol
				.getCellFactory();

		loonCol.setCellFactory(c -> new MoneyCell<>());
		loonBtwCol.setCellFactory(column -> {
			TableCell<AangenomenModel, Double> cell = loonBtwCellFactory.call(column);
			cell.setAlignment(Pos.TOP_RIGHT);
			return cell;
		});
		materiaalCol.setCellFactory(c -> new MoneyCell<>());
		materiaalBtwCol.setCellFactory(column -> {
			TableCell<AangenomenModel, Double> cell = materiaalBtwCellFactory.call(column);
			cell.setAlignment(Pos.TOP_RIGHT);
			return cell;
		});

		return Arrays.asList(omschrCol, loonCol, loonBtwCol, materiaalCol, materiaalBtwCol,
				this.getDeleteCol());
	}

	public static class AangenomenModel {

		private final String omschrijving;
		private final double loon;
		private final double loonBtwPercentage;
		private final double materiaal;
		private final double materiaalBtwPercentage;

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

		public double getLoon() {
			return this.loon;
		}

		public double getLoonBtwPercentage() {
			return this.loonBtwPercentage;
		}

		public double getMateriaal() {
			return this.materiaal;
		}

		public double getMateriaalBtwPercentage() {
			return this.materiaalBtwPercentage;
		}
	}
}

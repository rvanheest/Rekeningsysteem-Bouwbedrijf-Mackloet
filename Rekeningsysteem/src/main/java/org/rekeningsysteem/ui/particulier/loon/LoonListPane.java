package org.rekeningsysteem.ui.particulier.loon;

import java.util.Arrays;
import java.util.List;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.ui.list.AbstractListPane;
import org.rekeningsysteem.ui.list.MoneyCell;
import org.rekeningsysteem.ui.particulier.loon.LoonListPane.LoonModel;

public class LoonListPane extends AbstractListPane<LoonModel> {

	public LoonListPane() {
		super("Loonlijst");
	}

	@Override
	protected List<TableColumn<LoonModel, ?>> initTableColumns() {
		TableColumn<LoonModel, String> omschrCol = new TableColumn<>("Omschrijving");
		TableColumn<LoonModel, Double> urenCol = new TableColumn<>("Uren");
		TableColumn<LoonModel, Double> loonCol = new TableColumn<>("Loon");
		TableColumn<LoonModel, Double> btwCol = new TableColumn<>("BTW");
		
		omschrCol.setMinWidth(200);
		urenCol.setMinWidth(50);
		loonCol.setMinWidth(90);
		btwCol.setMinWidth(50);
		
		omschrCol.setCellValueFactory(new PropertyValueFactory<>("omschrijving"));
		urenCol.setCellValueFactory(new PropertyValueFactory<>("uren"));
		loonCol.setCellValueFactory(new PropertyValueFactory<>("loon"));
		btwCol.setCellValueFactory(new PropertyValueFactory<>("btwPercentage"));
		
		loonCol.setCellFactory(param -> new MoneyCell<>());
		
		return Arrays.asList(omschrCol, urenCol, loonCol, btwCol, this.getDeleteCol());
	}

	public static class LoonModel {

		private final String omschrijving;
		private final String uren;
		private final Geld uurloon;
		private final double loon;
		private final double btwPercentage;

		public LoonModel(String omschrijving, String uren, Geld uurloon, double loon,
				double btwPercentage) {
			this.omschrijving = omschrijving;
			this.uren = uren;
			this.uurloon = uurloon;
			this.loon = loon;
			this.btwPercentage = btwPercentage;
		}

		public String getOmschrijving() {
			return this.omschrijving;
		}

		public String getUren() {
			return this.uren;
		}
		
		public Geld getUurloon() {
			return this.uurloon;
		}

		public double getLoon() {
			return this.loon;
		}

		public double getBtwPercentage() {
			return this.btwPercentage;
		}
	}
}

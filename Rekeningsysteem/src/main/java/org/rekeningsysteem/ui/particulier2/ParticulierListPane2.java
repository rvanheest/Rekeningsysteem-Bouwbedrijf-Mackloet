package org.rekeningsysteem.ui.particulier2;

import java.util.Arrays;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import org.rekeningsysteem.data.particulier2.EsselinkParticulierArtikel;
import org.rekeningsysteem.data.particulier2.ParticulierArtikel2Impl;
import org.rekeningsysteem.data.particulier2.loon.InstantLoon2;
import org.rekeningsysteem.data.particulier2.loon.ProductLoon2;
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

		private final EsselinkParticulierArtikel esselink;
		private final ParticulierArtikel2Impl ander;
		private final InstantLoon2 instant;
		private final ProductLoon2 product;

		private final String omschrijving;
		private final double loon;
		private final double loonBtwPercentage;
		private final double materiaal;
		private final double materiaalBtwPercentage;

		public ParticulierModel2(EsselinkParticulierArtikel epa) {
			this.esselink = epa;
			this.ander = null;
			this.instant = null;
			this.product = null;

			this.omschrijving = epa.getOmschrijving();
			this.loon = epa.getLoon().getBedrag();
			this.loonBtwPercentage = epa.getLoonBtwPercentage();
			this.materiaal = epa.getMateriaal().getBedrag();
			this.materiaalBtwPercentage = epa.getMateriaalBtwPercentage();
		}

		public ParticulierModel2(ParticulierArtikel2Impl pa2i) {
			this.esselink = null;
			this.ander = pa2i;
			this.instant = null;
			this.product = null;

			this.omschrijving = pa2i.getOmschrijving();
			this.loon = pa2i.getLoon().getBedrag();
			this.loonBtwPercentage = pa2i.getLoonBtwPercentage();
			this.materiaal = pa2i.getMateriaal().getBedrag();
			this.materiaalBtwPercentage = pa2i.getMateriaalBtwPercentage();
		}

		public ParticulierModel2(InstantLoon2 il2) {
			this.esselink = null;
			this.ander = null;
			this.instant = il2;
			this.product = null;

			this.omschrijving = il2.getOmschrijving();
			this.loon = il2.getLoon().getBedrag();
			this.loonBtwPercentage = il2.getLoonBtwPercentage();
			this.materiaal = il2.getMateriaal().getBedrag();
			this.materiaalBtwPercentage = il2.getMateriaalBtwPercentage();
		}

		public ParticulierModel2(ProductLoon2 pl2) {
			this.esselink = null;
			this.ander = null;
			this.instant = null;
			this.product = pl2;

			this.omschrijving = pl2.getOmschrijving();
			this.loon = pl2.getLoon().getBedrag();
			this.loonBtwPercentage = pl2.getLoonBtwPercentage();
			this.materiaal = pl2.getMateriaal().getBedrag();
			this.materiaalBtwPercentage = pl2.getMateriaalBtwPercentage();
		}

		public EsselinkParticulierArtikel getEsselink() {
			return this.esselink;
		}

		public ParticulierArtikel2Impl getAnder() {
			return this.ander;
		}

		public InstantLoon2 getInstant() {
			return this.instant;
		}

		public ProductLoon2 getProduct() {
			return this.product;
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

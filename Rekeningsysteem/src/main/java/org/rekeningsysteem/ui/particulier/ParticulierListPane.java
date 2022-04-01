package org.rekeningsysteem.ui.particulier;

import java.util.Arrays;
import java.util.List;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.ui.list.AbstractListPane;
import org.rekeningsysteem.ui.list.BtwPercentageCell;
import org.rekeningsysteem.ui.list.MoneyCell;
import org.rekeningsysteem.ui.particulier.ParticulierListPane.ParticulierModel;

public class ParticulierListPane extends AbstractListPane<ParticulierModel> {

	public ParticulierListPane() {
		super("Factuurlijst");
	}

	@Override
	protected List<TableColumn<ParticulierModel, ?>> initTableColumns() {
		TableColumn<ParticulierModel, String> omschrCol = new TableColumn<>("Omschrijving");
		TableColumn<ParticulierModel, Double> loonCol = new TableColumn<>("Arbeid");
		TableColumn<ParticulierModel, BtwPercentage> loonBtwCol = new TableColumn<>("BTW arbeid");
		TableColumn<ParticulierModel, Double> materiaalCol = new TableColumn<>("Materiaal");
		TableColumn<ParticulierModel, BtwPercentage> materiaalBtwCol = new TableColumn<>("BTW materiaal");

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

		loonCol.setCellFactory(c -> new MoneyCell<>());
		loonBtwCol.setCellFactory(c -> new BtwPercentageCell<>());
		materiaalCol.setCellFactory(c -> new MoneyCell<>());
		materiaalBtwCol.setCellFactory(c -> new BtwPercentageCell<>());

		return Arrays.asList(omschrCol, loonCol, loonBtwCol, materiaalCol, materiaalBtwCol, this.getDeleteCol());
	}

	public static class ParticulierModel {

		private final GebruiktEsselinkArtikel esselink;
		private final AnderArtikel ander;
		private final InstantLoon instant;
		private final ProductLoon product;

		private final String omschrijving;
		private final Double loon;
		private final BtwPercentage loonBtwPercentage;
		private final Double materiaal;
		private final BtwPercentage materiaalBtwPercentage;

		public ParticulierModel(GebruiktEsselinkArtikel epa) {
			this.esselink = epa;
			this.ander = null;
			this.instant = null;
			this.product = null;

			this.omschrijving = epa.getOmschrijving();
			this.loon = null;
			this.loonBtwPercentage = null;
			this.materiaal = epa.getMateriaal().getBedrag();
			this.materiaalBtwPercentage = epa.getMateriaalBtwPercentage();
		}

		public ParticulierModel(AnderArtikel pa2i) {
			this.esselink = null;
			this.ander = pa2i;
			this.instant = null;
			this.product = null;

			this.omschrijving = pa2i.getOmschrijving();
			this.loon = null;
			this.loonBtwPercentage = null;
			this.materiaal = pa2i.getMateriaal().getBedrag();
			this.materiaalBtwPercentage = pa2i.getMateriaalBtwPercentage();
		}

		public ParticulierModel(InstantLoon il2) {
			this.esselink = null;
			this.ander = null;
			this.instant = il2;
			this.product = null;

			this.omschrijving = il2.getOmschrijving();
			this.loon = il2.getLoon().getBedrag();
			this.loonBtwPercentage = il2.getLoonBtwPercentage();
			this.materiaal = null;
			this.materiaalBtwPercentage = null;
		}

		public ParticulierModel(ProductLoon pl2) {
			this.esselink = null;
			this.ander = null;
			this.instant = null;
			this.product = pl2;

			this.omschrijving = pl2.getOmschrijving();
			this.loon = pl2.getLoon().getBedrag();
			this.loonBtwPercentage = pl2.getLoonBtwPercentage();
			this.materiaal = null;
			this.materiaalBtwPercentage = null;
		}

		public GebruiktEsselinkArtikel getEsselink() {
			return this.esselink;
		}

		public AnderArtikel getAnder() {
			return this.ander;
		}

		public InstantLoon getInstant() {
			return this.instant;
		}

		public ProductLoon getProduct() {
			return this.product;
		}

		public String getOmschrijving() {
			return this.omschrijving;
		}

		public Double getLoon() {
			return this.loon;
		}

		public BtwPercentage getLoonBtwPercentage() {
			return this.loonBtwPercentage;
		}

		public Double getMateriaal() {
			return this.materiaal;
		}

		public BtwPercentage getMateriaalBtwPercentage() {
			return this.materiaalBtwPercentage;
		}
	}
}

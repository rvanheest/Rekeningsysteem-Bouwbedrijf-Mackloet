package org.rekeningsysteem.ui.particulier;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.logic.database.ArtikellijstDBInteraction;
import org.rekeningsysteem.ui.list.AbstractListItemController;
import org.rekeningsysteem.ui.particulier.loon.InstantLoonController;
import org.rekeningsysteem.ui.particulier.loon.ProductLoonController;

public class ParticulierArtikelController extends AbstractListItemController<ParticulierArtikel> {

	private final AnderArtikelController anderController;
	private final GebruiktEsselinkArtikelController gebruiktController;
	private final InstantLoonController instantController;
	private final ProductLoonController productController;

	public ParticulierArtikelController(Currency currency, Database db, BtwPercentage defaultBtw) {
		this(defaultBtw, new ParticulierArtikelPane(currency),
				new AnderArtikelController(currency),
				new GebruiktEsselinkArtikelController(currency,
						new ArtikellijstDBInteraction(db)),
				new InstantLoonController(currency),
				new ProductLoonController(currency));
	}

//	public ParticulierArtikel2Controller(Currency currency, Database db, BtwPercentage defaultBtw,
//			ParticulierArtikel2Impl input) {
//		this(currency, db, defaultBtw);
//		this.setAnderArtikel(input);
//	}

	public ParticulierArtikelController(BtwPercentage defaultBtw, ParticulierArtikelPane ui,
			AnderArtikelController anderController,
			GebruiktEsselinkArtikelController gebruiktController,
			InstantLoonController instantController,
			ProductLoonController productController) {
		super(ui, ui.getType().<ParticulierArtikel> flatMap(type -> {
			switch (type) {
				case ESSELINK:
					return gebruiktController.getModel();
				case ANDER:
					return anderController.getModel();
				case INSTANT:
					return instantController.getModel();
				case PRODUCT:
					return productController.getModel();
				default:
					return null;
					// Does never happen!!!
			}
		}).sample(ui.getAddButtonEvent()).map(Optional::of)
				.mergeWith(ui.getCancelButtonEvent().map(event -> Optional.empty()))
				.first());

		this.anderController = anderController;
		this.gebruiktController = gebruiktController;
		this.instantController = instantController;
		this.productController = productController;

		ui.addContent(ParticulierArtikelType.ESSELINK, this.gebruiktController.getUI());
		ui.addContent(ParticulierArtikelType.ANDER, this.anderController.getUI());
		ui.addContent(ParticulierArtikelType.INSTANT, this.instantController.getUI());
		ui.addContent(ParticulierArtikelType.PRODUCT, this.productController.getUI());

		this.setBtwPercentage(defaultBtw.getMateriaalPercentage());
	}

	@Override
	public ParticulierArtikelPane getUI() {
		return (ParticulierArtikelPane) super.getUI();
	}

//	private void setAnderArtikel(ParticulierArtikel2Impl ander) {
//		this.anderController.getUI().setOmschrijving(ander.getOmschrijving());
//		this.anderController.getUI().setPrijs(ander.getMateriaal().getBedrag());
//		this.anderController.getUI().setBtwPercentage(ander.getMateriaalBtwPercentage());
//	}

	public void setBtwPercentage(Double btwPercentage) {
		this.anderController.getUI().setBtwPercentage(btwPercentage);
		this.gebruiktController.getUI().setBtwPercentage(btwPercentage);
		this.instantController.getUI().setBtwPercentage(btwPercentage);
		this.productController.getUI().setBtwPercentage(btwPercentage);
	}
}

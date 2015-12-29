package org.rekeningsysteem.ui.particulier2;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.data.particulier.ParticulierArtikel2;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.logic.database.ArtikellijstDBInteraction;
import org.rekeningsysteem.ui.list.AbstractListItemController;
import org.rekeningsysteem.ui.particulier2.loon.InstantLoonController2;
import org.rekeningsysteem.ui.particulier2.loon.ProductLoonController2;

// TODO ParticulierArtikelController
public class ParticulierArtikel2Controller extends AbstractListItemController<ParticulierArtikel2> {

	private final ParticulierArtikelImplController anderController;
	private final EsselinkParticulierArtikelController gebruiktController;
	private final InstantLoonController2 instantController;
	private final ProductLoonController2 productController;

	public ParticulierArtikel2Controller(Currency currency, Database db, BtwPercentage defaultBtw) {
		this(defaultBtw, new ParticulierArtikel2Pane(currency),
				new ParticulierArtikelImplController(currency),
				new EsselinkParticulierArtikelController(currency,
						new ArtikellijstDBInteraction(db)),
				new InstantLoonController2(currency),
				new ProductLoonController2(currency));
	}

//	public ParticulierArtikel2Controller(Currency currency, Database db, BtwPercentage defaultBtw,
//			ParticulierArtikel2Impl input) {
//		this(currency, db, defaultBtw);
//		this.setAnderArtikel(input);
//	}

	public ParticulierArtikel2Controller(BtwPercentage defaultBtw, ParticulierArtikel2Pane ui,
			ParticulierArtikelImplController anderController,
			EsselinkParticulierArtikelController gebruiktController,
			InstantLoonController2 instantController,
			ProductLoonController2 productController) {
		super(ui, ui.getType().<ParticulierArtikel2> flatMap(type -> {
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

		ui.addContent(ParticulierArtikelType2.ESSELINK, this.gebruiktController.getUI());
		ui.addContent(ParticulierArtikelType2.ANDER, this.anderController.getUI());
		ui.addContent(ParticulierArtikelType2.INSTANT, this.instantController.getUI());
		ui.addContent(ParticulierArtikelType2.PRODUCT, this.productController.getUI());

		this.setBtwPercentage(defaultBtw.getMateriaalPercentage());
	}

	@Override
	public ParticulierArtikel2Pane getUI() {
		return (ParticulierArtikel2Pane) super.getUI();
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

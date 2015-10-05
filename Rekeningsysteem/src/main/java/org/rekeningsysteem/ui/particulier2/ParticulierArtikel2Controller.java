package org.rekeningsysteem.ui.particulier2;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.data.particulier2.ParticulierArtikel2;
import org.rekeningsysteem.data.particulier2.ParticulierArtikel2Impl;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.logic.database.ArtikellijstDBInteraction;
import org.rekeningsysteem.ui.list.AbstractListItemController;

// TODO ParticulierArtikelController
public class ParticulierArtikel2Controller extends AbstractListItemController<ParticulierArtikel2> {

	private final ParticulierArtikelImplController anderController;
	private final EsselinkParticulierArtikelController gebruiktController;

	public ParticulierArtikel2Controller(Currency currency, Database db, BtwPercentage defaultBtw) {
		this(defaultBtw, new ParticulierArtikel2Pane(currency),
				new ParticulierArtikelImplController(currency),
				new EsselinkParticulierArtikelController(currency,
						new ArtikellijstDBInteraction(db)));
	}

	public ParticulierArtikel2Controller(Currency currency, Database db, BtwPercentage defaultBtw,
			ParticulierArtikel2Impl input) {
		this(currency, db, defaultBtw);
		this.setAnderArtikel(input);
	}

	public ParticulierArtikel2Controller(BtwPercentage defaultBtw, ParticulierArtikel2Pane ui,
			ParticulierArtikelImplController anderController,
			EsselinkParticulierArtikelController gebruiktController) {
		super(ui, ui.getType().<ParticulierArtikel2> flatMap(type -> {
			switch (type) {
				case ESSELINK:
					return gebruiktController.getModel();
				case ANDER:
					return anderController.getModel();
				default:
					return null;
					// Does never happen!!!
			}
		}).sample(ui.getAddButtonEvent()).map(Optional::of)
				.mergeWith(ui.getCancelButtonEvent().map(event -> Optional.empty()))
				.first());

		this.anderController = anderController;
		this.gebruiktController = gebruiktController;

		ui.addContent(ParticulierArtikelType2.ESSELINK, this.gebruiktController.getUI());
		ui.addContent(ParticulierArtikelType2.ANDER, this.anderController.getUI());

		this.setBtwPercentage(defaultBtw.getMateriaalPercentage());
	}

	@Override
	public ParticulierArtikel2Pane getUI() {
		return (ParticulierArtikel2Pane) super.getUI();
	}

	private void setAnderArtikel(ParticulierArtikel2Impl ander) {
		this.anderController.getUI().setOmschrijving(ander.getOmschrijving());
		this.anderController.getUI().setPrijs(ander.getMateriaal().getBedrag());
		this.anderController.getUI().setBtwPercentage(ander.getMateriaalBtwPercentage());
	}

	public void setBtwPercentage(Double btwPercentage) {
		this.anderController.getUI().setBtwPercentage(btwPercentage);
		this.gebruiktController.getUI().setBtwPercentage(btwPercentage);
	}
}

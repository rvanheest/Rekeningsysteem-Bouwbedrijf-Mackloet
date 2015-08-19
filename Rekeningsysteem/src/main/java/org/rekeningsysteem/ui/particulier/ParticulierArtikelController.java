package org.rekeningsysteem.ui.particulier;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.ui.list.AbstractListItemController;

public class ParticulierArtikelController extends AbstractListItemController<ParticulierArtikel> {

	private final AnderArtikelController anderController;
	private final GebruiktEsselinkArtikelController gebruiktController;

	public ParticulierArtikelController(Currency currency, Database db, BtwPercentage defaultBtw) {
		this(defaultBtw, new ParticulierArtikelPane(currency),
				new AnderArtikelController(currency),
				new GebruiktEsselinkArtikelController(currency, db));
	}

	public ParticulierArtikelController(Currency currency, Database db, BtwPercentage defaultBtw,
			AnderArtikel input) {
		this(currency, db, defaultBtw);
		this.setAnderArtikel(input);
	}

	public ParticulierArtikelController(BtwPercentage defaultBtw, ParticulierArtikelPane ui,
			AnderArtikelController anderController,
			GebruiktEsselinkArtikelController gebruiktController) {
		super(ui, ui.getType().flatMap(type -> {
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

		ui.addContent(ParticulierArtikelType.ESSELINK, this.gebruiktController.getUI());
		ui.addContent(ParticulierArtikelType.ANDER, this.anderController.getUI());

		this.setBtwPercentage(defaultBtw.getMateriaalPercentage());
	}

	@Override
	public ParticulierArtikelPane getUI() {
		return (ParticulierArtikelPane) super.getUI();
	}

	private void setAnderArtikel(AnderArtikel ander) {
		this.anderController.getUI().setOmschrijving(ander.getOmschrijving());
		this.anderController.getUI().setPrijs(ander.getMateriaal().getBedrag());
		this.anderController.getUI().setBtwPercentage(ander.getMateriaalBtwPercentage());
	}

	public void setBtwPercentage(Double btwPercentage) {
		this.anderController.getUI().setBtwPercentage(btwPercentage);
		this.gebruiktController.getUI().setBtwPercentage(btwPercentage);
	}
}

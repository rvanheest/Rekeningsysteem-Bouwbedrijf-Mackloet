package org.rekeningsysteem.ui.particulier;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.util.BtwPercentages;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.logic.database.ArtikellijstDBInteraction;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.ui.list.AbstractListItemController;
import org.rekeningsysteem.ui.particulier.loon.InstantLoonController;
import org.rekeningsysteem.ui.particulier.loon.ProductLoonController;

public class ParticulierArtikelController extends AbstractListItemController<ParticulierArtikel> {

	private final AnderArtikelController anderController;
	private final GebruiktEsselinkArtikelController gebruiktController;
	private final InstantLoonController instantController;
	private final ProductLoonController productController;

	public ParticulierArtikelController(Currency currency, Database db, BtwPercentages defaultBtw) {
		this(
			defaultBtw,
			new ParticulierArtikelPane(),
			new AnderArtikelController(currency),
			new GebruiktEsselinkArtikelController(currency, new ArtikellijstDBInteraction(db)),
			new InstantLoonController(currency),
			new ProductLoonController(currency)
		);
	}

	public ParticulierArtikelController(
		BtwPercentages defaultBtw,
		ParticulierArtikelPane ui,
		AnderArtikelController anderController,
		GebruiktEsselinkArtikelController gebruiktController,
		InstantLoonController instantController,
		ProductLoonController productController
	) {
		super(ui,
			ui.getType()
				.flatMap(type -> {
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
							// Does never happen!!!
							return null;
					}
				})
				.sample(ui.getAddButtonEvent())
				.map(Optional::of)
				.mergeWith(ui.getCancelButtonEvent().map(event -> Optional.empty()))
				.firstElement()
		);

		PropertiesWorker properties = PropertiesWorker.getInstance();

		this.anderController = anderController;
		this.gebruiktController = gebruiktController;
		this.instantController = instantController;
		this.productController = productController;

		properties.getProperty(PropertyModelEnum.FEATURE_PARTICULIER_ESSELINK_ARTIKEL).map(Boolean::parseBoolean)
			.filter(b -> b)
			.ifPresent(b -> ui.addContent(ParticulierArtikelType.ESSELINK, this.gebruiktController.getUI()));

		properties.getProperty(PropertyModelEnum.FEATURE_PARTICULIER_EIGEN_ARTIKEL).map(Boolean::parseBoolean)
			.filter(b -> b)
			.ifPresent(b -> ui.addContent(ParticulierArtikelType.ANDER, this.anderController.getUI()));

		properties.getProperty(PropertyModelEnum.FEATURE_PARTICULIER_LOON).map(Boolean::parseBoolean)
			.filter(b -> b)
			.ifPresent(b -> ui.addContent(ParticulierArtikelType.INSTANT, this.instantController.getUI()));

		properties.getProperty(PropertyModelEnum.FEATURE_PARTICULIER_LOON_PER_UUR).map(Boolean::parseBoolean)
			.filter(b -> b)
			.ifPresent(b -> ui.addContent(ParticulierArtikelType.PRODUCT, this.productController.getUI()));

		this.setBtwPercentage(defaultBtw);
	}

	@Override
	public ParticulierArtikelPane getUI() {
		return (ParticulierArtikelPane) super.getUI();
	}

	public void setBtwPercentage(BtwPercentages btwPercentages) {
		this.anderController.getUI().setBtwPercentage(btwPercentages.getMateriaalPercentage());
		this.gebruiktController.getUI().setBtwPercentage(btwPercentages.getMateriaalPercentage());
		this.instantController.getUI().setBtwPercentage(btwPercentages.getLoonPercentage());
		this.productController.getUI().setBtwPercentage(btwPercentages.getLoonPercentage());
	}
}

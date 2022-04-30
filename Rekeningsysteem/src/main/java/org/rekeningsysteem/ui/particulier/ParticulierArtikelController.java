package org.rekeningsysteem.ui.particulier;

import io.reactivex.rxjava3.core.Observable;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.util.BtwPercentages;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.logic.database.ArtikellijstDBInteraction;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.ui.list.AbstractListItemController;
import org.rekeningsysteem.ui.particulier.loon.InstantLoonController;
import org.rekeningsysteem.ui.particulier.loon.ProductLoonController;

import java.util.Currency;

public class ParticulierArtikelController extends AbstractListItemController<ParticulierArtikel, ParticulierArtikelPane> {

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
		super(ui, getParticulierArtikel(ui, anderController, gebruiktController, instantController, productController));

		PropertiesWorker properties = PropertiesWorker.getInstance();

		this.anderController = anderController;
		this.gebruiktController = gebruiktController;
		this.instantController = instantController;
		this.productController = productController;

		properties.getBooleanProperty(PropertyModelEnum.FEATURE_PARTICULIER_ESSELINK_ARTIKEL)
			.filter(b -> b)
			.ifPresent(b -> ui.addContent(ParticulierArtikelType.ESSELINK, this.gebruiktController.getUI()));

		properties.getBooleanProperty(PropertyModelEnum.FEATURE_PARTICULIER_EIGEN_ARTIKEL)
			.filter(b -> b)
			.ifPresent(b -> ui.addContent(ParticulierArtikelType.ANDER, this.anderController.getUI()));

		properties.getBooleanProperty(PropertyModelEnum.FEATURE_PARTICULIER_LOON)
			.filter(b -> b)
			.ifPresent(b -> ui.addContent(ParticulierArtikelType.INSTANT, this.instantController.getUI()));

		properties.getBooleanProperty(PropertyModelEnum.FEATURE_PARTICULIER_LOON_PER_UUR)
			.filter(b -> b)
			.ifPresent(b -> ui.addContent(ParticulierArtikelType.PRODUCT, this.productController.getUI()));

		this.setBtwPercentage(defaultBtw);
	}

	private static Observable<ParticulierArtikel> getParticulierArtikel(
		ParticulierArtikelPane ui,
		AnderArtikelController anderController,
		GebruiktEsselinkArtikelController gebruiktController,
		InstantLoonController instantController,
		ProductLoonController productController
	) {
		return ui.getType()
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
			});
	}

	public void setBtwPercentage(BtwPercentages btwPercentages) {
		this.anderController.setBtwPercentage(btwPercentages.materiaalPercentage());
		this.gebruiktController.setBtwPercentage(btwPercentages.materiaalPercentage());
		this.instantController.setBtwPercentage(btwPercentages.loonPercentage());
		this.productController.setBtwPercentage(btwPercentages.loonPercentage());
	}

	@Override
	public boolean isDisposed() {
		return super.isDisposed() && this.gebruiktController.isDisposed();
	}

	@Override
	public void dispose() {
		super.dispose();
		this.gebruiktController.dispose();
	}
}

package org.rekeningsysteem.ui.particulier.loon;

import java.util.Currency;

import javafx.scene.Node;
import javafx.scene.control.TabPane.TabClosingPolicy;

import org.rekeningsysteem.data.util.loon.InstantLoon;
import org.rekeningsysteem.data.util.loon.ProductLoon;
import org.rekeningsysteem.ui.list.ItemPane;
import org.rekeningsysteem.ui.particulier.tabpane.ItemTabPane;

import rx.Observable;

public class LoonPane extends ItemPane {

	private final InstantLoonController instantController;
	private final ProductLoonController productController;

	private Observable<LoonType> type;

	public LoonPane(Currency currency) {
		super("Nieuw loon");
		this.instantController = new InstantLoonController(currency);
		this.productController = new ProductLoonController(currency);
		
		this.getChildren().add(1, this.getContent());
	}

	private Node getContent() {
		ItemTabPane content = new ItemTabPane(text -> {
			if (text.equals(LoonType.INSTANT.getTabName())) {
				return LoonType.INSTANT;
			}
			else if (text.equals(LoonType.PRODUCT.getTabName())) {
				return LoonType.PRODUCT;
			}
			else {
				return null;
			}
		});
		
		content.setId("particulier-tabs");
		content.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		content.add(LoonType.INSTANT, this.instantController.getUI());
		content.add(LoonType.PRODUCT, this.productController.getUI());
		
		this.type = content.getType().cast(LoonType.class);
		
		return content;
	}

	public Observable<LoonType> getType() {
		return this.type;
	}

	public Observable<InstantLoon> getInstantLoon() {
		return this.instantController.getModel();
	}
	
	public void setInstantLoon(InstantLoon loon) {
		this.instantController.getUI().setOmschrijving(loon.getOmschrijving());
		this.instantController.getUI().setLoon(loon.getLoon().getBedrag());
	}

	public Observable<ProductLoon> getProductLoon() {
		return this.productController.getModel();
	}

	public void setProductLoon(ProductLoon loon) {
		this.productController.getUI().setUren(loon.getUren());
		this.productController.getUI().setUurloon(loon.getUurloon().getBedrag());
	}
}

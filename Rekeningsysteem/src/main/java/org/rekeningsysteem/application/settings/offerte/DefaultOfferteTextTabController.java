package org.rekeningsysteem.application.settings.offerte;

import javafx.scene.control.Tab;

import org.apache.log4j.Logger;

@Deprecated
public class DefaultOfferteTextTabController {

	private final Tab ui;

	public DefaultOfferteTextTabController(Logger logger) {
		this(new DefaultOfferteTextPaneController(logger));
	}

	public DefaultOfferteTextTabController(DefaultOfferteTextPaneController subController) {
		this.ui = new Tab("Offerte", subController.getUI());
	}

	public Tab getUI() {
		return this.ui;
	}
}

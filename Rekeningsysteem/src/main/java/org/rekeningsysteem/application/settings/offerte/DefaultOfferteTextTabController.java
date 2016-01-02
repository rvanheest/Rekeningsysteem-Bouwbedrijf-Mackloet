package org.rekeningsysteem.application.settings.offerte;

import org.apache.log4j.Logger;

public class DefaultOfferteTextTabController {

	private final DefaultOfferteTextTab ui;

	public DefaultOfferteTextTabController(Logger logger) {
		this(new DefaultOfferteTextPaneController(logger));
	}

	public DefaultOfferteTextTabController(DefaultOfferteTextPaneController subController) {
		this.ui = new DefaultOfferteTextTab(subController.getUI());
	}

	public DefaultOfferteTextTab getUI() {
		return this.ui;
	}
}

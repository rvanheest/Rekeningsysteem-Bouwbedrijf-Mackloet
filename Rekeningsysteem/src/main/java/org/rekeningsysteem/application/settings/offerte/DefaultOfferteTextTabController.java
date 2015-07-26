package org.rekeningsysteem.application.settings.offerte;

public class DefaultOfferteTextTabController {

	private final DefaultOfferteTextTab ui;

	public DefaultOfferteTextTabController() {
		this(new DefaultOfferteTextPaneController());
	}

	public DefaultOfferteTextTabController(DefaultOfferteTextPaneController subController) {
		this.ui = new DefaultOfferteTextTab(subController.getUI());
	}

	public DefaultOfferteTextTab getUI() {
		return this.ui;
	}
}

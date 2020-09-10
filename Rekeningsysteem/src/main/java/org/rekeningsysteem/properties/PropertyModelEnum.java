package org.rekeningsysteem.properties;

public enum PropertyModelEnum implements PropertyKey {

	FACTUURNUMMER("factuur.nummer"),

	OFFERTENUMMER("offerte.nummer"),
	OFFERTE_DEFAULT_TEXT_LOCATION("offerte.default-text-location"),

	LOONBTWPERCENTAGE("btwpercentage.loon"),
	MATERIAALBTWPERCENTAGE("btwpercentage.materiaal"),

	UURLOON("uurloon"),

	VALUTA("valuta"),
	VALUTAISO4217("valutaiso4217"),

	DATE_FORMAT("dateformat"),

	DATABASE("database"),
	LAST_SAVE_LOCATION("factuur.last-save-location"),
	LAST_SAVE_LOCATION_OFFERTE("offerte.last-save-location"),

	PDF_MUTATIES_TEMPLATE("pdf.template.mutaties"),
	PDF_OFFERTE_TEMPLATE("pdf.template.offerte"),
	PDF_PARTICULIER_TEMPLATE("pdf.template.particulier"),
	PDF_REPARATIES_TEMPLATE("pdf.template.reparaties"),

	FEATURE_MUTATIES("feature.mutaties"),
	FEATURE_REPARATIES("feature.reparaties"),
	FEATURE_PARTICULIER("feature.particulier"),
	FEATURE_PARTICULIER_ESSELINK_ARTIKEL("feature.particulier.esselink-artikel"),
	FEATURE_PARTICULIER_EIGEN_ARTIKEL("feature.particulier.eigen-artikel"),
	FEATURE_PARTICULIER_LOON("feature.particulier.loon"),
	FEATURE_PARTICULIER_LOON_PER_UUR("feature.particulier.loon-per-uur"),
	FEATURE_OFFERTE("feature.offerte"),

	APPLICATION_TITLE("application.title"),
	APPLICATION_ICON("application.icon"),
	APPLICATION_LOGO("application.logo"),
	APPLICATION_NAME_LOGO("application.name_logo"),
	APPLICATION_FULL_SCREEN_MODE("application.fullscreen");

	private String key;

	private PropertyModelEnum(String key) {
		this.key = key;
	}

	@Override
	public String getKey() {
		return this.key;
	}
}

package org.rekeningsysteem.properties;

public enum PropertyModelEnum implements PropertyKey {

	FACTUURNUMMER("factuurnummer"),
	FACTUURNUMMERFILE("factuurnummerfile"),

	OFFERTENUMMER("offertenummer"),
	OFFERTENUMMERFILE("offertenummerfile"),

	LOONBTWPERCENTAGE("loonbtwpercentage"),
	MATERIAALBTWPERCENTAGE("materiaalbtwpercentage"),

	UURLOON("uurloon"),

	VALUTA("valuta"),
	VALUTAISO4217("valutaiso4217"),
	
	DATE_FORMAT("dateformat"),
	
	DATABASE("database"),
	LAST_SAVE_LOCATION("lastsavelocation"),
	
	PDF_AANGENOMEN_TEMPLATE("pdfaangenomentemplate"),
	PDF_MUTATIES_TEMPLATE("pdfmutatiestemplate"),
	PDF_OFFERTE_TEMPLATE("pdfoffertetemplate"),
	PDF_PARTICULIER_TEMPLATE("pdfparticuliertemplate"),
	PDF_REPARATIES_TEMPLATE("pdfreparatiestemplate"),
	
	FULL_SCREEN("fullscreen");

	private String key;

	private PropertyModelEnum(String key) {
		this.key = key;
	}

	@Override
	public String getKey() {
		return this.key;
	}
}

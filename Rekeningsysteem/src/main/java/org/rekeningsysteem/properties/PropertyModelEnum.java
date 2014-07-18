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

	WONINGBOUW_NAAM("woningbouwnaam"),
	WONINGBOUW_STRAAT("woningbouwstraat"),
	WONINGBOUW_NUMMER("woningbouwnummer"),
	WONINGBOUW_POSTCODE("woningbouwpostcode"),
	WONINGBOUW_PLAATS("woningbouwplaats"),
	WONINGBOUW_BTWNUMMER("woningbouwbtwnummer"),

	PDF_AANGENOMEN_TEMPLATE("pdfaangenomentemplate"),
	PDF_MUTATIES_TEMPLATE("pdfmutatiestemplate"),
	PDF_OFFERTE_TEMPLATE("pdfoffertetemplate"),
	PDF_PARTICULIER_TEMPLATE("pdfparticuliertemplate"),
	PDF_REPARATIES_TEMPLATE("pdfreparatiestemplate");

	private String key;

	private PropertyModelEnum(String key) {
		this.key = key;
	}

	@Override
	public String getKey() {
		return this.key;
	}
}

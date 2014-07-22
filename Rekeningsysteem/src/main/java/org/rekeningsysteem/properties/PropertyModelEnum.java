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
	VALUTAISO4217("valutaiso4217");

	private String key;

	private PropertyModelEnum(String key) {
		this.key = key;
	}

	@Override
	public String getKey() {
		return this.key;
	}
}

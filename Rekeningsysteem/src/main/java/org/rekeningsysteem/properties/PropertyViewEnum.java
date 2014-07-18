package org.rekeningsysteem.properties;

public enum PropertyViewEnum implements PropertyKey {

	BEDRIJFICON("bedrijficon"),
	BEDRIJFLOGO("bedrijflogo"),
	BEDRIJFNAME("bedrijfnaam"),

	AANGENOMENLOGO("aangenomenlogo"),
	MUTATIESLOGO("mutatieslogo"),
	OFFERTELOGO("offertelogo"),
	PARTICULIERLOGO("particulierlogo"),
	REPARATIESLOGO("reparatieslogo"),
	OPENLOGO("openlogo"),
	SAVELOGO("savelogo"),
	EXPORTLOGO("exportlogo");

	private String key;

	private PropertyViewEnum(String key) {
		this.key = key;
	}

	@Override
	public String getKey() {
		return this.key;
	}
}

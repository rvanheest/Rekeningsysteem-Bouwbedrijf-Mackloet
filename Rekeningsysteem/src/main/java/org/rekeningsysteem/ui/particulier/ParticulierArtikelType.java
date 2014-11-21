package org.rekeningsysteem.ui.particulier;

import org.rekeningsysteem.ui.particulier.tabpane.ItemType;

public enum ParticulierArtikelType implements ItemType {
	
	ESSELINK("Esselink artikel"),
	ANDER("Eigen artikel");
	
	private final String tabname;
	
	private ParticulierArtikelType(String tabname) {
		this.tabname = tabname;
	}
	
	public String getTabName() {
		return this.tabname;
	}
}
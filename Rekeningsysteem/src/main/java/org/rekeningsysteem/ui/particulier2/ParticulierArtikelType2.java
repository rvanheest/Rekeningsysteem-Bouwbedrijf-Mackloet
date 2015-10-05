package org.rekeningsysteem.ui.particulier2;

import org.rekeningsysteem.ui.particulier.tabpane.ItemType;

// TODO ParticulierArtikelType
public enum ParticulierArtikelType2 implements ItemType {

	ESSELINK("Esselink artikel"),
	ANDER("Eigen artikel");

	private final String tabname;

	private ParticulierArtikelType2(String tabname) {
		this.tabname = tabname;
	}

	public String getTabName() {
		return this.tabname;
	}
}

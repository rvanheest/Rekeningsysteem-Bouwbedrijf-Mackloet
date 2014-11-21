package org.rekeningsysteem.ui.particulier.loon;

import org.rekeningsysteem.ui.particulier.tabpane.ItemType;

public enum LoonType implements ItemType {

	INSTANT("Instant loon"),
	PRODUCT("Product loon");
	
	private final String tabname;
	
	private LoonType(String tabname) {
		this.tabname = tabname;
	}
	
	public String getTabName() {
		return this.tabname;
	}
}

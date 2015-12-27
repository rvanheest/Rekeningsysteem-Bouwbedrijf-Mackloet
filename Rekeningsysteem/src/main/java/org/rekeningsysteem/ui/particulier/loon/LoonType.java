package org.rekeningsysteem.ui.particulier.loon;

import org.rekeningsysteem.ui.particulier.tabpane.ItemType;

@Deprecated
public enum LoonType implements ItemType {

	INSTANT("Loon"),
	PRODUCT("Loon per uur");
	
	private final String tabname;
	
	private LoonType(String tabname) {
		this.tabname = tabname;
	}
	
	public String getTabName() {
		return this.tabname;
	}
}

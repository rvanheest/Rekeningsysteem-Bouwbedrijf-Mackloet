package org.rekeningsysteem.application.working;

import javafx.scene.control.TabPane;

public class RekeningTabPane extends TabPane {

	public RekeningTabPane() {
		this.setId("rekening-tabs");
		this.setTabClosingPolicy(TabClosingPolicy.SELECTED_TAB);
	}

	public void addTab(RekeningTab tab) {
		this.getTabs().add(tab);
	}

	public void selectTab(RekeningTab tab) {
		this.getSelectionModel().select(tab);
	}

	public RekeningTab getSelectedTab() {
		return (RekeningTab) this.getSelectionModel().getSelectedItem();
	}
}

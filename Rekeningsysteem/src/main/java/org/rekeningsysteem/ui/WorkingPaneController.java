package org.rekeningsysteem.ui;

public abstract class WorkingPaneController {

	private WorkingPane ui;

	public WorkingPaneController(WorkingPane ui) {
		this.ui = ui;
	}

	public WorkingPane getUI() {
		return this.ui;
	}
}

package org.rekeningsysteem.ui.list;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

public class ButtonCell<T> extends TableCell<T, Boolean> {

	private final Button cellButton;

	public ButtonCell(Button button) {
		this.cellButton = button;
	}

	// Display button if the row is not empty
	@Override
	protected void updateItem(Boolean t, boolean empty) {
		super.updateItem(t, empty);
		if (empty) {
			this.setGraphic(null);
		}
		else {
			this.setGraphic(this.cellButton);
		}
	}
}

package org.rekeningsysteem.ui.list;

import javafx.scene.control.TableCell;
import org.rekeningsysteem.data.util.BtwPercentage;

public class BtwPercentageCell<T> extends TableCell<T, BtwPercentage> {

	@Override
	protected void updateItem(BtwPercentage item, boolean empty) {
		super.updateItem(item, empty);
		this.setText(item == null ? "" : item.formattedString());
	}
}

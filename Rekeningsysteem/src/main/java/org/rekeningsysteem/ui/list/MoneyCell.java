package org.rekeningsysteem.ui.list;

import java.text.NumberFormat;

import javafx.scene.paint.Color;

public class MoneyCell<T> extends DoubleCell<T> {

	public MoneyCell() {
		super(NumberFormat.getCurrencyInstance());
	}

	@Override
	protected void updateItem(Double item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null) {
			double value = item.doubleValue();
			this.setTextFill(value == 0 ? Color.BLACK : value < 0 ? Color.RED : Color.GREEN);
		}
	}
}

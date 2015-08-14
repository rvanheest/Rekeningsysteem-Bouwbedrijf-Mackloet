package org.rekeningsysteem.ui.list;

import java.text.NumberFormat;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;

public class MoneyCell<T> extends TableCell<T, Double> {

	@Override
	protected void updateItem(Double item, boolean empty) {
		super.updateItem(item, empty);
		this.setText(item == null ? "" : NumberFormat.getCurrencyInstance().format(item));
		if (item != null) {
			double value = item.doubleValue();
			this.setTextFill(value == 0 ? Color.BLACK : value < 0 ? Color.RED : Color.GREEN);
		}
		this.setAlignment(Pos.TOP_RIGHT);
	}
}

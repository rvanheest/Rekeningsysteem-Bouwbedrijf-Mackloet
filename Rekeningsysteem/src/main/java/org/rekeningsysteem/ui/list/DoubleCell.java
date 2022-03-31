package org.rekeningsysteem.ui.list;

import java.text.NumberFormat;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;

public class DoubleCell<T> extends TableCell<T, Double> {

	private final NumberFormat format;

	public DoubleCell(NumberFormat format) {
		this.format = format;
	}

	@Override
	protected void updateItem(Double item, boolean empty) {
		super.updateItem(item, empty);
		this.setText(item == null ? "" : this.format.format(item));
		this.setAlignment(Pos.TOP_RIGHT);
	}
}

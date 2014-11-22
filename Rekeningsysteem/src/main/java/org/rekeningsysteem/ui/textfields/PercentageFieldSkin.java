package org.rekeningsysteem.ui.textfields;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;

import org.rekeningsysteem.logging.ApplicationLogger;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;

public class PercentageFieldSkin implements Skin<PercentageField> {

	private PercentageField control;
	private TextField textField;
	private DecimalFormat formatter;
	private boolean ignoreValueUpdate = false;

	private final InvalidationListener percentageFieldFocusListener = obs -> {
		if (this.control.isFocused()) {
			this.textField.requestFocus();
		}
	};
	private final InvalidationListener percentageFieldValueListener = obs -> {
		if (!this.ignoreValueUpdate) {
			this.updateText();
		}
	};
	private final InvalidationListener percentageFieldStyleClassListener = obs -> this.textField
			.getStyleClass().setAll(this.control.getStyleClass());

	public PercentageFieldSkin(PercentageField control) {
		this.control = control;

		this.textField = new TextField() {

			@Override
			public void replaceText(int start, int end, String text) {
				String t = Optional.ofNullable(this.getText()).orElse("");
				t = t.substring(0, start) + text + t.substring(end);
				if (PercentageFieldSkin.this.accept(t)) {
					super.replaceText(start, end, text);
				}
			}

			@Override
			public void replaceSelection(String text) {
				String t = Optional.ofNullable(this.getText()).orElse("");
				int start = Math.min(this.getAnchor(), this.getCaretPosition());
				int end = Math.max(this.getAnchor(), this.getCaretPosition());
				t = t.substring(0, start) + text + t.substring(end);
				if (PercentageFieldSkin.this.accept(t)) {
					super.replaceSelection(text);
				}
			}
		};

		this.textField.setId("text-field");
		this.textField.getStyleClass().setAll(control.getStyleClass());
		this.control.getStyleClass().addListener(this.percentageFieldStyleClassListener);

		// Align the text to the right
		this.textField.setAlignment(Pos.BASELINE_RIGHT);
		this.textField.promptTextProperty().bind(control.promptTextProperty());
		this.textField.editableProperty().bind(control.editableProperty());
		this.textField.prefColumnCountProperty().bind(control.prefColumnCountProperty());

		// Whenever the value changes on the control, we need to update the text
		// in the TextField. The only time this is not the case is when the update
		// to the control happened as a result of an update in the text textField.
		this.control.valueProperty().addListener(this.percentageFieldValueListener);

		// Whenever the text of the textField changes, we may need to
		// update the value.
		this.textField.textProperty().addListener(obs -> this.updateValue());

		// Right now there is some funny business regarding focus in JavaFX. So
		// we will just make sure the TextField gets focus whenever somebody tries
		// to give it to the MoneyField. This isn't right, but we need to fix
		// this in JavaFX, I don't think I can hack around it
		this.textField.setFocusTraversable(false);
		this.control.focusedProperty().addListener(this.percentageFieldFocusListener);

		this.textField.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent actionEvent) {
				// Because TextFieldBehavior fires an action event on the parent of the TextField
				// (maybe a misfeature?) I don't need to do this. But I think this is
				// a bug, because having to add an empty event handler to get an
				// event on the control is odd to say the least!
				// control.fireEvent(new ActionEvent(textField, textField));
			}
		});

		// Make sure the text is updated to the initial state of the PercentageField value
		this.updateLocale();
		this.updateText();
	}

	@Override
	public PercentageField getSkinnable() {
		return this.control;
	}

	@Override
	public Node getNode() {
		return this.textField;
	}

	@Override
	public void dispose() {
		this.control.getStyleClass().removeListener(this.percentageFieldStyleClassListener);
		this.control.valueProperty().removeListener(this.percentageFieldValueListener);
		this.control.focusedProperty().removeListener(this.percentageFieldFocusListener);
		this.textField = null;
	}

	private boolean accept(String text) {
		text = text.trim();
		if (text.length() == 0) {
			return true;
		}

		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		if (text.endsWith("%")) {
			text = text.substring(0, text.length() - 1).trim();
		}

		char thousandsSeparator = symbols.getGroupingSeparator();
		char decimalSeparator = symbols.getMonetaryDecimalSeparator();
		boolean thousandsSeparatorInUse = false;
		int decimalSeparatorIndex = -1;
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			if (ch == thousandsSeparator) {
				thousandsSeparatorInUse = true;
			}
			else if (decimalSeparatorIndex == -1 && ch == decimalSeparator) {
				decimalSeparatorIndex = i;
			}
			else if (!Character.isDigit(ch)) {
				return false;
			}
		}

		if (decimalSeparatorIndex != -1) {
			int trailingLength = text.substring(decimalSeparatorIndex + 1).length();
			if (trailingLength > this.formatter.getCurrency().getDefaultFractionDigits()) {
				return false;
			}
		}

		if (thousandsSeparatorInUse) {
			if (text.charAt(0) == thousandsSeparator) {
				return false;
			}
			int count = 1;
			for (int i = (decimalSeparatorIndex == -1 ? text.length() - 1
					: decimalSeparatorIndex - 1); i > 0; i--, count++) {
				if (count % 4 == 0 && text.charAt(i) != thousandsSeparator) {
					return false;
				}
				else if (text.charAt(i) == thousandsSeparator) {
						return false;
				}
			}
		}
		return true;
	}

	private void updateLocale() {
		Locale locale = Locale.getDefault();
		this.formatter = (DecimalFormat) NumberFormat.getPercentInstance(locale);
		this.formatter.setParseBigDecimal(true);
	}

	private void updateText() {
		Optional<BigDecimal> value = Optional.ofNullable(this.control.getValue())
				.map(bd -> bd.divide(BigDecimal.valueOf(100)));
		this.textField.setText(value.map(this.formatter::format).orElse(""));
	}

	private void updateValue() {
		boolean updateText = false;
		try {
			this.ignoreValueUpdate = true;
			BigDecimal value = this.control.getValue();
			BigDecimal newValue;
			String text = this.textField.getText() == null ? "" : this.textField.getText().trim();
			String symbol = "%";
			if (text.equals("") || text.equals(symbol)) {
				newValue = value == null ? null : BigDecimal.ZERO;
			}
			else {
				if (!text.endsWith(symbol)) {
					text = text + symbol;
					updateText = true;
				}
				
				Number n = this.formatter.parse(text);
				newValue = n instanceof BigDecimal
						? (BigDecimal) n
								: new BigDecimal(n.doubleValue());
				newValue = newValue.multiply(BigDecimal.valueOf(100));
			}
			if (value != newValue && (value == null || newValue == null || !value.equals(newValue))) {
				this.control.setValue(newValue);
			}
		}
		catch (ParseException ex) {
			ApplicationLogger.getInstance().error(ex);
		}
		finally {
			this.ignoreValueUpdate = false;
			if (updateText) {
				// Weird bug where updating text while processing causes the caret
				// to end up in the wrong place.
				Platform.runLater(() -> {
					this.textField.appendText("%");
					if (this.textField.getText().length() == 2) {
						this.textField.positionCaret(1);
					}
					else {
						this.textField.positionCaret(this.textField.getCaretPosition());
					}
				});
			}
		}
	}
}

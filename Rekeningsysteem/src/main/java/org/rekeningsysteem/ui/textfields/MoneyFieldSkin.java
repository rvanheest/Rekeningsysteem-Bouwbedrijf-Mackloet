package org.rekeningsysteem.ui.textfields;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;

import org.rekeningsysteem.logging.ApplicationLogger;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;

/**
 * Doesn't support comma separated formatting, unfortunately. The parser gets all uptight about that
 * sort of stuff. There is a fair bit of hacking here too. Need a real i18n expert to do the
 * formatting and parsing work.
 * @author Richard Bair
 * @author Richard van Heest - me (modified for my purpose)
 * @see http://fxexperience.com/2012/02/moneyfield/
 */
public class MoneyFieldSkin implements Skin<MoneyField> {

	/**
	 * The {@code Control} that is referencing this Skin. There is a one-to-one relationship between
	 * a {@code Skin} and a {@code Control}. When a {@code Skin} is set on a {@code Control}, this
	 * variable is automatically updated.
	 */
	private final MoneyField control;

	/**
	 * This textField is used to represent the MoneyField.
	 */
	private TextField textField;

	/**
	 * This is the formatter which will be used to format the money when editing (it just gives us
	 * the amount)
	 */
	private final DecimalFormat formatter;

	/**
	 * This flag is set whenever MoneyFieldSkin is going to set the value itself. When it does that,
	 * it sets this flag so that the updated value is then not used to update the text, causing a
	 * potential infinite loop
	 */
	private boolean ignoreValueUpdate = false;

	private final InvalidationListener moneyFieldFocusListener;
	private final InvalidationListener moneyFieldValueListener;
	private final InvalidationListener moneyFieldStyleClassListener;

	/**
	 * Create a new MoneyFieldSkin.
	 * 
	 * @param control The MoneyField
	 */
	public MoneyFieldSkin(MoneyField control) {
		this.control = control;

		// Create the TextField that we are going to use to represent this MoneyFieldSkin.
		// The textField restricts input so that only valid digits that contribute to the
		// Money can be input.
		this.textField = new TextField() {

			@Override
			public void replaceText(int start, int end, String text) {
				// What I need to do is to first mock up what the result would be after
				// this modification has been made. I then need to check whether this
				// would end up being acceptable (either fully formatted with dollar symbols,
				// thousands and decimal separators, or just a number).
				String t = Optional.ofNullable(this.getText()).orElse("");
				if (MoneyFieldSkin.this.accept(t.substring(0, start) + text + t.substring(end))) {
					super.replaceText(start, end, text.replace('.', ','));
				}
			}

			@Override
			public void replaceSelection(String text) {
				// What I need to do is to first mock up what the result would be after
				// this modification has been made. I then need to check whether this
				// would end up being acceptable (either fully formatted with dollar symbols,
				// thousands and decimal separators, or just a number).
				String t = Optional.ofNullable(this.getText()).orElse("");
				int start = Math.min(this.getAnchor(), this.getCaretPosition());
				int end = Math.max(this.getAnchor(), this.getCaretPosition());
				t = t.substring(0, start) + text + t.substring(end);
				if (MoneyFieldSkin.this.accept(t.substring(0, start) + text + t.substring(end))) {
					super.replaceSelection(text.replace('.', ','));
				}
			}
		};

		this.moneyFieldFocusListener = obs -> {
			if (this.control.isFocused()) {
				this.textField.requestFocus();
			}
		};
		this.moneyFieldValueListener = obs -> {
			if (!this.ignoreValueUpdate) {
				this.updateText();
			}
		};
		this.moneyFieldStyleClassListener = obs -> this.textField
				.getStyleClass().setAll(this.control.getStyleClass());

		this.textField.setId("text-field");
		this.textField.getStyleClass().setAll(control.getStyleClass());
		this.control.getStyleClass().addListener(this.moneyFieldStyleClassListener);

		// Align the text to the right
		this.textField.setAlignment(Pos.BASELINE_RIGHT);
		this.textField.promptTextProperty().bind(control.promptTextProperty());
		this.textField.editableProperty().bind(control.editableProperty());
		this.textField.prefColumnCountProperty().bind(control.prefColumnCountProperty());

		// Whenever the value changes on the control, we need to update the text
		// in the TextField. The only time this is not the case is when the update
		// to the control happened as a result of an update in the text textField.
		this.control.valueProperty().addListener(this.moneyFieldValueListener);

		// Whenever the text of the textField changes, we may need to
		// update the value.
		this.textField.textProperty().addListener(obs -> this.updateValue());

		// Right now there is some funny business regarding focus in JavaFX. So
		// we will just make sure the TextField gets focus whenever somebody tries
		// to give it to the MoneyField. This isn't right, but we need to fix
		// this in JavaFX, I don't think I can hack around it
		this.textField.setFocusTraversable(false);
		this.control.focusedProperty().addListener(this.moneyFieldFocusListener);

		this.textField.setOnAction(actionEvent -> {
			// Because TextFieldBehavior fires an action event on the parent of the TextField
			// (maybe a misfeature?) I don't need to do this. But I think this is
			// a bug, because having to add an empty event handler to get an
			// event on the control is odd to say the least!
			// control.fireEvent(new ActionEvent(textField, textField));
		});

		// Make sure the text is updated to the initial state of the MoneyField value
		this.formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(new Locale("nl", "NL"));
		this.formatter.setParseBigDecimal(true);
		this.formatter.setCurrency(this.getSkinnable().getCurrency());
		
		// this is to support negative numbers in the form of "€ -12,02" (without quotes)
		this.formatter.setNegativePrefix(this.formatter.getCurrency().getSymbol() + " -");
		this.formatter.setNegativeSuffix("");
		this.updateText();
	}

	@Override
	public MoneyField getSkinnable() {
		return this.control;
	}

	@Override
	public Node getNode() {
		return this.textField;
	}

	/**
	 * Called by a Skinnable when the Skin is replaced on the Skinnable. This method allows a Skin
	 * to implement any logic necessary to clean up itself after the Skin is no longer needed. It
	 * may be used to release native resources. The methods {@link #getSkinnable()} and
	 * {@link #getNode()} should return null following a call to dispose. Calling dispose twice has
	 * no effect.
	 */
	@Override
	public void dispose() {
		this.control.getStyleClass().removeListener(this.moneyFieldStyleClassListener);
		this.control.valueProperty().removeListener(this.moneyFieldValueListener);
		this.control.focusedProperty().removeListener(this.moneyFieldFocusListener);
		this.textField = null;
	}

	private boolean accept(String text) {
		text = text.trim();
		if (text.length() == 0) {
			return true;
		}

		// There must only be at most a single currency symbol, and it must lead.
		String currencySymbol = this.getSkinnable().getCurrency().getSymbol();
		if (text.startsWith(currencySymbol)) {
			text = text.substring(currencySymbol.length()).trim();
		}
		
		String negativePrefix = "-";
		if (text.startsWith(negativePrefix)) {
			text = text.substring(negativePrefix.length()).trim();
		}

		// There must be no illegal characters.
		// If there is a thousands separator, then it must be used correctly
		// There may be only a single decimal separator
		char decimalSeparator = ',';
		char point = '.';
		int decimalSeparatorIndex = -1;
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			if (decimalSeparatorIndex == -1 && (ch == decimalSeparator || ch == point)) {
				decimalSeparatorIndex = i;
			}
			else if (!Character.isDigit(ch)) {
				return false;
			}
		}

		// If there is a decimal separator, count the number of trailing digits and validate the
		// length
		if (decimalSeparatorIndex != -1) {
			int trailingLength = text.substring(decimalSeparatorIndex + 1).length();
			return trailingLength <= this.getSkinnable().getCurrency().getDefaultFractionDigits();
		}
		return true;
	}
	
	private void updateText() {
		Optional<BigDecimal> value = Optional.ofNullable(this.control.getValue());
		this.textField.setText(value.map(this.formatter::format).orElse(""));
	}

	private void updateValue() {
		// Convert the text to a Money. Check the new Money with the one already there
		// on the MoneyField. If it has changed, then set it. Otherwise, just ignore this call.
		// Be sure to set the "ignoreValueChanged" flag so that we don't end up looping
		boolean updateText = false;
		try {
			this.ignoreValueUpdate = true;
			BigDecimal value = this.control.getValue();
			BigDecimal newValue;
			String text = this.textField.getText() == null ? "" : this.textField.getText().trim();
			// I have to clean some of this up because the formatter parsing isn't forgiving enough
			// I am probably incorrect in assuming the currency symbol goes at the front...
			String symbol = this.getSkinnable().getCurrency().getSymbol();
			String negative = "-";
			if (text.equals("") || text.equals(symbol) || text.equals(negative) || text.equals(symbol + " " + negative)) {
				// This thing is just the symbol or empty string, so newValue is going to be 0
				newValue = value == null ? null : BigDecimal.ZERO;
			}
			else {
				if (!text.startsWith(symbol)) {
					text = symbol + " " + text;
					updateText = true;
				}

				Number n = this.formatter.parse(text);
				newValue = n instanceof BigDecimal ? (BigDecimal) n : BigDecimal.valueOf(n.doubleValue());
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
					int caretPosition = this.textField.getCaretPosition();
					String currencySymbol = this.getSkinnable().getCurrency().getSymbol();
					
					this.textField.insertText(0, currencySymbol + " ");
					this.textField.positionCaret(caretPosition + currencySymbol.length() + 1);
				});
			}
		}
	}
}

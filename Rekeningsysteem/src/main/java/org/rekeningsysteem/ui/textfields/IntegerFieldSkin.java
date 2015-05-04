package org.rekeningsysteem.ui.textfields;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;

import org.rekeningsysteem.logging.ApplicationLogger;

import javafx.beans.InvalidationListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;

public class IntegerFieldSkin implements Skin<IntegerField> {

	private final IntegerField control;
	private TextField textField;
	private final DecimalFormat formatter;
	private boolean ignoreValueUpdate = false;

	private final InvalidationListener integerFieldFocusListener = obs -> {
		if (this.control.isFocused()) {
			this.textField.requestFocus();
		}
	};
	private final InvalidationListener integerFieldValueListener = obs -> {
		if (!this.ignoreValueUpdate) {
			this.updateText();
		}
	};
	private final InvalidationListener integerFieldStyleClassListener = obs -> this.textField
			.getStyleClass().setAll(this.control.getStyleClass());

	public IntegerFieldSkin(IntegerField control) {
		this.control = control;

		this.textField = new TextField() {

			@Override
			public void replaceText(int start, int end, String text) {
				String t = Optional.ofNullable(this.getText()).orElse("");
				if (IntegerFieldSkin.this.accept(t.substring(0, start) + text + t.substring(end))) {
					super.replaceText(start, end, text.replace('.', ','));
				}
			}

			@Override
			public void replaceSelection(String text) {
				String t = Optional.ofNullable(this.getText()).orElse("");
				int start = Math.min(this.getAnchor(), this.getCaretPosition());
				int end = Math.max(this.getAnchor(), this.getCaretPosition());
				if (IntegerFieldSkin.this.accept(t.substring(0, start) + text + t.substring(end))) {
					super.replaceSelection(text.replace('.', ','));
				}
			}
		};

		this.textField.setId("text-field");
		this.textField.getStyleClass().setAll(control.getStyleClass());
		this.control.getStyleClass().addListener(this.integerFieldStyleClassListener);

		// Align the text to the right
		this.textField.setAlignment(Pos.BASELINE_RIGHT);
		this.textField.promptTextProperty().bind(control.promptTextProperty());
		this.textField.editableProperty().bind(control.editableProperty());
		this.textField.prefColumnCountProperty().bind(control.prefColumnCountProperty());

		// Whenever the value changes on the control, we need to update the text
		// in the TextField. The only time this is not the case is when the update
		// to the control happened as a result of an update in the text textField.
		this.control.valueProperty().addListener(this.integerFieldValueListener);

		// Whenever the text of the textField changes, we may need to
		// update the value.
		this.textField.textProperty().addListener(obs -> this.updateValue());

		// Right now there is some funny business regarding focus in JavaFX. So
		// we will just make sure the TextField gets focus whenever somebody tries
		// to give it to the MoneyField. This isn't right, but we need to fix
		// this in JavaFX, I don't think I can hack around it
		this.textField.setFocusTraversable(false);
		this.control.focusedProperty().addListener(this.integerFieldFocusListener);

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
		this.formatter = (DecimalFormat) NumberFormat.getIntegerInstance(Locale.getDefault());
		this.formatter.setParseIntegerOnly(true);
		this.updateText();
	}

	@Override
	public IntegerField getSkinnable() {
		return this.control;
	}

	@Override
	public Node getNode() {
		return this.textField;
	}

	@Override
	public void dispose() {
		this.control.getStyleClass().removeListener(this.integerFieldStyleClassListener);
		this.control.valueProperty().removeListener(this.integerFieldValueListener);
		this.control.focusedProperty().removeListener(this.integerFieldFocusListener);
		this.textField = null;
	}

	private boolean accept(String text) {
		text = text.trim();
		if (text.length() == 0) {
			return true;
		}
		
		String negativePrefix = "-";
		if (text.startsWith(negativePrefix)) {
			text = text.substring(negativePrefix.length()).trim();
		}

		return text.chars().allMatch(Character::isDigit);
	}

	private void updateText() {
		Optional<Integer> value = Optional.ofNullable(this.control.getValue());
		this.textField.setText(value.map(this.formatter::format).orElse(""));
	}

	private void updateValue() {
		try {
			this.ignoreValueUpdate = true;
			Integer value = this.control.getValue();
			Integer newValue;
			String text = this.textField.getText() == null ? "" : this.textField.getText().trim();
			String negative = "-";
			if (text.equals("") || text.equals(negative)) {
				newValue = value == null ? null : 0;
			}
			else {
				Number n = this.formatter.parse(text);
				newValue = n instanceof Integer ? (Integer) n : n.intValue();
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
		}
	}
}

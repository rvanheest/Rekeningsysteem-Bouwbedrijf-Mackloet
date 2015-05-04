package org.rekeningsysteem.ui.textfields;

import java.math.BigDecimal;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Control;

public class NumberField extends Control {

	public static final int DEFAULT_PREF_COLUMN_COUNT = 12;

	private ObjectProperty<BigDecimal> value = new SimpleObjectProperty<BigDecimal>(this, "number");

	public final BigDecimal getValue() {
		return this.value.get();
	}

	public final void setValue(BigDecimal value) {
		this.value.set(value);
	}

	public final ObjectProperty<BigDecimal> valueProperty() {
		return this.value;
	}

	private BooleanProperty editable = new SimpleBooleanProperty(this, "editable", true);

	public final boolean isEditable() {
		return this.editable.getValue();
	}

	public final void setEditable(boolean value) {
		this.editable.setValue(value);
	}

	public final BooleanProperty editableProperty() {
		return this.editable;
	}

	private StringProperty promptText = new StringPropertyBase("") {

		@Override
		protected void invalidated() {
			// Strip out newlines
			String txt = get();
			if (txt != null && txt.contains("\n")) {
				txt = txt.replace("\n", "");
				set(txt);
			}
		}

		@Override
		public Object getBean() {
			return NumberField.this;
		}

		@Override
		public String getName() {
			return "promptText";
		}
	};

	public final StringProperty promptTextProperty() {
		return this.promptText;
	}

	public final String getPromptText() {
		return this.promptText.get();
	}

	public final void setPromptText(String value) {
		this.promptText.set(value);
	}

	private IntegerProperty prefColumnCount = new IntegerPropertyBase(DEFAULT_PREF_COLUMN_COUNT) {

		@Override
		public void set(int value) {
			if (value < 0) {
				throw new IllegalArgumentException("value cannot be negative.");
			}

			super.set(value);
		}

		@Override
		public Object getBean() {
			return NumberField.this;
		}

		@Override
		public String getName() {
			return "prefColumnCount";
		}
	};

	public final IntegerProperty prefColumnCountProperty() {
		return this.prefColumnCount;
	}

	public final int getPrefColumnCount() {
		return this.prefColumnCount.getValue();
	}

	public final void setPrefColumnCount(int value) {
		this.prefColumnCount.setValue(value);
	}

	private ObjectProperty<EventHandler<ActionEvent>> onAction = new ObjectPropertyBase<EventHandler<ActionEvent>>() {

		@Override
		protected void invalidated() {
			NumberField.this.setEventHandler(ActionEvent.ACTION, get());
		}

		@Override
		public Object getBean() {
			return NumberField.this;
		}

		@Override
		public String getName() {
			return "onAction";
		}
	};

	public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
		return this.onAction;
	}

	public final EventHandler<ActionEvent> getOnAction() {
		return this.onActionProperty().get();
	}

	public final void setOnAction(EventHandler<ActionEvent> value) {
		this.onActionProperty().set(value);
	}

	public NumberField() {
		this.getStyleClass().setAll("number-field");
	}

	@Override
	public String getUserAgentStylesheet() {
		return this.getClass().getResource("/numberfield.css").toExternalForm();
	}
}

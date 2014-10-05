package org.rekeningsysteem.ui.textfields;

import java.math.BigDecimal;
import java.util.Currency;

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

import com.google.inject.Inject;

/**
 * A Control for handling the editing of Monetary values. This control uses BigDecimal as the
 * representation for Money, and uses the default Locale to define how that BigDecimal is to be
 * interpreted.
 *
 * <p>
 * MoneyField shares much API in common with TextInputControl and TextField. Specifically, you can
 * set the <code>promptText</code>, whether or not the control is <code>editable</code>, and the
 * <code>prefColumnCount</code>. You can also add an action event handler which is called when the
 * user presses the enter key while the MoneyField has focus.
 * </p>
 *
 * <p>
 * The MoneyField has all the same CSS styles supported by TextField, including the "readonly"
 * pseudo class state.
 * </p>
 *
 * <h2>Examples</h2>
 *
 * <h3>Basic Usage</h3>
 * <p>
 * This is an example of the most basic usage of a MoneyField. Here I will create a simple
 * MoneyField and explicitly set its value.
 * </p>
 * 
 * <pre>
 * <code>
 *     MoneyField field = new MoneyField();
 *     field.setValue(new BigDecimal(100));
 * </code>
 * </pre>
 *
 * <h3>Bidirectionally Binding With A Data Model</h3>
 * <p>
 * Here we have a MoneyField which bidirectionally binds to some data model which contains a
 * BigDecimal.
 * </p>
 * 
 * <pre>
 * <code>
 *     LineItem lineItem = ...;
 *     MoneyField field = new MoneyField();
 *     field.valueProperty().bindBidirectional(lineItem.pricePerUnitProperty());
 * </code>
 * </pre>
 */
public class MoneyField extends Control {
	
	/**
	 * The default value for {@link #prefColumnCount}.
	 */
	public static final int DEFAULT_PREF_COLUMN_COUNT = 12;
	
	/**
	 * The {@code MoneyField}'s currency to display, or <tt>null</tt> if no currency is displayed.
	 */
	private final ObjectProperty<Currency> currency = new SimpleObjectProperty<>(this, "currency");

	public final Currency getCurrency() {
		return this.currency.get();
	}

	public final void setCurrency(Currency value) {
		this.currency.set(value);
	}

	public final ObjectProperty<Currency> currencyProperty() {
		return this.currency;
	}

	/**
	 * The value of the MoneyField. If null, the value will be treated as "0", but will still
	 * actually be null.
	 */
	private ObjectProperty<BigDecimal> value = new SimpleObjectProperty<>(this, "value");

	public final BigDecimal getValue() {
		return this.value.get();
	}

	public final void setValue(BigDecimal value) {
		this.value.set(value);
	}

	public final ObjectProperty<BigDecimal> valueProperty() {
		return this.value;
	}

	/**
	 * Indicates whether this MoneyField can be edited by the user. If true, the "readonly" pseudo
	 * class will be false, but if false, the "readonly" pseudo class will be true.
	 */
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

	/**
	 * The {@code MoneyField}'s prompt text to display, or <tt>null</tt> if no prompt text is
	 * displayed.
	 */
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
			return MoneyField.this;
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

	/**
	 * The preferred number of text columns. This is used for calculating the {@code MoneyField}'s
	 * preferred width.
	 */
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
			return MoneyField.this;
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

	/**
	 * The action handler associated with this MoneyField, or <tt>null</tt> if no action handler is
	 * assigned.
	 *
	 * The action handler is normally called when the user types the ENTER key.
	 */
	private ObjectProperty<EventHandler<ActionEvent>> onAction = new ObjectPropertyBase<EventHandler<ActionEvent>>() {

		@Override
		protected void invalidated() {
			MoneyField.this.setEventHandler(ActionEvent.ACTION, get());
		}

		@Override
		public Object getBean() {
			return MoneyField.this;
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

	/**
	 * Creates a new MoneyField. The style class is set to "money-field".
	 * @param currency The currency of the money
	 */
	@Inject
	public MoneyField(Currency currency) {
		this.setCurrency(currency);
		
		this.getStyleClass().setAll("money-field");
	}

	@Override
	protected String getUserAgentStylesheet() {
		return this.getClass().getResource("/moneyfield.css").toExternalForm();
	}
}

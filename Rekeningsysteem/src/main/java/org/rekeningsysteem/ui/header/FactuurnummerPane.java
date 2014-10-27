package org.rekeningsysteem.ui.header;

import java.util.Objects;
import java.util.Optional;

import javafx.scene.control.Label;

import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.Page;

import rx.Observable;

public class FactuurnummerPane extends Page {

	public enum FactuurnummerType {
		FACTUUR("Factuurnummer"),
		OFFERTE("Offertenummer");

		private final String type;

		private FactuurnummerType(String type) {
			this.type = type;
		}

		public String getType() {
			return this.type;
		}
	}

	private static final String emptyText = "Er is nog geen factuurnummer toegekend aan deze factuur";

	private Label factNrL = new Label(emptyText);
	private Observable<Optional<String>> factuurnummer;

	public FactuurnummerPane(FactuurnummerType type) {
		super(type.getType());

		this.getChildren().add(this.factNrL);

		Observable<String> text = Observables.fromProperty(this.factNrL.textProperty());
		this.factuurnummer = text.map(s -> {
			if (Objects.isNull(s) || s.isEmpty() || emptyText.equals(s)) {
				return Optional.empty();
			}
				else {
					return Optional.of(s);
				}
			});
	}

	public Observable<Optional<String>> getFactuurnummer() {
		return this.factuurnummer;
	}

	public void setFactuurnummer(String factuurnummer) {
		this.factNrL.setText(factuurnummer);
	}
}

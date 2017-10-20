package org.rekeningsysteem.ui.header;

import java.util.Objects;
import java.util.Optional;

import javafx.scene.control.Label;

import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.Page;

import rx.Observable;

public class FactuurnummerPane extends Page {

	public enum FactuurnummerType {
		FACTUUR("Factuurnummer", "factuurnummer", "factuur"),
		OFFERTE("Offertenummer", "offertenummer", "offerte");

		private final String name;
		private final String name_lowercase;
		private final String type;

		FactuurnummerType(String name, String name_lowercase, String type) {
			this.name = name;
			this.name_lowercase = name_lowercase;
			this.type = type;
		}

		public String getName() {
			return this.name;
		}

		public String getNameLowercase() {
			return this.name_lowercase;
		}

		public String getType() {
			return this.type;
		}
	}

	private final String emptyText;

	private final Label factNrL;
	private final Observable<Optional<String>> factuurnummer;

	public FactuurnummerPane(FactuurnummerType type) {
		super(type.getName());

		this.emptyText = String.format("Er is nog geen %s toegekend aan deze %s", type.getNameLowercase(), type.getType());
		this.factNrL = new Label(this.emptyText);

		this.getChildren().add(this.factNrL);

		this.factuurnummer = Observables.fromProperty(this.factNrL.textProperty())
				.map(s -> Objects.isNull(s) || s.isEmpty() || this.emptyText.equals(s)
						? Optional.empty()
						: Optional.of(s));
	}

	public Observable<Optional<String>> getFactuurnummer() {
		return this.factuurnummer;
	}

	public void setFactuurnummer(Optional<String> factuurnummer) {
		this.factNrL.setText(factuurnummer.orElse(this.emptyText));
	}
}

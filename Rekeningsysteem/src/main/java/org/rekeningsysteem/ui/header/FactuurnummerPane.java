package org.rekeningsysteem.ui.header;

import java.util.Objects;
import java.util.Optional;

import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.Label;

import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.Page;

public class FactuurnummerPane extends Page {

	public enum FactuurnummerType {
		FACTUUR("Factuurnummer", "factuur"),
		OFFERTE("Offertenummer", "offerte");

		private final String name;
		private final String type;

		FactuurnummerType(String name, String type) {
			this.name = name;
			this.type = type;
		}

		public String getName() {
			return this.name;
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

		this.emptyText = String.format("Er is nog geen %s toegekend aan deze %s", type.getName().toLowerCase(), type.getType());
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

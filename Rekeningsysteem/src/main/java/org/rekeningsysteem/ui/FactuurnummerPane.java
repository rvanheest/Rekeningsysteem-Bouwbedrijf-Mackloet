package org.rekeningsysteem.ui;

import java.util.Objects;
import java.util.Optional;

import javafx.scene.control.Label;

import org.rekeningsysteem.rxjavafx.Observables;

import com.google.inject.Inject;

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

	private Label factNrL = new Label("Er is nog geen factuurnummer toegekend aan deze factuur");
	private Observable<Optional<String>> factuurnummer;

	@Inject
	public FactuurnummerPane(FactuurnummerType type) {
		super(type.getType());

		this.getChildren().add(this.factNrL);

		Observable<String> text = Observables.fromProperty(this.factNrL.textProperty());
		this.factuurnummer = Observable.merge(
				text.filter(s -> Objects.isNull(s) || s.isEmpty())
						.map(s -> Optional.empty()),
				text.filter(Objects::nonNull)
						.filter(s -> !s.isEmpty())
						.map(Optional::of));
	}

	public Observable<Optional<String>> getFactuurnummer() {
		return this.factuurnummer;
	}

	public void setFactuurnummer(String factuurnummer) {
		this.factNrL.setText(factuurnummer);
	}
}

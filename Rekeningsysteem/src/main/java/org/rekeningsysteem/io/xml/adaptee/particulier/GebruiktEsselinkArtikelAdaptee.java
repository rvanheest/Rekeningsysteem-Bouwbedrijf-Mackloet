package org.rekeningsysteem.io.xml.adaptee.particulier;

import java.util.function.Function;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdapteeVisitable;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdapteeVisitor;
import org.rekeningsysteem.io.xml.adapter.particulier.EsselinkArtikelAdapter;
import org.rekeningsysteem.io.xml.adapter.util.BtwPercentageAdapter;

@XmlRootElement(name = "gebruikt-esselink-artikel")
@XmlType(propOrder = { "omschrijving", "artikel", "aantal", "materiaalBtwPercentage" })
public class GebruiktEsselinkArtikelAdaptee extends ListItemAdapteeVisitable {

	private String omschrijving;
	private EsselinkArtikel artikel;
	private double aantal;
	private BtwPercentage materiaalBtwPercentage;

	private GebruiktEsselinkArtikelAdaptee() {
	}

	@XmlElement
	public String getOmschrijving() {
		return this.omschrijving;
	}

	public GebruiktEsselinkArtikelAdaptee setOmschrijving(String omschrijving) {
		this.omschrijving = omschrijving;
		return this;
	}

	@XmlJavaTypeAdapter(EsselinkArtikelAdapter.class)
	public EsselinkArtikel getArtikel() {
		return this.artikel;
	}

	public GebruiktEsselinkArtikelAdaptee setArtikel(EsselinkArtikel artikel) {
		this.artikel = artikel;
		return this;
	}

	@XmlElement
	public double getAantal() {
		return this.aantal;
	}

	public GebruiktEsselinkArtikelAdaptee setAantal(double aantal) {
		this.aantal = aantal;
		return this;
	}

	@XmlJavaTypeAdapter(BtwPercentageAdapter.class)
	public BtwPercentage getMateriaalBtwPercentage() {
		return this.materiaalBtwPercentage;
	}

	public GebruiktEsselinkArtikelAdaptee setMateriaalBtwPercentage(BtwPercentage materiaalBtwPercentage) {
		this.materiaalBtwPercentage = materiaalBtwPercentage;
		return this;
	}

	@Override
	public <T> T accept(ListItemAdapteeVisitor<T> visitor) {
		return visitor.visit(this);
	}

	public static GebruiktEsselinkArtikelAdaptee build(
			Function<GebruiktEsselinkArtikelAdaptee, GebruiktEsselinkArtikelAdaptee> builder) {
		return builder.apply(new GebruiktEsselinkArtikelAdaptee());
	}
}

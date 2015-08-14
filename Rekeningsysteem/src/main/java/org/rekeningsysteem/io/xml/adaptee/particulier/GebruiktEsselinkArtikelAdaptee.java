package org.rekeningsysteem.io.xml.adaptee.particulier;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.io.xml.adapter.particulier.EsselinkArtikelAdapter;

@XmlRootElement(name = "gebruikt-esselink-artikel")
@XmlType(propOrder = { "artikel", "aantal", "materiaalBtwPercentage" })
public class GebruiktEsselinkArtikelAdaptee extends ParticulierArtikelAdaptee {

	private EsselinkArtikel artikel;
	private double aantal;
	private double materiaalBtwPercentage;

	@XmlJavaTypeAdapter(EsselinkArtikelAdapter.class)
	public EsselinkArtikel getArtikel() {
		return this.artikel;
	}

	public void setArtikel(EsselinkArtikel artikel) {
		this.artikel = artikel;
	}

	@XmlElement
	public double getAantal() {
		return this.aantal;
	}

	public void setAantal(double aantal) {
		this.aantal = aantal;
	}

	@XmlElement
	public double getMateriaalBtwPercentage() {
		return this.materiaalBtwPercentage;
	}

    public void setMateriaalBtwPercentage(double materiaalBtwPercentage) {
    	this.materiaalBtwPercentage = materiaalBtwPercentage;
    }
}

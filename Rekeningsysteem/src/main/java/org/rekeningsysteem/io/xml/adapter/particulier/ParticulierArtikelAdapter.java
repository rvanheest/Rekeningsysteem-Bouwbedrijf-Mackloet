package org.rekeningsysteem.io.xml.adapter.particulier;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.io.xml.adaptee.particulier.AnderArtikelAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.GebruiktEsselinkArtikelAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.ParticulierArtikelAdaptee;

public class ParticulierArtikelAdapter extends
		XmlAdapter<ParticulierArtikelAdaptee, ParticulierArtikel> {

	@Override
	public ParticulierArtikel unmarshal(ParticulierArtikelAdaptee adaptee) {
		if (adaptee instanceof AnderArtikelAdaptee) {
			return this.unmarshal((AnderArtikelAdaptee) adaptee);
		}
		else if (adaptee instanceof GebruiktEsselinkArtikelAdaptee) {
			return this.unmarshal((GebruiktEsselinkArtikelAdaptee) adaptee);
		}
		return null;
	}

	private AnderArtikel unmarshal(AnderArtikelAdaptee adaptee) {
		return new AnderArtikel(adaptee.getOmschrijving(), adaptee.getPrijs());
	}

	private GebruiktEsselinkArtikel unmarshal(GebruiktEsselinkArtikelAdaptee adaptee) {
		return new GebruiktEsselinkArtikel(adaptee.getArtikel(), adaptee.getAantal());
	}

	@Override
	public ParticulierArtikelAdaptee marshal(ParticulierArtikel v) {
		if (v instanceof AnderArtikel) {
			return this.marshal((AnderArtikel) v);
		}
		else if (v instanceof GebruiktEsselinkArtikel) {
			return this.marshal((GebruiktEsselinkArtikel) v);
		}
		return null;
	}

	private AnderArtikelAdaptee marshal(AnderArtikel artikel) {
		AnderArtikelAdaptee adaptee = new AnderArtikelAdaptee();
		adaptee.setOmschrijving(artikel.getOmschrijving());
		adaptee.setPrijs(artikel.getMateriaal());
		return adaptee;
	}

	private GebruiktEsselinkArtikelAdaptee marshal(GebruiktEsselinkArtikel artikel) {
		GebruiktEsselinkArtikelAdaptee adaptee = new GebruiktEsselinkArtikelAdaptee();
		adaptee.setArtikel(artikel.getArtikel());
		adaptee.setAantal(artikel.getAantal());
		return adaptee;
	}
}

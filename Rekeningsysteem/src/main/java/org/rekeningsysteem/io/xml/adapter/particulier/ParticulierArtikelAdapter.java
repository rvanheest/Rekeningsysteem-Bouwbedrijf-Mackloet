package org.rekeningsysteem.io.xml.adapter.particulier;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.io.xml.adaptee.particulier.GebruiktEsselinkArtikelAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.ParticulierArtikelAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.AnderArtikelAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.InstantLoonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.ProductLoonAdaptee;

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
		else if (adaptee instanceof InstantLoonAdaptee) {
			return this.unmarshall((InstantLoonAdaptee) adaptee);
		}
		else if (adaptee instanceof ProductLoonAdaptee) {
			return this.unmarshall((ProductLoonAdaptee) adaptee);
		}
		return null;
	}

	private AnderArtikel unmarshal(AnderArtikelAdaptee adaptee) {
		return new AnderArtikel(adaptee.getOmschrijving(), adaptee.getPrijs(),
				adaptee.getMateriaalBtwPercentage());
	}

	private GebruiktEsselinkArtikel unmarshal(GebruiktEsselinkArtikelAdaptee adaptee) {
		return new GebruiktEsselinkArtikel(adaptee.getOmschrijving(), adaptee.getArtikel(),
				adaptee.getAantal(), adaptee.getMateriaalBtwPercentage());
	}

	private InstantLoon unmarshall(InstantLoonAdaptee adaptee) {
		return new InstantLoon(adaptee.getOmschrijving(), adaptee.getLoon(),
				adaptee.getLoonBtwPercentage());
	}

	private ProductLoon unmarshall(ProductLoonAdaptee adaptee) {
		return new ProductLoon(adaptee.getOmschrijving(), adaptee.getUren(), adaptee.getUurloon(),
				adaptee.getLoonBtwPercentage());
	}

	@Override
	public ParticulierArtikelAdaptee marshal(ParticulierArtikel v) {
		if (v instanceof AnderArtikel) {
			return this.marshal((AnderArtikel) v);
		}
		else if (v instanceof GebruiktEsselinkArtikel) {
			return this.marshal((GebruiktEsselinkArtikel) v);
		}
		else if (v instanceof InstantLoon) {
			return this.marshal((InstantLoon) v);
		}
		else if (v instanceof ProductLoon) {
			return this.marshal((ProductLoon) v);
		}
		return null;
	}

	private AnderArtikelAdaptee marshal(AnderArtikel artikel) {
		AnderArtikelAdaptee adaptee = new AnderArtikelAdaptee();
		adaptee.setOmschrijving(artikel.getOmschrijving());
		adaptee.setPrijs(artikel.getMateriaal());
		adaptee.setMateriaalBtwPercentage(artikel.getMateriaalBtwPercentage());
		return adaptee;
	}

	private GebruiktEsselinkArtikelAdaptee marshal(GebruiktEsselinkArtikel artikel) {
		GebruiktEsselinkArtikelAdaptee adaptee = new GebruiktEsselinkArtikelAdaptee();
		adaptee.setOmschrijving(artikel.getOmschrijving());
		adaptee.setArtikel(artikel.getArtikel());
		adaptee.setAantal(artikel.getAantal());
		adaptee.setMateriaalBtwPercentage(artikel.getMateriaalBtwPercentage());
		return adaptee;
	}

	private InstantLoonAdaptee marshal(InstantLoon loon) {
		InstantLoonAdaptee adaptee = new InstantLoonAdaptee();
		adaptee.setOmschrijving(loon.getOmschrijving());
		adaptee.setLoon(loon.getLoon());
		adaptee.setLoonBtwPercentage(loon.getLoonBtwPercentage());

		return adaptee;
	}

	private ProductLoonAdaptee marshal(ProductLoon loon) {
		ProductLoonAdaptee adaptee = new ProductLoonAdaptee();
		adaptee.setOmschrijving(loon.getOmschrijving());
		adaptee.setUren(loon.getUren());
		adaptee.setUurloon(loon.getUurloon());
		adaptee.setLoonBtwPercentage(loon.getLoonBtwPercentage());
		return adaptee;
	}
}

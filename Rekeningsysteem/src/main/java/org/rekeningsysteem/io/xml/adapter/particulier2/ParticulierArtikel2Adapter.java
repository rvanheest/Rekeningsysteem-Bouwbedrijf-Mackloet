package org.rekeningsysteem.io.xml.adapter.particulier2;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.particulier2.ParticulierArtikel2Impl;
import org.rekeningsysteem.data.particulier2.EsselinkParticulierArtikel;
import org.rekeningsysteem.data.particulier2.ParticulierArtikel2;
import org.rekeningsysteem.data.particulier2.loon.InstantLoon2;
import org.rekeningsysteem.data.particulier2.loon.ProductLoon2;
import org.rekeningsysteem.io.xml.adaptee.particulier2.ParticulierArtikel2ImplAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier2.EsselinkParticulierArtikelAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier2.ParticulierArtikel2Adaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier2.loon.InstantLoon2Adaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier2.loon.ProductLoon2Adaptee;

public class ParticulierArtikel2Adapter extends
		XmlAdapter<ParticulierArtikel2Adaptee, ParticulierArtikel2> {

	@Override
	public ParticulierArtikel2 unmarshal(ParticulierArtikel2Adaptee adaptee) {
		if (adaptee instanceof ParticulierArtikel2ImplAdaptee) {
			return this.unmarshal((ParticulierArtikel2ImplAdaptee) adaptee);
		}
		else if (adaptee instanceof EsselinkParticulierArtikelAdaptee) {
			return this.unmarshal((EsselinkParticulierArtikelAdaptee) adaptee);
		}
		else if (adaptee instanceof InstantLoon2Adaptee) {
			return this.unmarshall((InstantLoon2Adaptee) adaptee);
		}
		else if (adaptee instanceof ProductLoon2Adaptee) {
			return this.unmarshall((ProductLoon2Adaptee) adaptee);
		}
		return null;
	}

	private ParticulierArtikel2Impl unmarshal(ParticulierArtikel2ImplAdaptee adaptee) {
		return new ParticulierArtikel2Impl(adaptee.getOmschrijving(), adaptee.getPrijs(),
				adaptee.getMateriaalBtwPercentage());
	}

	private EsselinkParticulierArtikel unmarshal(EsselinkParticulierArtikelAdaptee adaptee) {
		return new EsselinkParticulierArtikel(adaptee.getOmschrijving(), adaptee.getArtikel(),
				adaptee.getAantal(), adaptee.getMateriaalBtwPercentage());
	}

	private InstantLoon2 unmarshall(InstantLoon2Adaptee adaptee) {
		return new InstantLoon2(adaptee.getOmschrijving(), adaptee.getLoon(),
				adaptee.getLoonBtwPercentage());
	}

	private ProductLoon2 unmarshall(ProductLoon2Adaptee adaptee) {
		return new ProductLoon2(adaptee.getOmschrijving(), adaptee.getUren(), adaptee.getUurloon(),
				adaptee.getLoonBtwPercentage());
	}

	@Override
	public ParticulierArtikel2Adaptee marshal(ParticulierArtikel2 v) {
		if (v instanceof ParticulierArtikel2Impl) {
			return this.marshal((ParticulierArtikel2Impl) v);
		}
		else if (v instanceof EsselinkParticulierArtikel) {
			return this.marshal((EsselinkParticulierArtikel) v);
		}
		else if (v instanceof InstantLoon2) {
			return this.marshal((InstantLoon2) v);
		}
		else if (v instanceof ProductLoon2) {
			return this.marshal((ProductLoon2) v);
		}
		return null;
	}

	private ParticulierArtikel2ImplAdaptee marshal(ParticulierArtikel2Impl artikel) {
		ParticulierArtikel2ImplAdaptee adaptee = new ParticulierArtikel2ImplAdaptee();
		adaptee.setOmschrijving(artikel.getOmschrijving());
		adaptee.setPrijs(artikel.getMateriaal());
		adaptee.setMateriaalBtwPercentage(artikel.getMateriaalBtwPercentage());
		return adaptee;
	}

	private EsselinkParticulierArtikelAdaptee marshal(EsselinkParticulierArtikel artikel) {
		EsselinkParticulierArtikelAdaptee adaptee = new EsselinkParticulierArtikelAdaptee();
		adaptee.setOmschrijving(artikel.getOmschrijving());
		adaptee.setArtikel(artikel.getArtikel());
		adaptee.setAantal(artikel.getAantal());
		adaptee.setMateriaalBtwPercentage(artikel.getMateriaalBtwPercentage());
		return adaptee;
	}

	private InstantLoon2Adaptee marshal(InstantLoon2 loon) {
		InstantLoon2Adaptee adaptee = new InstantLoon2Adaptee();
		adaptee.setOmschrijving(loon.getOmschrijving());
		adaptee.setLoon(loon.getLoon());
		adaptee.setLoonBtwPercentage(loon.getLoonBtwPercentage());

		return adaptee;
	}

	private ProductLoon2Adaptee marshal(ProductLoon2 loon) {
		ProductLoon2Adaptee adaptee = new ProductLoon2Adaptee();
		adaptee.setOmschrijving(loon.getOmschrijving());
		adaptee.setUren(loon.getUren());
		adaptee.setUurloon(loon.getUurloon());
		adaptee.setLoonBtwPercentage(loon.getLoonBtwPercentage());
		return adaptee;
	}
}

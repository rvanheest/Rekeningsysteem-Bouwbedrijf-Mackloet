package org.rekeningsysteem.io.xml.adapter.particulier;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.io.xml.adaptee.particulier.EsselinkArtikelAdaptee;

public class EsselinkArtikelAdapter extends XmlAdapter<EsselinkArtikelAdaptee, EsselinkArtikel> {

	@Override
	public EsselinkArtikel unmarshal(EsselinkArtikelAdaptee adaptee) {
		return new EsselinkArtikel(adaptee.getArtikelNummer(), adaptee.getOmschrijving(),
				adaptee.getPrijsPer(), adaptee.getEenheid(), adaptee.getVerkoopPrijs());
	}

	@Override
	public EsselinkArtikelAdaptee marshal(EsselinkArtikel esselinkArtikel) {
		EsselinkArtikelAdaptee adaptee = new EsselinkArtikelAdaptee();
		adaptee.setArtikelNummer(esselinkArtikel.getArtikelNummer());
		adaptee.setOmschrijving(esselinkArtikel.getOmschrijving());
		adaptee.setPrijsPer(esselinkArtikel.getPrijsPer());
		adaptee.setEenheid(esselinkArtikel.getEenheid());
		adaptee.setVerkoopPrijs(esselinkArtikel.getVerkoopPrijs());
		return adaptee;
	}
}

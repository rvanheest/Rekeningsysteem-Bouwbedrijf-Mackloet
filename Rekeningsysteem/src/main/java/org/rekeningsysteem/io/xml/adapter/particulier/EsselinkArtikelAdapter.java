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
		return EsselinkArtikelAdaptee.build(a -> a
        		.setArtikelNummer(esselinkArtikel.getArtikelNummer())
        		.setOmschrijving(esselinkArtikel.getOmschrijving())
        		.setPrijsPer(esselinkArtikel.getPrijsPer())
        		.setEenheid(esselinkArtikel.getEenheid())
        		.setVerkoopPrijs(esselinkArtikel.getVerkoopPrijs()));
	}
}

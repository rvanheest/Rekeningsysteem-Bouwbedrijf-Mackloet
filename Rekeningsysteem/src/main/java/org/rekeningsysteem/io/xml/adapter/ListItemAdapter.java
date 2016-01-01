package org.rekeningsysteem.io.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdaptee;
import org.rekeningsysteem.io.xml.adaptee.mutaties.MutatiesBonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.AnderArtikelAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.GebruiktEsselinkArtikelAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.InstantLoonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.ProductLoonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.reparaties.ReparatiesBonAdaptee;

public class ListItemAdapter extends XmlAdapter<ListItemAdaptee, ListItem> {

	// TODO refactor to visitor pattern (2x)

	@Override
	public ListItem unmarshal(ListItemAdaptee adaptee) {
		System.out.println("unmarshal called with " + adaptee);
		if (adaptee instanceof AnderArtikelAdaptee) {
			return this.unmarshal((AnderArtikelAdaptee) adaptee);
		}
		else if (adaptee instanceof GebruiktEsselinkArtikelAdaptee) {
			return this.unmarshal((GebruiktEsselinkArtikelAdaptee) adaptee);
		}
		else if (adaptee instanceof InstantLoonAdaptee) {
			return this.unmarshal((InstantLoonAdaptee) adaptee);
		}
		else if (adaptee instanceof ProductLoonAdaptee) {
			return this.unmarshal((ProductLoonAdaptee) adaptee);
		}
		else if (adaptee instanceof MutatiesBonAdaptee) {
			return this.unmarshal((MutatiesBonAdaptee) adaptee);
		}
		else if (adaptee instanceof ReparatiesBonAdaptee) {
			return this.unmarshal((ReparatiesBonAdaptee) adaptee);
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

	private InstantLoon unmarshal(InstantLoonAdaptee adaptee) {
		return new InstantLoon(adaptee.getOmschrijving(), adaptee.getLoon(),
				adaptee.getLoonBtwPercentage());
	}

	private ProductLoon unmarshal(ProductLoonAdaptee adaptee) {
		return new ProductLoon(adaptee.getOmschrijving(), adaptee.getUren(), adaptee.getUurloon(),
				adaptee.getLoonBtwPercentage());
	}

	private MutatiesBon unmarshal(MutatiesBonAdaptee adaptee) {
		return new MutatiesBon(adaptee.getOmschrijving(), adaptee.getBonnummer(),
				adaptee.getPrijs());
	}

	private ReparatiesBon unmarshal(ReparatiesBonAdaptee adaptee) {
		return new ReparatiesBon(adaptee.getOmschrijving(), adaptee.getBonnummer(),
				adaptee.getLoon(), adaptee.getMateriaal());
	}

	@Override
	public ListItemAdaptee marshal(ListItem v) {
		System.out.println("marshal called with " + v);
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
		else if (v instanceof MutatiesBon) {
			return this.marshal((MutatiesBon) v);
		}
		else if (v instanceof ReparatiesBon) {
			return this.marshal((ReparatiesBon) v);
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

	private MutatiesBonAdaptee marshal(MutatiesBon bon) {
		MutatiesBonAdaptee adaptee = new MutatiesBonAdaptee();
		adaptee.setOmschrijving(bon.getOmschrijving());
		adaptee.setBonnummer(bon.getBonnummer());
		adaptee.setPrijs(bon.getMateriaal());
		return adaptee;
	}

	private ReparatiesBonAdaptee marshal(ReparatiesBon bon) {
		ReparatiesBonAdaptee adaptee = new ReparatiesBonAdaptee();
		adaptee.setOmschrijving(bon.getOmschrijving());
		adaptee.setBonnummer(bon.getBonnummer());
		adaptee.setLoon(bon.getLoon());
		adaptee.setMateriaal(bon.getMateriaal());
		return adaptee;
	}
}

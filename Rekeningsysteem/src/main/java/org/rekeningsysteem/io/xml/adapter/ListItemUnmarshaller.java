package org.rekeningsysteem.io.xml.adapter;

import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdapteeVisitor;
import org.rekeningsysteem.io.xml.adaptee.mutaties.MutatiesInkoopOrderAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.AnderArtikelAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.GebruiktEsselinkArtikelAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.InstantLoonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.ProductLoonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.reparaties.ReparatiesInkoopOrderAdaptee;

public class ListItemUnmarshaller implements ListItemAdapteeVisitor<ListItem> {

	@Override
	public ListItem visit(MutatiesInkoopOrderAdaptee adaptee) {
		return new MutatiesInkoopOrder(adaptee.getOmschrijving(), adaptee.getBonnummer(),
				adaptee.getPrijs());
	}

	@Override
	public ListItem visit(GebruiktEsselinkArtikelAdaptee adaptee) {
		return new GebruiktEsselinkArtikel(adaptee.getOmschrijving(), adaptee.getArtikel(),
				adaptee.getAantal(), adaptee.getMateriaalBtwPercentage());
	}

	@Override
	public ListItem visit(AnderArtikelAdaptee adaptee) {
		return new AnderArtikel(adaptee.getOmschrijving(), adaptee.getPrijs(),
				adaptee.getMateriaalBtwPercentage());
	}

	@Override
	public ListItem visit(ReparatiesInkoopOrderAdaptee adaptee) {
		return new ReparatiesInkoopOrder(adaptee.getOmschrijving(), adaptee.getBonnummer(),
				adaptee.getLoon(), adaptee.getMateriaal());
	}

	@Override
	public ListItem visit(InstantLoonAdaptee adaptee) {
		return new InstantLoon(adaptee.getOmschrijving(), adaptee.getLoon(),
				adaptee.getLoonBtwPercentage());
	}

	@Override
	public ListItem visit(ProductLoonAdaptee adaptee) {
		return new ProductLoon(adaptee.getOmschrijving(), adaptee.getUren(), adaptee.getUurloon(),
				adaptee.getLoonBtwPercentage());
	}
}

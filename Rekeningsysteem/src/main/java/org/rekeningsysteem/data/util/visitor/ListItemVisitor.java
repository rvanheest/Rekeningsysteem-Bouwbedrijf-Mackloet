package org.rekeningsysteem.data.util.visitor;

import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier2.EsselinkParticulierArtikel;
import org.rekeningsysteem.data.particulier2.ParticulierArtikel2Impl;
import org.rekeningsysteem.data.particulier2.loon.InstantLoon2;
import org.rekeningsysteem.data.particulier2.loon.ProductLoon2;
import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.util.loon.InstantLoon;
import org.rekeningsysteem.data.util.loon.ProductLoon;

public interface ListItemVisitor<T> {

	T visit(AangenomenListItem item);
	
	T visit(MutatiesBon item);
	
	@Deprecated
	T visit(GebruiktEsselinkArtikel item);
	
	@Deprecated
	T visit(AnderArtikel item);
	
	T visit(ReparatiesBon item);
	
	@Deprecated
	T visit(InstantLoon item);
	
	@Deprecated
	T visit(ProductLoon item);

	// TODO dit herschikken once done
	T visit(ParticulierArtikel2Impl item);

	T visit(EsselinkParticulierArtikel item);

	T visit(InstantLoon2 item);

	T visit(ProductLoon2 item);
}

package org.rekeningsysteem.io.pdf;

import java.util.Arrays;
import java.util.List;

import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.util.loon.InstantLoon;
import org.rekeningsysteem.data.util.loon.ProductLoon;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

public class PdfListItemVisitor implements ListItemVisitor<List<String>> {

	@Override
	public List<String> visit(AangenomenListItem item) {
		return Arrays.asList(item.getOmschrijving(), 
				item.getLoon().formattedString(),
				item.getMateriaal().formattedString(),
				item.getTotaal().formattedString());
	}

	@Override
	public List<String> visit(MutatiesBon item) {
		return Arrays.asList(item.getOmschrijving(),
				item.getBonnummer(),
				item.getTotaal().formattedString());
	}

	@Override
	public List<String> visit(GebruiktEsselinkArtikel item) {
		EsselinkArtikel subItem = item.getArtikel();
		return Arrays.asList(subItem.getArtikelNummer(),
				subItem.getOmschrijving(),
				"" + item.getAantal(),
				subItem.getEenheid(),
				item.getTotaal().formattedString());
	}

	@Override
	public List<String> visit(AnderArtikel item) {
		return Arrays.asList("",
				item.getOmschrijving(),
				"",
				"",
				item.getTotaal().formattedString());
	}

	@Override
	public List<String> visit(ReparatiesBon item) {
		return Arrays.asList(item.getOmschrijving(),
				item.getBonnummer(),
				item.getLoon().formattedString(),
				item.getMateriaal().formattedString(),
				item.getTotaal().formattedString());
	}

	@Override
	public List<String> visit(InstantLoon item) {
		return Arrays.asList(item.getOmschrijving(),
				"",
				"",
				item.getLoon().formattedString());
	}

	@Override
	public List<String> visit(ProductLoon item) {
		return Arrays.asList(item.getOmschrijving(),
				"" + item.getUren(),
				"uren",
				item.getLoon().formattedString());
	}
}

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

public class PdfListItemVisitor implements ListItemVisitor<List<List<String>>> {

	@Override
	public List<List<String>> visit(AangenomenListItem item) {
		return Arrays.asList(
				Arrays.asList(
						item.getOmschrijving() + " (arbeid)",
						item.getLoon().formattedString(),
						String.valueOf(item.getLoonBtwPercentage())),
				Arrays.asList(
						item.getOmschrijving() + " (materiaal)",
						item.getMateriaal().formattedString(),
						String.valueOf(item.getMateriaalBtwPercentage())));
	}

	@Override
	public List<List<String>> visit(MutatiesBon item) {
		return Arrays.asList(
				Arrays.asList(
						item.getOmschrijving(),
						item.getBonnummer(),
						item.getTotaal().formattedString()));
	}

	@Override
	public List<List<String>> visit(GebruiktEsselinkArtikel item) {
		EsselinkArtikel artikel = item.getArtikel();
		return Arrays.asList(
				Arrays.asList(
						item.getAantal() + " " + artikel.getEenheid() + " "
								+ artikel.getOmschrijving(),
						item.getMateriaal().formattedString(),
						String.valueOf(item.getMateriaalBtwPercentage())));
	}

	@Override
	public List<List<String>> visit(AnderArtikel item) {
		return Arrays.asList(
				Arrays.asList(
						item.getOmschrijving(),
						item.getMateriaal().formattedString(),
						String.valueOf(item.getMateriaalBtwPercentage())));
	}

	@Override
	public List<List<String>> visit(ReparatiesBon item) {
		return Arrays.asList(
				Arrays.asList(
						item.getOmschrijving(),
						item.getBonnummer(),
						item.getLoon().formattedString(),
						item.getMateriaal().formattedString(),
						item.getTotaal().formattedString()));
	}

	@Override
	public List<List<String>> visit(InstantLoon item) {
		return Arrays.asList(
				Arrays.asList(
						item.getOmschrijving(),
						item.getLoon().formattedString(),
						String.valueOf(item.getLoonBtwPercentage())));
	}

	@Override
	public List<List<String>> visit(ProductLoon item) {
		return Arrays.asList(
				Arrays.asList(
						item.getUren() + " uren à " + item.getUurloon().formattedString(),
						item.getLoon().formattedString(),
						String.valueOf(item.getLoonBtwPercentage())));
	}
}

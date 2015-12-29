package org.rekeningsysteem.io.pdf;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.EsselinkParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel2Impl;
import org.rekeningsysteem.data.particulier.loon.InstantLoon2;
import org.rekeningsysteem.data.particulier.loon.ProductLoon2;
import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

// TODO now this can be ListItemVisitor<List<String>> (one less List)
public class PdfListItemVisitor implements ListItemVisitor<List<List<String>>> {

	@Override
	public List<List<String>> visit(MutatiesBon item) {
		Geld totaal = item.getTotaal();

		if (!totaal.isZero()) {
			return Arrays.asList(
					Arrays.asList(
							item.getOmschrijving(),
							item.getBonnummer(),
							totaal.formattedString()));
		}
		return Collections.emptyList();
	}

	@Override
	public List<List<String>> visit(EsselinkParticulierArtikel item) {
		EsselinkArtikel artikel = item.getArtikel();
		Geld materiaal = item.getMateriaal();

		if (!materiaal.isZero()) {
			Double aantal = item.getAantal();
			String aantalAsString = aantal == Math.floor(aantal) && !Double.isInfinite(aantal)
					? String.valueOf(aantal.intValue())
					: String.valueOf(aantal);

			return Arrays.asList(
					Arrays.asList(
							aantalAsString + "x " + artikel.getOmschrijving(),
							materiaal.formattedString(),
							String.valueOf(item.getMateriaalBtwPercentage())));
		}
		return Collections.emptyList();
	}

	@Override
	public List<List<String>> visit(ParticulierArtikel2Impl item) {
		Geld materiaal = item.getMateriaal();

		if (!materiaal.isZero()) {
			return Arrays.asList(
					Arrays.asList(
							item.getOmschrijving(),
							materiaal.formattedString(),
							String.valueOf(item.getMateriaalBtwPercentage())));
		}
		return Collections.emptyList();
	}

	@Override
	public List<List<String>> visit(ReparatiesBon item) {
		Geld totaal = item.getTotaal();

		if (!totaal.isZero()) {
			return Arrays.asList(
					Arrays.asList(
							item.getOmschrijving(),
							item.getBonnummer(),
							item.getLoon().formattedString(),
							item.getMateriaal().formattedString(),
							totaal.formattedString()));
		}
		return Collections.emptyList();
	}

	@Override
	public List<List<String>> visit(InstantLoon2 item) {
		Geld loon = item.getLoon();

		if (!loon.isZero()) {
			return Arrays.asList(
					Arrays.asList(
							item.getOmschrijving(),
							loon.formattedString(),
							String.valueOf(item.getLoonBtwPercentage())));
		}
		return Collections.emptyList();
	}

	@Override
	public List<List<String>> visit(ProductLoon2 item) {
		Geld loon = item.getLoon();

		if (!loon.isZero()) {
			return Arrays.asList(
					Arrays.asList(
							item.getUren() + " uren à " + item.getUurloon().formattedString(),
							loon.formattedString(),
							String.valueOf(item.getLoonBtwPercentage())));
		}
		return Collections.emptyList();
	}
}

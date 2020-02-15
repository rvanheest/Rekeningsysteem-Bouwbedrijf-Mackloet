package org.rekeningsysteem.io.pdf;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

public class PdfListItemVisitor implements ListItemVisitor<List<String>> {

	@Override
	public List<String> visit(MutatiesInkoopOrder item) {
		Geld totaal = item.getTotaal();

		if (!totaal.isZero()) {
			return Arrays.asList(
					item.getOmschrijving(),
					item.getInkoopOrderNummer(),
					totaal.formattedString());
		}
		return Collections.emptyList();
	}

	private String formatBtwPercentage(BtwPercentage btwPercentage) {
			if (btwPercentage.getPercentage() == 0)
					return "verlegd";
			else {
				String btw = String.valueOf(btwPercentage) + "%";
				return btwPercentage.isVerlegd() ? btw + ", verlegd" : btw;
			}
	}

	@Override
	public List<String> visit(GebruiktEsselinkArtikel item) {
		EsselinkArtikel artikel = item.getArtikel();
		Geld materiaal = item.getMateriaal();

		if (!materiaal.isZero()) {
			Double aantal = item.getAantal();
			String aantalAsString = aantal == Math.floor(aantal) && !Double.isInfinite(aantal)
					? String.valueOf(aantal.intValue())
					: String.valueOf(aantal);

			return Arrays.asList(
					aantalAsString + "x " + artikel.getOmschrijving(),
					materiaal.formattedString(),
					formatBtwPercentage(item.getMateriaalBtwPercentage()));
		}
		return Collections.emptyList();
	}

	@Override
	public List<String> visit(AnderArtikel item) {
		Geld materiaal = item.getMateriaal();

		if (!materiaal.isZero()) {
			return Arrays.asList(
					item.getOmschrijving(),
					materiaal.formattedString(),
					formatBtwPercentage(item.getMateriaalBtwPercentage()));
		}
		return Collections.emptyList();
	}

	@Override
	public List<String> visit(ReparatiesInkoopOrder item) {
		Geld totaal = item.getTotaal();

		if (!totaal.isZero()) {
			return Arrays.asList(
					item.getOmschrijving(),
					item.getInkoopOrderNummer(),
					item.getLoon().formattedString(),
					item.getMateriaal().formattedString(),
					totaal.formattedString());
		}
		return Collections.emptyList();
	}

	@Override
	public List<String> visit(InstantLoon item) {
		Geld loon = item.getLoon();

		if (!loon.isZero()) {
			return Arrays.asList(
					item.getOmschrijving(),
					loon.formattedString(),
					formatBtwPercentage(item.getLoonBtwPercentage()));
		}
		return Collections.emptyList();
	}

	@Override
	public List<String> visit(ProductLoon item) {
		Geld loon = item.getLoon();

		if (!loon.isZero()) {
			return Arrays.asList(
					item.getUren() + " uren à " + item.getUurloon().formattedString(),
					loon.formattedString(),
					formatBtwPercentage(item.getLoonBtwPercentage()));
		}
		return Collections.emptyList();
	}
}

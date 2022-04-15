package org.rekeningsysteem.io.pdf;

import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PdfListItemVisitor {

	public List<String> visit(MutatiesInkoopOrder item) {
		Geld totaal = item.getTotaal();

		if (totaal.isZero())
			return Collections.emptyList();

		return Arrays.asList(
			item.omschrijving(),
			item.inkoopOrderNummer(),
			totaal.formattedString()
		);
	}

	private String formatBtwPercentage(BtwPercentage btwPercentage) {
		return btwPercentage.percentage() == 0 ? "verlegd" : btwPercentage.formattedString();
	}

	public List<String> visit(GebruiktEsselinkArtikel item) {
		Geld materiaal = item.materiaal();

		if (materiaal.isZero())
			return Collections.emptyList();

		Double aantal = item.getAantal();
		String aantalAsString = aantal == Math.floor(aantal) && !Double.isInfinite(aantal)
			? String.valueOf(aantal.intValue())
			: String.valueOf(aantal);

		return Arrays.asList(
			aantalAsString + "x " + item.getArtikel().omschrijving(),
			materiaal.formattedString(),
			formatBtwPercentage(item.getMateriaalBtwPercentage())
		);
	}

	public List<String> visit(AnderArtikel item) {
		Geld materiaal = item.materiaal();

		if (materiaal.isZero())
			return Collections.emptyList();

		return Arrays.asList(
			item.getOmschrijving(),
			materiaal.formattedString(),
			formatBtwPercentage(item.getMateriaalBtwPercentage())
		);
	}

	public List<String> visit(ReparatiesInkoopOrder item) {
		Geld totaal = item.getTotaal();

		if (totaal.isZero())
			return Collections.emptyList();

		return Arrays.asList(
			item.omschrijving(),
			item.inkoopOrderNummer(),
			item.loon().formattedString(),
			item.materiaal().formattedString(),
			totaal.formattedString()
		);
	}

	public List<String> visit(InstantLoon item) {
		Geld loon = item.loon();

		if (loon.isZero())
			return Collections.emptyList();

		return Arrays.asList(
			item.getOmschrijving(),
			loon.formattedString(),
			formatBtwPercentage(item.getLoonBtwPercentage())
		);
	}

	public List<String> visit(ProductLoon item) {
		Geld loon = item.loon();

		if (loon.isZero())
			return Collections.emptyList();

		return Arrays.asList(
			item.getUren() + " uren à " + item.getUurloon().formattedString(),
			loon.formattedString(),
			formatBtwPercentage(item.getLoonBtwPercentage())
		);
	}
}

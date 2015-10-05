package org.rekeningsysteem.io.pdf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier2.EsselinkParticulierArtikel;
import org.rekeningsysteem.data.particulier2.ParticulierArtikel2Impl;
import org.rekeningsysteem.data.particulier2.loon.InstantLoon2;
import org.rekeningsysteem.data.particulier2.loon.ProductLoon2;
import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.loon.InstantLoon;
import org.rekeningsysteem.data.util.loon.ProductLoon;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

public class PdfListItemVisitor implements ListItemVisitor<List<List<String>>> {

	@Override
	public List<List<String>> visit(AangenomenListItem item) {
		Geld loon = item.getLoon();
		Geld materiaal = item.getMateriaal();
		List<List<String>> list = new ArrayList<>();

		if (!loon.isZero()) {
			list.add(
					Arrays.asList(
							item.getOmschrijving() + " (arbeid)",
							loon.formattedString(),
							String.valueOf(item.getLoonBtwPercentage())));
		}
		if (!materiaal.isZero()) {
			list.add(
					Arrays.asList(
							item.getOmschrijving() + " (materiaal)",
							materiaal.formattedString(),
							String.valueOf(item.getMateriaalBtwPercentage())));
		}

		return list;
	}

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
	public List<List<String>> visit(GebruiktEsselinkArtikel item) {
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
	public List<List<String>> visit(AnderArtikel item) {
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
	public List<List<String>> visit(InstantLoon item) {
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
	public List<List<String>> visit(ProductLoon item) {
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

	@Override
	public List<List<String>> visit(ParticulierArtikel2Impl item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<List<String>> visit(EsselinkParticulierArtikel item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<List<String>> visit(InstantLoon2 item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<List<String>> visit(ProductLoon2 item) {
		// TODO Auto-generated method stub
		return null;
	}
}

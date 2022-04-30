package org.rekeningsysteem.io.xml;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.particulier.loon.AbstractLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.BtwPercentages;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.exception.GeldParseException;
import org.rekeningsysteem.exception.XmlParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class XmlReader1 extends XmlLoader {

	private Currency currency;

	public XmlReader1(DocumentBuilder builder) {
		super(builder);
	}

	@Override
	public Single<AbstractRekening> read(Document document) {
		Node factuur = document.getElementsByTagName("bestand").item(0);
		String soort = factuur.getFirstChild().getNodeName();
		return switch (soort) {
			case "particulierfactuur1", "partfactuur" -> parseRekening(factuur, this::makeParticulierFactuur1, soort);
			case "particulierfactuur2" -> parseRekening(factuur, this::makeParticulierFactuur2, soort);
			case "reparatiesfactuur" -> parseRekening(factuur, this::makeReparatiesFactuur, soort);
			case "mutatiesfactuur" -> parseRekening(factuur, this::makeMutatiesFactuur, soort);
			case "offerte" -> parseRekening(factuur, XmlReader1::makeOfferte, soort);
			default -> Single.error(new XmlParseException(String.format("Geen geschikte Node gevonden. Nodenaam = %s.", soort)));
		};
	}

	private Single<Geld> makeGeld(String s) {
		String[] ss = s.split(" ");

		switch (ss.length) {
			case 1:
				return Single.fromCallable(() -> new Geld(ss[0]));
			case 2:
				if (this.currency == null || this.currency.getSymbol().equals(ss[0])) {
					this.currency = Maybe.fromOptional(
						Currency.getAvailableCurrencies().parallelStream()
							.filter(cur -> ss[0].equals(cur.getSymbol()))
							.findFirst()
					).blockingGet();
					return Single.fromCallable(() -> new Geld(ss[1]));
				}
				else {
					// corrupte file: verschillende valuta's
					return Single.error(new IllegalArgumentException("This file is corrupt. It has multiple currencies."));
				}
			default:
				return Single.error(new GeldParseException("Het bedrag " + s + " kan niet worden gelezen."));
		}
	}

	private static Maybe<LocalDate> makeDatum(Node node) {
		Maybe<Integer> dag = getNodeValue(node, "dag").map(Integer::parseInt);
		Maybe<Integer> maand = getNodeValue(node, "maand").map(Integer::parseInt);
		Maybe<Integer> jaar = getNodeValue(node, "jaar").map(Integer::parseInt);

		return Maybe.zip(jaar, maand, dag, LocalDate::of);
	}

	private static Maybe<Debiteur> makeDebiteur(Node node) {
		Maybe<String> naam = getNodeValue(node, "naam");
		Maybe<String> straat = getNodeValue(node, "straat");
		Maybe<String> nummer = getNodeValue(node, "nummer");
		Maybe<String> postcode = getNodeValue(node, "postcode");
		Maybe<String> plaats = getNodeValue(node, "plaats");
		Maybe<String> btwnr = getNodeValue(node, "btwnr");

		return btwnr.filter(s -> !s.isEmpty()).isEmpty().flatMapMaybe(b -> b
			? Maybe.zip(naam, straat, nummer, postcode, plaats, Debiteur::new)
			: Maybe.zip(naam, straat, nummer, postcode, plaats, btwnr, Debiteur::new));
	}

	private static Maybe<FactuurHeader> makeFactuurHeader(Node node) {
		Maybe<Debiteur> debiteur = makeDebiteur(getElement(node, "debiteur"));
		Maybe<LocalDate> datum = makeDatum(getElement(node, "datum"));
		Maybe<String> factuurnummer = getNodeValue(node, "factuurnummer");

		return Maybe.zip(debiteur, datum, factuurnummer, FactuurHeader::new);
	}

	private static Maybe<FactuurHeader> makeOfferteFactuurHeader(Node node) {
		Maybe<Debiteur> debiteur = makeDebiteur(getElement(node, "debiteur"));
		Maybe<LocalDate> datum = makeDatum(getElement(node, "datum"));
		Maybe<String> offertenummer = getNodeValue(node, "offertenummer");

		return Maybe.zip(debiteur, datum, offertenummer, FactuurHeader::new);
	}

	private Maybe<Function<BtwPercentage, AnderArtikel>> makeAnderArtikel(Node node) {
		Maybe<String> omschrijving = getNodeValue(getElement(node, "artikel"), "omschrijving");
		Maybe<Geld> prijs = getNodeValue(node, "prijs").flatMapSingle(this::makeGeld);

		return Maybe.zip(omschrijving, prijs, (omschr, pr) -> btw -> new AnderArtikel(omschr, pr, btw));
	}

	private Maybe<EsselinkArtikel> makeArtikelEsselink(Node node) {
		Maybe<String> artikelNummer = getNodeValue(node, "artikelnummer");
		Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
		Maybe<Integer> prijsPer = getNodeValue(node, "prijsper").map(Integer::parseInt);
		Maybe<String> eenheid = getNodeValue(node, "eenheid");
		Maybe<Geld> verkoopPrijs = getNodeValue(node, "verkoopprijs").flatMapSingle(this::makeGeld);

		return Maybe.zip(artikelNummer, omschrijving, prijsPer, eenheid, verkoopPrijs, EsselinkArtikel::new);
	}

	private Maybe<Function<BtwPercentage, GebruiktEsselinkArtikel>> makeGebruiktArtikelEsselink(Node node) {
		Maybe<EsselinkArtikel> artikel = this.makeArtikelEsselink(getElement(node, "artikel"));
		Maybe<Double> aantal = getNodeValue(node, "aantal").map(Double::parseDouble);

		return Maybe.zip(artikel, aantal, (art, aant) -> btw -> new GebruiktEsselinkArtikel(art, aant, btw));
	}

	private Maybe<Function<BtwPercentage, ProductLoon>> makeLoon(Node node) {
		Maybe<Geld> uurloon = getNodeValue(node, "uurloon").flatMapSingle(this::makeGeld);
		Maybe<Double> uren = getNodeValue(node, "uren").map(Double::parseDouble);

		return Maybe.zip(uurloon, uren, (ul, ur) -> btw -> new ProductLoon("Uurloon à " + ul.formattedString(), ur, ul, btw));
	}

	private static Maybe<BtwPercentages> makeEnkelBtw(Node node) {
		Maybe<BtwPercentage> btw = getNodeValue(node, "btw")
			.map(Double::parseDouble)
			.map(b -> new BtwPercentage(b, false));

		return btw.map(b -> new BtwPercentages(b, b));
	}

	private static Maybe<BtwPercentages> makeDubbelBtw(Node node) {
		Maybe<BtwPercentage> btwArt = getNodeValue(node, "btwpercentageart")
			.map(Double::parseDouble)
			.map(b -> new BtwPercentage(b, false));
		Maybe<BtwPercentage> btwLoon = getNodeValue(node, "btwpercentageloon")
			.map(Double::parseDouble)
			.map(b -> new BtwPercentage(b, false));

		return Maybe.zip(btwLoon, btwArt, BtwPercentages::new);
	}

	private Maybe<ParticulierFactuur> makeParticulierFactuur1(Node node) {
		NodeList gal = getElement(getElement(node, "artikellijst"), "gebruiktartikellijst").getChildNodes();

		Maybe<FactuurHeader> header = makeFactuurHeader(node);
		Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
		Function<BtwPercentage, Single<List<ParticulierArtikel>>> items = btw ->
			Observable.range(0, gal.getLength())
				.map(gal::item)
				.flatMapMaybe(item -> switch (item.getNodeName()) {
					case "gebruiktartikelander" -> this.makeAnderArtikel(item);
					case "gebruiktartikelesselink" -> this.makeGebruiktArtikelEsselink(item);
					default -> Maybe.error(new IllegalArgumentException("Unknown artikel type found."));
				})
				.map(f -> f.apply(btw))
				.collect(() -> new ArrayList<ParticulierArtikel>(), List::add);
		Function<BtwPercentage, Maybe<AbstractLoon>> loon = btw -> this.makeLoon(getElement(node, "loon")).map(f -> f.apply(btw));

		return makeEnkelBtw(node)
				.flatMap(percentages -> Maybe.zip(
						header,
						omschrijving,
						items.apply(percentages.materiaalPercentage())
								.map(list -> new ItemList<>(this.currency, list))
								.flatMapMaybe(arts -> loon.apply(percentages.loonPercentage())
										.doOnSuccess(arts::add)
										.map(ignored -> arts)
								),
						ParticulierFactuur::new
				));
	}

	private Maybe<ParticulierFactuur> makeParticulierFactuur2(Node node) {
		NodeList gal = getElement(getElement(node, "artikellijst"), "gebruiktartikellijst").getChildNodes();

		Maybe<FactuurHeader> header = makeFactuurHeader(node);
		Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
		Function<BtwPercentage, Single<List<ParticulierArtikel>>> items = btw ->
			Observable.range(0, gal.getLength())
				.map(gal::item)
				.flatMapMaybe(item -> switch (item.getNodeName()) {
					case "gebruiktartikelander" -> this.makeAnderArtikel(item);
					case "gebruiktartikelesselink" -> this.makeGebruiktArtikelEsselink(item);
					default -> Maybe.error(new IllegalArgumentException("Unknown artikel type found."));
				})
				.map(f -> f.apply(btw))
				.collect(() -> new ArrayList<ParticulierArtikel>(), List::add);
		Function<BtwPercentage, Maybe<AbstractLoon>> loonList = btw -> this.makeLoon(getElement(node, "loon")).map(f -> f.apply(btw));

		return makeDubbelBtw(node)
				.flatMap(percentage -> Maybe.zip(
						header,
						omschrijving,
						items.apply(percentage.materiaalPercentage())
								.map(list -> new ItemList<>(this.currency, list))
								.flatMapMaybe(arts -> loonList.apply(percentage.loonPercentage())
										.doOnSuccess(arts::add)
										.map(ignored -> arts)
								),
						ParticulierFactuur::new
				));
	}

	private Maybe<MutatiesInkoopOrder> makeMutatiesInkoopOrder(Node node) {
		Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
		Maybe<String> inkoopOrderNummer = getNodeValue(node, "bonnummer");
		Maybe<Geld> prijs = getNodeValue(node, "prijs").flatMapSingle(this::makeGeld);

		return Maybe.zip(omschrijving, inkoopOrderNummer, prijs, MutatiesInkoopOrder::new);
	}

	private Maybe<MutatiesFactuur> makeMutatiesFactuur(Node node) {
		Maybe<FactuurHeader> header = makeFactuurHeader(node);

		NodeList orders = getNodeList(getElement(node, "mutatiesbonlijst"), "mutatiesbon");
		Single<ItemList<MutatiesInkoopOrder>> itemList = Observable.range(0, orders.getLength())
			.map(orders::item)
			.flatMapMaybe(this::makeMutatiesInkoopOrder)
			.collect(ArrayList<MutatiesInkoopOrder>::new, List::add)
			.map(list -> new ItemList<>(this.currency, list));

		return Maybe.zip(header, itemList.toMaybe(), MutatiesFactuur::new);
	}

	private Maybe<ReparatiesInkoopOrder> makeReparatiesInkoopOrder(Node node) {
		Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
		Maybe<String> inkoopOrderNummer = getNodeValue(node, "bonnummer");
		Maybe<Geld> uurloon = getNodeValue(node, "uurloon").flatMapSingle(this::makeGeld);
		Maybe<Geld> materiaal = getNodeValue(node, "materiaal").flatMapSingle(this::makeGeld);

		return Maybe.zip(omschrijving, inkoopOrderNummer, uurloon, materiaal, ReparatiesInkoopOrder::new);
	}

	private Maybe<ReparatiesFactuur> makeReparatiesFactuur(Node node) {
		Maybe<FactuurHeader> header = makeFactuurHeader(node);

		NodeList orders = getNodeList(getElement(node, "reparatiesbonlijst"), "reparatiesbon");
		Single<ItemList<ReparatiesInkoopOrder>> itemList = Observable.range(0, orders.getLength())
			.map(orders::item)
			.flatMapMaybe(this::makeReparatiesInkoopOrder)
			.collect(ArrayList<ReparatiesInkoopOrder>::new, List::add)
			.map(list -> new ItemList<>(this.currency, list));

		return Maybe.zip(header, itemList.toMaybe(), ReparatiesFactuur::new);
	}

	private static Maybe<Offerte> makeOfferte(Node node) {
		Maybe<FactuurHeader> header = makeOfferteFactuurHeader(node);
		Maybe<String> tekst = getNodeValue(node, "tekst");
		Maybe<Boolean> ondertekenen = getNodeValue(node, "ondertekenen").map(Boolean::parseBoolean);

		return Maybe.zip(header, tekst, ondertekenen, Offerte::new);
	}
}

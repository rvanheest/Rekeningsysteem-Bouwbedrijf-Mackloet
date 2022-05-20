package org.rekeningsysteem.io.xml;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.javamoney.moneta.Money;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.materiaal.AnderArtikel;
import org.rekeningsysteem.data.particulier.materiaal.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.materiaal.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.exception.XmlParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.xml.parsers.DocumentBuilder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

public class XmlReader4 extends XmlLoader {

	public XmlReader4(DocumentBuilder builder) {
		super(builder);
	}

	@Override
	public Single<org.rekeningsysteem.data.util.Document> read(Document document) {
		Node doc = document.getElementsByTagName("bestand").item(0);

		return Optional.ofNullable(doc.getAttributes().getNamedItem("type"))
			.flatMap(t -> Optional.ofNullable(doc.getAttributes().getNamedItem("version")).map(v -> {
				if ("4".equals(v.getNodeValue())) {
					return switch (t.getNodeValue()) {
						case "MutatiesFactuur" -> parseRekening(doc, XmlReader4::makeMutatiesFactuur, "mutaties-factuur");
						case "Offerte" -> parseRekening(doc, XmlReader4::makeOfferte, "offerte");
						case "ParticulierFactuur" -> parseRekening(doc, XmlReader4::makeParticulierFactuur, "particulier-factuur");
						case "ReparatiesFactuur" -> parseRekening(doc, XmlReader4::makeReparatiesFactuur, "reparaties-factuur");
						default -> Single.<org.rekeningsysteem.data.util.Document> error(new XmlParseException(String.format("No suitable invoice type found. Found type: %s", t.getNodeValue())));
					};
				}
				return Single.<org.rekeningsysteem.data.util.Document> error(new XmlParseException(String.format("Incorrect version for this parser. Found version: %s", v.getNodeValue())));
			}))
			.orElseGet(() -> Single.error(new XmlParseException("No invoice type is specified")));
	}

	private static Maybe<Debiteur> makeDebiteur(Node node) {
		Maybe<String> naam = getNodeValue(node, "naam");
		Maybe<String> straat = getNodeValue(node, "straat");
		Maybe<String> nummer = getNodeValue(node, "nummer");
		Maybe<String> postcode = getNodeValue(node, "postcode");
		Maybe<String> plaats = getNodeValue(node, "plaats");
		Maybe<String> btwnr = getNodeValue(node, "btwNummer");

		return btwnr
			.filter(s -> !s.isEmpty())
			.isEmpty()
			.flatMapMaybe(b -> b
				? Maybe.zip(naam, straat, nummer, postcode, plaats, Debiteur::new)
				: Maybe.zip(naam, straat, nummer, postcode, plaats, btwnr, Debiteur::new)
			);
	}

	private static Maybe<LocalDate> makeDatum(Node node) {
		Maybe<Integer> dag = getNodeValue(node, "dag").map(Integer::parseInt);
		Maybe<Integer> maand = getNodeValue(node, "maand").map(Integer::parseInt);
		Maybe<Integer> jaar = getNodeValue(node, "jaar").map(Integer::parseInt);

		return Maybe.zip(jaar, maand, dag, LocalDate::of);
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
		Maybe<String> offertenummer = getNodeValue(node, "factuurnummer");

		return Maybe.zip(debiteur, datum, offertenummer, FactuurHeader::new);
	}

	private static Function<CurrencyUnit, Maybe<MonetaryAmount>> makeMoney(Node node, String name) {
		return currency -> getNodeValue(node, name)
				.map(Double::parseDouble)
				.map(amount -> Money.of(amount, currency));
	}

	private static Maybe<CurrencyUnit> makeCurrency(Node node) {
		return getNodeValue(node, "currency").map(Monetary::getCurrency);
	}

	private static Maybe<BtwPercentage> makeBtwPercentage(Node node) {
		Maybe<Double> percentage = getStringValue(node).map(Double::parseDouble);
		Single<Boolean> verlegd = getAttribute(node, "verlegd").map(Boolean::parseBoolean).defaultIfEmpty(false);

		return Maybe.zip(percentage, verlegd.toMaybe(), BtwPercentage::new);
	}

	private static Function<CurrencyUnit, Maybe<MutatiesInkoopOrder>> makeMutatiesInkoopOrder(Node node) {
		return currency -> {
			Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
			Maybe<String> inkoopOrderNummer = getNodeValue(node, "bonnummer");
			Maybe<MonetaryAmount> prijs = makeMoney(node, "prijs").apply(currency);

			return Maybe.zip(omschrijving, inkoopOrderNummer, prijs, MutatiesInkoopOrder::new);
		};
	}

	private static Function<CurrencyUnit, Single<Collection<MutatiesInkoopOrder>>> makeMutatiesList(Node node) {
		return currency -> iterate(getNodeList(node, "mutaties-bon"))
				.map(XmlReader4::makeMutatiesInkoopOrder)
				.flatMapMaybe(f -> f.apply(currency))
				.collect(ArrayList::new, Collection::add);
	}

	private static Maybe<MutatiesFactuur> makeMutatiesFactuur(Node node) {
		Maybe<FactuurHeader> header = makeFactuurHeader(getElement(node, "factuurHeader"));
		Maybe<CurrencyUnit> currency = makeCurrency(node);
		Function<CurrencyUnit, Single<Collection<MutatiesInkoopOrder>>> fList = makeMutatiesList(getElement(node, "list"));

		return currency
				.flatMap(c -> Maybe.zip(
						header,
						fList.apply(c).map(list -> new ItemList<>(c, list)).toMaybe(),
						MutatiesFactuur::new
				));
	}

	private static Maybe<Offerte> makeOfferte(Node node) {
		Maybe<FactuurHeader> header = makeOfferteFactuurHeader(getElement(node, "factuurHeader"));
		Maybe<String> text = getNodeValue(node, "tekst");
		Maybe<Boolean> ondertekenen = getNodeValue(node, "ondertekenen").map(Boolean::parseBoolean);

		return Maybe.zip(header, text, ondertekenen, Offerte::new);
	}

	private static Function<CurrencyUnit, Maybe<EsselinkArtikel>> makeEsselinkArtikel(Node node) {
		return currency -> {
			Maybe<String> artikelNummer = getNodeValue(node, "artikelNummer");
			Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
			Maybe<Integer> prijsPer = getNodeValue(node, "prijsPer").map(Integer::parseInt);
			Maybe<String> eenheid = getNodeValue(node, "eenheid");
			Maybe<MonetaryAmount> verkoopPrijs = makeMoney(node, "verkoopPrijs").apply(currency);

			return Maybe.zip(artikelNummer, omschrijving, prijsPer, eenheid, verkoopPrijs, EsselinkArtikel::new);
		};
	}

	private static Function<CurrencyUnit, Maybe<GebruiktEsselinkArtikel>> makeGebruiktArtikelEsselink(Node node) {
		return currency -> {
			Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
			Maybe<EsselinkArtikel> artikel = makeEsselinkArtikel(getElement(node, "artikel")).apply(currency);
			Maybe<Double> aantal = getNodeValue(node, "aantal").map(Double::parseDouble);
			Maybe<BtwPercentage> btw = makeBtwPercentage(getElement(node, "materiaalBtwPercentage"));

			return Maybe.zip(omschrijving, artikel, aantal, btw, GebruiktEsselinkArtikel::new);
		};
	}

	private static Function<CurrencyUnit, Maybe<AnderArtikel>> makeAnderArtikel(Node node) {
		return currency -> {
			Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
			Maybe<MonetaryAmount> prijs = makeMoney(node, "prijs").apply(currency);
			Maybe<BtwPercentage> btw = makeBtwPercentage(getElement(node, "materiaalBtwPercentage"));

			return Maybe.zip(omschrijving, prijs, btw, AnderArtikel::new);
		};
	}

	private static Function<CurrencyUnit, Maybe<ProductLoon>> makeProductLoon(Node node) {
		return currency -> {
			Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
			Maybe<Double> uren = getNodeValue(node, "uren").map(Double::parseDouble);
			Maybe<MonetaryAmount> uurloon = makeMoney(node, "uurloon").apply(currency);
			Maybe<BtwPercentage> btw = makeBtwPercentage(getElement(node, "loonBtwPercentage"));

			return Maybe.zip(omschrijving, uren, uurloon, btw, ProductLoon::new);
		};
	}

	private static Function<CurrencyUnit, Maybe<InstantLoon>> makeInstantLoon(Node node) {
		return currency -> {
			Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
			Maybe<MonetaryAmount> loon = makeMoney(node, "loon").apply(currency);
			Maybe<BtwPercentage> btw = makeBtwPercentage(getElement(node, "loonBtwPercentage"));

			return Maybe.zip(omschrijving, loon, btw, InstantLoon::new);
		};
	}

	private static Function<CurrencyUnit, Single<Collection<ParticulierArtikel>>> makeItemList(Node node) {
		return currency -> iterate(node.getChildNodes())
			.filter(n -> !"#text".equals(n.getNodeName()))
			.flatMapMaybe(item -> {
				String name = item.getNodeName();
				return switch (name) {
					case "gebruikt-esselink-artikel" -> makeGebruiktArtikelEsselink(item).apply(currency);
					case "ander-artikel" -> makeAnderArtikel(item).apply(currency);
					case "product-loon" -> makeProductLoon(item).apply(currency);
					case "instant-loon" -> makeInstantLoon(item).apply(currency);
					default -> Maybe.error(new IllegalArgumentException("Unknown artikel type found. Name = " + name));
				};
			})
			.collect(ArrayList::new, Collection::add);
	}

	private static Maybe<ParticulierFactuur> makeParticulierFactuur(Node node) {
		Maybe<FactuurHeader> header = makeFactuurHeader(getElement(node, "factuurHeader"));
		Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
		Maybe<CurrencyUnit> currency = makeCurrency(node);
		Function<CurrencyUnit, Single<Collection<ParticulierArtikel>>> fItemList = makeItemList(getElement(node, "list"));

		return currency
				.flatMap(c -> Maybe.zip(
						header,
						omschrijving,
						fItemList.apply(c).map(list -> new ItemList<>(c, list)).toMaybe(),
						ParticulierFactuur::new
				));
	}

	private static Function<CurrencyUnit, Maybe<ReparatiesInkoopOrder>> makeReparatiesInkoopOrder(Node node) {
		return currency -> {
			Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
			Maybe<String> inkoopOrderNummer = getNodeValue(node, "bonnummer");
			Maybe<MonetaryAmount> loon = makeMoney(node, "loon").apply(currency);
			Maybe<MonetaryAmount> materiaal = makeMoney(node, "materiaal").apply(currency);

			return Maybe.zip(omschrijving, inkoopOrderNummer, loon, materiaal, ReparatiesInkoopOrder::new);
		};
	}

	private static Function<CurrencyUnit, Single<Collection<ReparatiesInkoopOrder>>> makeReparatiesList(Node node) {
		return currency -> iterate(getNodeList(node, "reparaties-bon"))
			.map(XmlReader4::makeReparatiesInkoopOrder)
			.flatMapMaybe(f -> f.apply(currency))
			.collect(ArrayList::new, Collection::add);
	}

	private static Maybe<ReparatiesFactuur> makeReparatiesFactuur(Node node) {
		Maybe<FactuurHeader> header = makeFactuurHeader(getElement(node, "factuurHeader"));
		Maybe<CurrencyUnit> currency = makeCurrency(node);
		Function<CurrencyUnit, Single<Collection<ReparatiesInkoopOrder>>> fList = makeReparatiesList(getElement(node, "list"));

		return currency
				.flatMap(c -> Maybe.zip(
						header,
						fList.apply(c).map(list -> new ItemList<>(c, list)).toMaybe(),
						ReparatiesFactuur::new
				));
	}
}

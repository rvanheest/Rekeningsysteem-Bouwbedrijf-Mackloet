package org.rekeningsysteem.io.xml;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Function3;
import org.javamoney.moneta.Money;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.Loon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.particulier.materiaal.AnderArtikel;
import org.rekeningsysteem.data.particulier.materiaal.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.materiaal.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.materiaal.Materiaal;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.BtwPercentages;
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

public class XmlReader2 extends XmlLoader {

	public XmlReader2(DocumentBuilder builder) {
		super(builder);
	}

	@Override
	public Single<org.rekeningsysteem.data.util.Document> read(Document document) {
		Node bestand = document.getElementsByTagName("bestand").item(0);
		return Optional.ofNullable(bestand.getAttributes().getNamedItem("type")).map(kindNode -> {
			String type = kindNode.getNodeValue();
			return switch (type) {
				case "AangenomenFactuur" -> parseRekening(bestand, XmlReader2::makeAangenomenFactuur, "rekening");
				case "MutatiesFactuur" -> parseRekening(bestand, XmlReader2::makeMutatiesFactuur, "rekening");
				case "Offerte" -> parseRekening(bestand, XmlReader2::makeOfferte, "rekening");
				case "ParticulierFactuur" -> parseRekening(bestand, XmlReader2::makeParticulierFactuur, "rekening");
				case "ReparatiesFactuur" -> parseRekening(bestand, XmlReader2::makeReparatiesFactuur, "rekening");
				default -> Single.<org.rekeningsysteem.data.util.Document> error(new XmlParseException(String.format("Geen geschikte Node gevonden. Nodenaam = %s.", type)));
			};
		}).orElseGet(() -> Single.error(new XmlParseException("Geen factuur type gespecificeerd")));
	}

	private static Maybe<Debiteur> makeDebiteur(Node node) {
		Maybe<String> naam = getNodeValue(node, "naam");
		Maybe<String> straat = getNodeValue(node, "straat");
		Maybe<String> nummer = getNodeValue(node, "nummer");
		Maybe<String> postcode = getNodeValue(node, "postcode");
		Maybe<String> plaats = getNodeValue(node, "plaats");
		Maybe<String> btwnr = getNodeValue(node, "btwNummer");

		return btwnr.filter(s -> !s.isEmpty()).isEmpty().flatMapMaybe(b -> b
			? Maybe.zip(naam, straat, nummer, postcode, plaats, Debiteur::new)
			: Maybe.zip(naam, straat, nummer, postcode, plaats, btwnr, Debiteur::new));
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

	private static Function<CurrencyUnit, Maybe<MonetaryAmount>> makeMoney(Node node) {
		return currency -> getNodeValue(node, "bedrag").map(Double::parseDouble).map(amount -> Money.of(amount, currency));
	}

	private static Maybe<CurrencyUnit> makeCurrency(Node node) {
		return getNodeValue(node, "currency").map(Monetary::getCurrency);
	}

	private static Maybe<BtwPercentages> makeBtwPercentage(Node node) {
		Maybe<BtwPercentage> loon = getNodeValue(node, "loonPercentage")
			.map(Double::parseDouble)
			.map(b -> new BtwPercentage(b, false));
		Maybe<BtwPercentage> materiaal = getNodeValue(node, "materiaalPercentage")
			.map(Double::parseDouble)
			.map(b -> new BtwPercentage(b, false));

		return Maybe.zip(loon, materiaal, BtwPercentages::new);
	}

	private static Function<CurrencyUnit, Maybe<BiFunction<BtwPercentage, BtwPercentage, Observable<ParticulierArtikel>>>> makeAangenomenListItem(Node node) {
		return currency -> {
			Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
			Maybe<MonetaryAmount> loon = makeMoney(getElement(node, "loon")).apply(currency);
			Maybe<MonetaryAmount> materiaal = makeMoney(getElement(node, "materiaal")).apply(currency);
	
			return Maybe.zip(omschrijving, loon, materiaal,
				(omschr, l, m) -> (lb, mb) -> Observable.just(
					new AnderArtikel(omschr, m, mb),
					new InstantLoon(omschr, l, lb)
				)
			);
		};
	}

	private static Function3<CurrencyUnit, BtwPercentage, BtwPercentage, Single<Collection<ParticulierArtikel>>> makeAangenomenList(Node node) {
		return (currency, btwL, btwM) -> iterate(getNodeList(node, "list-item"))
			.map(XmlReader2::makeAangenomenListItem)
			.flatMap(f -> f.apply(currency).flatMapObservable(g -> g.apply(btwL, btwM)))
			.collect(ArrayList::new, Collection::add);
	}

	private static Maybe<ParticulierFactuur> makeAangenomenFactuur(Node node) {
		Maybe<FactuurHeader> header = makeFactuurHeader(getElement(node, "factuurHeader"));
		Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
		Maybe<CurrencyUnit> currency = makeCurrency(node);
		Function3<CurrencyUnit, BtwPercentage, BtwPercentage, Single<Collection<ParticulierArtikel>>> listFunc = makeAangenomenList(getElement(node, "list"));
		Maybe<Collection<ParticulierArtikel>> list = makeBtwPercentage(getElement(node, "btwPercentage"))
			.flatMap(b -> currency.flatMapSingle(c -> listFunc.apply(c, b.loonPercentage(), b.materiaalPercentage())));
		
		return Maybe.zip(header, omschrijving, currency.zipWith(list, ItemList::new), ParticulierFactuur::new);
	}

	private static Function<CurrencyUnit, Maybe<MutatiesInkoopOrder>> makeMutatiesInkoopOrder(Node node) {
		return currency -> {
			Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
			Maybe<String> inkoopOrderNummer = getNodeValue(node, "bonnummer");
			Maybe<MonetaryAmount> prijs = makeMoney(getElement(node, "prijs")).apply(currency);

			return Maybe.zip(omschrijving, inkoopOrderNummer, prijs, MutatiesInkoopOrder::new);
		};
	}

	private static Function<CurrencyUnit, Single<Collection<MutatiesInkoopOrder>>> makeMutatiesList(Node node) {
		return currency -> iterate(getNodeList(node, "list-item"))
			.map(XmlReader2::makeMutatiesInkoopOrder)
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
			Maybe<MonetaryAmount> verkoopPrijs = makeMoney(getElement(node, "verkoopPrijs")).apply(currency);

			return Maybe.zip(artikelNummer, omschrijving, prijsPer, eenheid, verkoopPrijs, EsselinkArtikel::new);
		};
	}

	private static Function<CurrencyUnit, Maybe<Function<BtwPercentage, GebruiktEsselinkArtikel>>> makeGebruiktArtikelEsselink(Node node) {
		return currency -> {
			Maybe<EsselinkArtikel> artikel = makeEsselinkArtikel(getElement(node, "artikel")).apply(currency);
			Maybe<Double> aantal = getNodeValue(node, "aantal").map(Double::parseDouble);

			return Maybe.zip(artikel, aantal, (art, aant) -> btw -> new GebruiktEsselinkArtikel(art, aant, btw));
		};
	}

	private static Function<CurrencyUnit, Maybe<Function<BtwPercentage, AnderArtikel>>> makeAnderArtikel(Node node) {
		return currency -> {
			Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
			Maybe<MonetaryAmount> prijs = makeMoney(getElement(node, "prijs")).apply(currency);

			return Maybe.zip(omschrijving, prijs, (omschr, pr) -> btw -> new AnderArtikel(omschr, pr, btw));
		};
	}

	private static BiFunction<CurrencyUnit, BtwPercentage, Observable<Materiaal>> makeMateriaal(Node node) {
		return (currency, btw) -> iterate(node.getChildNodes())
			.filter(item -> !"#text".equals(item.getNodeName()))
			.flatMapMaybe(item -> switch (item.getNodeName()) {
				case "gebruikt-esselink-artikel" -> makeGebruiktArtikelEsselink(item).apply(currency);
				case "ander-artikel" -> makeAnderArtikel(item).apply(currency);
				default -> Maybe.error(new IllegalArgumentException("Unknown artikel type found."));
			})
			.map(f -> f.apply(btw));
	}

	private static Function<CurrencyUnit, Maybe<Function<BtwPercentage, ProductLoon>>> makeProductLoon(Node node) {
		return currency -> {
			Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
			Maybe<Double> uren = getNodeValue(node, "uren").map(Double::parseDouble);
			Maybe<MonetaryAmount> uurloon = makeMoney(getElement(node, "uurloon")).apply(currency);

			return Maybe.zip(omschrijving, uren, uurloon, (omschr, u, ul) -> btw -> new ProductLoon(omschr, u, ul, btw));
		};
	}

	private static Function<CurrencyUnit, Maybe<Function<BtwPercentage, InstantLoon>>> makeInstantLoon(Node node) {
		return currency -> {
			Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
			Maybe<MonetaryAmount> loon = makeMoney(getElement(node, "loon")).apply(currency);

			return Maybe.zip(omschrijving, loon, (omschr, l) -> btw -> new InstantLoon(omschr, l, btw));
		};
	}

	private static BiFunction<CurrencyUnit, BtwPercentage, Observable<Loon>> makeLoonList(Node node) {
		return (currency, btw) -> iterate(node.getChildNodes())
			.filter(n -> !"#text".equals(n.getNodeName()))
			.flatMapMaybe(item -> switch (item.getNodeName()) {
				case "product-loon" -> makeProductLoon(item).apply(currency);
				case "instant-loon" -> makeInstantLoon(item).apply(currency);
				default -> Maybe.error(new IllegalArgumentException("Unknown loon type found."));
			})
			.map(f -> f.apply(btw));
	}

	private static Maybe<ParticulierFactuur> makeParticulierFactuur(Node node) {
		Maybe<FactuurHeader> header = makeFactuurHeader(getElement(node, "factuurHeader"));
		Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
		Maybe<CurrencyUnit> currency = makeCurrency(node);
		BiFunction<CurrencyUnit, BtwPercentage, Observable<Materiaal>> materiaalFunc = makeMateriaal(getElement(node, "itemList"));
		BiFunction<CurrencyUnit, BtwPercentage, Observable<Loon>> loonFunc = makeLoonList(getElement(node, "loonList"));
		Maybe<BtwPercentages> btw = makeBtwPercentage(getElement(node, "btwPercentage"));

		return btw.flatMap(percentage -> currency.flatMap(c -> {
			Observable<Materiaal> arts = materiaalFunc.apply(c, percentage.materiaalPercentage());
			Observable<Loon> loon = loonFunc.apply(c, percentage.loonPercentage());
			Single<Collection<ParticulierArtikel>> items = Observable.concat(arts, loon).collect(ArrayList::new, Collection::add);

			Maybe<ItemList<ParticulierArtikel>> itemList = Maybe.zip(currency, items.toMaybe(), ItemList::new);
			return Maybe.zip(header, omschrijving, itemList, ParticulierFactuur::new);
		}));
	}

	private static Function<CurrencyUnit, Maybe<ReparatiesInkoopOrder>> makeReparatiesInkoopOrder(Node node) {
		return currency -> {
			Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
			Maybe<String> inkoopOrderNummer = getNodeValue(node, "bonnummer");
			Maybe<MonetaryAmount> loon = makeMoney(getElement(node, "loon")).apply(currency);
			Maybe<MonetaryAmount> materiaal = makeMoney(getElement(node, "materiaal")).apply(currency);

			return Maybe.zip(omschrijving, inkoopOrderNummer, loon, materiaal, ReparatiesInkoopOrder::new);
		};
	}

	private static Function<CurrencyUnit, Single<Collection<ReparatiesInkoopOrder>>> makeReparatiesList(Node node) {
		return currency -> iterate(getNodeList(node, "list-item"))
			.map(XmlReader2::makeReparatiesInkoopOrder)
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

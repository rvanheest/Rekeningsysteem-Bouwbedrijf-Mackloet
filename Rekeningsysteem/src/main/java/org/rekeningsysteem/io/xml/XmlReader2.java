package org.rekeningsysteem.io.xml;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.BiFunction;
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
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
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
import org.rekeningsysteem.exception.XmlParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Currency;
import java.util.Optional;

public class XmlReader2 extends XmlLoader {

	public XmlReader2(DocumentBuilder builder) {
		super(builder);
	}

	@Override
	public Single<AbstractRekening> read(Document document) {
		Node bestand = document.getElementsByTagName("bestand").item(0);
		return Optional.ofNullable(bestand.getAttributes().getNamedItem("type")).map(kindNode -> {
			String type = kindNode.getNodeValue();
			return switch (type) {
				case "AangenomenFactuur" -> parseRekening(bestand, XmlReader2::makeAangenomenFactuur, "rekening");
				case "MutatiesFactuur" -> parseRekening(bestand, XmlReader2::makeMutatiesFactuur, "rekening");
				case "Offerte" -> parseRekening(bestand, XmlReader2::makeOfferte, "rekening");
				case "ParticulierFactuur" -> parseRekening(bestand, XmlReader2::makeParticulierFactuur, "rekening");
				case "ReparatiesFactuur" -> parseRekening(bestand, XmlReader2::makeReparatiesFactuur, "rekening");
				default -> Single.<AbstractRekening> error(new XmlParseException(String.format("Geen geschikte Node gevonden. Nodenaam = %s.", type)));
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

	private static Maybe<Geld> makeGeld(Node node) {
		return getNodeValue(node, "bedrag").map(Double::parseDouble).map(Geld::new);
	}

	private static Maybe<Currency> makeCurrency(Node node) {
		return getNodeValue(node, "currency")
			.flatMap(currency ->
				Maybe.fromOptional(
					Currency.getAvailableCurrencies().parallelStream()
						.filter(cur -> currency.equals(cur.getCurrencyCode()))
						.findFirst()
				)
			);
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

	private static Maybe<BiFunction<BtwPercentage, BtwPercentage, Observable<ParticulierArtikel>>> makeAangenomenListItem(Node node) {
		Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
		Maybe<Geld> loon = makeGeld(getElement(node, "loon"));
		Maybe<Geld> materiaal = makeGeld(getElement(node, "materiaal"));

		return Maybe.zip(omschrijving, loon, materiaal,
			(omschr, l, m) -> (lb, mb) -> Observable.just(
				new AnderArtikel(omschr, m, mb),
				new InstantLoon(omschr, l, lb)
			)
		);
	}

	private static BiFunction<BtwPercentage, BtwPercentage, Single<ItemList<ParticulierArtikel>>> makeAangenomenList(Node node) {
		return (btwL, btwM) -> iterate(getNodeList(node, "list-item"))
			.flatMapMaybe(XmlReader2::makeAangenomenListItem)
			.flatMap(f -> f.apply(btwL, btwM))
			.collect(ItemList::new, Collection::add);
	}

	private static Maybe<ParticulierFactuur> makeAangenomenFactuur(Node node) {
		Maybe<FactuurHeader> header = makeFactuurHeader(getElement(node, "factuurHeader"));
		Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
		Maybe<Currency> currency = makeCurrency(node);
		BiFunction<BtwPercentage, BtwPercentage, Single<ItemList<ParticulierArtikel>>> listFunc = makeAangenomenList(getElement(node, "list"));
		Maybe<ItemList<ParticulierArtikel>> list = makeBtwPercentage(getElement(node, "btwPercentage"))
			.flatMapSingle(b -> listFunc.apply(b.loonPercentage(), b.materiaalPercentage()));

		return Maybe.zip(header, omschrijving, currency, list, ParticulierFactuur::new);
	}

	private static Maybe<MutatiesInkoopOrder> makeMutatiesInkoopOrder(Node node) {
		Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
		Maybe<String> inkoopOrderNummer = getNodeValue(node, "bonnummer");
		Maybe<Geld> prijs = makeGeld(getElement(node, "prijs"));

		return Maybe.zip(omschrijving, inkoopOrderNummer, prijs, MutatiesInkoopOrder::new);
	}

	private static Single<ItemList<MutatiesInkoopOrder>> makeMutatiesList(Node node) {
		return iterate(getNodeList(node, "list-item"))
			.flatMapMaybe(XmlReader2::makeMutatiesInkoopOrder)
			.collect(ItemList::new, Collection::add);
	}

	private static Maybe<MutatiesFactuur> makeMutatiesFactuur(Node node) {
		Maybe<FactuurHeader> header = makeFactuurHeader(getElement(node, "factuurHeader"));
		Maybe<Currency> currency = makeCurrency(node);
		Single<ItemList<MutatiesInkoopOrder>> list = makeMutatiesList(getElement(node, "list"));

		return Maybe.zip(header, currency, list.toMaybe(), MutatiesFactuur::new);
	}

	private static Maybe<Offerte> makeOfferte(Node node) {
		Maybe<FactuurHeader> header = makeOfferteFactuurHeader(getElement(node, "factuurHeader"));
		Maybe<String> text = getNodeValue(node, "tekst");
		Maybe<Boolean> ondertekenen = getNodeValue(node, "ondertekenen").map(Boolean::parseBoolean);

		return Maybe.zip(header, text, ondertekenen, Offerte::new);
	}

	private static Maybe<EsselinkArtikel> makeEsselinkArtikel(Node node) {
		Maybe<String> artikelNummer = getNodeValue(node, "artikelNummer");
		Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
		Maybe<Integer> prijsPer = getNodeValue(node, "prijsPer").map(Integer::parseInt);
		Maybe<String> eenheid = getNodeValue(node, "eenheid");
		Maybe<Geld> verkoopPrijs = makeGeld(getElement(node, "verkoopPrijs"));

		return Maybe.zip(artikelNummer, omschrijving, prijsPer, eenheid, verkoopPrijs, EsselinkArtikel::new);
	}

	private static Maybe<Function<BtwPercentage, GebruiktEsselinkArtikel>> makeGebruiktArtikelEsselink(Node node) {
		Maybe<EsselinkArtikel> artikel = makeEsselinkArtikel(getElement(node, "artikel"));
		Maybe<Double> aantal = getNodeValue(node, "aantal").map(Double::parseDouble);

		return Maybe.zip(artikel, aantal, (art, aant) -> btw -> new GebruiktEsselinkArtikel(art, aant, btw));
	}

	private static Maybe<Function<BtwPercentage, AnderArtikel>> makeAnderArtikel(Node node) {
		Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
		Maybe<Geld> prijs = makeGeld(getElement(node, "prijs"));

		return Maybe.zip(omschrijving, prijs, (omschr, pr) -> btw -> new AnderArtikel(omschr, pr, btw));
	}

	private static Function<BtwPercentage, Single<ItemList<ParticulierArtikel>>> makeParticulierList(Node node) {
		return btw -> iterate(node.getChildNodes())
			.filter(item -> !"#text".equals(item.getNodeName()))
			.flatMapMaybe(item -> switch (item.getNodeName()) {
				case "gebruikt-esselink-artikel" -> makeGebruiktArtikelEsselink(item);
				case "ander-artikel" -> makeAnderArtikel(item);
				default -> Maybe.error(new IllegalArgumentException("Unknown artikel type found."));
			})
			.map(f -> f.apply(btw))
			.collect(ItemList::new, Collection::add);
	}

	private static Maybe<Function<BtwPercentage, ProductLoon>> makeProductLoon(Node node) {
		Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
		Maybe<Double> uren = getNodeValue(node, "uren").map(Double::parseDouble);
		Maybe<Geld> uurloon = makeGeld(getElement(node, "uurloon"));

		return Maybe.zip(omschrijving, uren, uurloon, (omschr, u, ul) -> btw -> new ProductLoon(omschr, u, ul, btw));
	}

	private static Maybe<Function<BtwPercentage, InstantLoon>> makeInstantLoon(Node node) {
		Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
		Maybe<Geld> loon = makeGeld(getElement(node, "loon"));

		return Maybe.zip(omschrijving, loon, (omschr, l) -> btw -> new InstantLoon(omschr, l, btw));
	}

	private static Function<BtwPercentage, Single<ItemList<AbstractLoon>>> makeLoonList(Node node) {
		return btw -> iterate(node.getChildNodes())
			.filter(n -> !"#text".equals(n.getNodeName()))
			.flatMapMaybe(item -> switch (item.getNodeName()) {
				case "product-loon" -> makeProductLoon(item);
				case "instant-loon" -> makeInstantLoon(item);
				default -> Maybe.error(new IllegalArgumentException("Unknown loon type found."));
			})
			.map(f -> f.apply(btw))
			.collect(ItemList::new, Collection::add);
	}

	private static Maybe<ParticulierFactuur> makeParticulierFactuur(Node node) {
		Maybe<FactuurHeader> header = makeFactuurHeader(getElement(node, "factuurHeader"));
		Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
		Maybe<Currency> currency = makeCurrency(node);
		Function<BtwPercentage, Single<ItemList<ParticulierArtikel>>> listFunc = makeParticulierList(getElement(node, "itemList"));
		Function<BtwPercentage, Single<ItemList<AbstractLoon>>> loonFunc = makeLoonList(getElement(node, "loonList"));
		Maybe<BtwPercentages> btw = makeBtwPercentage(getElement(node, "btwPercentage"));

		return btw.flatMap(percentage -> {
			Single<ItemList<ParticulierArtikel>> arts = listFunc.apply(percentage.materiaalPercentage());
			Single<ItemList<AbstractLoon>> loon = loonFunc.apply(percentage.loonPercentage());

			return Maybe.zip(header, omschrijving, currency, arts.toMaybe(), loon.toMaybe(), (h, o, c, li, lo) -> {
				li.addAll(lo);
				return new ParticulierFactuur(h, o, c, li);
			});
		});
	}

	private static Maybe<ReparatiesInkoopOrder> makeReparatiesInkoopOrder(Node node) {
		Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
		Maybe<String> inkoopOrderNummer = getNodeValue(node, "bonnummer");
		Maybe<Geld> loon = makeGeld(getElement(node, "loon"));
		Maybe<Geld> materiaal = makeGeld(getElement(node, "materiaal"));

		return Maybe.zip(omschrijving, inkoopOrderNummer, loon, materiaal, ReparatiesInkoopOrder::new);
	}

	private static Single<ItemList<ReparatiesInkoopOrder>> makeReparatiesList(Node node) {
		return iterate(getNodeList(node, "list-item"))
			.flatMapMaybe(XmlReader2::makeReparatiesInkoopOrder)
			.collect(ItemList::new, Collection::add);
	}

	private static Maybe<ReparatiesFactuur> makeReparatiesFactuur(Node node) {
		Maybe<FactuurHeader> header = makeFactuurHeader(getElement(node, "factuurHeader"));
		Maybe<Currency> currency = makeCurrency(node);
		Single<ItemList<ReparatiesInkoopOrder>> list = makeReparatiesList(getElement(node, "list"));

		return Maybe.zip(header, currency, list.toMaybe(), ReparatiesFactuur::new);
	}
}

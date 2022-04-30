package org.rekeningsysteem.io.xml;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.exception.XmlParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Currency;
import java.util.Optional;

public class XmlReader4 extends XmlLoader {

	public XmlReader4(DocumentBuilder builder) {
		super(builder);
	}

	@Override
	public Single<AbstractRekening> read(Document document) {
		Node doc = document.getElementsByTagName("bestand").item(0);

		return Optional.ofNullable(doc.getAttributes().getNamedItem("type"))
			.flatMap(t -> Optional.ofNullable(doc.getAttributes().getNamedItem("version")).map(v -> {
				if ("4".equals(v.getNodeValue())) {
					return switch (t.getNodeValue()) {
						case "MutatiesFactuur" -> parseRekening(doc, XmlReader4::makeMutatiesFactuur, "mutaties-factuur");
						case "Offerte" -> parseRekening(doc, XmlReader4::makeOfferte, "offerte");
						case "ParticulierFactuur" -> parseRekening(doc, XmlReader4::makeParticulierFactuur, "particulier-factuur");
						case "ReparatiesFactuur" -> parseRekening(doc, XmlReader4::makeReparatiesFactuur, "reparaties-factuur");
						default -> Single.<AbstractRekening> error(new XmlParseException(String.format("No suitable invoice type found. Found type: %s", t.getNodeValue())));
					};
				}
				return Single.<AbstractRekening> error(new XmlParseException(String.format("Incorrect version for this parser. Found version: %s", v.getNodeValue())));
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

	private static Maybe<Geld> makeGeld(Node node, String name) {
		return getNodeValue(node, name).map(Geld::new);
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

	private static Maybe<BtwPercentage> makeBtwPercentage(Node node) {
		Maybe<Double> percentage = getStringValue(node).map(Double::parseDouble);
		Single<Boolean> verlegd = getAttribute(node, "verlegd").map(Boolean::parseBoolean).defaultIfEmpty(false);

		return Maybe.zip(percentage, verlegd.toMaybe(), BtwPercentage::new);
	}

	private static Maybe<MutatiesInkoopOrder> makeMutatiesInkoopOrder(Node node) {
		Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
		Maybe<String> inkoopOrderNummer = getNodeValue(node, "bonnummer");
		Maybe<Geld> prijs = makeGeld(node, "prijs");

		return Maybe.zip(omschrijving, inkoopOrderNummer, prijs, MutatiesInkoopOrder::new);
	}

	private static Single<Collection<MutatiesInkoopOrder>> makeMutatiesList(Node node) {
		return iterate(getNodeList(node, "mutaties-bon"))
			.flatMapMaybe(XmlReader4::makeMutatiesInkoopOrder)
			.collect(ArrayList::new, Collection::add);
	}

	private static Maybe<MutatiesFactuur> makeMutatiesFactuur(Node node) {
		Maybe<FactuurHeader> header = makeFactuurHeader(getElement(node, "factuurHeader"));
		Maybe<Currency> currency = makeCurrency(node);
		Single<Collection<MutatiesInkoopOrder>> list = makeMutatiesList(getElement(node, "list"));

		return Maybe.zip(header, currency.zipWith(list.toMaybe(), ItemList::new), MutatiesFactuur::new);
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
		Maybe<Geld> verkoopPrijs = makeGeld(node, "verkoopPrijs");

		return Maybe.zip(artikelNummer, omschrijving, prijsPer, eenheid, verkoopPrijs, EsselinkArtikel::new);
	}

	private static Maybe<GebruiktEsselinkArtikel> makeGebruiktArtikelEsselink(Node node) {
		Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
		Maybe<EsselinkArtikel> artikel = makeEsselinkArtikel(getElement(node, "artikel"));
		Maybe<Double> aantal = getNodeValue(node, "aantal").map(Double::parseDouble);
		Maybe<BtwPercentage> btw = makeBtwPercentage(getElement(node, "materiaalBtwPercentage"));

		return Maybe.zip(omschrijving, artikel, aantal, btw, GebruiktEsselinkArtikel::new);
	}

	private static Maybe<AnderArtikel> makeAnderArtikel(Node node) {
		Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
		Maybe<Geld> prijs = makeGeld(node, "prijs");
		Maybe<BtwPercentage> btw = makeBtwPercentage(getElement(node, "materiaalBtwPercentage"));

		return Maybe.zip(omschrijving, prijs, btw, AnderArtikel::new);
	}

	private static Maybe<ProductLoon> makeProductLoon(Node node) {
		Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
		Maybe<Double> uren = getNodeValue(node, "uren").map(Double::parseDouble);
		Maybe<Geld> uurloon = makeGeld(node, "uurloon");
		Maybe<BtwPercentage> btw = makeBtwPercentage(getElement(node, "loonBtwPercentage"));

		return Maybe.zip(omschrijving, uren, uurloon, btw, ProductLoon::new);
	}

	private static Maybe<InstantLoon> makeInstantLoon(Node node) {
		Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
		Maybe<Geld> loon = makeGeld(node, "loon");
		Maybe<BtwPercentage> btw = makeBtwPercentage(getElement(node, "loonBtwPercentage"));

		return Maybe.zip(omschrijving, loon, btw, InstantLoon::new);
	}

	private static Single<Collection<ParticulierArtikel>> makeItemList(Node node) {
		return iterate(node.getChildNodes())
			.filter(n -> !"#text".equals(n.getNodeName()))
			.flatMapMaybe(item -> {
				String name = item.getNodeName();
				return switch (name) {
					case "gebruikt-esselink-artikel" -> makeGebruiktArtikelEsselink(item);
					case "ander-artikel" -> makeAnderArtikel(item);
					case "product-loon" -> makeProductLoon(item);
					case "instant-loon" -> makeInstantLoon(item);
					default -> Maybe.error(new IllegalArgumentException("Unknown artikel type found. Name = " + name));
				};
			})
			.collect(ArrayList::new, Collection::add);
	}

	private static Maybe<ParticulierFactuur> makeParticulierFactuur(Node node) {
		Maybe<FactuurHeader> header = makeFactuurHeader(getElement(node, "factuurHeader"));
		Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
		Maybe<Currency> currency = makeCurrency(node);
		Single<Collection<ParticulierArtikel>> itemList = makeItemList(getElement(node, "list"));

		return Maybe.zip(header, omschrijving, currency.zipWith(itemList.toMaybe(), ItemList::new), ParticulierFactuur::new);
	}

	private static Maybe<ReparatiesInkoopOrder> makeReparatiesInkoopOrder(Node node) {
		Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
		Maybe<String> inkoopOrderNummer = getNodeValue(node, "bonnummer");
		Maybe<Geld> loon = makeGeld(node, "loon");
		Maybe<Geld> materiaal = makeGeld(node, "materiaal");

		return Maybe.zip(omschrijving, inkoopOrderNummer, loon, materiaal, ReparatiesInkoopOrder::new);
	}

	private static Single<Collection<ReparatiesInkoopOrder>> makeReparatiesList(Node node) {
		return iterate(getNodeList(node, "reparaties-bon"))
			.flatMapMaybe(XmlReader4::makeReparatiesInkoopOrder)
			.collect(ArrayList::new, Collection::add);
	}

	private static Maybe<ReparatiesFactuur> makeReparatiesFactuur(Node node) {
		Maybe<FactuurHeader> header = makeFactuurHeader(getElement(node, "factuurHeader"));
		Maybe<Currency> currency = makeCurrency(node);
		Single<Collection<ReparatiesInkoopOrder>> list = makeReparatiesList(getElement(node, "list"));

		return Maybe.zip(header, currency.zipWith(list.toMaybe(), ItemList::new), ReparatiesFactuur::new);
	}
}

package org.rekeningsysteem.io.xml;

import java.io.File;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Currency;
import java.util.function.Function;

import javax.management.modelmbean.XMLParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.core.Logger;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.particulier.loon.AbstractLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.BtwPercentages;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.exception.GeldParseException;
import org.rekeningsysteem.io.FactuurLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import rx.Observable;
import rx.functions.Func1;

/**
 * This is the XML reader from the 'old days', where we had 2 types of ParticulierFactuur,
 * no AangenomenFactuur and another model.
 * 
 * This XML reader needs to be in here for backwards compatability.
 */
public class XmlReader1 implements FactuurLoader {

	private Currency currency;
	private DocumentBuilder builder;

	public XmlReader1(Logger logger) {
		try {
			this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		catch (ParserConfigurationException e) {
			// Should not happen
			logger.fatal("DocumentBuilder could not be made. (should not happen)", e);
		}
	}

	public XmlReader1(DocumentBuilder builder) {
		this.builder = builder;
	}

	@Override
	public Observable<AbstractRekening> load(File file) {
		try {
			return this.loadRekening(file);
		}
		catch (SAXException | IOException exception) {
			return Observable.error(exception);
		}
	}

	private Observable<AbstractRekening> loadRekening(File file) throws SAXException,
			IOException {
		Document doc = this.builder.parse(file);
		doc.getDocumentElement().normalize();
		Node factuur = doc.getElementsByTagName("bestand").item(0).getFirstChild();
		String soort = factuur.getNodeName();
		if (soort.equals("particulierfactuur1") || soort.equals("partfactuur")) {
			return this.parseRekening(factuur, this::makeParticulierFactuur1);
		}
		else if (soort.equals("particulierfactuur2")) {
			return this.parseRekening(factuur, this::makeParticulierFactuur2);
		}
		else if (soort.equals("reparatiesfactuur")) {
			return this.parseRekening(factuur, this::makeReparatiesFactuur);
		}
		else if (soort.equals("mutatiesfactuur")) {
			return this.parseRekening(factuur, this::makeMutatiesFactuur);
		}
		else if (soort.equals("offerte")) {
			return this.parseRekening(factuur, this::makeOfferte);
		}
		else {
			return Observable.error(new XMLParseException("Geen geschikte Node gevonden. "
					+ "Nodenaam = " + soort + "."));
		}
	}

	private Observable<AbstractRekening> parseRekening(Node bestand,
			Function<Node, Observable<? extends AbstractRekening>> parse) {
		return parse.apply(bestand).cast(AbstractRekening.class);
	}

	private Observable<String> getNodeValue(Node node, String s) {
		if (node == null) {
			return Observable.error(new IllegalArgumentException("node is null"));
		}
		return this.getNodeValue(((Element) node).getElementsByTagName(s));
	}

	private Observable<String> getNodeValue(NodeList list) {
		Node n = list.item(0);
		if (n == null) {
			return Observable.empty();
		}

		n = n.getChildNodes().item(0);

		if (n == null) {
			return Observable.just("");
		}
		return Observable.just(n.getNodeValue());
	}

	private Observable<Geld> makeGeld(String s) {
		String[] ss = s.split(" ");

		try {
			switch (ss.length) {
				case 1:
					return Observable.just(new Geld(ss[0]));
				case 2:
					if (this.currency == null || this.currency.getSymbol().equals(ss[0])) {
						this.currency = Currency.getAvailableCurrencies().parallelStream()
								.filter(cur -> ss[0].equals(cur.getSymbol()))
								.findFirst().get();
						return Observable.just(new Geld(ss[1]));
					}
					else {
						// corrupte file: verschillende valuta's
						return Observable.error(new IllegalArgumentException(
								"This file is corrupt. It has multiple currencies."));
					}

				default:
					throw new GeldParseException("Het bedrag " + s + " kan niet worden gelezen.");
			}
		}
		catch (GeldParseException e) {
			return Observable.error(e);
		}
	}

	private Observable<LocalDate> makeDatum(Node node) {
		Observable<Integer> dag = this.getNodeValue(node, "dag").map(Integer::parseInt);
		Observable<Integer> maand = this.getNodeValue(node, "maand").map(Integer::parseInt);
		Observable<Integer> jaar = this.getNodeValue(node, "jaar").map(Integer::parseInt);

		try {
			return Observable.zip(jaar, maand, dag, LocalDate::of);
		}
		catch (DateTimeException e) {
			return Observable.error(e);
		}
	}

	private Observable<Debiteur> makeDebiteur(Node node) {
		Observable<String> naam = this.getNodeValue(node, "naam");
		Observable<String> straat = this.getNodeValue(node, "straat");
		Observable<String> nummer = this.getNodeValue(node, "nummer");
		Observable<String> postcode = this.getNodeValue(node, "postcode");
		Observable<String> plaats = this.getNodeValue(node, "plaats");
		Observable<String> btwnr = this.getNodeValue(node, "btwnr");

		return btwnr.filter(s -> !s.isEmpty()).isEmpty().flatMap(b -> b
				? Observable.zip(naam, straat, nummer, postcode, plaats, Debiteur::new)
				: Observable.zip(naam, straat, nummer, postcode, plaats, btwnr, Debiteur::new));
	}

	private Observable<OmschrFactuurHeader> makeFactuurHeader(Node node) {
		Observable<Debiteur> debiteur = this.makeDebiteur(((Element) node).getElementsByTagName(
				"debiteur").item(0));
		Observable<LocalDate> datum = this.makeDatum(((Element) node).getElementsByTagName("datum")
				.item(0));
		Observable<String> factuurnummer = this.getNodeValue(node, "factuurnummer");
		Observable<String> omschrijving = this.getNodeValue(node, "omschrijving");

		return Observable.zip(debiteur, datum, factuurnummer, omschrijving,
				OmschrFactuurHeader::new);
	}

	private Observable<FactuurHeader> makeFactuurHeaderWithoutOmschrijving(Node node) {
		Observable<Debiteur> debiteur = this.makeDebiteur(((Element) node).getElementsByTagName(
				"debiteur").item(0));
		Observable<LocalDate> datum = this.makeDatum(((Element) node).getElementsByTagName("datum")
				.item(0));
		Observable<String> factuurnummer = this.getNodeValue(node, "factuurnummer");

		return Observable.zip(debiteur, datum, factuurnummer, FactuurHeader::new);
	}

	private Observable<FactuurHeader> makeOfferteFactuurHeader(Node node) {
		Observable<Debiteur> debiteur = this.makeDebiteur(((Element) node).getElementsByTagName(
				"debiteur").item(0));
		Observable<LocalDate> datum = this.makeDatum(((Element) node).getElementsByTagName("datum")
				.item(0));
		Observable<String> offertenummer = this.getNodeValue(node, "offertenummer");

		return Observable.zip(debiteur, datum, offertenummer, FactuurHeader::new);
	}

	private Observable<Func1<BtwPercentage, AnderArtikel>> makeAnderArtikel(Node node) {
		Node temp = ((Element) node).getElementsByTagName("artikel").item(0);
		Observable<String> omschrijving = this.getNodeValue(temp, "omschrijving");
		Observable<Geld> prijs = this.getNodeValue(((Element) node).getElementsByTagName("prijs"))
				.flatMap(this::makeGeld);

		return Observable.zip(omschrijving, prijs, (omschr, pr) ->
				btw -> new AnderArtikel(omschr, pr, btw));
	}

	private Observable<EsselinkArtikel> makeArtikelEsselink(Node node) {
		Observable<String> artikelNummer = this.getNodeValue(node, "artikelnummer");
		Observable<String> omschrijving = this.getNodeValue(node, "omschrijving");
		Observable<Integer> prijsPer = this.getNodeValue(node, "prijsper").map(Integer::parseInt);
		Observable<String> eenheid = this.getNodeValue(node, "eenheid");
		Observable<Geld> verkoopPrijs = this.getNodeValue(node, "verkoopprijs")
				.flatMap(this::makeGeld);

		return Observable.zip(artikelNummer, omschrijving, prijsPer, eenheid, verkoopPrijs,
				EsselinkArtikel::new);
	}

	private Observable<Func1<BtwPercentage, GebruiktEsselinkArtikel>> makeGebruiktArtikelEsselink(Node node) {
		Observable<EsselinkArtikel> artikel = this.makeArtikelEsselink(((Element) node)
				.getElementsByTagName("artikel").item(0));
		Observable<Double> aantal = this.getNodeValue(node, "aantal").map(Double::parseDouble);

		return Observable.zip(artikel, aantal, (art, aant) ->
				btw -> new GebruiktEsselinkArtikel(art, aant, btw));
	}

	private Observable<Func1<BtwPercentage, ProductLoon>> makeLoon(Node node) {
		Observable<Geld> uurloon = this.getNodeValue(node, "uurloon").flatMap(this::makeGeld);
		Observable<Double> uren = this.getNodeValue(node, "uren").map(Double::parseDouble);

		return Observable.zip(uurloon, uren, (ul, ur) ->
				btw -> new ProductLoon("Uurloon à " + ul.formattedString(), ur, ul, btw));
	}

	private Observable<BtwPercentages> makeEnkelBtw(Node node) {
		Observable<BtwPercentage> btw = this.getNodeValue(node, "btw").map(Double::parseDouble).map(b -> new BtwPercentage(b, false));

		return btw.map(b -> new BtwPercentages(b, b));
	}

	private Observable<BtwPercentages> makeDubbelBtw(Node node) {
		Observable<BtwPercentage> btwArt = this.getNodeValue(node, "btwpercentageart")
				.map(Double::parseDouble)
				.map(b -> new BtwPercentage(b, false));
		Observable<BtwPercentage> btwLoon = this.getNodeValue(node, "btwpercentageloon")
				.map(Double::parseDouble)
				.map(b -> new BtwPercentage(b, false));

		return Observable.zip(btwLoon, btwArt, BtwPercentages::new);
	}

	public Observable<ParticulierFactuur> makeParticulierFactuur1(Node node) {
		Node al = ((Element) node).getElementsByTagName("artikellijst").item(0);
		NodeList gal = ((Element) al).getElementsByTagName("gebruiktartikellijst").item(0)
				.getChildNodes();

		Observable<OmschrFactuurHeader> header = this.makeFactuurHeader(node);
		Func1<BtwPercentage, Observable<ItemList<ParticulierArtikel>>> itemList = btw ->
				Observable.range(0, gal.getLength())
						.map(gal::item)
						.flatMap(item -> {
							switch (item.getNodeName()) {
								case "gebruiktartikelander":
									return this.makeAnderArtikel(item);
								case "gebruiktartikelesselink":
									return this.makeGebruiktArtikelEsselink(item);
								default:
									return Observable.error(new IllegalArgumentException(
											"Unknown artikel type found."));
							}
						})
						.map(f -> f.call(btw))
						.collect(ItemList::new, Collection::add);
		Func1<BtwPercentage, Observable<ItemList<AbstractLoon>>> loonList = btw ->
				this.makeLoon(((Element) node).getElementsByTagName("loon").item(0))
						.map(f -> f.call(btw))
						.collect(ItemList::new, Collection::add);

		Observable<BtwPercentages> btw = this.makeEnkelBtw(node);
		return btw.first().publish(percentage -> {
			Observable<ItemList<ParticulierArtikel>> art = percentage.map(BtwPercentages::getMateriaalPercentage).flatMap(itemList);
			Observable<ItemList<AbstractLoon>> loon = percentage.map(BtwPercentages::getLoonPercentage).flatMap(loonList);
			
			return Observable.zip(header, art, loon,(h, li, lo) -> {
				li.addAll(lo);
				return new ParticulierFactuur(h, this.currency, li);
			});
		});
	}

	private Observable<ParticulierFactuur> makeParticulierFactuur2(Node node) {
		Node al = ((Element) node).getElementsByTagName("artikellijst").item(0);
		NodeList gal = ((Element) al).getElementsByTagName("gebruiktartikellijst").item(0)
				.getChildNodes();

		Observable<OmschrFactuurHeader> header = this.makeFactuurHeader(node);
		Func1<BtwPercentage, Observable<ItemList<ParticulierArtikel>>> itemList = btw ->
		Observable.range(0, gal.getLength())
        				.map(gal::item)
        				.flatMap(item -> {
        					switch (item.getNodeName()) {
        						case "gebruiktartikelander":
        							return this.makeAnderArtikel(item);
        						case "gebruiktartikelesselink":
        							return this.makeGebruiktArtikelEsselink(item);
        						default:
        							return Observable.error(new IllegalArgumentException(
        									"Unknown artikel type found."));
        					}
        				})
        				.map(f -> f.call(btw))
        				.collect(ItemList::new, Collection::add);
        Func1<BtwPercentage, Observable<ItemList<AbstractLoon>>> loonList = btw ->
        		this.makeLoon(((Element) node).getElementsByTagName("loon").item(0))
        				.map(f -> f.call(btw))
        				.collect(ItemList::new, Collection::add);

		Observable<BtwPercentages> btw = this.makeDubbelBtw(node);
		return btw.first().publish(percentage -> {
			Observable<ItemList<ParticulierArtikel>> art = percentage.map(BtwPercentages::getMateriaalPercentage).flatMap(itemList);
			Observable<ItemList<AbstractLoon>> loon = percentage.map(BtwPercentages::getLoonPercentage).flatMap(loonList);
			
			return Observable.zip(header, art, loon, (h, li, lo) -> {
				li.addAll(lo);
				return new ParticulierFactuur(h, this.currency, li);
			});
		});
	}

	private Observable<MutatiesInkoopOrder> makeMutatiesInkoopOrder(Node node) {
		Observable<String> omschrijving = this.getNodeValue(node, "omschrijving");
		Observable<String> inkoopOrderNummer = this.getNodeValue(node, "bonnummer");
		Observable<Geld> prijs = this.getNodeValue(node, "prijs").flatMap(this::makeGeld);

		return Observable.zip(omschrijving, inkoopOrderNummer, prijs, MutatiesInkoopOrder::new);
	}

	private Observable<MutatiesFactuur> makeMutatiesFactuur(Node node) {
		Observable<FactuurHeader> header = this.makeFactuurHeaderWithoutOmschrijving(node);

		Node mbl = ((Element) node).getElementsByTagName("mutatiesbonlijst").item(0);
		NodeList orders = ((Element) mbl).getElementsByTagName("mutatiesbon");
		Observable<ItemList<MutatiesInkoopOrder>> itemList = Observable.range(0, orders.getLength())
				.map(orders::item)
				.flatMap(this::makeMutatiesInkoopOrder)
				.collect(ItemList::new, Collection::add);

		return Observable.zip(header, itemList, (h, il) -> new MutatiesFactuur(h, this.currency,
				il));
	}

	private Observable<ReparatiesInkoopOrder> makeReparatiesInkoopOrder(Node node) {
		Observable<String> omschrijving = this.getNodeValue(node, "omschrijving");
		Observable<String> inkoopOrderNummer = this.getNodeValue(node, "bonnummer");
		Observable<Geld> uurloon = this.getNodeValue(node, "uurloon").flatMap(this::makeGeld);
		Observable<Geld> materiaal = this.getNodeValue(node, "materiaal").flatMap(this::makeGeld);

		return Observable.zip(omschrijving, inkoopOrderNummer, uurloon, materiaal, ReparatiesInkoopOrder::new);
	}

	private Observable<ReparatiesFactuur> makeReparatiesFactuur(Node node) {
		Observable<FactuurHeader> header = this.makeFactuurHeaderWithoutOmschrijving(node);

		Node rbl = ((Element) node).getElementsByTagName("reparatiesbonlijst").item(0);
		NodeList orders = ((Element) rbl).getElementsByTagName("reparatiesbon");
		Observable<ItemList<ReparatiesInkoopOrder>> itemList = Observable.range(0, orders.getLength())
				.map(orders::item)
				.flatMap(this::makeReparatiesInkoopOrder)
				.collect(ItemList::new, Collection::add);

		return Observable.zip(header, itemList, (h, il) -> new ReparatiesFactuur(h, this.currency,
				il));
	}

	private Observable<Offerte> makeOfferte(Node node) {
		Observable<FactuurHeader> header = this.makeOfferteFactuurHeader(node);
		Observable<String> tekst = this.getNodeValue(node, "tekst");
		Observable<Boolean> ondertekenen = this.getNodeValue(node, "ondertekenen")
				.map(Boolean::parseBoolean);

		return Observable.zip(header, tekst, ondertekenen, Offerte::new);
	}
}

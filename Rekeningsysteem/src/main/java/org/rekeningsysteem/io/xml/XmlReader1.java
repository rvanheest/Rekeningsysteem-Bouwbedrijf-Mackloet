package org.rekeningsysteem.io.xml;

import java.io.File;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Currency;

import javax.management.modelmbean.XMLParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier2.EsselinkParticulierArtikel;
import org.rekeningsysteem.data.particulier2.ParticulierArtikel2;
import org.rekeningsysteem.data.particulier2.ParticulierArtikel2Impl;
import org.rekeningsysteem.data.particulier2.ParticulierFactuur2;
import org.rekeningsysteem.data.particulier2.loon.AbstractLoon2;
import org.rekeningsysteem.data.particulier2.loon.ProductLoon2;
import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.exception.GeldParseException;
import org.rekeningsysteem.io.FactuurLoader;
import org.rekeningsysteem.logging.ApplicationLogger;
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

	public XmlReader1() {
		try {
			this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		catch (ParserConfigurationException e) {
			// Should not happen
			ApplicationLogger.getInstance().fatal("DocumentBuilder could not be made. "
					+ "(should not happen)", e);
		}
	}

	public XmlReader1(DocumentBuilder builder) {
		this.builder = builder;
	}

	@Override
	public Observable<? extends AbstractRekening> load(File file) {
		try {
			return this.loadRekening(file);
		}
		catch (SAXException | IOException exception) {
			return Observable.error(exception);
		}
	}

	private Observable<? extends AbstractRekening> loadRekening(File file) throws SAXException,
			IOException {
		Document doc = this.builder.parse(file);
		doc.getDocumentElement().normalize();
		Node factuur = doc.getElementsByTagName("bestand").item(0).getFirstChild();
		String soort = factuur.getNodeName();
		if (soort.equals("particulierfactuur1") || soort.equals("partfactuur")) {
			return this.makeParticulierFactuur1(factuur);
		}
		else if (soort.equals("particulierfactuur2")) {
			return this.makeParticulierFactuur2(factuur);
		}
		else if (soort.equals("reparatiesfactuur")) {
			return this.makeReparatiesFactuur(factuur);
		}
		else if (soort.equals("mutatiesfactuur")) {
			return this.makeMutatiesFactuur(factuur);
		}
		else if (soort.equals("offerte")) {
			return this.makeOfferte(factuur);
		}
		else {
			return Observable.error(new XMLParseException("Geen geschikte Node gevonden. "
					+ "Nodenaam = " + soort + "."));
		}
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

		return btwnr.isEmpty().flatMap(b -> b
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

	private Observable<Func1<Double, ParticulierArtikel2Impl>> makeAnderArtikel(Node node) {
		Node temp = ((Element) node).getElementsByTagName("artikel").item(0);
		Observable<String> omschrijving = this.getNodeValue(temp, "omschrijving");
		Observable<Geld> prijs = this.getNodeValue(((Element) node).getElementsByTagName("prijs"))
				.flatMap(this::makeGeld);

		return Observable.zip(omschrijving, prijs, (omschr, pr) ->
				btw -> new ParticulierArtikel2Impl(omschr, pr, btw));
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

	private Observable<Func1<Double, EsselinkParticulierArtikel>> makeGebruiktArtikelEsselink(Node node) {
		Observable<EsselinkArtikel> artikel = this.makeArtikelEsselink(((Element) node)
				.getElementsByTagName("artikel").item(0));
		Observable<Double> aantal = this.getNodeValue(node, "aantal").map(Double::parseDouble);

		return Observable.zip(artikel, aantal, (art, aant) ->
				btw -> new EsselinkParticulierArtikel(art, aant, btw));
	}

	private Observable<Func1<Double, ProductLoon2>> makeLoon(Node node) {
		Observable<Geld> uurloon = this.getNodeValue(node, "uurloon").flatMap(this::makeGeld);
		Observable<Double> uren = this.getNodeValue(node, "uren").map(Double::parseDouble);

		return Observable.zip(uurloon, uren, (ul, ur) ->
				btw -> new ProductLoon2("Uurloon à " + ul.formattedString(), ur, ul, btw));
	}

	private Observable<BtwPercentage> makeEnkelBtw(Node node) {
		Observable<Double> btw = this.getNodeValue(node, "btw").map(Double::parseDouble);

		return btw.map(b -> new BtwPercentage(b, b));
	}

	private Observable<BtwPercentage> makeDubbelBtw(Node node) {
		Observable<Double> btwArt = this.getNodeValue(node, "btwpercentageart")
				.map(Double::parseDouble);
		Observable<Double> btwLoon = this.getNodeValue(node, "btwpercentageloon")
				.map(Double::parseDouble);

		return Observable.zip(btwLoon, btwArt, BtwPercentage::new);
	}

	public Observable<ParticulierFactuur2> makeParticulierFactuur1(Node node) {
		Node al = ((Element) node).getElementsByTagName("artikellijst").item(0);
		NodeList gal = ((Element) al).getElementsByTagName("gebruiktartikellijst").item(0)
				.getChildNodes();

		Observable<OmschrFactuurHeader> header = this.makeFactuurHeader(node);
		Func1<Double, Observable<ItemList<ParticulierArtikel2>>> itemList = btw ->
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
		Func1<Double, Observable<ItemList<AbstractLoon2>>> loonList = btw ->
				this.makeLoon(((Element) node).getElementsByTagName("loon").item(0))
						.map(f -> f.call(btw))
						.collect(ItemList::new, Collection::add);

		Observable<BtwPercentage> btw = this.makeEnkelBtw(node);
		return btw.first().publish(percentage -> {
			Observable<ItemList<ParticulierArtikel2>> art = percentage.map(b -> b.materiaalPercentage).flatMap(b -> itemList.call(b));
			Observable<ItemList<AbstractLoon2>> loon = percentage.map(b -> b.loonPercentage).flatMap(b -> loonList.call(b));
			
			return Observable.zip(header, art, loon,(h, li, lo) -> {
				li.addAll(lo);
				return new ParticulierFactuur2(h, this.currency, li);
			});
		});
	}

	private Observable<ParticulierFactuur2> makeParticulierFactuur2(Node node) {
		Node al = ((Element) node).getElementsByTagName("artikellijst").item(0);
		NodeList gal = ((Element) al).getElementsByTagName("gebruiktartikellijst").item(0)
				.getChildNodes();

		Observable<OmschrFactuurHeader> header = this.makeFactuurHeader(node);
		Func1<Double, Observable<ItemList<ParticulierArtikel2>>> itemList = btw ->
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
        Func1<Double, Observable<ItemList<AbstractLoon2>>> loonList = btw ->
        		this.makeLoon(((Element) node).getElementsByTagName("loon").item(0))
        				.map(f -> f.call(btw))
        				.collect(ItemList::new, Collection::add);

		Observable<BtwPercentage> btw = this.makeDubbelBtw(node);
		return btw.first().publish(percentage -> {
			Observable<ItemList<ParticulierArtikel2>> art = percentage.map(b -> b.materiaalPercentage).flatMap(b -> itemList.call(b));
			Observable<ItemList<AbstractLoon2>> loon = percentage.map(b -> b.loonPercentage).flatMap(b -> loonList.call(b));
			
			return Observable.zip(header, art, loon, (h, li, lo) -> {
				li.addAll(lo);
				return new ParticulierFactuur2(h, this.currency, li);
			});
		});
	}

	private Observable<MutatiesBon> makeMutatiesBon(Node node) {
		Observable<String> omschrijving = this.getNodeValue(node, "omschrijving");
		Observable<String> bonnummer = this.getNodeValue(node, "bonnummer");
		Observable<Geld> prijs = this.getNodeValue(node, "prijs").flatMap(this::makeGeld);

		return Observable.zip(omschrijving, bonnummer, prijs, MutatiesBon::new);
	}

	private Observable<MutatiesFactuur> makeMutatiesFactuur(Node node) {
		Observable<FactuurHeader> header = this.makeFactuurHeaderWithoutOmschrijving(node);

		Node mbl = ((Element) node).getElementsByTagName("mutatiesbonlijst").item(0);
		NodeList bonnen = ((Element) mbl).getElementsByTagName("mutatiesbon");
		Observable<ItemList<MutatiesBon>> itemList = Observable.range(0, bonnen.getLength())
				.map(bonnen::item)
				.flatMap(this::makeMutatiesBon)
				.collect(ItemList::new, Collection::add);

		return Observable.zip(header, itemList, (h, il) -> new MutatiesFactuur(h, this.currency,
				il));
	}

	private Observable<ReparatiesBon> makeReparatiesBon(Node node) {
		Observable<String> omschrijving = this.getNodeValue(node, "omschrijving");
		Observable<String> bonnummer = this.getNodeValue(node, "bonnummer");
		Observable<Geld> uurloon = this.getNodeValue(node, "uurloon").flatMap(this::makeGeld);
		Observable<Geld> materiaal = this.getNodeValue(node, "materiaal").flatMap(this::makeGeld);

		return Observable.zip(omschrijving, bonnummer, uurloon, materiaal, ReparatiesBon::new);
	}

	private Observable<ReparatiesFactuur> makeReparatiesFactuur(Node node) {
		Observable<FactuurHeader> header = this.makeFactuurHeaderWithoutOmschrijving(node);

		Node rbl = ((Element) node).getElementsByTagName("reparatiesbonlijst").item(0);
		NodeList bonnen = ((Element) rbl).getElementsByTagName("reparatiesbon");
		Observable<ItemList<ReparatiesBon>> itemList = Observable.range(0, bonnen.getLength())
				.map(bonnen::item)
				.flatMap(this::makeReparatiesBon)
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

	public final class BtwPercentage {

		public final double loonPercentage;
		public final double materiaalPercentage;

		public BtwPercentage(double loonPercentage, double materiaalPercentage) {
			this.loonPercentage = loonPercentage;
			this.materiaalPercentage = materiaalPercentage;
		}
	}
}

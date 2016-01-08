package org.rekeningsysteem.io.xml;

import java.io.File;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Currency;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.management.modelmbean.XMLParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.particulier.loon.AbstractLoon;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.FactuurLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import rx.Observable;

/**
 * This is the XML reader from the times where we had a AangenomenFactuur as well as a
 * ParticulierFactuur.
 * 
 * This XML reader needs to be in here for backwards compatibility.
 */
public class XmlReader3 implements FactuurLoader {

	private DocumentBuilder builder;

	public XmlReader3(Logger logger) {
		try {
			this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		catch (ParserConfigurationException e) {
			// Should not happen
			logger.fatal("DocumentBuilder could not be made. (should not happen)", e);
		}
	}

	public XmlReader3(DocumentBuilder builder) {
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
		Node bestand = doc.getElementsByTagName("bestand").item(0);
		Node typeNode = bestand.getAttributes().getNamedItem("type");
		Node versionNode = bestand.getAttributes().getNamedItem("version");
		if (typeNode == null || versionNode == null) {
			return Observable.error(new XMLParseException("No factuur type is specified"));
		}
		else {
			String version = versionNode.getNodeValue();
    		if ("3".equals(version)) {
    			String type = typeNode.getNodeValue();
    			if (type.equals("AangenomenFactuur")) {
					return this.parseRekening(bestand, this::makeAangenomenFactuur, "rekening");
        		}
        		else if (type.equals("MutatiesFactuur")) {
					return this.parseRekening(bestand, this::makeMutatiesFactuur, "rekening");
        		}
        		else if (type.equals("Offerte")) {
					return this.parseRekening(bestand, this::makeOfferte, "rekening");
        		}
        		else if (type.equals("ParticulierFactuur")) {
					return this.parseRekening(bestand, this::makeParticulierFactuur, "rekening");
        		}
        		else if (type.equals("ReparatiesFactuur")) {
					return this.parseRekening(bestand, this::makeReparatiesFactuur, "rekening");
        		}
        		else {
        			return Observable.error(new XMLParseException("Geen geschikte Node gevonden. "
        					+ "Nodenaam = " + type + "."));
        		}
    		}
    		else {
    			return Observable.error(new XMLParseException("Geen geschikte Node gevonden. "
    					+ "Versie = " + version + "."));
    		}
		}
	}

	private Observable<AbstractRekening> parseRekening(Node bestand,
			Function<Node, Observable<? extends AbstractRekening>> parse, String name) {
		NodeList nodes = ((Element) bestand).getElementsByTagName(name);
		if (nodes.getLength() != 0) {
			return parse.apply(nodes.item(0)).cast(AbstractRekening.class);
		}
		else {
			return Observable.error(new XMLParseException("Could not parse file to object."));
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

	private Observable<Node> nodeListOrError(Supplier<NodeList> listSupplier) {
		return Observable.<NodeList>create(subscriber -> {
			NodeList list = listSupplier.get();
			if (list == null) {
				subscriber.onError(new IllegalArgumentException("No itemList found."));
			}
			else {
				subscriber.onNext(list);
				subscriber.onCompleted();
			}
		}).flatMap(list -> Observable.range(0, list.getLength()).map(list::item));
	}

	private Observable<Debiteur> makeDebiteur(Node node) {
		Observable<String> naam = this.getNodeValue(node, "naam");
		Observable<String> straat = this.getNodeValue(node, "straat");
		Observable<String> nummer = this.getNodeValue(node, "nummer");
		Observable<String> postcode = this.getNodeValue(node, "postcode");
		Observable<String> plaats = this.getNodeValue(node, "plaats");
		Observable<String> btwnr = this.getNodeValue(node, "btwNummer");

		return btwnr.isEmpty().flatMap(b -> b
				? Observable.zip(naam, straat, nummer, postcode, plaats, Debiteur::new)
				: Observable.zip(naam, straat, nummer, postcode, plaats, btwnr, Debiteur::new));
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
		Observable<String> offertenummer = this.getNodeValue(node, "factuurnummer");

		return Observable.zip(debiteur, datum, offertenummer, FactuurHeader::new);
	}

	private Observable<Geld> makeGeld(Node node) {
		return this.getNodeValue(node, "bedrag").map(Double::parseDouble).map(Geld::new);
	}

	private Observable<ParticulierArtikel> makeAangenomenListItem(Node node) {
		Observable<String> omschrijving = this.getNodeValue(node, "omschrijving");
		Observable<Geld> loon = this
				.makeGeld(((Element) node).getElementsByTagName("loon").item(0));
		Observable<Double> loonBtw = this.getNodeValue(node, "loonBtwPercentage").map(Double::parseDouble);
		Observable<Geld> materiaal = this.makeGeld(((Element) node).getElementsByTagName(
				"materiaal").item(0));
		Observable<Double> materiaalBtw = this.getNodeValue(node, "materiaalBtwPercentage").map(Double::parseDouble);

		return Observable.zip(omschrijving, loon, loonBtw, materiaal, materiaalBtw,
				(omschr, l, lb, m, mb) -> Observable.just(
						new AnderArtikel(omschr, m, mb),
						new InstantLoon(omschr, l, lb)))
				.flatMap(xs -> xs);
	}

	private Observable<ItemList<ParticulierArtikel>> makeAangenomenList(Node node) {
		return this.nodeListOrError(() -> ((Element) node).getElementsByTagName("list-item"))
				.flatMap(this::makeAangenomenListItem)
				.collect(ItemList::new, Collection::add);
	}

	private Observable<ParticulierFactuur> makeAangenomenFactuur(Node node) {
		Observable<OmschrFactuurHeader> header = this.makeFactuurHeader(((Element) node)
				.getElementsByTagName("factuurHeader").item(0));
		Observable<Currency> currency = this.getNodeValue(node, "currency")
				.map(s -> Currency.getAvailableCurrencies().parallelStream()
						.filter(cur -> s.equals(cur.getCurrencyCode()))
						.findFirst().get());
		Observable<ItemList<ParticulierArtikel>> list =
				this.makeAangenomenList(((Element) node).getElementsByTagName("list").item(0));

		return Observable.zip(header, currency, list, ParticulierFactuur::new);
	}

	private Observable<MutatiesBon> makeMutatiesBon(Node node) {
		Observable<String> omschrijving = this.getNodeValue(node, "omschrijving");
		Observable<String> bonnummer = this.getNodeValue(node, "bonnummer");
		Observable<Geld> prijs = this.makeGeld(((Element) node).getElementsByTagName("prijs")
				.item(0));

		return Observable.zip(omschrijving, bonnummer, prijs, MutatiesBon::new);
	}

	private Observable<ItemList<MutatiesBon>> makeMutatiesList(Node node) {
		return this.nodeListOrError(() -> ((Element) node).getElementsByTagName("list-item"))
				.flatMap(this::makeMutatiesBon)
				.collect(ItemList::new, Collection::add);
	}

	private Observable<MutatiesFactuur> makeMutatiesFactuur(Node node) {
		Observable<FactuurHeader> header = this.makeFactuurHeaderWithoutOmschrijving(
				((Element) node).getElementsByTagName("factuurHeader").item(0));
		Observable<Currency> currency = this.getNodeValue(node, "currency")
				.map(s -> Currency.getAvailableCurrencies().parallelStream()
						.filter(cur -> s.equals(cur.getCurrencyCode()))
						.findFirst().get());
		Observable<ItemList<MutatiesBon>> list = this.makeMutatiesList(
				((Element) node).getElementsByTagName("list").item(0));

		return Observable.zip(header, currency, list, MutatiesFactuur::new);
	}

	private Observable<Offerte> makeOfferte(Node node) {
		Observable<FactuurHeader> header = this.makeOfferteFactuurHeader(
				((Element) node).getElementsByTagName("factuurHeader").item(0));
		Observable<String> text = this.getNodeValue(node, "tekst");
		Observable<Boolean> ondertekenen = this.getNodeValue(node, "ondertekenen")
				.map(Boolean::parseBoolean);

		return Observable.zip(header, text, ondertekenen, Offerte::new);
	}

	private Observable<EsselinkArtikel> makeEsselinkArtikel(Node node) {
		Observable<String> artikelNummer = this.getNodeValue(node, "artikelNummer");
		Observable<String> omschrijving = this.getNodeValue(node, "omschrijving");
		Observable<Integer> prijsPer = this.getNodeValue(node, "prijsPer").map(Integer::parseInt);
		Observable<String> eenheid = this.getNodeValue(node, "eenheid");
		Observable<Geld> verkoopPrijs = this.makeGeld(((Element) node)
				.getElementsByTagName("verkoopPrijs").item(0));

		return Observable.zip(artikelNummer, omschrijving, prijsPer, eenheid, verkoopPrijs,
				EsselinkArtikel::new);
	}

	private Observable<GebruiktEsselinkArtikel> makeGebruiktArtikelEsselink(Node node) {
		Observable<EsselinkArtikel> artikel = this.makeEsselinkArtikel(((Element) node)
				.getElementsByTagName("artikel").item(0));
		Observable<Double> aantal = this.getNodeValue(node, "aantal").map(Double::parseDouble);
		Observable<Double> btw = this.getNodeValue(node, "materiaalBtwPercentage").map(Double::parseDouble);

		return Observable.zip(artikel, aantal, btw, GebruiktEsselinkArtikel::new);
	}

	private Observable<AnderArtikel> makeAnderArtikel(Node node) {
		Observable<String> omschrijving = this.getNodeValue(node, "omschrijving");
		Observable<Geld> prijs = this.makeGeld(((Element) node)
				.getElementsByTagName("prijs").item(0));
		Observable<Double> btw = this.getNodeValue(node, "materiaalBtwPercentage").map(Double::parseDouble);

		return Observable.zip(omschrijving, prijs, btw, AnderArtikel::new);
	}

	private Observable<ItemList<ParticulierArtikel>> makeParticulierList(Node node) {
		return this.nodeListOrError(() -> node.getChildNodes())
				.filter(item -> !"#text".equals(item.getNodeName()))
				.flatMap(item -> {
					switch (item.getNodeName()) {
						case "gebruikt-esselink-artikel":
							return this.makeGebruiktArtikelEsselink(item);
						case "ander-artikel":
							return this.makeAnderArtikel(item);
						default:
							return Observable.error(new IllegalArgumentException(
									"Unknown artikel type found."));
					}
				})
				.collect(ItemList::new, Collection::add);
	}

	private Observable<ProductLoon> makeProductLoon(Node node) {
		Observable<String> omschrijving = this.getNodeValue(node, "omschrijving");
		Observable<Double> uren = this.getNodeValue(node, "uren").map(Double::parseDouble);
		Observable<Geld> uurloon = this.makeGeld(((Element) node)
				.getElementsByTagName("uurloon").item(0));
		Observable<Double> btw = this.getNodeValue(node, "loonBtwPercentage").map(Double::parseDouble);

		return Observable.zip(omschrijving, uren, uurloon, btw, ProductLoon::new);
	}

	private Observable<InstantLoon> makeInstantLoon(Node node) {
		Observable<String> omschrijving = this.getNodeValue(node, "omschrijving");
		Observable<Geld> loon = this.makeGeld(((Element) node)
				.getElementsByTagName("loon").item(0));
		Observable<Double> btw = this.getNodeValue(node, "loonBtwPercentage").map(Double::parseDouble);

		return Observable.zip(omschrijving, loon, btw, InstantLoon::new);
	}

	private Observable<ItemList<AbstractLoon>> makeLoonList(Node node) {
		return this.nodeListOrError(() -> node.getChildNodes())
				.filter(n -> !"#text".equals(n.getNodeName()))
				.flatMap(item -> {
					switch (item.getNodeName()) {
						case "product-loon":
							return this.makeProductLoon(item);
						case "instant-loon":
							return this.makeInstantLoon(item);
						default:
							return Observable.error(new IllegalArgumentException(
									"Unknown loon type found."));
					}
				})
				.collect(ItemList::new, Collection::add);
	}

	private Observable<ParticulierFactuur> makeParticulierFactuur(Node node) {
		Observable<OmschrFactuurHeader> header = this.makeFactuurHeader(((Element) node)
				.getElementsByTagName("factuurHeader").item(0));
		Observable<Currency> currency = this.getNodeValue(node, "currency")
				.map(s -> Currency.getAvailableCurrencies().parallelStream()
						.filter(cur -> s.equals(cur.getCurrencyCode()))
						.findFirst().get());
		Observable<ItemList<ParticulierArtikel>> artList =
				this.makeParticulierList(((Element) node).getElementsByTagName("itemList").item(0));
		Observable<ItemList<AbstractLoon>> loonList =
				this.makeLoonList(((Element) node).getElementsByTagName("loonList").item(0));
		return Observable.zip(header, currency, artList, loonList, (h, c, li, lo) -> {
			li.addAll(lo);
			return new ParticulierFactuur(h, c, li);
		});
	}

	private Observable<ReparatiesBon> makeReparatiesBon(Node node) {
		Observable<String> omschrijving = this.getNodeValue(node, "omschrijving");
		Observable<String> bonnummer = this.getNodeValue(node, "bonnummer");
		Observable<Geld> loon = this.makeGeld(((Element) node)
				.getElementsByTagName("loon").item(0));
		Observable<Geld> materiaal = this.makeGeld(((Element) node)
				.getElementsByTagName("materiaal").item(0));

		return Observable.zip(omschrijving, bonnummer, loon, materiaal, ReparatiesBon::new);
	}

	private Observable<ItemList<ReparatiesBon>> makeReparatiesList(Node node) {
		return this.nodeListOrError(() -> ((Element) node).getElementsByTagName("list-item"))
				.flatMap(this::makeReparatiesBon)
				.collect(ItemList::new, Collection::add);
	}

	private Observable<ReparatiesFactuur> makeReparatiesFactuur(Node node) {
		Observable<FactuurHeader> header = this.makeFactuurHeaderWithoutOmschrijving(
				((Element) node).getElementsByTagName("factuurHeader").item(0));
		Observable<Currency> currency = this.getNodeValue(node, "currency")
				.map(s -> Currency.getAvailableCurrencies().parallelStream()
						.filter(cur -> s.equals(cur.getCurrencyCode()))
						.findFirst().get());
		Observable<ItemList<ReparatiesBon>> list = this.makeReparatiesList(
				((Element) node).getElementsByTagName("list").item(0));

		return Observable.zip(header, currency, list, ReparatiesFactuur::new);
	}
}

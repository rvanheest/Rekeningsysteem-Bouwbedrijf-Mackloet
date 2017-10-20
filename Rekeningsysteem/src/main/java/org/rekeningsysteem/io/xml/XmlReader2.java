package org.rekeningsysteem.io.xml;

import java.io.File;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Currency;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.management.modelmbean.XMLParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
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
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
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
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * This is the XML reader from the times where an AbstractFactuur had a BtwPercentage.
 * 
 * This XML reader needs to be in here for backwards compatibility.
 */
public class XmlReader2 implements FactuurLoader {

	private DocumentBuilder builder;

	public XmlReader2(Logger logger) {
		try {
			this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		catch (ParserConfigurationException e) {
			// Should not happen
			logger.fatal("DocumentBuilder could not be made. (should not happen)", e);
		}
	}

	public XmlReader2(DocumentBuilder builder) {
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
		if (typeNode == null) {
			return Observable.error(new XMLParseException("No factuur type is specified"));
		}
		else {
			String type = typeNode.getNodeValue();
			if (type.equals("AangenomenFactuur")) {
				return this.parseRekening(bestand, this::makeAangenomenFactuur, "rekening");
			}
			else if (type.equals("MutatiesFactuur")) {
				return this.parseRekening(bestand, this::makeMutatiesFactuur, "rekening",
						"mutaties-factuur");
			}
			else if (type.equals("Offerte")) {
				return this.parseRekening(bestand, this::makeOfferte, "rekening", "offerte");
			}
			else if (type.equals("ParticulierFactuur")) {
				return this.parseRekening(bestand, this::makeParticulierFactuur, "rekening",
						"particulier-factuur");
			}
			else if (type.equals("ReparatiesFactuur")) {
				return this.parseRekening(bestand, this::makeReparatiesFactuur, "rekening",
						"reparaties-factuur");
			}
			else {
				return Observable.error(new XMLParseException("Geen geschikte Node gevonden. "
						+ "Nodenaam = " + type + "."));
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

	private Observable<AbstractRekening> parseRekening(Node bestand,
			Function<Node, Observable<? extends AbstractRekening>> parse, String... names) {
		return Stream.of(names)
				.map(((Element) bestand)::getElementsByTagName)
				.filter(list -> list.getLength() != 0)
				.map(list -> list.item(0))
				.map(parse)
				.findFirst()
				.orElse(Observable.error(new XMLParseException("Could not parse file to object.")))
				.cast(AbstractRekening.class);
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
		return Observable.<NodeList> create(subscriber -> {
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

	private Observable<BtwPercentage> makeBtwPercentage(Node node) {
		Observable<Double> loon = this.getNodeValue(node, "loonPercentage")
				.map(Double::parseDouble);
		Observable<Double> materiaal = this.getNodeValue(node, "materiaalPercentage")
				.map(Double::parseDouble);

		return Observable.zip(loon, materiaal, BtwPercentage::new);
	}

	private Observable<Func2<Double, Double, Observable<ParticulierArtikel>>> makeAangenomenListItem(Node node) {
		Observable<String> omschrijving = this.getNodeValue(node, "omschrijving");
		Observable<Geld> loon = this
				.makeGeld(((Element) node).getElementsByTagName("loon").item(0));
		Observable<Geld> materiaal = this.makeGeld(((Element) node).getElementsByTagName(
				"materiaal").item(0));

		return Observable.zip(omschrijving, loon, materiaal,
				(omschr, l, m) -> (lb, mb) -> Observable.just(
						new AnderArtikel(omschr, m, mb),
						new InstantLoon(omschr, l, lb)));
	}

	private Func2<Double, Double, Observable<ItemList<ParticulierArtikel>>> makeAangenomenList(Node node) {
		return (btwL, btwM) -> this.nodeListOrError(() -> ((Element) node).getElementsByTagName("list-item"))
				.flatMap(this::makeAangenomenListItem)
				.flatMap(f -> f.call(btwL, btwM))
				.collect(ItemList::new, Collection::add);
	}

	private Observable<ParticulierFactuur> makeAangenomenFactuur(Node node) {
		Observable<OmschrFactuurHeader> header = this.makeFactuurHeader(((Element) node)
				.getElementsByTagName("factuurHeader").item(0));
		Observable<Currency> currency = this.getNodeValue(node, "currency")
				.map(s -> Currency.getAvailableCurrencies().parallelStream()
						.filter(cur -> s.equals(cur.getCurrencyCode()))
						.findFirst().get());
		Func2<Double, Double, Observable<ItemList<ParticulierArtikel>>> listFunc =
				this.makeAangenomenList(((Element) node).getElementsByTagName("list").item(0));
		Observable<BtwPercentage> btw = this.makeBtwPercentage(((Element) node)
				.getElementsByTagName("btwPercentage").item(0));
		Observable<ItemList<ParticulierArtikel>> list = btw.flatMap(b -> listFunc
				.call(b.loonPercentage, b.materiaalPercentage));

		return Observable.zip(header, currency, list, ParticulierFactuur::new);
	}

	private Observable<MutatiesInkoopOrder> makeMutatiesInkoopOrder(Node node) {
		Observable<String> omschrijving = this.getNodeValue(node, "omschrijving");
		Observable<String> inkoopOrderNummer = this.getNodeValue(node, "bonnummer");
		Observable<Geld> prijs = this.makeGeld(((Element) node).getElementsByTagName("prijs")
				.item(0));

		return Observable.zip(omschrijving, inkoopOrderNummer, prijs, MutatiesInkoopOrder::new);
	}

	private Observable<ItemList<MutatiesInkoopOrder>> makeMutatiesList(Node node) {
		return this.nodeListOrError(() -> ((Element) node).getElementsByTagName("list-item"))
				.flatMap(this::makeMutatiesInkoopOrder)
				.collect(ItemList::new, Collection::add);
	}

	private Observable<MutatiesFactuur> makeMutatiesFactuur(Node node) {
		Observable<FactuurHeader> header = this.makeFactuurHeaderWithoutOmschrijving(
				((Element) node).getElementsByTagName("factuurHeader").item(0));
		Observable<Currency> currency = this.getNodeValue(node, "currency")
				.map(s -> Currency.getAvailableCurrencies().parallelStream()
						.filter(cur -> s.equals(cur.getCurrencyCode()))
						.findFirst().get());
		Observable<ItemList<MutatiesInkoopOrder>> list = this.makeMutatiesList(
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

	private Observable<Func1<Double, GebruiktEsselinkArtikel>> makeGebruiktArtikelEsselink(Node node) {
		Observable<EsselinkArtikel> artikel = this.makeEsselinkArtikel(((Element) node)
				.getElementsByTagName("artikel").item(0));
		Observable<Double> aantal = this.getNodeValue(node, "aantal").map(Double::parseDouble);

		return Observable.zip(artikel, aantal, (art, aant) ->
				btw -> new GebruiktEsselinkArtikel(art, aant, btw));
	}

	private Observable<Func1<Double, AnderArtikel>> makeAnderArtikel(Node node) {
		Observable<String> omschrijving = this.getNodeValue(node, "omschrijving");
		Observable<Geld> prijs = this.makeGeld(((Element) node)
				.getElementsByTagName("prijs").item(0));

		return Observable.zip(omschrijving, prijs, (omschr, pr) ->
				btw -> new AnderArtikel(omschr, pr, btw));
	}

	private Func1<Double, Observable<ItemList<ParticulierArtikel>>> makeParticulierList(Node node) {
		return btw -> this.nodeListOrError(node::getChildNodes)
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
				.map(f -> f.call(btw))
				.collect(ItemList::new, Collection::add);
	}

	private Observable<Func1<Double, ProductLoon>> makeProductLoon(Node node) {
		Observable<String> omschrijving = this.getNodeValue(node, "omschrijving");
		Observable<Double> uren = this.getNodeValue(node, "uren").map(Double::parseDouble);
		Observable<Geld> uurloon = this.makeGeld(((Element) node)
				.getElementsByTagName("uurloon").item(0));

		return Observable.zip(omschrijving, uren, uurloon, (omschr, u, ul) ->
				btw -> new ProductLoon(omschr, u, ul, btw));
	}

	private Observable<Func1<Double, InstantLoon>> makeInstantLoon(Node node) {
		Observable<String> omschrijving = this.getNodeValue(node, "omschrijving");
		Observable<Geld> loon = this.makeGeld(((Element) node)
				.getElementsByTagName("loon").item(0));

		return Observable.zip(omschrijving, loon, (omschr, l) ->
				btw -> new InstantLoon(omschr, l, btw));
	}

	private Func1<Double, Observable<ItemList<AbstractLoon>>> makeLoonList(Node node) {
		return btw -> this.nodeListOrError(node::getChildNodes)
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
				.map(f -> f.call(btw))
				.collect(ItemList::new, Collection::add);
	}

	private Observable<ParticulierFactuur> makeParticulierFactuur(Node node) {
		Observable<OmschrFactuurHeader> header = this.makeFactuurHeader(((Element) node)
				.getElementsByTagName("factuurHeader").item(0));
		Observable<Currency> currency = this.getNodeValue(node, "currency")
				.map(s -> Currency.getAvailableCurrencies().parallelStream()
						.filter(cur -> s.equals(cur.getCurrencyCode()))
						.findFirst().get());
		Func1<Double, Observable<ItemList<ParticulierArtikel>>> listFunc =
				this.makeParticulierList(((Element) node).getElementsByTagName("itemList").item(0));
		Func1<Double, Observable<ItemList<AbstractLoon>>> loonFunc =
				this.makeLoonList(((Element) node).getElementsByTagName("loonList").item(0));
		Observable<BtwPercentage> btw = this.makeBtwPercentage(((Element) node)
				.getElementsByTagName("btwPercentage").item(0));
		return btw.first().publish(percentage -> {
			Observable<ItemList<ParticulierArtikel>> arts = percentage
					.map(b -> b.materiaalPercentage)
					.flatMap(listFunc);
			Observable<ItemList<AbstractLoon>> loon = percentage
					.map(b -> b.loonPercentage)
					.flatMap(loonFunc);

			return Observable.zip(header, currency, arts, loon, (h, c, li, lo) -> {
				li.addAll(lo);
				return new ParticulierFactuur(h, c, li);
			});
		});
	}

	private Observable<ReparatiesInkoopOrder> makeReparatiesInkoopOrder(Node node) {
		Observable<String> omschrijving = this.getNodeValue(node, "omschrijving");
		Observable<String> inkoopOrderNummer = this.getNodeValue(node, "bonnummer");
		Observable<Geld> loon = this.makeGeld(((Element) node)
				.getElementsByTagName("loon").item(0));
		Observable<Geld> materiaal = this.makeGeld(((Element) node)
				.getElementsByTagName("materiaal").item(0));

		return Observable.zip(omschrijving, inkoopOrderNummer, loon, materiaal, ReparatiesInkoopOrder::new);
	}

	private Observable<ItemList<ReparatiesInkoopOrder>> makeReparatiesList(Node node) {
		return this.nodeListOrError(() -> ((Element) node).getElementsByTagName("list-item"))
				.flatMap(this::makeReparatiesInkoopOrder)
				.collect(ItemList::new, Collection::add);
	}

	private Observable<ReparatiesFactuur> makeReparatiesFactuur(Node node) {
		Observable<FactuurHeader> header = this.makeFactuurHeaderWithoutOmschrijving(
				((Element) node).getElementsByTagName("factuurHeader").item(0));
		Observable<Currency> currency = this.getNodeValue(node, "currency")
				.map(s -> Currency.getAvailableCurrencies().parallelStream()
						.filter(cur -> s.equals(cur.getCurrencyCode()))
						.findFirst().get());
		Observable<ItemList<ReparatiesInkoopOrder>> list = this.makeReparatiesList(
				((Element) node).getElementsByTagName("list").item(0));

		return Observable.zip(header, currency, list, ReparatiesFactuur::new);
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

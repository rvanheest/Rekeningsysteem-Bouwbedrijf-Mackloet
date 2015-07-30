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

import org.rekeningsysteem.data.aangenomen.AangenomenFactuur;
import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.data.util.loon.InstantLoon;
import org.rekeningsysteem.data.util.loon.ProductLoon;
import org.rekeningsysteem.io.FactuurLoader;
import org.rekeningsysteem.logging.ApplicationLogger;
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
 * This XML reader needs to be in here for backwards compatability.
 */
public class XmlReader2 implements FactuurLoader {

	private DocumentBuilder builder;

	public XmlReader2() {
		try {
			this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		catch (ParserConfigurationException e) {
			// Should not happen
			ApplicationLogger.getInstance().fatal("DocumentBuilder could not be made. " +
					"(should not happen)", e);
		}
	}

	public XmlReader2(DocumentBuilder builder) {
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
		String type = factuur.getAttributes().getNamedItem("type").getNodeName();
		if (type.equals("AangenomenFactuur")) {
			return this.makeAangenomenFactuur(((Element) factuur)
					.getElementsByTagName("rekening").item(0));
		}
		else if (type.equals("mutatiesfactuur")) {
			return this.makeMutatiesFactuur(((Element) factuur)
					.getElementsByTagName("rekening").item(0));
		}
		else if (type.equals("offerte")) {
			return this.makeOfferte(((Element) factuur)
					.getElementsByTagName("rekening").item(0));
		}
		else if (type.equals("ParticulierFactuur")) {
			return this.makeParticulierFactuur(((Element) factuur)
					.getElementsByTagName("rekening").item(0));
		}
		else if (type.equals("reparatiesfactuur")) {
			return this.makeReparatiesFactuur(((Element) factuur)
					.getElementsByTagName("rekening").item(0));
		}
		else {
			return Observable.error(new XMLParseException("Geen geschikte Node gevonden. "
					+ "Nodenaam = " + type + "."));
		}
	}

	private Observable<String> getNodeValue(Node node, String s) {
		return this.getNodeValue(((Element) node).getElementsByTagName(s));
	}

	private Observable<String> getNodeValue(NodeList list) {
		Node n = list.item(0);
		if (n == null) {
			return Observable.empty();
		}

		n = n.getChildNodes().item(0);

		if (n == null) {
			return Observable.empty();
		}
		return Observable.just(n.getNodeValue());
	}

	private Observable<Debiteur> makeDebiteur(Node node) {
		Observable<String> naam = this.getNodeValue(node, "naam");
		Observable<String> straat = this.getNodeValue(node, "straat");
		Observable<String> nummer = this.getNodeValue(node, "nummer");
		Observable<String> postcode = this.getNodeValue(node, "postcode");
		Observable<String> plaats = this.getNodeValue(node, "plaats");
		Observable<String> btwNummer = this.getNodeValue(node, "btwNummer");

		Observable<Boolean> empty = btwNummer.isEmpty();

		return empty.filter(b -> b)
				.flatMap(b -> b ? Observable.zip(naam, straat, nummer, postcode, plaats,
						Debiteur::new)
						: Observable.zip(naam, straat, nummer, postcode, plaats, btwNummer,
								Debiteur::new));
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
		Observable<String> offertenummer = this.getNodeValue(node, "offertenummer");

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

	private Observable<Func2<Double, Double, AangenomenListItem>> makeAangenomenListItem(Node node) {
		Observable<String> omschrijving = this.getNodeValue(node, "omschrijving");
		Observable<Geld> loon = this
				.makeGeld(((Element) node).getElementsByTagName("loon").item(0));
		Observable<Geld> materiaal = this.makeGeld(((Element) node).getElementsByTagName(
				"materiaal").item(0));

		return Observable.zip(omschrijving, loon, materiaal, (omschr, l, m) ->
				(btwL, btwM) -> new AangenomenListItem(omschr, l, btwL, m, btwM));
	}

	private Func2<Double, Double, Observable<ItemList<AangenomenListItem>>> makeAangenomenList(
			Node node) {
		NodeList list = ((Element) node).getElementsByTagName("list-item").item(0).getChildNodes();

		return (btwL, btwM) -> Observable.range(0, list.getLength())
				.map(list::item)
				.flatMap(this::makeAangenomenListItem)
				.map(f -> f.call(btwL, btwM))
				.collect(ItemList::new, Collection::add);
	}

	private Observable<AangenomenFactuur> makeAangenomenFactuur(Node node) {
		Observable<OmschrFactuurHeader> header = this.makeFactuurHeader(((Element) node)
				.getElementsByTagName("factuurHeader").item(0));
		Observable<Currency> currency = this.getNodeValue(node, "currency")
				.map(s -> Currency.getAvailableCurrencies().parallelStream()
						.filter(cur -> s.equals(cur.getSymbol()))
						.findFirst().get());
		Func2<Double, Double, Observable<ItemList<AangenomenListItem>>> listFunc =
				this.makeAangenomenList(((Element) node).getElementsByTagName("list").item(0));
		Observable<BtwPercentage> btw = this.makeBtwPercentage(((Element) node)
				.getElementsByTagName("btwPercentage").item(0));
		Observable<ItemList<AangenomenListItem>> list = btw.flatMap(b -> listFunc
				.call(b.loonPercentage, b.materiaalPercentage));

		return Observable.zip(header, currency, list, AangenomenFactuur::new);
	}

	private Observable<MutatiesBon> makeMutatiesBon(Node node) {
		Observable<String> omschrijving = this.getNodeValue(node, "omschrijving");
		Observable<String> bonnummer = this.getNodeValue(node, "bonnummer");
		Observable<Geld> prijs = this.makeGeld(((Element) node).getElementsByTagName("prijs")
				.item(0));

		return Observable.zip(omschrijving, bonnummer, prijs, MutatiesBon::new);
	}

	private Observable<ItemList<MutatiesBon>> makeMutatiesList(Node node) {
		NodeList list = ((Element) node).getElementsByTagName("list-item").item(0).getChildNodes();

		return Observable.range(0, list.getLength())
				.map(list::item)
				.flatMap(this::makeMutatiesBon)
				.collect(ItemList::new, Collection::add);
	}

	private Observable<MutatiesFactuur> makeMutatiesFactuur(Node node) {
		Observable<FactuurHeader> header = this.makeFactuurHeaderWithoutOmschrijving(
				((Element) node).getElementsByTagName("factuurHeader").item(0));
		Observable<Currency> currency = this.getNodeValue(node, "currency")
				.map(s -> Currency.getAvailableCurrencies().parallelStream()
						.filter(cur -> s.equals(cur.getSymbol()))
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
		Observable<String> artikelNummer = this.getNodeValue(node, "artikelnummer");
		Observable<String> omschrijving = this.getNodeValue(node, "omschrijving");
		Observable<Integer> prijsPer = this.getNodeValue(node, "prijsper").map(Integer::parseInt);
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
		NodeList list = node.getChildNodes();

		return btw -> Observable.range(0, list.getLength())
				.map(list::item)
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

		return Observable.zip(omschrijving, uren, uurloon, (omschr, u, ul) -> btw -> new ProductLoon(omschr, u, ul, btw));
	}

	private Observable<Func1<Double, InstantLoon>> makeInstantLoon(Node node) {
		Observable<String> omschrijving = this.getNodeValue(node, "omschrijving");
		Observable<Geld> loon = this.makeGeld(((Element) node)
				.getElementsByTagName("loon").item(0));

		return Observable.zip(omschrijving, loon, (omschr, l) -> btw -> new InstantLoon(omschr, l, btw));
	}

	private Func1<Double, Observable<ItemList<AbstractLoon>>> makeLoonList(Node node) {
		NodeList list = node.getChildNodes();

		return btw -> Observable.range(0, list.getLength())
				.map(list::item)
				.flatMap(item -> {
					switch (item.getNodeValue()) {
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
						.filter(cur -> s.equals(cur.getSymbol()))
						.findFirst().get());
		Func1<Double, Observable<ItemList<ParticulierArtikel>>> listFunc =
				this.makeParticulierList(((Element) node).getElementsByTagName("itemList").item(0));
		Func1<Double, Observable<ItemList<AbstractLoon>>> loonFunc =
				this.makeLoonList(((Element) node).getElementsByTagName("loonList").item(0));
		Observable<BtwPercentage> btw = this.makeBtwPercentage(((Element) node)
				.getElementsByTagName("btwPercentage").item(0));
		return btw.first().publish(percentage -> {
			Observable<ItemList<ParticulierArtikel>> arts = percentage.map(b -> b.materiaalPercentage).flatMap(b -> listFunc.call(b));
			Observable<ItemList<AbstractLoon>> loon = percentage.map(b -> b.loonPercentage).flatMap(b -> loonFunc.call(b));

			return Observable.zip(header, currency, arts, loon, ParticulierFactuur::new);
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
		NodeList list = ((Element) node).getElementsByTagName("list-item").item(0).getChildNodes();

		return Observable.range(0, list.getLength())
				.map(list::item)
				.flatMap(this::makeReparatiesBon)
				.collect(ItemList::new, Collection::add);
	}

	private Observable<ReparatiesFactuur> makeReparatiesFactuur(Node node) {
		Observable<FactuurHeader> header = this.makeFactuurHeaderWithoutOmschrijving(
				((Element) node).getElementsByTagName("factuurHeader").item(0));
		Observable<Currency> currency = this.getNodeValue(node, "currency")
				.map(s -> Currency.getAvailableCurrencies().parallelStream()
						.filter(cur -> s.equals(cur.getSymbol()))
						.findFirst().get());
		Observable<ItemList<ReparatiesBon>> list = this.makeReparatiesList(
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

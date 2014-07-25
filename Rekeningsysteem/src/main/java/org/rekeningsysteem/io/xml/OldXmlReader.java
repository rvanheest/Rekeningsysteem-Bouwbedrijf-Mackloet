package org.rekeningsysteem.io.xml;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.management.modelmbean.XMLParseException;
import javax.xml.parsers.DocumentBuilder;

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
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.data.util.loon.ProductLoon;
import org.rekeningsysteem.exception.GeldParseException;
import org.rekeningsysteem.io.FactuurLoader;
import org.rekeningsysteem.utils.Try;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.inject.Inject;

public class OldXmlReader implements FactuurLoader {

	private String valuta;
	private DocumentBuilder builder;

	@Inject
	public OldXmlReader(DocumentBuilder builder) {
		this.builder = builder;
	}

	@Override
	public Try<? extends AbstractRekening> load(File file) {
		try {
			return this.loadRekening(file);
		}
		catch (SAXException | IOException exception) {
			return Try.failure(exception);
		}
	}

	private Try<? extends AbstractRekening> loadRekening(File file)
			throws SAXException, IOException {
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
			return Try.failure(new XMLParseException("Geen geschikte Node gevonden. "
					+ "Nodenaam = " + soort + "."));
		}
	}

	private String getNodeValue(NodeList list) {
		Node n = list.item(0);
		if (n == null) {
			return null;
		}

		n = n.getChildNodes().item(0);

		if (n == null) {
			return null;
		}
		return n.getNodeValue();
	}

	private Try<Geld> makeGeld(String s) {
		String[] ss = s.split(" ");

		try {
			switch (ss.length) {
				case 1:
					return Try.of(new Geld(ss[0]));
				case 2:
					if (this.valuta == null || this.valuta.equals(ss[0])) {
						this.valuta = ss[0];
						return Try.of(new Geld(ss[1]));
					}
					else {
						// corrupte file: verschillende valuta's
						return Try.failure(new IllegalArgumentException("This file is corrupt. "
								+ "It has multiple currencies."));
					}

				default:
					throw new GeldParseException("Het bedrag " + s + " kan niet worden gelezen.");
			}
		}
		catch (GeldParseException e) {
			return Try.failure(e);
		}
	}

	private LocalDate makeDatum(Node node) {
		String dag = this.getNodeValue(((Element) node).getElementsByTagName("dag"));
		String maand = this.getNodeValue(((Element) node).getElementsByTagName("maand"));
		String jaar = this.getNodeValue(((Element) node).getElementsByTagName("jaar"));

		return LocalDate.of(Integer.parseInt(jaar), Integer.parseInt(maand), Integer.parseInt(dag));
	}

	private Debiteur makeDebiteur(Node node) {
		String naam = this.getNodeValue(((Element) node).getElementsByTagName("naam"));
		String straat = this.getNodeValue(((Element) node).getElementsByTagName("straat"));
		String nummer = this.getNodeValue(((Element) node).getElementsByTagName("nummer"));
		String postcode = this.getNodeValue(((Element) node).getElementsByTagName("postcode"));
		String plaats = this.getNodeValue(((Element) node).getElementsByTagName("plaats"));
		Optional<String> btwnr = Optional.ofNullable(this.getNodeValue(((Element) node)
				.getElementsByTagName("btwnr")));

		return new Debiteur(naam, straat, nummer, postcode, plaats, btwnr);
	}

	private OmschrFactuurHeader makeFactuurHeader(Node node) {
		Debiteur debiteur = this.makeDebiteur(((Element) node).getElementsByTagName("debiteur")
				.item(0));
		LocalDate datum = this.makeDatum(((Element) node).getElementsByTagName("datum").item(0));
		String factuurnummer = this.getNodeValue(((Element) node)
				.getElementsByTagName("factuurnummer"));
		String omschrijving = this.getNodeValue(((Element) node)
				.getElementsByTagName("omschrijving"));

		return new OmschrFactuurHeader(debiteur, datum, factuurnummer, omschrijving);
	}

	private FactuurHeader makeFactuurHeaderWithoutOmschrijving(Node node) {
		Debiteur debiteur = this.makeDebiteur(((Element) node).getElementsByTagName("debiteur")
				.item(0));
		LocalDate datum = this.makeDatum(((Element) node).getElementsByTagName("datum").item(0));
		String factuurnummer = this.getNodeValue(((Element) node)
				.getElementsByTagName("factuurnummer"));

		return new FactuurHeader(debiteur, datum, factuurnummer);
	}

	private FactuurHeader makeOfferteFactuurHeader(Node node) {
		Debiteur debiteur = this.makeDebiteur(((Element) node).getElementsByTagName("debiteur")
				.item(0));
		LocalDate datum = this.makeDatum(((Element) node).getElementsByTagName("datum").item(0));
		String factuurnummer = this.getNodeValue(((Element) node)
				.getElementsByTagName("offertenummer"));

		return new FactuurHeader(debiteur, datum, factuurnummer);
	}

	private Try<AnderArtikel> makeAnderArtikel(Node node) {
		Node temp = ((Element) node).getElementsByTagName("artikel").item(0);
		String omschrijving = this.getNodeValue(((Element) temp)
				.getElementsByTagName("omschrijving"));
		Try<Geld> prijs = this.makeGeld(this.getNodeValue(((Element) node)
				.getElementsByTagName("prijs")));

		return prijs.map(p -> new AnderArtikel(omschrijving, p));
	}

	private Try<EsselinkArtikel> makeArtikelEsselink(Node node) {
		String artikelNummer = this.getNodeValue(((Element) node)
				.getElementsByTagName("artikelnummer"));
		String omschrijving = this.getNodeValue(((Element) node)
				.getElementsByTagName("omschrijving"));
		int prijsPer = Integer.parseInt(this.getNodeValue(((Element) node)
				.getElementsByTagName("prijsper")));
		String eenheid = this.getNodeValue(((Element) node).getElementsByTagName("eenheid"));
		Try<Geld> verkoopPrijs = this.makeGeld(this.getNodeValue(((Element) node)
				.getElementsByTagName("verkoopprijs")));

		return verkoopPrijs.map(p -> new EsselinkArtikel(artikelNummer, omschrijving, prijsPer,
				eenheid, p));
	}

	private Try<GebruiktEsselinkArtikel> makeGebruiktArtikelEsselink(Node node) {
		Try<EsselinkArtikel> artikel = this.makeArtikelEsselink(((Element) node)
				.getElementsByTagName("artikel").item(0));
		double aantal = Double.parseDouble(this.getNodeValue(((Element) node)
				.getElementsByTagName("aantal")));

		return artikel.map(a -> new GebruiktEsselinkArtikel(a, aantal));
	}

	private Try<ProductLoon> makeLoon(Node node) {
		Try<Geld> uurloon = this.makeGeld(this.getNodeValue(((Element) node)
				.getElementsByTagName("uurloon")));
		double uren = Double.parseDouble(this.getNodeValue(((Element) node)
				.getElementsByTagName("uren")));

		return uurloon.map(u -> new ProductLoon("Uurloon à " + u.formattedString(), uren, u));
	}

	private BtwPercentage makeEnkelBtw(Node node) {
		double btw = Double.parseDouble(this.getNodeValue(((Element) node)
				.getElementsByTagName("btw")));

		return new BtwPercentage(btw, btw);
	}

	private BtwPercentage makeDubbelBtw(Node node) {
		double btwArt = Double.parseDouble(this.getNodeValue(((Element) node)
				.getElementsByTagName("btwpercentageart")));
		double btwLoon = Double.parseDouble(this.getNodeValue(((Element) node)
				.getElementsByTagName("btwpercentageloon")));

		return new BtwPercentage(btwLoon, btwArt);
	}

	public Try<ParticulierFactuur> makeParticulierFactuur1(Node node) {
		Node al = ((Element) node).getElementsByTagName("artikellijst").item(0);
		NodeList gal = ((Element) al).getElementsByTagName("gebruiktartikellijst").item(0)
				.getChildNodes();

		Try<ItemList<ParticulierArtikel>> itemList;
		try {
			itemList = Try.of(IntStream
					.range(0, gal.getLength())
					.boxed()
					.map(i -> gal.item(i))
					.<Try<? extends ParticulierArtikel>> map(item -> {
						if ("gebruiktartikelander".equals(item.getNodeName())) {
							return this.makeAnderArtikel(item);
						}
						else if ("gebruiktartikelesselink".equals(item.getNodeName())) {
							return this.makeGebruiktArtikelEsselink(item);
						}
						return Try.failure(new IllegalArgumentException(
								"Unknown artikel type found."));
						})
					.map(Try::get)
					.collect(Collectors.toCollection(ItemList::new)));
		}
		catch (Throwable e) {
			itemList = Try.failure(e);
		}

		Try<ItemList<AbstractLoon>> loonList = this
				.makeLoon(((Element) node).getElementsByTagName("loon").item(0))
				.map(Arrays::asList)
				.map(ItemList<AbstractLoon>::new);

		return itemList.flatMap(il -> loonList.map(ll -> new ParticulierFactuur(this
				.makeFactuurHeader(node), this.valuta, il,
				ll, this.makeEnkelBtw(node))));
	}

	private Try<ParticulierFactuur> makeParticulierFactuur2(Node node) {
		Node al = ((Element) node).getElementsByTagName("artikellijst").item(0);
		NodeList gal = ((Element) al).getElementsByTagName("gebruiktartikellijst").item(0)
				.getChildNodes();
		Try<ItemList<ParticulierArtikel>> itemList;
		try {
			itemList = Try.of(IntStream
					.range(0, gal.getLength())
					.boxed()
					.map(i -> gal.item(i))
					.<Try<? extends ParticulierArtikel>> map(
							item -> {
								if ("gebruiktartikelander".equals(item.getNodeName())) {
									return this.makeAnderArtikel(item);
								}
								else if ("gebruiktartikelesselink".equals(item.getNodeName())) {
									return this.makeGebruiktArtikelEsselink(item);
								}
								return Try.failure(new IllegalArgumentException(
										"Unknown artikel type found."));
							})
					.map(Try::get)
					.collect(Collectors.toCollection(ItemList::new)));
		}
		catch (Throwable e) {
			itemList = Try.failure(e);
		}

		Try<ItemList<AbstractLoon>> loonList = this
				.makeLoon(((Element) node).getElementsByTagName("loon").item(0))
				.map(Arrays::asList)
				.map(ItemList<AbstractLoon>::new);

		return itemList.flatMap(il -> loonList.map(ll -> new ParticulierFactuur(this
				.makeFactuurHeader(node), this.valuta, il,
				ll, this.makeDubbelBtw(node))));
	}

	private Try<MutatiesBon> makeMutatiesBon(Node node) {
		String omschrijving = this.getNodeValue(((Element) node)
				.getElementsByTagName("omschrijving"));
		String bonnummer = this.getNodeValue(((Element) node).getElementsByTagName("bonnummer"));
		Try<Geld> prijs = this.makeGeld(this.getNodeValue(((Element) node)
				.getElementsByTagName("prijs")));

		return prijs.map(p -> new MutatiesBon(omschrijving, bonnummer, p));
	}

	private Try<MutatiesFactuur> makeMutatiesFactuur(Node node) {
		FactuurHeader header = this.makeFactuurHeaderWithoutOmschrijving(node);
		BtwPercentage btw = new BtwPercentage(0.0, 0.0);

		Node mbl = ((Element) node).getElementsByTagName("mutatiesbonlijst").item(0);
		NodeList bonnen = ((Element) mbl).getElementsByTagName("mutatiesbon");
		Try<ItemList<MutatiesBon>> itemList;
		try {
			itemList = Try.of(IntStream.range(0, bonnen.getLength())
					.boxed()
					.map(i -> bonnen.item(i))
					.map(this::makeMutatiesBon)
					.map(Try::get)
					.collect(Collectors.toCollection(ItemList::new)));
		}
		catch (Throwable e) {
			itemList = Try.failure(e);
		}

		return itemList.map(il -> new MutatiesFactuur(header, this.valuta, il, btw));
	}

	private Try<ReparatiesBon> makeReparatiesBon(Node node) {
		String omschrijving = this.getNodeValue(((Element) node)
				.getElementsByTagName("omschrijving"));
		String bonnummer = this.getNodeValue(((Element) node).getElementsByTagName("bonnummer"));
		Try<Geld> uurloon = this.makeGeld(this.getNodeValue(((Element) node)
				.getElementsByTagName("uurloon")));
		Try<Geld> materiaal = this.makeGeld(this.getNodeValue(((Element) node)
				.getElementsByTagName("materiaal")));

		return uurloon.flatMap(uu -> materiaal.map(m -> new ReparatiesBon(omschrijving, bonnummer,
				uu, m)));
	}

	private Try<ReparatiesFactuur> makeReparatiesFactuur(Node node) {
		FactuurHeader header = this.makeFactuurHeaderWithoutOmschrijving(node);
		BtwPercentage btw = new BtwPercentage(0.0, 0.0);

		Node rbl = ((Element) node).getElementsByTagName("reparatiesbonlijst").item(0);
		NodeList bonnen = ((Element) rbl).getElementsByTagName("reparatiesbon");
		Try<ItemList<ReparatiesBon>> itemList;
		try {
			itemList = Try.of(IntStream.range(0, bonnen.getLength())
					.boxed()
					.map(i -> bonnen.item(i))
					.map(this::makeReparatiesBon)
					.map(Try::get)
					.collect(Collectors.toCollection(ItemList::new)));
		}
		catch (Throwable e) {
			itemList = Try.failure(e);
		}

		return itemList.map(il -> new ReparatiesFactuur(header, this.valuta, il, btw));
	}

	private Try<Offerte> makeOfferte(Node node) {
		FactuurHeader header = this.makeOfferteFactuurHeader(node);
		String tekst = this.getNodeValue(((Element) node).getElementsByTagName("tekst"));
		boolean ondertekenen = Boolean.parseBoolean(this.getNodeValue(((Element) node)
				.getElementsByTagName("ondertekenen")));

		return Try.of(new Offerte(header, tekst, ondertekenen));
	}
}

package org.rekeningsysteem.io.xml;

import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.materiaal.AnderArtikel;
import org.rekeningsysteem.data.particulier.materiaal.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.materiaal.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.time.LocalDate;
import java.util.Currency;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.rekeningsysteem.io.xml.XmlWriterUtils.appendNode;
import static org.rekeningsysteem.io.xml.XmlWriterUtils.createElement;
import static org.rekeningsysteem.io.xml.XmlWriterUtils.optionalStringNode;
import static org.rekeningsysteem.io.xml.XmlWriterUtils.stringNode;
import static org.rekeningsysteem.io.xml.XmlWriterUtils.stringNodeWithAttributes;

class XmlWriterDocumentVisitor {

	private Function<Document, Node> visit(Debiteur debiteur) {
		return createElement("debiteur",
			xml -> stringNode(xml, "naam", debiteur.naam())
				.andThen(stringNode(xml, "straat", debiteur.straat()))
				.andThen(stringNode(xml, "nummer", debiteur.nummer()))
				.andThen(stringNode(xml, "postcode", debiteur.postcode()))
				.andThen(stringNode(xml, "plaats", debiteur.plaats()))
				.andThen(optionalStringNode(xml, "btwNummer", debiteur.btwNummer())));
	}

	private Function<Document, Node> visit(LocalDate date) {
		return createElement("datum",
			xml -> stringNode(xml, "dag", String.valueOf(date.getDayOfMonth()))
				.andThen(stringNode(xml, "maand", String.valueOf(date.getMonthValue())))
				.andThen(stringNode(xml, "jaar", String.valueOf(date.getYear()))));
	}

	private Function<Document, Node> visit(FactuurHeader header) {
		return createElement("factuurHeader",
			xml -> appendNode(xml, visit(header.debiteur()))
				.andThen(appendNode(xml, visit(header.datum())))
				.andThen(optionalStringNode(xml, "factuurnummer", header.factuurnummer())));
	}

	private Function<Document, Node> visit(Currency currency) {
		return createElement("currency", xml -> doc -> xml.appendChild(doc.createTextNode(currency.getCurrencyCode())));
	}

	private Consumer<Document> visit(Node root, String key, Geld value) {
		return stringNode(root, key, value.formattedString());
	}

	private Consumer<Document> visit(Node root, String key, BtwPercentage btw) {
		SortedMap<String, String> attributes = new TreeMap<>();
		attributes.put("verlegd", String.valueOf(btw.verlegd()));
		return stringNodeWithAttributes(root, key, String.valueOf(btw.percentage()), attributes);
	}

	private Function<Document, Node> visit(EsselinkArtikel item) {
		return createElement("artikel",
				xml -> stringNode(xml, "artikelNummer", item.artikelNummer())
						.andThen(stringNode(xml, "omschrijving", item.omschrijving()))
						.andThen(stringNode(xml, "prijsPer", String.valueOf(item.prijsPer())))
						.andThen(stringNode(xml, "eenheid", item.eenheid()))
						.andThen(visit(xml, "verkoopPrijs", item.verkoopPrijs()))
		);
	}

	public Function<Document, Node> visit(MutatiesInkoopOrder item) {
		return createElement("mutaties-bon",
				xml -> stringNode(xml, "omschrijving", item.omschrijving())
						.andThen(stringNode(xml, "bonnummer", item.inkoopOrderNummer()))
						.andThen(visit(xml, "prijs", item.materiaal()))
		);
	}

	public Function<Document, Node> visit(ReparatiesInkoopOrder item) {
		return createElement("reparaties-bon",
				xml -> stringNode(xml, "omschrijving", item.omschrijving())
						.andThen(stringNode(xml, "bonnummer", item.inkoopOrderNummer()))
						.andThen(visit(xml, "loon", item.loon()))
						.andThen(visit(xml, "materiaal", item.materiaal()))
		);
	}

	public Function<Document, Node> visit(AnderArtikel item) {
		return createElement("ander-artikel",
				xml -> stringNode(xml, "omschrijving", item.omschrijving())
						.andThen(visit(xml, "prijs", item.materiaal()))
						.andThen(visit(xml, "materiaalBtwPercentage", item.materiaalBtwPercentage()))
		);
	}

	public Function<Document, Node> visit(GebruiktEsselinkArtikel item) {
		return createElement("gebruikt-esselink-artikel",
				xml -> stringNode(xml, "omschrijving", item.omschrijving())
						.andThen(appendNode(xml, visit(item.artikel())))
						.andThen(stringNode(xml, "aantal", String.valueOf(item.aantal())))
						.andThen(visit(xml, "materiaalBtwPercentage", item.materiaalBtwPercentage()))
		);
	}

	public Function<Document, Node> visit(InstantLoon item) {
		return createElement("instant-loon",
				xml -> stringNode(xml, "omschrijving", item.omschrijving())
						.andThen(visit(xml, "loon", item.loon()))
						.andThen(visit(xml, "loonBtwPercentage", item.loonBtwPercentage()))
		);
	}

	public Function<Document, Node> visit(ProductLoon item) {
		return createElement("product-loon",
				xml -> stringNode(xml, "omschrijving", item.omschrijving())
						.andThen(stringNode(xml, "uren", String.valueOf(item.uren())))
						.andThen(visit(xml, "uurloon", item.uurloon()))
						.andThen(visit(xml, "loonBtwPercentage", item.loonBtwPercentage()))
		);
	}

	private <T extends ListItem> Function<Document, Node> visit(Stream<T> stream) {
		return createElement(
				"list",
				xml -> stream
					.map(t -> appendNode(xml, switch (t) {
						case MutatiesInkoopOrder item -> visit(item);
						case ReparatiesInkoopOrder item -> visit(item);
						case AnderArtikel item -> visit(item);
						case GebruiktEsselinkArtikel item -> visit(item);
						case InstantLoon item -> visit(item);
						case ProductLoon item -> visit(item);
						case default -> throw new RuntimeException();
					}))
					.reduce(d -> {}, Consumer::andThen)
		);
	}

	public Function<Document, Node> visit(MutatiesFactuur mutatiesFactuur) {
		return createElement("mutaties-factuur",
			xml -> appendNode(xml, visit(mutatiesFactuur.header()))
				.andThen(appendNode(xml, visit(mutatiesFactuur.itemList().getCurrency())))
				.andThen(appendNode(xml, visit(mutatiesFactuur.itemList().stream()))));
	}

	public Function<Document, Node> visit(Offerte offerte) {
		return createElement("offerte",
			xml -> appendNode(xml, visit(offerte.header()))
				.andThen(stringNode(xml, "tekst", offerte.tekst()))
				.andThen(stringNode(xml, "ondertekenen", String.valueOf(offerte.ondertekenen()))));
	}

	public Function<Document, Node> visit(ParticulierFactuur factuur) {
		return createElement("particulier-factuur",
			xml -> doc -> {
				Node header = visit(factuur.header()).apply(doc);

				stringNode(header, "omschrijving", factuur.omschrijving())
					.andThen(appendNode(xml, header))
					.andThen(appendNode(xml, visit(factuur.itemList().getCurrency())))
					.andThen(appendNode(xml, visit(factuur.itemList().stream())))
					.accept(doc);
			});
	}

	public Function<Document, Node> visit(ReparatiesFactuur factuur) {
		return createElement("reparaties-factuur",
			xml -> appendNode(xml, visit(factuur.header()))
				.andThen(appendNode(xml, visit(factuur.itemList().getCurrency())))
				.andThen(appendNode(xml, visit(factuur.itemList().stream()))));
	}
}

package org.rekeningsysteem.io.xml;

import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.rekeningsysteem.io.xml.XmlWriterUtils.appendNode;
import static org.rekeningsysteem.io.xml.XmlWriterUtils.createElement;
import static org.rekeningsysteem.io.xml.XmlWriterUtils.stringNode;
import static org.rekeningsysteem.io.xml.XmlWriterUtils.stringNodeWithAttributes;

class XmlWriterItemVisitor implements ListItemVisitor<Function<Document, Node>> {

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

	@Override
	public Function<Document, Node> visit(MutatiesInkoopOrder item) {
		return createElement("mutaties-bon",
			xml -> stringNode(xml, "omschrijving", item.omschrijving())
				.andThen(stringNode(xml, "bonnummer", item.inkoopOrderNummer()))
				.andThen(visit(xml, "prijs", item.materiaal()))
		);
	}

	@Override
	public Function<Document, Node> visit(ReparatiesInkoopOrder item) {
		return createElement("reparaties-bon",
			xml -> stringNode(xml, "omschrijving", item.omschrijving())
				.andThen(stringNode(xml, "bonnummer", item.inkoopOrderNummer()))
				.andThen(visit(xml, "loon", item.loon()))
				.andThen(visit(xml, "materiaal", item.materiaal()))
		);
	}

	@Override
	public Function<Document, Node> visit(AnderArtikel item) {
		return createElement("ander-artikel",
			xml -> stringNode(xml, "omschrijving", item.getOmschrijving())
				.andThen(visit(xml, "prijs", item.materiaal()))
				.andThen(visit(xml, "materiaalBtwPercentage", item.getMateriaalBtwPercentage()))
		);
	}

	@Override
	public Function<Document, Node> visit(GebruiktEsselinkArtikel item) {
		return createElement("gebruikt-esselink-artikel",
			xml -> stringNode(xml, "omschrijving", item.getOmschrijving())
				.andThen(appendNode(xml, visit(item.getArtikel())))
				.andThen(stringNode(xml, "aantal", String.valueOf(item.getAantal())))
				.andThen(visit(xml, "materiaalBtwPercentage", item.getMateriaalBtwPercentage()))
		);
	}

	@Override
	public Function<Document, Node> visit(InstantLoon item) {
		return createElement("instant-loon",
			xml -> stringNode(xml, "omschrijving", item.getOmschrijving())
				.andThen(visit(xml, "loon", item.loon()))
				.andThen(visit(xml, "loonBtwPercentage", item.getLoonBtwPercentage()))
		);
	}

	@Override
	public Function<Document, Node> visit(ProductLoon item) {
		return createElement("product-loon",
			xml -> stringNode(xml, "omschrijving", item.getOmschrijving())
				.andThen(stringNode(xml, "uren", String.valueOf(item.getUren())))
				.andThen(visit(xml, "uurloon", item.getUurloon()))
				.andThen(visit(xml, "loonBtwPercentage", item.getLoonBtwPercentage()))
		);
	}
}

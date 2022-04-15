package org.rekeningsysteem.io.xml;

import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.rekeningsysteem.io.xml.XmlWriterUtils.appendNode;
import static org.rekeningsysteem.io.xml.XmlWriterUtils.createElement;
import static org.rekeningsysteem.io.xml.XmlWriterUtils.optionalStringNode;
import static org.rekeningsysteem.io.xml.XmlWriterUtils.stringNode;

class XmlWriterDocumentVisitor implements RekeningVisitor<Function<Document, Node>> {

	private final ListItemVisitor<Function<Document, Node>> itemVisitor;

	XmlWriterDocumentVisitor(ListItemVisitor<Function<Document, Node>> itemVisitor) {
		this.itemVisitor = itemVisitor;
	}

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
		return createElement("currency",
			xml -> doc -> xml.appendChild(doc.createTextNode(currency.getCurrencyCode())));
	}

	private <T extends ListItem> Function<Document, Node> visit(List<T> list) {
		return createElement("list",
			xml -> list.stream().map(t -> appendNode(xml, t.accept(this.itemVisitor))).reduce(d -> {}, Consumer::andThen));
	}

	@Override
	public Function<Document, Node> visit(MutatiesFactuur mutatiesFactuur) {
		return createElement("mutaties-factuur",
			xml -> appendNode(xml, visit(mutatiesFactuur.getFactuurHeader()))
				.andThen(appendNode(xml, visit(mutatiesFactuur.getCurrency())))
				.andThen(appendNode(xml, visit(mutatiesFactuur.getItemList()))));
	}

	@Override
	public Function<Document, Node> visit(Offerte offerte) {
		return createElement("offerte",
			xml -> appendNode(xml, visit(offerte.getFactuurHeader()))
				.andThen(stringNode(xml, "tekst", offerte.getTekst()))
				.andThen(stringNode(xml, "ondertekenen", String.valueOf(offerte.isOndertekenen()))));
	}

	@Override
	public Function<Document, Node> visit(ParticulierFactuur factuur) {
		return createElement("particulier-factuur",
			xml -> doc -> {
				Node header = visit(factuur.getFactuurHeader()).apply(doc);

				stringNode(header, "omschrijving", factuur.getFactuurHeader().getOmschrijving())
					.andThen(appendNode(xml, header))
					.andThen(appendNode(xml, visit(factuur.getCurrency())))
					.andThen(appendNode(xml, visit(factuur.getItemList())))
					.accept(doc);
			});
	}

	@Override
	public Function<Document, Node> visit(ReparatiesFactuur factuur) {
		return createElement("reparaties-factuur",
			xml -> appendNode(xml, visit(factuur.getFactuurHeader()))
				.andThen(appendNode(xml, visit(factuur.getCurrency())))
				.andThen(appendNode(xml, visit(factuur.getItemList()))));
	}
}

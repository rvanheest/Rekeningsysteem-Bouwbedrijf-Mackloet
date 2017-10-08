package com.github.rvanheest.rekeningsysteem.xml;

import com.github.rvanheest.rekeningsysteem.function.Consumer;
import com.github.rvanheest.rekeningsysteem.model.document.ListItem;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.mutation.MutationInvoice;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalInvoice;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairInvoice;
import com.github.rvanheest.rekeningsysteem.model.visitor.DocumentVisitor;
import com.github.rvanheest.rekeningsysteem.model.visitor.ListItemVisitor;
import io.reactivex.functions.Function;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.money.CurrencyUnit;
import java.time.LocalDate;
import java.util.List;

import static com.github.rvanheest.rekeningsysteem.xml.XmlWriterUtils.appendNode;
import static com.github.rvanheest.rekeningsysteem.xml.XmlWriterUtils.createElement;
import static com.github.rvanheest.rekeningsysteem.xml.XmlWriterUtils.optionalStringNode;
import static com.github.rvanheest.rekeningsysteem.xml.XmlWriterUtils.stringNode;

class XmlWriterDocumentVisitor implements DocumentVisitor<Function<Document, Node>> {

  private final ListItemVisitor<Function<Document, Node>> itemVisitor;

  XmlWriterDocumentVisitor(ListItemVisitor<Function<Document, Node>> itemVisitor) {
    this.itemVisitor = itemVisitor;
  }

  private Function<Document, Node> visit(Debtor debtor) {
    return createElement("debiteur",
        xml -> stringNode(xml, "naam", debtor.getName())
            .andThen(stringNode(xml, "straat", debtor.getStreet()))
            .andThen(stringNode(xml, "nummer", debtor.getNumber()))
            .andThen(stringNode(xml, "postcode", debtor.getZipcode()))
            .andThen(stringNode(xml, "plaats", debtor.getCity()))
            .andThen(optionalStringNode(xml, "btwNummer", debtor.getVatNumber())));
  }

  private Function<Document, Node> visit(LocalDate date) {
    return createElement("datum",
        xml -> stringNode(xml, "dag", String.valueOf(date.getDayOfMonth()))
            .andThen(stringNode(xml, "maand", String.valueOf(date.getMonthValue())))
            .andThen(stringNode(xml, "jaar", String.valueOf(date.getYear()))));
  }

  private Function<Document, Node> visit(Header header) {
    return createElement("factuurHeader",
        xml -> appendNode(xml, visit(header.getDebtor()))
            .andThen(appendNode(xml, visit(header.getDate())))
            .andThen(optionalStringNode(xml, "factuurnummer", header.getInvoiceNumber())));
  }

  private Function<Document, Node> visit(CurrencyUnit currencyUnit) {
    return createElement("currency",
        xml -> doc -> xml.appendChild(doc.createTextNode(currencyUnit.getCurrencyCode())));
  }

  private <T extends ListItem> Function<Document, Node> visit(List<T> list) {
    return createElement("list",
        xml -> list.stream().map(t -> appendNode(xml, t.accept(this.itemVisitor))).reduce(d -> {}, Consumer::andThen));
  }

  @Override
  public Function<Document, Node> visit(MutationInvoice invoice) {
    return createElement("mutaties-factuur",
        xml -> appendNode(xml, visit(invoice.getHeader()))
            .andThen(appendNode(xml, visit(invoice.getItemList().getCurrency())))
            .andThen(appendNode(xml, visit(invoice.getItemList().getList()))));
  }

  @Override
  public Function<Document, Node> visit(Offer offer) {
    return createElement("offerte",
        xml -> appendNode(xml, visit(offer.getHeader()))
            .andThen(stringNode(xml, "tekst", offer.getText()))
            .andThen(stringNode(xml, "ondertekenen", String.valueOf(offer.isSign()))));
  }

  @Override
  public Function<Document, Node> visit(NormalInvoice invoice) {
    return createElement("particulier-factuur",
        xml -> doc -> {
          Node header = visit(invoice.getHeader()).apply(doc);

          stringNode(header, "omschrijving", invoice.getDescription())
              .andThen(appendNode(xml, header))
              .andThen(appendNode(xml, visit(invoice.getItemList().getCurrency())))
              .andThen(appendNode(xml, visit(invoice.getItemList().getList())))
              .accept(doc);
        });
  }

  @Override
  public Function<Document, Node> visit(RepairInvoice invoice) {
    return createElement("reparaties-factuur",
        xml -> appendNode(xml, visit(invoice.getHeader()))
            .andThen(appendNode(xml, visit(invoice.getItemList().getCurrency())))
            .andThen(appendNode(xml, visit(invoice.getItemList().getList()))));
  }
}

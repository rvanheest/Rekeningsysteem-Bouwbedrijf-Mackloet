package com.github.rvanheest.rekeningsysteem.xml;

import com.github.rvanheest.rekeningsysteem.model.mutation.MutationListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkItem;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.SimpleListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.DefaultWage;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.HourlyWage;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairListItem;
import com.github.rvanheest.rekeningsysteem.model.visitor.ListItemVisitor;
import io.reactivex.functions.Function;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.money.MonetaryAmount;

import static com.github.rvanheest.rekeningsysteem.xml.XmlWriterUtils.appendNode;
import static com.github.rvanheest.rekeningsysteem.xml.XmlWriterUtils.createElement;
import static com.github.rvanheest.rekeningsysteem.xml.XmlWriterUtils.stringNode;

public class XmlWriterItemVisitor implements ListItemVisitor<Function<Document, Node>> {

  private Function<String, Function<Document, Node>> visit(MonetaryAmount amount) {
    return elementName -> createElement(elementName,
        xml -> stringNode(xml, "bedrag", String.valueOf(amount.getNumber().doubleValue())));
  }

  private Function<Document, Node> visit(EsselinkItem item) {
    return createElement("artikel",
        xml -> stringNode(xml, "artikelNummer", item.getItemId())
            .andThen(stringNode(xml, "omschrijving", item.getDescription()))
            .andThen(stringNode(xml, "prijsPer", String.valueOf(item.getAmountPer())))
            .andThen(stringNode(xml, "eenheid", item.getUnit()))
            .andThen(appendNode(xml, visit(item.getPricePerUnit()).apply("verkoopPrijs"))));
  }

  @Override
  public Function<Document, Node> visit(MutationListItem item) {
    return createElement("mutaties-bon",
        xml -> stringNode(xml, "omschrijving", item.getDescription())
            .andThen(stringNode(xml, "bonnummer", item.getItemId()))
            .andThen(appendNode(xml, visit(item.getMaterialCosts()).apply("prijs"))));
  }

  @Override
  public Function<Document, Node> visit(RepairListItem item) {
    return createElement("reparaties-bon",
        xml -> stringNode(xml, "omschrijving", item.getDescription())
            .andThen(stringNode(xml, "bonnummer", item.getItemId()))
            .andThen(appendNode(xml, visit(item.getWage()).apply("loon")))
            .andThen(appendNode(xml, visit(item.getMaterialCosts()).apply("materiaal"))));
  }

  @Override
  public Function<Document, Node> visit(SimpleListItem item) {
    return createElement("ander-artikel",
        xml -> stringNode(xml, "omschrijving", item.getDescription())
            .andThen(appendNode(xml, visit(item.getMaterialCosts()).apply("prijs")))
            .andThen(stringNode(xml, "materiaalBtwPercentage", String.valueOf(item.getMaterialCostsTaxPercentage()))));
  }

  @Override
  public Function<Document, Node> visit(EsselinkListItem item) {
    return createElement("gebruikt-esselink-artikel",
        xml -> stringNode(xml, "omschrijving", item.getDescription())
            .andThen(appendNode(xml, visit(item.getItem())))
            .andThen(stringNode(xml, "aantal", String.valueOf(item.getAmount())))
            .andThen(stringNode(xml, "materiaalBtwPercentage", String.valueOf(item.getMaterialCostsTaxPercentage()))));
  }

  @Override
  public Function<Document, Node> visit(DefaultWage item) {
    return createElement("instant-loon",
        xml -> stringNode(xml, "omschrijving", item.getDescription())
            .andThen(appendNode(xml, visit(item.getWage()).apply("loon")))
            .andThen(stringNode(xml, "loonBtwPercentage", String.valueOf(item.getWageTaxPercentage()))));
  }

  @Override
  public Function<Document, Node> visit(HourlyWage item) {
    return createElement("product-loon",
        xml -> stringNode(xml, "omschrijving", item.getDescription())
            .andThen(stringNode(xml, "uren", String.valueOf(item.getHours())))
            .andThen(appendNode(xml, visit(item.getWagePerHour()).apply("uurloon")))
            .andThen(stringNode(xml, "loonBtwPercentage", String.valueOf(item.getWageTaxPercentage()))));
  }
}

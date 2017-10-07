package com.github.rvanheest.rekeningsysteem.xml;

import com.github.rvanheest.rekeningsysteem.exception.XmlParseException;
import com.github.rvanheest.rekeningsysteem.model.document.AbstractDocument;
import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.document.ListItem;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.mutation.MutationInvoice;
import com.github.rvanheest.rekeningsysteem.model.mutation.MutationListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkItem;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalInvoice;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.SimpleListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.DefaultWage;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.HourlyWage;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairInvoice;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairListItem;
import io.reactivex.Maybe;
import io.reactivex.functions.Function;
import org.javamoney.moneta.Money;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.xml.parsers.DocumentBuilder;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Optional;

import static com.github.rvanheest.rekeningsysteem.xml.XmlReaderUtils.getAll;
import static com.github.rvanheest.rekeningsysteem.xml.XmlReaderUtils.getFirst;
import static com.github.rvanheest.rekeningsysteem.xml.XmlReaderUtils.getNodeValue;
import static com.github.rvanheest.rekeningsysteem.xml.XmlReaderUtils.iterate;

public class XmlReader4 implements XmlLoader {

  private DocumentBuilder builder;

  public XmlReader4(DocumentBuilder builder) {
    this.builder = builder;
  }

  @Override
  public Maybe<AbstractDocument> load(Path path) {
    return this.load(this.builder, path);
  }

  @Override
  public Maybe<AbstractDocument> read(Document document) {
    Node invoice = document.getElementsByTagName("bestand").item(0);

    return Optional.ofNullable(invoice.getAttributes().getNamedItem("type"))
        .flatMap(t -> Optional.ofNullable(invoice.getAttributes().getNamedItem("version"))
            .map(v -> {
              String version = v.getNodeValue();
              switch (version) {
                case "4":
                  String type = t.getNodeValue();
                  switch (type) {
                    case "MutatiesFactuur":
                      return this.makeMutationInvoice(getFirst(invoice, "mutaties-factuur")).cast(AbstractDocument.class);
                    case "Offerte":
                      return this.makeOffer(getFirst(invoice, "offerte")).cast(AbstractDocument.class);
                    case "ParticulierFactuur":
                      return this.makeNormalInvoice(getFirst(invoice, "particulier-factuur")).cast(AbstractDocument.class);
                    case "ReparatiesFactuur":
                      return this.makeRepairInvoice(getFirst(invoice, "reparaties-factuur")).cast(AbstractDocument.class);
                    default:
                      return Maybe.<AbstractDocument> error(new XmlParseException(String.format("No suitable invoice type found. Found type: %s", type)));
                  }
                default:
                  return Maybe.<AbstractDocument> error(new XmlParseException(String.format("Incorrect version for this parser. Found version: %s", version)));
              }
            }))
        .orElseGet(() -> Maybe.error(new XmlParseException("No invoice type is specified")));
  }

  private Function<CurrencyUnit, Maybe<MonetaryAmount>> makeMoney(Node node) {
    return currencyUnit -> getNodeValue(node, "bedrag")
        .map(Double::parseDouble)
        .map(d -> Money.of(d, currencyUnit));
  }

  private Maybe<Debtor> makeDebtor(Node node) {
    Maybe<String> naam = getNodeValue(node, "naam");
    Maybe<String> straat = getNodeValue(node, "straat");
    Maybe<String> nummer = getNodeValue(node, "nummer");
    Maybe<String> postcode = getNodeValue(node, "postcode");
    Maybe<String> plaats = getNodeValue(node, "plaats");
    Maybe<String> btwnr = getNodeValue(node, "btwNummer");

    return btwnr.isEmpty()
        .flatMapMaybe(b -> b
            ? Maybe.zip(naam, straat, nummer, postcode, plaats, Debtor::new)
            : Maybe.zip(naam, straat, nummer, postcode, plaats, btwnr, Debtor::new));
  }

  private Maybe<LocalDate> makeDate(Node node) {
    Maybe<Integer> dag = getNodeValue(node, "dag").map(Integer::parseInt);
    Maybe<Integer> maand = getNodeValue(node, "maand").map(Integer::parseInt);
    Maybe<Integer> jaar = getNodeValue(node, "jaar").map(Integer::parseInt);

    return Maybe.zip(jaar, maand, dag, LocalDate::of)
        .onErrorResumeNext(t -> {
          if (t instanceof DateTimeException)
            return Maybe.error(new XmlParseException(t));
          return Maybe.error(t);
        });
  }

  private Maybe<Header> makeInvoiceHeader(Node node) {
    Maybe<Debtor> debtor = this.makeDebtor(getFirst(node, "debiteur"));
    Maybe<LocalDate> date = this.makeDate(getFirst(node, "datum"));
    Maybe<String> invoiceNumber = getNodeValue(node, "factuurnummer");

    return Maybe.zip(debtor, date, invoiceNumber, Header::new);
  }

  private Function<CurrencyUnit, Maybe<MutationListItem>> makeMutationListItem(Node node) {
    return currency -> {
      Maybe<String> description = getNodeValue(node, "omschrijving");
      Maybe<String> itemId = getNodeValue(node, "bonnummer");
      Maybe<MonetaryAmount> amount = this.makeMoney(getFirst(node, "prijs")).apply(currency);

      return Maybe.zip(description, itemId, amount, MutationListItem::new);
    };
  }

  private Function<CurrencyUnit, Maybe<SimpleListItem>> makeSimpleListItem(Node node) {
    return currency -> {
      Maybe<String> description = getNodeValue(node, "omschrijving");
      Maybe<MonetaryAmount> amount = this.makeMoney(getFirst(node, "prijs")).apply(currency);
      Maybe<Double> materialTaxPercentage = getNodeValue(node, "materiaalBtwPercentage").map(Double::parseDouble);

      return Maybe.zip(description, amount, materialTaxPercentage, SimpleListItem::new);
    };
  }

  private Function<CurrencyUnit, Maybe<EsselinkItem>> makeEsselinkItem(Node node) {
    return currency -> {
      Maybe<String> itemId = getNodeValue(node, "artikelNummer");
      Maybe<String> description = getNodeValue(node, "omschrijving");
      Maybe<Integer> amountPer = getNodeValue(node, "prijsPer").map(Integer::parseInt);
      Maybe<String> unit = getNodeValue(node, "eenheid");
      Maybe<MonetaryAmount> pricePerUnit = makeMoney(getFirst(node, "verkoopPrijs")).apply(currency);

      return Maybe.zip(itemId, description, amountPer, unit, pricePerUnit, EsselinkItem::new);
    };
  }

  private Function<CurrencyUnit, Maybe<EsselinkListItem>> makeEsselinkListItem(Node node) {
    return currency -> {
      Maybe<String> description = getNodeValue(node, "omschrijving");
      Maybe<EsselinkItem> esselinkItem = this.makeEsselinkItem(getFirst(node, "artikel")).apply(currency);
      Maybe<Double> number = getNodeValue(node, "aantal").map(Double::parseDouble);
      Maybe<Double> tax = getNodeValue(node, "materiaalBtwPercentage").map(Double::parseDouble);

      return Maybe.zip(description, esselinkItem, number, tax, EsselinkListItem::new);
    };
  }

  private Function<CurrencyUnit, Maybe<DefaultWage>> makeDefaultWage(Node node) {
    return currency -> {
      Maybe<String> description = getNodeValue(node, "omschrijving");
      Maybe<MonetaryAmount> wage = this.makeMoney(getFirst(node, "loon")).apply(currency);
      Maybe<Double> tax = getNodeValue(node, "loonBtwPercentage").map(Double::parseDouble);

      return Maybe.zip(description, wage, tax, DefaultWage::new);
    };
  }

  private Function<CurrencyUnit, Maybe<HourlyWage>> makeHourlyWage(Node node) {
    return currency -> {
      Maybe<String> description = getNodeValue(node, "omschrijving");
      Maybe<Double> hours = getNodeValue(node, "uren").map(Double::parseDouble);
      Maybe<MonetaryAmount> hourlyWage = this.makeMoney(getFirst(node, "uurloon")).apply(currency);
      Maybe<Double> tax = getNodeValue(node, "loonBtwPercentage").map(Double::parseDouble);

      return Maybe.zip(description, hours, hourlyWage, tax, HourlyWage::new);
    };
  }

  private Function<CurrencyUnit, Maybe<RepairListItem>> makeRepairListItem(Node node) {
    return currency -> {
      Maybe<String> description = getNodeValue(node, "omschrijving");
      Maybe<String> itemId = getNodeValue(node, "bonnummer");
      Maybe<MonetaryAmount> wage = this.makeMoney(getFirst(node, "loon")).apply(currency);
      Maybe<MonetaryAmount> material = this.makeMoney(getFirst(node, "materiaal")).apply(currency);

      return Maybe.zip(description, itemId, wage, material, RepairListItem::new);
    };
  }

  private <T extends ListItem> Function<CurrencyUnit, Maybe<ItemList<T>>> makeItemList(NodeList nodes, Function<Node, Function<CurrencyUnit, Maybe<T>>> itemGenerator) {
    return currency -> iterate(nodes)
        .map(itemGenerator)
        .flatMapMaybe(f -> f.apply(currency))
        .collect(() -> new ItemList<T>(currency), ItemList::add)
        .toMaybe();
  }

  private Function<CurrencyUnit, Maybe<NormalListItem>> error(Throwable throwable) {
    return c -> Maybe.error(throwable);
  }

  private Function<CurrencyUnit, Maybe<ItemList<NormalListItem>>> makeNormalItemList(NodeList nodes) {
    return currency -> iterate(nodes)
        .filter(item -> !"#text".equals(item.getNodeName()))
        .map(item -> {
          String name = item.getNodeName();
          switch (item.getNodeName()) {
            case "gebruikt-esselink-artikel":
              return this.makeEsselinkListItem(item);
            case "ander-artikel":
              return this.makeSimpleListItem(item);
            case "instant-loon":
              return this.makeDefaultWage(item);
            case "product-loon":
              return this.makeHourlyWage(item);
            default:
              return this.error(new IllegalArgumentException(String.format("Unknown artikel type found. Name = %s", name)));
          }
        })
        .flatMapMaybe(f -> f.apply(currency))
        .collect(() -> new ItemList<NormalListItem>(currency), ItemList::add)
        .toMaybe();
  }

  private Maybe<MutationInvoice> makeMutationInvoice(Node node) {
    Maybe<Header> header = this.makeInvoiceHeader(getFirst(node, "factuurHeader"));
    Maybe<CurrencyUnit> currency = getNodeValue(node, "currency").map(Monetary::getCurrency);
    NodeList list = getAll(getFirst(node, "list"), "mutaties-bon");
    Function<CurrencyUnit, Maybe<ItemList<MutationListItem>>> fMutationItemList = this.makeItemList(list, this::makeMutationListItem);

    return Maybe.merge(Maybe.zip(header, currency, (h, c) -> fMutationItemList.apply(c).map(i -> new MutationInvoice(h, i))));
  }

  private Maybe<Offer> makeOffer(Node node) {
    Maybe<Header> header = this.makeInvoiceHeader(getFirst(node, "factuurHeader"));
    Maybe<String> text = getNodeValue(node, "tekst");
    Maybe<Boolean> ondertekenen = getNodeValue(node, "ondertekenen").map(Boolean::parseBoolean);

    return Maybe.zip(header, text, ondertekenen, Offer::new);
  }

  private Maybe<NormalInvoice> makeNormalInvoice(Node node) {
    Node headerNode = getFirst(node, "factuurHeader");
    Maybe<Header> header = this.makeInvoiceHeader(headerNode);
    Maybe<String> description = getNodeValue(headerNode, "omschrijving");
    Maybe<CurrencyUnit> currency = getNodeValue(node, "currency").map(Monetary::getCurrency);
    Function<CurrencyUnit, Maybe<ItemList<NormalListItem>>> fList = this.makeNormalItemList(getFirst(node, "list").getChildNodes());

    return Maybe.merge(Maybe.zip(header, description, currency,
        (h, d, c) -> fList.apply(c).map(l -> new NormalInvoice(h, d, l))));
  }

  private Maybe<RepairInvoice> makeRepairInvoice(Node node) {
    Maybe<Header> header = this.makeInvoiceHeader(getFirst(node, "factuurHeader"));
    Maybe<CurrencyUnit> currency = getNodeValue(node, "currency").map(Monetary::getCurrency);
    NodeList list = getAll(getFirst(node, "list"), "reparaties-bon");
    Function<CurrencyUnit, Maybe<ItemList<RepairListItem>>> fRepairItemList = this.makeItemList(list, this::makeRepairListItem);

    return Maybe.merge(Maybe.zip(header, currency,
        (h, c) -> fRepairItemList.apply(c).map(i -> new RepairInvoice(h, i))));
  }
}

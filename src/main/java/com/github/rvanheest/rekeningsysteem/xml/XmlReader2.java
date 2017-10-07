package com.github.rvanheest.rekeningsysteem.xml;

import com.github.rvanheest.rekeningsysteem.exception.XmlParseException;
import com.github.rvanheest.rekeningsysteem.model.TaxPercentages;
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
import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.github.rvanheest.rekeningsysteem.xml.XmlUtils.getAll;
import static com.github.rvanheest.rekeningsysteem.xml.XmlUtils.getFirst;
import static com.github.rvanheest.rekeningsysteem.xml.XmlUtils.getNodeValue;
import static com.github.rvanheest.rekeningsysteem.xml.XmlUtils.iterate;

public class XmlReader2 implements XmlLoader {

  private final DocumentBuilder builder;

  public XmlReader2(DocumentBuilder builder) {
    this.builder = builder;
  }

  @Override
  public Maybe<AbstractDocument> load(Path path) {
    return this.load(this.builder, path);
  }

  @Override
  public Maybe<AbstractDocument> read(Document document) {
    Node doc = document.getElementsByTagName("bestand").item(0);

    return Optional.ofNullable(doc.getAttributes().getNamedItem("type")).map(kindNode -> {
      Node invoice = getFirst(doc, "rekening");
      String kind = kindNode.getNodeValue();
      switch (kind) {
        case "AangenomenFactuur":
          return this.createAangenomenInvoice(invoice).cast(AbstractDocument.class);
        case "MutatiesFactuur":
          return this.createMutationInvoice(invoice).cast(AbstractDocument.class);
        case "Offerte":
          return this.createOffer(invoice).cast(AbstractDocument.class);
        case "ParticulierFactuur":
          return this.createNormalInvoice(invoice).cast(AbstractDocument.class);
        case "ReparatiesFactuur":
          return this.createRepairInvoice(invoice).cast(AbstractDocument.class);
        default:
          return Maybe.<AbstractDocument> error(new XmlParseException(String.format("Geen geschikte Node gevonden. Nodenaam = %s.", kind)));
      }
    }).orElseGet(() -> Maybe.error(new XmlParseException("Geen factuur type gespecificeerd")));
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

  private BiFunction<CurrencyUnit, TaxPercentages, Maybe<List<NormalListItem>>> makeAangenomenListItem(Node node) {
    return (currency, tax) -> {
      Maybe<String> description = getNodeValue(node, "omschrijving");
      Maybe<MonetaryAmount> wage = this.makeMoney(getFirst(node, "loon")).apply(currency);
      Maybe<MonetaryAmount> materialCosts = this.makeMoney(getFirst(node, "materiaal")).apply(currency);

      return Maybe.zip(description, wage, materialCosts, (omschr, l, m) -> Arrays.asList(
          new SimpleListItem(omschr, m, tax.getMaterialCostsPercentage()),
          new DefaultWage(omschr, l, tax.getWagePercentage())));
    };
  }

  private Function<CurrencyUnit, Maybe<MutationListItem>> makeMutationListItem(Node node) {
    return currency -> {
      Maybe<String> description = getNodeValue(node, "omschrijving");
      Maybe<String> itemId = getNodeValue(node, "bonnummer");
      Maybe<MonetaryAmount> amount = makeMoney(getFirst(node, "prijs")).apply(currency);

      return Maybe.zip(description, itemId, amount, MutationListItem::new);
    };
  }

  private BiFunction<CurrencyUnit, Double, Maybe<SimpleListItem>> makeSimpleListItem(Node node) {
    return (currency, tax) -> {
      Maybe<String> description = getNodeValue(node, "omschrijving");
      Maybe<MonetaryAmount> amount = this.makeMoney(getFirst(node, "prijs")).apply(currency);

      return Maybe.zip(description, amount, (d, a) -> new SimpleListItem(d, a, tax));
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

  private BiFunction<CurrencyUnit, Double, Maybe<EsselinkListItem>> makeEsselinkListItem(Node node) {
    return (currency, tax) -> {
      Maybe<EsselinkItem> esselinkItem = this.makeEsselinkItem(getFirst(node, "artikel")).apply(currency);
      Maybe<Double> number = getNodeValue(node, "aantal").map(Double::parseDouble);

      return Maybe.zip(esselinkItem, number, (e, n) -> new EsselinkListItem(e, n, tax));
    };
  }

  private BiFunction<CurrencyUnit, Double, Maybe<DefaultWage>> makeDefaultWage(Node node) {
    return (currency, tax) -> {
      Maybe<String> description = getNodeValue(node, "omschrijving");
      Maybe<MonetaryAmount> wage = this.makeMoney(getFirst(node, "loon")).apply(currency);

      return Maybe.zip(description, wage, (d, w) -> new DefaultWage(d, w, tax));
    };
  }

  private BiFunction<CurrencyUnit, Double, Maybe<HourlyWage>> makeHourlyWage(Node node) {
    return (currency, tax) -> {
      Maybe<String> description = getNodeValue(node, "omschrijving");
      Maybe<Double> hours = getNodeValue(node, "uren").map(Double::parseDouble);
      Maybe<MonetaryAmount> hourlyWage = this.makeMoney(getFirst(node, "uurloon")).apply(currency);

      return Maybe.zip(description, hours, hourlyWage, (d, h, hw) -> new HourlyWage(d, h, hw, tax));
    };
  }

  private Function<CurrencyUnit, Maybe<RepairListItem>> makeRepairListItem(Node node) {
    return currency -> {
      Maybe<String> omschrijving = getNodeValue(node, "omschrijving");
      Maybe<String> bonnummer = getNodeValue(node, "bonnummer");
      Maybe<MonetaryAmount> loon = this.makeMoney(getFirst(node, "loon")).apply(currency);
      Maybe<MonetaryAmount> materiaal = this.makeMoney(getFirst(node, "materiaal")).apply(currency);

      return Maybe.zip(omschrijving, bonnummer, loon, materiaal, RepairListItem::new);
    };
  }

  private <T extends ListItem> Function<CurrencyUnit, Maybe<ItemList<T>>> makeItemList(NodeList nodes, Function<Node, Function<CurrencyUnit, Maybe<T>>> itemGenerator) {
    return currency -> iterate(nodes)
        .map(itemGenerator)
        .flatMapMaybe(f -> f.apply(currency))
        .collect(() -> new ItemList<T>(currency), ItemList::add)
        .toMaybe();
  }

  private BiFunction<CurrencyUnit, TaxPercentages, Maybe<ItemList<NormalListItem>>> makeAangenomenItemList(NodeList nodes) {
    return (currency, tax) -> iterate(nodes)
          .map(this::makeAangenomenListItem)
          .flatMap(f -> f.apply(currency, tax).flatMapObservable(Observable::fromIterable))
          .collect(() -> new ItemList<NormalListItem>(currency), ItemList::add)
          .toMaybe();
  }

  private BiFunction<CurrencyUnit, Double, Maybe<NormalListItem>> error(Throwable throwable) {
    return (c, t) -> Maybe.error(throwable);
  }

  private BiFunction<CurrencyUnit, Double, Maybe<ItemList<NormalListItem>>> makeNormalItemList(NodeList nodes) {
    return (currency, tax) -> iterate(nodes)
        .filter(item -> !"#text".equals(item.getNodeName()))
        .map(item -> {
          String name = item.getNodeName();
          switch (name) {
            case "gebruikt-esselink-artikel":
              return this.makeEsselinkListItem(item);
            case "ander-artikel":
              return this.makeSimpleListItem(item);
            default:
              return this.error(new IllegalArgumentException(String.format("Unknown artikel type found. Name = %s", name)));
          }
        })
        .flatMapMaybe(f -> f.apply(currency, tax))
        .collect(() -> new ItemList<NormalListItem>(currency), ItemList::add)
        .toMaybe();
  }

  private BiFunction<CurrencyUnit, Double, Maybe<ItemList<NormalListItem>>> makeNormalWageItemList(NodeList nodes) {
    return (currency, tax) -> iterate(nodes)
        .filter(item -> !"#text".equals(item.getNodeName()))
        .map(item -> {
          String name = item.getNodeName();
          switch (name) {
            case "instant-loon":
              return this.makeDefaultWage(item);
            case "product-loon":
              return this.makeHourlyWage(item);
            default:
              return this.error(new IllegalArgumentException(String.format("Unknown artikel type found. Name = %s", name)));
          }
        })
        .flatMapMaybe(f -> f.apply(currency, tax))
        .collect(() -> new ItemList<NormalListItem>(currency), ItemList::add)
        .toMaybe();
  }

  private Maybe<TaxPercentages> makeTaxPercentages(Node node) {
    Maybe<Double> loon = getNodeValue(node, "loonPercentage").map(Double::parseDouble);
    Maybe<Double> materiaal = getNodeValue(node, "materiaalPercentage").map(Double::parseDouble);

    return Maybe.zip(loon, materiaal, TaxPercentages::new);
  }

  private Maybe<NormalInvoice> createAangenomenInvoice(Node node) {
    Node headerNode = getFirst(node, "factuurHeader");
    Maybe<Header> header = this.makeInvoiceHeader(headerNode);
    Maybe<String> description = getNodeValue(headerNode, "omschrijving");
    Maybe<CurrencyUnit> currency = getNodeValue(node, "currency").map(Monetary::getCurrency);
    NodeList list = getAll(getFirst(node, "list"), "list-item");
    BiFunction<CurrencyUnit, TaxPercentages, Maybe<ItemList<NormalListItem>>> fNormalItemList = this.makeAangenomenItemList(list);
    Maybe<TaxPercentages> tax = this.makeTaxPercentages(getFirst(node, "btwPercentage"));

    return Maybe.merge(Maybe.zip(header, description, currency, tax,
        (h, d, c, t) -> fNormalItemList.apply(c, t).map(i -> new NormalInvoice(h, d, i))));
  }

  private Maybe<MutationInvoice> createMutationInvoice(Node node) {
    Maybe<Header> header = this.makeInvoiceHeader(getFirst(node, "factuurHeader"));
    Maybe<CurrencyUnit> currency = getNodeValue(node, "currency").map(Monetary::getCurrency);
    NodeList list = getAll(getFirst(node, "list"), "list-item");
    Function<CurrencyUnit, Maybe<ItemList<MutationListItem>>> fMutationItemList = this.makeItemList(list, this::makeMutationListItem);

    return Maybe.merge(Maybe.zip(header, currency,
        (h, c) -> fMutationItemList.apply(c).map(i -> new MutationInvoice(h, i))));
  }

  private Maybe<Offer> createOffer(Node node) {
    Maybe<Header> header = this.makeInvoiceHeader(getFirst(node, "factuurHeader"));
    Maybe<String> text = getNodeValue(node, "tekst");
    Maybe<Boolean> sign = getNodeValue(node, "ondertekenen").map(Boolean::parseBoolean);

    return Maybe.zip(header, text, sign, Offer::new);
  }

  private Maybe<NormalInvoice> createNormalInvoice(Node node) {
    Node headerNode = getFirst(node, "factuurHeader");
    Maybe<Header> header = this.makeInvoiceHeader(headerNode);
    Maybe<String> description = getNodeValue(headerNode, "omschrijving");
    Maybe<CurrencyUnit> currency = getNodeValue(node, "currency").map(Monetary::getCurrency);
    BiFunction<CurrencyUnit, Double, Maybe<ItemList<NormalListItem>>> fMaterialList = this.makeNormalItemList(getFirst(node, "itemList").getChildNodes());
    BiFunction<CurrencyUnit, Double, Maybe<ItemList<NormalListItem>>> fWageList = this.makeNormalWageItemList(getFirst(node, "loonList").getChildNodes());
    Maybe<TaxPercentages> tax = this.makeTaxPercentages(getFirst(node, "btwPercentage"));

    return Maybe.merge(Maybe.zip(header, description, currency, tax,
        (h, d, c, t) -> fMaterialList.apply(c, t.getMaterialCostsPercentage())
            .flatMap(m -> fWageList.apply(c, t.getWagePercentage())
                .map(w -> ItemList.merge(m, w)), (x, list) -> new NormalInvoice(h, d, list))));
  }

  private Maybe<RepairInvoice> createRepairInvoice(Node node) {
    Maybe<Header> header = this.makeInvoiceHeader(getFirst(node, "factuurHeader"));
    Maybe<CurrencyUnit> currency = getNodeValue(node, "currency").map(Monetary::getCurrency);
    NodeList list = getAll(getFirst(node, "list"), "list-item");
    Function<CurrencyUnit, Maybe<ItemList<RepairListItem>>> fRepairItemList = this.makeItemList(list, this::makeRepairListItem);

    return Maybe.merge(Maybe.zip(header, currency,
        (h, c) -> fRepairItemList.apply(c).map(i -> new RepairInvoice(h, i))));
  }
}

package com.github.rvanheest.rekeningsysteem.xml;

import com.github.rvanheest.rekeningsysteem.exception.XmlParseException;
import com.github.rvanheest.rekeningsysteem.model.MyMonetaryFormatter;
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
import com.github.rvanheest.rekeningsysteem.model.normal.wage.AbstractWage;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.HourlyWage;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairInvoice;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairListItem;
import io.reactivex.Maybe;
import io.reactivex.functions.Function;
import org.javamoney.moneta.Money;
import org.javamoney.moneta.format.CurrencyStyle;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import javax.money.format.MonetaryParseException;
import javax.xml.parsers.DocumentBuilder;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.regex.PatternSyntaxException;

import static com.github.rvanheest.rekeningsysteem.xml.XmlUtils.getAll;
import static com.github.rvanheest.rekeningsysteem.xml.XmlUtils.getFirst;
import static com.github.rvanheest.rekeningsysteem.xml.XmlUtils.getNodeValue;
import static com.github.rvanheest.rekeningsysteem.xml.XmlUtils.iterate;

public class XmlReader1 implements XmlLoader {

  private final DocumentBuilder builder;
  private final MonetaryAmountFormat parserNoCurrency;
  private final MonetaryAmountFormat parserWithCurrency;
  private final CurrencyUnit currency;

  public XmlReader1(DocumentBuilder builder, Locale locale, CurrencyUnit currency) {
    this(builder, currency, MyMonetaryFormatter.of(locale),
        MonetaryFormats.getAmountFormat(AmountFormatQueryBuilder.of(locale).set(CurrencyStyle.SYMBOL).build()));
  }

  public XmlReader1(DocumentBuilder builder, CurrencyUnit currency, MonetaryAmountFormat parserNoCurrency,
      MonetaryAmountFormat parserWithCurrency) {
    this.builder = builder;
    this.parserNoCurrency = parserNoCurrency;
    this.parserWithCurrency = parserWithCurrency;
    this.currency = currency;
  }

  @Override
  public Maybe<AbstractDocument> load(Path path) {
    return this.load(this.builder, path);
  }

  @Override
  public Maybe<AbstractDocument> read(Document document) {
    Node invoice = document.getElementsByTagName("bestand").item(0).getFirstChild();
    String kind = invoice.getNodeName();

    switch (kind) {
      case "particulierfactuur1":
      case "partfactuur":
        return this.makeNormalInvoice1(invoice).cast(AbstractDocument.class);
      case "particulierfactuur2":
        return this.makeNormalInvoice2(invoice).cast(AbstractDocument.class);
      case "reparatiesfactuur":
        return this.makeRepairInvoice(invoice).cast(AbstractDocument.class);
      case "mutatiesfactuur":
        return this.makeMutationInvoice(invoice).cast(AbstractDocument.class);
      case "offerte":
        return this.makeOffer(invoice).cast(AbstractDocument.class);
      default:
        return Maybe.error(new XmlParseException(String.format("Geen geschikte Node gevonden. Nodenaam = %s.", kind)));
    }
  }

  private MonetaryAmount makeMoney(String s) throws XmlParseException {
    try {
      switch (s.split(" ").length) {
        case 1:
          return this.parserNoCurrency.parse(s).with(m -> Money.of(m.getNumber(), this.currency));
        case 2:
          return this.parserWithCurrency.parse(s);
        default:
          throw new XmlParseException(String.format("Could not parse money '%s'", s));
      }
    }
    catch (PatternSyntaxException | MonetaryParseException e) {
      throw new XmlParseException(String.format("Could not parse money '%s'", s), e);
    }
  }

  private Maybe<Debtor> makeDebtor(Node node) {
    Maybe<String> naam = getNodeValue(node, "naam");
    Maybe<String> straat = getNodeValue(node, "straat");
    Maybe<String> nummer = getNodeValue(node, "nummer");
    Maybe<String> postcode = getNodeValue(node, "postcode");
    Maybe<String> plaats = getNodeValue(node, "plaats");
    Maybe<String> btwnr = getNodeValue(node, "btwnr").filter(s -> !s.replace(" ", "").isEmpty());

    return btwnr.isEmpty()
        .flatMapMaybe(b -> b ?
            Maybe.zip(naam, straat, nummer, postcode, plaats, Debtor::new) :
            Maybe.zip(naam, straat, nummer, postcode, plaats, btwnr, Debtor::new));
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

  private Maybe<Header> makeOfferHeader(Node node) {
    Maybe<Debtor> debtor = this.makeDebtor(getFirst(node, "debiteur"));
    Maybe<LocalDate> date = this.makeDate(getFirst(node, "datum"));
    Maybe<String> invoiceNumber = getNodeValue(node, "offertenummer");

    return Maybe.zip(debtor, date, invoiceNumber, Header::new);
  }

  private Maybe<MutationListItem> makeMutationListItem(Node node) {
    Maybe<String> description = getNodeValue(node, "omschrijving");
    Maybe<String> itemId = getNodeValue(node, "bonnummer");
    Maybe<MonetaryAmount> amount = getNodeValue(node, "prijs").map(this::makeMoney);

    return Maybe.zip(description, itemId, amount, MutationListItem::new);
  }

  private Maybe<Function<Double, SimpleListItem>> makeSimpleListItem(Node node) {
    Maybe<String> description = getNodeValue(getFirst(node, "artikel"), "omschrijving");
    Maybe<MonetaryAmount> amount = getNodeValue(getAll(node, "prijs")).map(this::makeMoney);

    return Maybe.zip(description, amount, (d, a) -> tax -> new SimpleListItem(d, a, tax));
  }

  private Maybe<EsselinkItem> makeEsselinkItem(Node node) {
    Maybe<String> itemId = getNodeValue(node, "artikelnummer");
    Maybe<String> description = getNodeValue(node, "omschrijving");
    Maybe<Integer> amountPer = getNodeValue(node, "prijsper").map(Integer::parseInt);
    Maybe<String> unit = getNodeValue(node, "eenheid");
    Maybe<MonetaryAmount> pricePerUnit = getNodeValue(node, "verkoopprijs").map(this::makeMoney);

    return Maybe.zip(itemId, description, amountPer, unit, pricePerUnit, EsselinkItem::new);
  }

  private Maybe<Function<Double, EsselinkListItem>> makeEsselinkListItem(Node node) {
    Maybe<EsselinkItem> item = this.makeEsselinkItem(getFirst(node, "artikel"));
    Maybe<Double> number = getNodeValue(node, "aantal").map(Double::parseDouble);

    return Maybe.zip(item, number, (i, n) -> tax -> new EsselinkListItem(i, n, tax));
  }

  private Maybe<Function<Double, HourlyWage>> makeHourlyWage(Node node) {
    Maybe<MonetaryAmount> hourlyWage = getNodeValue(node, "uurloon").map(this::makeMoney);
    Maybe<Double> hours = getNodeValue(node, "uren").map(Double::parseDouble);

    return Maybe.zip(hourlyWage, hours,
        (hw, h) -> tax -> new HourlyWage(String.format("Uurloon à %s", this.parserNoCurrency.format(hw)), h, hw,
            tax));
  }

  private Maybe<TaxPercentages> makeOneTaxPercentage(Node node) {
    return getNodeValue(node, "btw").map(Double::parseDouble).map(tax -> new TaxPercentages(tax, tax));
  }

  private Maybe<TaxPercentages> makeTwoTaxPercentage(Node node) {
    Maybe<Double> material = getNodeValue(node, "btwpercentageart").map(Double::parseDouble);
    Maybe<Double> wage = getNodeValue(node, "btwpercentageloon").map(Double::parseDouble);

    return Maybe.zip(wage, material, TaxPercentages::new);
  }

  private Maybe<RepairListItem> makeRepairListItem(Node node) {
    Maybe<String> description = getNodeValue(node, "omschrijving");
    Maybe<String> itemId = getNodeValue(node, "bonnummer");
    Maybe<MonetaryAmount> wage = getNodeValue(node, "uurloon").map(this::makeMoney);
    Maybe<MonetaryAmount> materialCosts = getNodeValue(node, "materiaal").map(this::makeMoney);

    return Maybe.zip(description, itemId, wage, materialCosts, RepairListItem::new);
  }

  private <T extends ListItem> Maybe<ItemList<T>> makeItemList(NodeList nodes, Function<Node, Maybe<T>> itemGenerator) {
    return iterate(nodes)
        .flatMapMaybe(itemGenerator)
        .collect(() -> new ItemList<T>(this.currency), ItemList::add)
        .toMaybe();
  }

  private Function<Double, Maybe<ItemList<NormalListItem>>> makeNormalItemList(NodeList nodes) {
    return tax -> iterate(nodes)
        .flatMapMaybe(item -> {
          switch (item.getNodeName()) {
            case "gebruiktartikelander":
              return this.makeSimpleListItem(item);
            case "gebruiktartikelesselink":
              return this.makeEsselinkListItem(item);
            default:
              return Maybe.error(new IllegalArgumentException("Unknown artikel type found."));
          }
        })
        .map(f -> f.apply(tax))
        .collect(() -> new ItemList<NormalListItem>(this.currency), ItemList::add)
        .toMaybe();
  }

  private Maybe<MutationInvoice> makeMutationInvoice(Node node) {
    Maybe<Header> header = this.makeInvoiceHeader(node);
    NodeList list = getAll(getFirst(node, "mutatiesbonlijst"), "mutatiesbon");
    Maybe<ItemList<MutationListItem>> itemList = this.makeItemList(list, this::makeMutationListItem);

    return Maybe.zip(header, itemList, MutationInvoice::new);
  }

  private Maybe<Offer> makeOffer(Node node) {
    Maybe<Header> header = this.makeOfferHeader(node);
    Maybe<String> text = getNodeValue(node, "tekst");
    Maybe<Boolean> sign = getNodeValue(node, "ondertekenen").map(Boolean::parseBoolean);

    return Maybe.zip(header, text, sign, Offer::new);
  }

  private Function<TaxPercentages, Maybe<NormalInvoice>> makeNormalInvoice(Node node) {
    return tp -> {
      Maybe<Header> header = this.makeInvoiceHeader(node);
      Maybe<String> description = getNodeValue(node, "omschrijving");
      NodeList gal = getFirst(getFirst(node, "artikellijst"), "gebruiktartikellijst").getChildNodes();
      Maybe<ItemList<NormalListItem>> list = makeNormalItemList(gal).apply(tp.getMaterialCostsPercentage());
      Maybe<AbstractWage> wage = this.makeHourlyWage(getFirst(node, "loon")).map(f -> f.apply(tp.getWagePercentage()));

      return Maybe.zip(header, description, list, wage, (h, d, l, w) -> {
        l.add(w);
        return new NormalInvoice(h, d, l);
      });
    };
  }

  private Maybe<NormalInvoice> makeNormalInvoice1(Node node) {
    return this.makeOneTaxPercentage(node).flatMap(this.makeNormalInvoice(node));
  }

  private Maybe<NormalInvoice> makeNormalInvoice2(Node node) {
    return this.makeTwoTaxPercentage(node).flatMap(this.makeNormalInvoice(node));
  }

  private Maybe<RepairInvoice> makeRepairInvoice(Node node) {
    Maybe<Header> header = this.makeInvoiceHeader(node);
    NodeList list = getAll(getFirst(node, "reparatiesbonlijst"), "reparatiesbon");
    Maybe<ItemList<RepairListItem>> itemList = this.makeItemList(list, this::makeRepairListItem);

    return Maybe.zip(header, itemList, RepairInvoice::new);
  }
}

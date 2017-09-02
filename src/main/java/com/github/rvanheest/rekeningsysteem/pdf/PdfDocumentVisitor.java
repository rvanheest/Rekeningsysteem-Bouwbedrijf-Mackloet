package com.github.rvanheest.rekeningsysteem.pdf;

import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.mutation.MutationInvoice;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalInvoice;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairInvoice;
import com.github.rvanheest.rekeningsysteem.model.totals.Totals;
import com.github.rvanheest.rekeningsysteem.model.visitor.DocumentVisitor;

import javax.money.CurrencyUnit;
import javax.money.format.MonetaryAmountFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PdfDocumentVisitor implements DocumentVisitor<Consumer<PdfConverter>> {

  private final String datePattern;
  private final MonetaryAmountFormat moneyFormatter;
  private final PdfListItemVisitor itemVisitor;
  private final Locale locale;

  public PdfDocumentVisitor(String datePattern, Locale locale, MonetaryAmountFormat moneyFormatter,
      PdfListItemVisitor itemVisitor) {
    this.datePattern = datePattern;
    this.locale = locale;
    this.moneyFormatter = moneyFormatter;
    this.itemVisitor = itemVisitor;
  }

  private Consumer<PdfConverter> visitDebtor(Debtor debtor) {
    return converter -> {
      converter.replace("DebiteurNaam", debtor.getName());
      converter.replace("DebiteurStraat", debtor.getStreet());
      converter.replace("DebiteurNummer", debtor.getNumber());
      converter.replace("DebiteurPostcode", debtor.getZipcode());
      converter.replace("DebiteurPlaats", debtor.getCity());
      converter.replace("HasDebiteurBtwNummer", debtor.getVatNumber().isPresent());
      converter.replace("DebiteurBtwNummer", debtor.getVatNumber().orElse(""));
    };
  }

  private Consumer<PdfConverter> visitHeader(Header header) {
    return this.visitDebtor(header.getDebtor())
        .andThen(converter -> converter.replace("Factuurnummer", header.getInvoiceNumber().orElse("")))
        .andThen(converter -> converter.replace("Datum",
            header.getDate().format(DateTimeFormatter.ofPattern(this.datePattern))));
  }

  private Consumer<PdfConverter> visitCurrency(CurrencyUnit currency) {
    return converter -> converter.replace("Valuta",
        Currency.getInstance(currency.getCurrencyCode()).getSymbol(this.locale));
  }

  private Consumer<PdfConverter> visitTotals(Totals totals) {
    return converter -> {
      converter.replace("SubTotaalBedrag", this.moneyFormatter.format(totals.getSubtotal()));
      converter.replace("btwList", totals.getNettoBtwTuple()
          .entrySet()
          .stream()
          .sorted(Map.Entry.comparingByKey())
          .map(entry -> Arrays.asList(String.valueOf(entry.getKey()),
              this.moneyFormatter.format(entry.getValue().getNet()),
              this.moneyFormatter.format(entry.getValue().getTax())))
          .collect(Collectors.toList()));
      converter.replace("TotaalBedrag", this.moneyFormatter.format(totals.getTotal()));
    };
  }

  @Override
  public Consumer<PdfConverter> visit(MutationInvoice invoice) throws Exception {
    return this.visitHeader(invoice.getHeader())
        .andThen(this.visitCurrency(invoice.getItemList().getCurrency()))
        .andThen(converter -> converter.replace("bonList", invoice.getItemList()
            .getList()
            .stream()
            .map(item -> item.accept(this.itemVisitor))
            .collect(Collectors.toList())))
        .andThen(
            converter -> converter.replace("TotaalBedrag", this.moneyFormatter.format(invoice.getTotals().getTotal())));
  }

  @Override
  public Consumer<PdfConverter> visit(Offer offer) throws Exception {
    return this.visitHeader(offer.getHeader())
        .andThen(converter -> converter.replace("Tekst", offer.getText()))
        .andThen(converter -> converter.replace("Ondertekenen", String.valueOf(offer.isSign())));
  }

  @Override
  public Consumer<PdfConverter> visit(NormalInvoice invoice) throws Exception {
    return this.visitHeader(invoice.getHeader())
        .andThen(converter -> converter.replace("Omschrijving", invoice.getDescription()))
        .andThen(this.visitCurrency(invoice.getItemList().getCurrency()))
        .andThen(converter -> converter.replace("artikelList", invoice.getItemList()
            .getList()
            .stream()
            .map(item -> item.accept(this.itemVisitor))
            .collect(Collectors.toList())))
        .andThen(this.visitTotals(invoice.getTotals()));
  }

  @Override
  public Consumer<PdfConverter> visit(RepairInvoice invoice) throws Exception {
    return this.visitHeader(invoice.getHeader())
        .andThen(this.visitCurrency(invoice.getItemList().getCurrency()))
        .andThen(converter -> converter.replace("bonList", invoice.getItemList()
            .getList()
            .stream()
            .map(item -> item.accept(this.itemVisitor))
            .collect(Collectors.toList())))
        .andThen(
            converter -> converter.replace("TotaalBedrag", this.moneyFormatter.format(invoice.getTotals().getTotal())));
  }
}

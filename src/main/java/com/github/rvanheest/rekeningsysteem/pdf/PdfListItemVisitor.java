package com.github.rvanheest.rekeningsysteem.pdf;

import com.github.rvanheest.rekeningsysteem.model.mutation.MutationListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkItem;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.SimpleListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.DefaultWage;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.HourlyWage;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairListItem;
import com.github.rvanheest.rekeningsysteem.model.visitor.ListItemVisitor;

import javax.money.MonetaryAmount;
import javax.money.format.MonetaryAmountFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PdfListItemVisitor implements ListItemVisitor<List<String>> {

  private final MonetaryAmountFormat moneyFormatter;

  public PdfListItemVisitor(MonetaryAmountFormat moneyFormatter) {
    this.moneyFormatter = moneyFormatter;
  }

  @Override
  public List<String> visit(MutationListItem item) {
    MonetaryAmount total = item.getTotal();

    if (total.isZero())
      return Collections.emptyList();
    else
      return Arrays.asList(
          item.getDescription(),
          item.getItemId(),
          this.moneyFormatter.format(total));
  }

  @Override
  public List<String> visit(RepairListItem item) {
    MonetaryAmount total = item.getTotal();

    if (total.isZero())
      return Collections.emptyList();
    else
      return Arrays.asList(
          item.getDescription(),
          item.getItemId(),
          this.moneyFormatter.format(item.getWage()),
          this.moneyFormatter.format(item.getMaterialCosts()),
          this.moneyFormatter.format(total));
  }

  @Override
  public List<String> visit(SimpleListItem item) {
    MonetaryAmount materialCosts = item.getMaterialCosts();

    if (materialCosts.isZero())
      return Collections.emptyList();
    else
      return Arrays.asList(
          item.getDescription(),
          this.moneyFormatter.format(materialCosts),
          formatBtwPercentage(item.getMaterialCostsTaxPercentage()));
  }

  @Override
  public List<String> visit(EsselinkListItem item) {
    EsselinkItem sub = item.getItem();
    MonetaryAmount materialCosts = item.getMaterialCosts();

    if (materialCosts.isZero())
      return Collections.emptyList();
    else {
      double number = item.getAmount();
      String numberString = Double.compare(number, Math.floor(number)) == 0 && !Double.isInfinite(number)
          ? String.valueOf(((Double) number).intValue())
          : String.valueOf(number);

      return Arrays.asList(
          String.format("%sx %s", numberString, sub.getDescription()),
          this.moneyFormatter.format(materialCosts),
          formatBtwPercentage(item.getMaterialCostsTaxPercentage())
      );
    }
  }

  @Override
  public List<String> visit(DefaultWage item) {
    MonetaryAmount wage = item.getWage();

    if (wage.isZero())
      return Collections.emptyList();
    else
      return Arrays.asList(
          item.getDescription(),
          this.moneyFormatter.format(wage),
          formatBtwPercentage(item.getWageTaxPercentage()));
  }

  @Override
  public List<String> visit(HourlyWage item) {
    MonetaryAmount wage = item.getWage();

    if (wage.isZero())
      return Collections.emptyList();
    else
      return Arrays.asList(
          String.format("%s uren à %s", String.valueOf(item.getHours()), this.moneyFormatter.format(item.getWagePerHour())),
          this.moneyFormatter.format(wage),
          formatBtwPercentage(item.getWageTaxPercentage()));
  }

  private String formatBtwPercentage(double btwPercentage) {
    return btwPercentage == 0
        ? "verlegd"
        : String.valueOf(btwPercentage) + "%";
  }
}

package com.github.rvanheest.rekeningsysteem.model.totals;

import com.github.rvanheest.rekeningsysteem.model.mutation.MutationListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.SimpleListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.DefaultWage;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.HourlyWage;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairListItem;
import com.github.rvanheest.rekeningsysteem.model.visitor.ListItemVisitor;

import java.util.function.Function;

public class TotalsListItemVisitor implements ListItemVisitor<Function<Totals, Totals>> {

  @Override
  public Function<Totals, Totals> visit(MutationListItem item) {
    return totals -> totals.add(item.getMaterialCosts());
  }

  @Override
  public Function<Totals, Totals> visit(RepairListItem item) {
    return totals -> totals.add(item.getTotal());
  }

  @Override
  public Function<Totals, Totals> visit(SimpleListItem item) {
    return totals -> totals.add(item.getMaterialCostsTaxPercentage(), item.getMaterialCosts(),
        item.getMaterialCostsTax());
  }

  @Override
  public Function<Totals, Totals> visit(EsselinkListItem item) {
    return totals -> totals.add(item.getMaterialCostsTaxPercentage(), item.getMaterialCosts(),
        item.getMaterialCostsTax());
  }

  @Override
  public Function<Totals, Totals> visit(DefaultWage item) {
    return totals -> totals.add(item.getWageTaxPercentage(), item.getWage(), item.getWageTax());
  }

  @Override
  public Function<Totals, Totals> visit(HourlyWage item) {
    return totals -> totals.add(item.getWageTaxPercentage(), item.getWage(), item.getWageTax());
  }
}

package com.github.rvanheest.rekeningsysteem.model.visitor;

import com.github.rvanheest.rekeningsysteem.model.mutation.MutationListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.SimpleListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.DefaultWage;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.HourlyWage;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairListItem;

public interface ListItemVisitor<T> {

  T visit(MutationListItem item);

  T visit(RepairListItem item);

  T visit(SimpleListItem item);

  T visit(EsselinkListItem item);

  T visit(DefaultWage item);

  T visit(HourlyWage item);
}

package com.github.rvanheest.rekeningsysteem.model.visitor;

import com.github.rvanheest.rekeningsysteem.model.mutation.MutationInvoice;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalInvoice;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairInvoice;

public interface DocumentVisitor<T> {

  T visit(MutationInvoice invoice) throws Exception;

  T visit(Offer offer) throws Exception;

  T visit(NormalInvoice invoice) throws Exception;

  T visit(RepairInvoice invoice) throws Exception;
}

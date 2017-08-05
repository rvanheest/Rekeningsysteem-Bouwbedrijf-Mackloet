package com.github.rvanheest.rekeningsysteem.model.visitor;

import com.github.rvanheest.rekeningsysteem.model.mutation.MutationInvoice;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalInvoice;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairInvoice;

// TODO still needed?
public interface DocumentVoidVisitor {

  void visit(MutationInvoice invoice) throws Exception;

  void visit(Offer offer) throws Exception;

  void visit(NormalInvoice invoice) throws Exception;

  void visit(RepairInvoice invoice) throws Exception;
}

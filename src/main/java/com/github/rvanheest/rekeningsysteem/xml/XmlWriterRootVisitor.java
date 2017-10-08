package com.github.rvanheest.rekeningsysteem.xml;

import com.github.rvanheest.rekeningsysteem.model.mutation.MutationInvoice;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalInvoice;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairInvoice;
import com.github.rvanheest.rekeningsysteem.model.visitor.DocumentVisitor;

public class XmlWriterRootVisitor implements DocumentVisitor<String> {

  @Override
  public String visit(MutationInvoice invoice) throws Exception {
    return "MutatiesFactuur";
  }

  @Override
  public String visit(Offer offer) throws Exception {
    return "Offerte";
  }

  @Override
  public String visit(NormalInvoice invoice) throws Exception {
    return "ParticulierFactuur";
  }

  @Override
  public String visit(RepairInvoice invoice) throws Exception {
    return "ReparatiesFactuur";
  }
}

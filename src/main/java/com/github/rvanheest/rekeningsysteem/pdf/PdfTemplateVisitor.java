package com.github.rvanheest.rekeningsysteem.pdf;

import com.github.rvanheest.rekeningsysteem.model.mutation.MutationInvoice;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalInvoice;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairInvoice;
import com.github.rvanheest.rekeningsysteem.model.visitor.DocumentVisitor;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PdfTemplateVisitor implements DocumentVisitor<Path> {

  private final PropertiesConfiguration configuration;

  public PdfTemplateVisitor(PropertiesConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public Path visit(MutationInvoice invoice) throws Exception {
    return Paths.get(this.configuration.getString("pdf.template.mutation"));
  }

  @Override
  public Path visit(Offer offer) throws Exception {
    return Paths.get(this.configuration.getString("pdf.template.offer"));
  }

  @Override
  public Path visit(NormalInvoice invoice) throws Exception {
    return Paths.get(this.configuration.getString("pdf.template.normal"));
  }

  @Override
  public Path visit(RepairInvoice invoice) throws Exception {
    return Paths.get(this.configuration.getString("pdf.template.repair"));
  }
}

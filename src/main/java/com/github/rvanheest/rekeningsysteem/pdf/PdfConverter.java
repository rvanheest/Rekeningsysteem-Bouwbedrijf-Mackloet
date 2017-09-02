package com.github.rvanheest.rekeningsysteem.pdf;

import de.nixosoft.jlr.JLRConverter;

import java.nio.file.Path;

public class PdfConverter extends JLRConverter {

  private final LaTeXCharacterReplacer replacer;

  public PdfConverter(Path path) {
    this(path, new LaTeXCharacterReplacer());
  }

  public PdfConverter(Path path, LaTeXCharacterReplacer replacer) {
    super(path.toFile());
    this.replacer = replacer;
  }

  @Override
  public void replace(String key, Object value) {
    super.replace(key, this.replacer.replace(value));
  }
}

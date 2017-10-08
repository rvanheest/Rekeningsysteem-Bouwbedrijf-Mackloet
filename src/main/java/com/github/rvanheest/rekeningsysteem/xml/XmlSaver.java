package com.github.rvanheest.rekeningsysteem.xml;

import com.github.rvanheest.rekeningsysteem.exception.XmlWriteException;
import com.github.rvanheest.rekeningsysteem.model.document.AbstractDocument;

import java.nio.file.Path;

public interface XmlSaver {

  void save(AbstractDocument document, Path path) throws XmlWriteException;
}

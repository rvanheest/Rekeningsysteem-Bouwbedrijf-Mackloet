package com.github.rvanheest.rekeningsysteem.xml;

import com.github.rvanheest.rekeningsysteem.exception.XmlParseException;
import com.github.rvanheest.rekeningsysteem.model.document.AbstractDocument;
import io.reactivex.Maybe;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import java.io.IOException;
import java.nio.file.Path;

public interface XmlLoader {

  Maybe<AbstractDocument> load(Path path);

  default Maybe<AbstractDocument> load(DocumentBuilder builder, Path path) {
    try {
      Document document = builder.parse(path.toFile());
      document.getDocumentElement().normalize();
      return this.read(document);
    }
    catch (SAXException | IOException e) {
      return Maybe.error(new XmlParseException(e));
    }
  }

  Maybe<AbstractDocument> read(Document document);
}

package com.github.rvanheest.rekeningsysteem.xml;

import com.github.rvanheest.rekeningsysteem.model.document.AbstractDocument;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import java.nio.file.Path;

public class XmlReader implements XmlLoader {

  private final XmlLoader[] loaders;
  private final DocumentBuilder builder;

  public XmlReader(DocumentBuilder builder, XmlLoader... loaders) {
    this.builder = builder;
    this.loaders = loaders;
  }

  @Override
  public Maybe<AbstractDocument> load(Path path) {
    return this.load(this.builder, path);
  }

  @Override
  public Maybe<AbstractDocument> read(Document document) {
    return Observable.fromArray(this.loaders)
        .flatMapMaybe(loader -> loader.read(document))
        .onErrorResumeNext(Observable.empty())
        .firstElement();
  }
}

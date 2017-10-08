package com.github.rvanheest.rekeningsysteem.xml;

import com.github.rvanheest.rekeningsysteem.model.document.AbstractDocument;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import org.w3c.dom.Document;

import javax.money.CurrencyUnit;
import javax.xml.parsers.DocumentBuilder;
import java.nio.file.Path;
import java.util.Locale;

public class XmlReader implements XmlLoader {

  private final XmlLoader[] loaders;
  private final DocumentBuilder builder;

  public XmlReader(DocumentBuilder builder, Locale locale, CurrencyUnit currency) {
    this.builder = builder;
    this.loaders = new XmlLoader[] {
        new XmlReader4(builder),
        new XmlReader3(builder),
        new XmlReader2(builder),
        new XmlReader1(builder, locale, currency)
    };
  }

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
        .concatMapDelayError(loader -> loader.read(document).toObservable())
        .onErrorResumeNext(Observable.empty())
        .firstElement();
  }
}

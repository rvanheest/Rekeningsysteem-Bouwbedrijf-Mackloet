package com.github.rvanheest.rekeningsysteem.offerText;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import rx.Completable;
import rx.Single;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DefaultOfferTextHandler {

  private final Path defaultOfferTextPath;

  public DefaultOfferTextHandler(PropertiesConfiguration configuration) {
    this(Paths.get(configuration.getString("offer.defaulttext")));
  }

  public DefaultOfferTextHandler(Path defaultOfferTextPath) {
    this.defaultOfferTextPath = defaultOfferTextPath;
  }

  public Single<String> getDefaultText() {
    return Single.fromCallable(() -> FileUtils.readFileToString(this.defaultOfferTextPath.toFile()));
  }

  public Completable setDefaultText(String text) {
    return Completable.create(subscriber -> {
      try {
        FileUtils.write(this.defaultOfferTextPath.toFile(), text, false);
        subscriber.onCompleted();
      }
      catch (IOException e) {
        subscriber.onError(e);
      }
    });
  }
}

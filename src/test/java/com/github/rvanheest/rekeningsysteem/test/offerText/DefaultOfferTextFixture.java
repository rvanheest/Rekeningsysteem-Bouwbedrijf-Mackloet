package com.github.rvanheest.rekeningsysteem.test.offerText;

import com.github.rvanheest.rekeningsysteem.offerText.DefaultOfferTextHandler;
import com.github.rvanheest.rekeningsysteem.test.TestSupportFixture;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public interface DefaultOfferTextFixture extends TestSupportFixture {

  default DefaultOfferTextHandler getDefaultOfferTextHandler() throws IOException, URISyntaxException {
    return new DefaultOfferTextHandler(Files.copy(
        Paths.get(getClass().getClassLoader().getResource("offerText/DefaultOfferText.txt").toURI()),
        this.getTestDir().resolve("DefaultOfferText.txt")));
  }
}

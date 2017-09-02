package com.github.rvanheest.rekeningsysteem.test.offerText;

import com.github.rvanheest.rekeningsysteem.offerText.DefaultOfferTextHandler;
import com.github.rvanheest.rekeningsysteem.test.TestSupportFixture;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class DefaultOfferTextHandlerTest implements DefaultOfferTextFixture {

  private DefaultOfferTextHandler handler;

  @Before
  public void setUp() throws Exception {
    this.resetTestDir();
    this.handler = this.getDefaultOfferTextHandler();
  }

  @Test
  public void testSecondConstructor() throws ConfigurationException {
    PropertiesConfiguration configuration = new PropertiesConfiguration();
    configuration.addProperty("offer.defaulttext", this.getTestDir().resolve("SecondDefaultOfferText.txt").toString());

    this.handler = new DefaultOfferTextHandler(configuration);
    this.testSetDefaultText();
  }

  @Test
  public void testGetDefaultText() {
    this.handler.getDefaultText()
        .test()
        .assertValue("This is a testing text! Here you can put your name, adres, etc.\r\n")
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testSetDefaultText() {
    this.handler.setDefaultText("hello world")
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    this.handler.getDefaultText()
        .test()
        .assertValue("hello world")
        .assertNoErrors()
        .assertComplete();
  }
}

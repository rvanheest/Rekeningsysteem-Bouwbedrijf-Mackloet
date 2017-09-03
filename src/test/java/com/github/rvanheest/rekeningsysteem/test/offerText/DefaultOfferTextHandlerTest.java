package com.github.rvanheest.rekeningsysteem.test.offerText;

import com.github.rvanheest.rekeningsysteem.offerText.DefaultOfferTextHandler;
import com.github.rvanheest.rekeningsysteem.test.ConfigurationFixture;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class DefaultOfferTextHandlerTest implements ConfigurationFixture {

  private DefaultOfferTextHandler handler;
  private PropertiesConfiguration configuration;

  @Before
  public void setUp() throws Exception {
    this.resetTestDir();
    this.configuration = this.getConfiguration();
    this.handler = new DefaultOfferTextHandler(this.getTestDir().resolve("DefaultOfferText.txt"));
  }

  @Test
  public void testSecondConstructor() throws ConfigurationException, IOException, URISyntaxException {
    this.handler = new DefaultOfferTextHandler(this.configuration);
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

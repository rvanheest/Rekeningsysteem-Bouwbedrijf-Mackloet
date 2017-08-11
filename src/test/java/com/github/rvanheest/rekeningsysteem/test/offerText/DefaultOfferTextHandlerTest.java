package com.github.rvanheest.rekeningsysteem.test.offerText;

import com.github.rvanheest.rekeningsysteem.offerText.DefaultOfferTextHandler;
import com.github.rvanheest.rekeningsysteem.test.TestSupportFixture;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Before;
import org.junit.Test;
import rx.observers.TestSubscriber;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DefaultOfferTextHandlerTest implements TestSupportFixture {

  private DefaultOfferTextHandler handler;

  @Before
  public void setUp() throws IOException, URISyntaxException {
    Path testDir = this.resetTestDir();
    Path textPath = Paths.get(getClass().getClassLoader().getResource("offerText/DefaultOfferText.txt").toURI());
    Path defaultTextPath = Files.copy(textPath, testDir.resolve("DefaultOfferText.txt"));
    this.handler = new DefaultOfferTextHandler(defaultTextPath);
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
    TestSubscriber<String> testSubscriber = new TestSubscriber<>();
    this.handler.getDefaultText().subscribe(testSubscriber);

    testSubscriber.assertValue("This is a testing text! Here you can put your name, adres, etc.\r\n");
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
    testSubscriber.assertUnsubscribed();
  }

  @Test
  public void testSetDefaultText() {
    TestSubscriber<Void> testSubscriber1 = new TestSubscriber<>();
    this.handler.setDefaultText("hello world").subscribe(testSubscriber1);

    testSubscriber1.assertNoValues();
    testSubscriber1.assertNoErrors();
    testSubscriber1.onCompleted();
    testSubscriber1.assertUnsubscribed();

    TestSubscriber<String> testSubscriber2 = new TestSubscriber<>();
    this.handler.getDefaultText().subscribe(testSubscriber2);

    testSubscriber2.assertValue("hello world");
    testSubscriber2.assertNoErrors();
    testSubscriber2.assertCompleted();
    testSubscriber2.assertUnsubscribed();
  }
}

package com.github.rvanheest.rekeningsysteem.test.integration;

import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.offerText.DefaultOfferTextHandler;
import org.junit.Before;
import org.junit.Test;

public class OfferIntegrationTest extends AbstractDocumentIntegrationTest {

  private DefaultOfferTextHandler handler;

  @Before
  @Override
  public void setUp() throws Exception {
    super.setUp();
    this.handler = new DefaultOfferTextHandler(this.getConfiguration());
  }

  private String makeText() {
    return "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce quis quam tortor. "
        + "Nunc id leo in eros tincidunt lobortis eget a erat. Mauris id velit ut diam "
        + "tincidunt auctor ut sed sapien. Morbi in tellus ut sapien molestie sodales "
        + "vitae molestie libero. Sed sodales augue nulla, ut pulvinar odio placerat eu. "
        + "Nunc at arcu euismod, pulvinar orci nec, fermentum tellus. In hac habitasse "
        + "platea dictumst. Interdum et malesuada fames ac ante ipsum primis in faucibus. "
        + "Nullam eu sagittis libero, quis pretium tortor. Aliquam at placerat dolor. "
        + "Nunc fringilla quam venenatis lacus condimentum, vitae mattis risus "
        + "ullamcorper. Maecenas pulvinar gravida libero, ac scelerisque justo dignissim in.\n\n"

        + "Maecenas pretium mi id magna convallis, vel auctor erat pulvinar. Cras et "
        + "tellus nec lacus pellentesque rutrum eget sodales nisi. In vitae sagittis "
        + "urna. Sed vestibulum suscipit vulputate. Vivamus commodo augue at dolor "
        + "volutpat blandit. Nunc in justo bibendum, cursus purus quis, scelerisque est. "
        + "Aenean ut accumsan arcu.\n\n"

        + "Cras volutpat auctor mollis. Sed aliquam elit et accumsan dictum. Pellentesque "
        + "sollicitudin, turpis sollicitudin tincidunt porttitor, velit quam mollis sem, "
        + "non volutpat nunc arcu quis magna. Fusce vitae mattis nisl. Nulla sit amet "
        + "mollis neque, et hendrerit eros. Cras id risus adipiscing, sollicitudin odio "
        + "non, mollis metus. Pellentesque vulputate tempus nibh a ultricies. Quisque "
        + "ullamcorper dictum velit, sit amet pellentesque risus facilisis vel.";
  }

  @Override
  protected Offer makeDocument(Header header) {
    return new Offer(header, this.makeText(), true);
  }

  @Test
  public void testOfferWithDefaultText() {
    this.handler.getDefaultText()
        .map(t -> new Offer(this.getHeaderWithoutInvoiceNumber(), t, true))
        .test()
        .assertValue(new Offer(this.getHeaderWithoutInvoiceNumber(),
            "This is a testing text! Here you can put your name, adres, etc.\r\n", true))
        .assertNoErrors()
        .assertComplete();
  }
}

package org.rekeningsysteem.logic.offerte;

import com.github.rjeschke.txtmark.Configuration;
import com.github.rjeschke.txtmark.DefaultDecorator;
import com.github.rjeschke.txtmark.Processor;

public class HtmlMarkdownTransformer implements MarkdownTransformer {

  @Override
  public String transform(String markdown) {
    Configuration config1 = Configuration.builder()
        .setDecorator(new DefaultDecorator())
        .setSafeMode(true)
        .build();

    return Processor.process(markdown, config1);
  }
}

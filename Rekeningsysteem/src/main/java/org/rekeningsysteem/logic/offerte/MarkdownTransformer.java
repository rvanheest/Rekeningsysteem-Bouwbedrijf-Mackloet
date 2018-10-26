package org.rekeningsysteem.logic.offerte;

import java.io.IOException;

public interface MarkdownTransformer {

  String transform(String markdown) throws IOException;
}

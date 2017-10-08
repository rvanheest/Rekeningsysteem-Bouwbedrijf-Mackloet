package com.github.rvanheest.rekeningsysteem.xml;

import com.github.rvanheest.rekeningsysteem.function.Consumer;
import io.reactivex.functions.Function;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Optional;

class XmlWriterUtils {

  static Function<Document, Node> createElement(String name, Function<Node, Consumer<Document>> consumer) {
    return document -> {
      Element xml = document.createElement(name);
      consumer.apply(xml).accept(document);
      return xml;
    };
  }

  static Consumer<Document> stringNode(Node root, String key, String value) {
    return document -> root.appendChild(document.createElement(key)).appendChild(document.createTextNode(value));
  }

  static Consumer<Document> optionalStringNode(Node root, String key, Optional<String> value) {
    return value.map(v -> stringNode(root, key, v)).orElseGet(() -> d -> {});
  }

  static Consumer<Document> appendNode(Node root, Node node) {
    return document -> root.appendChild(node);
  }

  static Consumer<Document> appendNode(Node root, Function<Document, Node> f) {
    return document -> appendNode(root, f.apply(document)).accept(document);
  }
}

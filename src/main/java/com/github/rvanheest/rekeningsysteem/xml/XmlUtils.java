package com.github.rvanheest.rekeningsysteem.xml;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Optional;

public class XmlUtils {

  public static Maybe<String> getNodeValue(Node node, String name) {
    return Optional.ofNullable(node)
        .map(n -> getAll(n, name))
        .map(XmlUtils::getNodeValue)
        .orElseGet(() -> Maybe.error(new IllegalArgumentException(String.format("node is null (key = %s)", name))));
  }

  public static Maybe<String> getNodeValue(NodeList list) {
    return Optional.ofNullable(list.item(0))
        .map(n -> Optional.ofNullable(n.getChildNodes().item(0))
            .map(Node::getNodeValue)
            .map(Maybe::just)
            .orElseGet(() -> Maybe.just("")))
        .orElseGet(Maybe::empty);
  }

  public static Node getFirst(Node node, String name) {
    return getAll(node, name).item(0);
  }

  public static NodeList getAll(Node node, String name) {
    return ((Element) node).getElementsByTagName(name);
  }

  public static Observable<Node> iterate(NodeList nodes) {
    return Observable.range(0, nodes.getLength()).map(nodes::item);
  }
}

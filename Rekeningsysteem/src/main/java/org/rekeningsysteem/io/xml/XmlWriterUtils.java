package org.rekeningsysteem.io.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Optional;
import java.util.SortedMap;
import java.util.function.Consumer;
import java.util.function.Function;

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

	static Consumer<Document> stringNodeWithAttributes(Node root, String key, String value, SortedMap<String, String> attributes) {
		return document -> {
			Element keyChild = document.createElement(key);
			attributes.forEach(keyChild::setAttribute);
			root.appendChild(keyChild).appendChild(document.createTextNode(value));
		};
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

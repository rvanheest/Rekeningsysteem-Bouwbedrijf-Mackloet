package org.rekeningsysteem.io.xml;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.exception.XmlParseException;
import org.rekeningsysteem.io.FactuurLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public abstract class XmlLoader implements FactuurLoader {

	private final DocumentBuilder builder;

	public XmlLoader(DocumentBuilder builder) {
		this.builder = builder;
	}

	@Override
	public Single<AbstractRekening> load(Path path) {
		return this.load(this.builder, path);
	}

	Single<AbstractRekening> load(DocumentBuilder builder, Path path) {
		try {
			Document document = builder.parse(path.toFile());
			document.getDocumentElement().normalize();
			return this.read(document);
		}
		catch (SAXException | IOException e) {
			return Single.error(new XmlParseException(e));
		}
	}

	abstract Single<AbstractRekening> read(Document document);

	static Single<AbstractRekening> parseRekening(Node bestand, Function<Node, Maybe<? extends AbstractRekening>> parse, String... names) {
		return Observable.fromArray(names)
			.map(name -> getNodeList(bestand, name))
			.filter(nodeList -> nodeList.getLength() > 0)
			.map(nodeList -> nodeList.item(0))
			.flatMapMaybe(parse)
			.onErrorResumeNext(e -> Observable.empty())
			.firstElement()
			.switchIfEmpty(Single.defer(() -> Single.error(new XmlParseException("Could not parse file to object."))));
	}

	static Maybe<String> getNodeValue(Node node, String name) {
		return Optional.ofNullable(node)
			.map(n -> getStringValue(getElement(n, name)))
			.orElseGet(() -> Maybe.error(new IllegalArgumentException(String.format("node is null (key = %s)", name))));
	}

	static Maybe<String> getStringValue(Node node) {
		return Optional.ofNullable(node)
			.map(n -> n.getChildNodes().item(0))
			.map(Node::getNodeValue)
			.map(Maybe::just)
			.orElseGet(() -> Maybe.just(""));
	}

	static Maybe<String> getAttribute(Node node, String name) {
		return Maybe.fromOptional(Optional.ofNullable(node.getAttributes().getNamedItem(name)).map(Node::getNodeValue));
	}

	static Node getElement(Node node, String name) {
		return getNodeList(node, name).item(0);
	}

	static NodeList getNodeList(Node node, String name) {
		return ((Element) node).getElementsByTagName(name);
	}

	static Observable<Node> iterate(NodeList nodes) {
		return Observable.range(0, nodes.getLength()).map(nodes::item);
	}
}

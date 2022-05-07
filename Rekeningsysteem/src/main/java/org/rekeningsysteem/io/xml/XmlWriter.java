package org.rekeningsysteem.io.xml;

import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.exception.XmlWriteException;
import org.rekeningsysteem.io.FactuurSaver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.nio.file.Path;
import java.util.function.Function;

public class XmlWriter implements FactuurSaver {

	private static final String documentVersion = "4";

	private final XmlWriterDocumentVisitor documentVisitor;
	private final DocumentBuilder documentBuilder;
	private final TransformerFactory transformerFactory;

	public XmlWriter(DocumentBuilder documentBuilder, TransformerFactory transformerFactory) {
		this(new XmlWriterDocumentVisitor(), documentBuilder, transformerFactory);
	}

	public XmlWriter(XmlWriterDocumentVisitor documentVisitor, DocumentBuilder documentBuilder, TransformerFactory transformerFactory) {
		this.documentVisitor = documentVisitor;
		this.documentBuilder = documentBuilder;
		this.transformerFactory = transformerFactory;
	}

	@Override
	public void save(org.rekeningsysteem.data.util.Document document, Path path) {
		try {
			Document xmlDoc = this.documentBuilder.newDocument();
			xmlDoc.setXmlStandalone(true);

			Transformer transformer = this.transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			Element root = xmlDoc.createElement("bestand");

			root.setAttribute("type", switch (document) {
				case MutatiesFactuur ignored -> "MutatiesFactuur";
				case Offerte ignored -> "Offerte";
				case ParticulierFactuur ignored -> "ParticulierFactuur";
				case ReparatiesFactuur ignored -> "ReparatiesFactuur";
				case default -> throw new RuntimeException();
			});
			root.setAttribute("version", documentVersion);

			Function<Document, Node> f = switch (document) {
				case MutatiesFactuur item -> this.documentVisitor.visit(item);
				case Offerte item -> this.documentVisitor.visit(item);
				case ParticulierFactuur item -> this.documentVisitor.visit(item);
				case ReparatiesFactuur item -> this.documentVisitor.visit(item);
				case default -> throw new RuntimeException();
			};
			root.appendChild(f.apply(xmlDoc));

			xmlDoc.appendChild(root);

			transformer.transform(new DOMSource(xmlDoc), new StreamResult(path.toFile()));
		}
		catch (Exception e) {
			throw new XmlWriteException(e);
		}
	}
}

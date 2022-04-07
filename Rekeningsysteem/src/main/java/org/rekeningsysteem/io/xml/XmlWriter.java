package org.rekeningsysteem.io.xml;

import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;
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
import java.io.File;
import java.util.function.Function;

public class XmlWriter implements FactuurSaver {

	private static final String documentVersion = "4";

	private final RekeningVisitor<Function<Document, Node>> documentVisitor;
	private final RekeningVisitor<String> typeVisitor;
	private final DocumentBuilder documentBuilder;
	private final TransformerFactory transformerFactory;

	public XmlWriter(DocumentBuilder documentBuilder, TransformerFactory transformerFactory) {
		this(new XmlWriterDocumentVisitor(new XmlWriterItemVisitor()), new XmlWriterRekeningTypeVisitor(), documentBuilder, transformerFactory);
	}

	public XmlWriter(RekeningVisitor<Function<Document, Node>> documentVisitor, RekeningVisitor<String> typeVisitor,
		DocumentBuilder documentBuilder, TransformerFactory transformerFactory) {
		this.documentVisitor = documentVisitor;
		this.typeVisitor = typeVisitor;
		this.documentBuilder = documentBuilder;
		this.transformerFactory = transformerFactory;
	}

	@Override
	public void save(AbstractRekening rekening, File file) {
		try {
			Document xmlDoc = this.documentBuilder.newDocument();
			xmlDoc.setXmlStandalone(true);

			Transformer transformer = this.transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			Element root = xmlDoc.createElement("bestand");
			root.setAttribute("type", rekening.accept(this.typeVisitor));
			root.setAttribute("version", documentVersion);
			root.appendChild(rekening.accept(this.documentVisitor).apply(xmlDoc));

			xmlDoc.appendChild(root);

			transformer.transform(new DOMSource(xmlDoc), new StreamResult(file));
		}
		catch (Exception e) {
			throw new XmlWriteException(e);
		}
	}
}

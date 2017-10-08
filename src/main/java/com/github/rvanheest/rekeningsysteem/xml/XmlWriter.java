package com.github.rvanheest.rekeningsysteem.xml;

import com.github.rvanheest.rekeningsysteem.exception.XmlWriteException;
import com.github.rvanheest.rekeningsysteem.model.document.AbstractDocument;
import com.github.rvanheest.rekeningsysteem.model.visitor.DocumentVisitor;
import io.reactivex.functions.Function;
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

public class XmlWriter implements XmlSaver {

  private static final String documentVersion = "4";

  private final DocumentVisitor<Function<Document, Node>> documentVisitor;
  private final DocumentVisitor<String> typeVisitor;
  private final DocumentBuilder documentBuilder;
  private final TransformerFactory transformerFactory;

  public XmlWriter(DocumentBuilder documentBuilder, TransformerFactory transformerFactory) {
    this(new XmlWriterDocumentVisitor(new XmlWriterItemVisitor()), new XmlWriterRootVisitor(), documentBuilder, transformerFactory);
  }

  public XmlWriter(DocumentVisitor<Function<Document, Node>> documentVisitor, DocumentVisitor<String> typeVisitor,
      DocumentBuilder documentBuilder, TransformerFactory transformerFactory) {
    this.documentVisitor = documentVisitor;
    this.typeVisitor = typeVisitor;
    this.documentBuilder = documentBuilder;
    this.transformerFactory = transformerFactory;
  }

  @Override
  public void save(AbstractDocument document, Path path) throws XmlWriteException {
    try {
      Document xmlDoc = this.documentBuilder.newDocument();
      xmlDoc.setXmlStandalone(true);

      Transformer transformer = this.transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

      Element root = xmlDoc.createElement("bestand");
      root.setAttribute("type", document.accept(this.typeVisitor));
      root.setAttribute("version", documentVersion);
      root.appendChild(document.accept(this.documentVisitor).apply(xmlDoc));

      xmlDoc.appendChild(root);

      transformer.transform(new DOMSource(xmlDoc), new StreamResult(path.toFile()));
    }
    catch (Exception e) {
      throw new XmlWriteException(e);
    }
  }
}

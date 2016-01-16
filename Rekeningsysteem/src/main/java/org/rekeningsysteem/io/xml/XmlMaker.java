package org.rekeningsysteem.io.xml;

import java.io.File;

import org.apache.log4j.Logger;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.io.FactuurSaver;

public class XmlMaker implements FactuurSaver {

	private XmlMakerVisitor visitor;
	private Logger logger;

	public XmlMaker(Logger logger) {
		this(new XmlMakerVisitor(logger), logger);
	}

	public XmlMaker(XmlMakerVisitor visitor, Logger logger) {
		this.visitor = visitor;
		this.logger = logger;
	}

	@Override
	public void save(AbstractRekening rekening, File saveLocation) {
		try {
			this.visitor.setSaveLocation(saveLocation);
			rekening.accept(this.visitor);
		}
		catch (Exception exception) {
			this.logger.error(exception.getMessage(), exception);
		}
	}
}

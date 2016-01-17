package org.rekeningsysteem.io.xml.root;

import javax.xml.bind.annotation.XmlAttribute;

import org.rekeningsysteem.data.util.AbstractRekening;

public interface Root<T extends AbstractRekening> {

	@XmlAttribute
	String getType();

	@XmlAttribute
	String getVersion();

	T getRekening();

	Root<T> setRekening(T rekening);
}

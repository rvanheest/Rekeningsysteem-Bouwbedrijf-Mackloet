package org.rekeningsysteem.io.xml.root;

import java.util.function.Function;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.io.xml.adapter.RekeningAdapter;

@XmlRootElement(name = "bestand")
public class ParticulierFactuurRoot implements Root<ParticulierFactuur> {

	private ParticulierFactuur factuur;

	private ParticulierFactuurRoot() {
	}

	@Override
	@XmlAttribute
	public String getType() {
		return "ParticulierFactuur";
	}

	@Override
	@XmlAttribute
	public String getVersion() {
		return "4";
	}

	@Override
	@XmlElementRef
	@XmlJavaTypeAdapter(RekeningAdapter.class)
	public ParticulierFactuur getRekening() {
		return this.factuur;
	}

	@Override
	public ParticulierFactuurRoot setRekening(ParticulierFactuur factuur) {
		this.factuur = factuur;
		return this;
	}

	public static ParticulierFactuurRoot build(
			Function<ParticulierFactuurRoot, ParticulierFactuurRoot> builder) {
		return builder.apply(new ParticulierFactuurRoot());
	}
}

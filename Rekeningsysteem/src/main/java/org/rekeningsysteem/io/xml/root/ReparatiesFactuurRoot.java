package org.rekeningsysteem.io.xml.root;

import java.util.function.Function;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.io.xml.adapter.RekeningAdapter;

@XmlRootElement(name = "bestand")
public class ReparatiesFactuurRoot implements Root<ReparatiesFactuur> {

	private ReparatiesFactuur factuur;

	private ReparatiesFactuurRoot() {
	}

	@Override
	@XmlAttribute
	public String getType() {
		return "ReparatiesFactuur";
	}

	@Override
	@XmlAttribute
	public String getVersion() {
		return "4";
	}

	@Override
	@XmlElementRef
	@XmlJavaTypeAdapter(RekeningAdapter.class)
	public ReparatiesFactuur getRekening() {
		return this.factuur;
	}

	@Override
	public ReparatiesFactuurRoot setRekening(ReparatiesFactuur factuur) {
		this.factuur = factuur;
		return this;
	}

	public static ReparatiesFactuurRoot build(
			Function<ReparatiesFactuurRoot, ReparatiesFactuurRoot> builder) {
		return builder.apply(new ReparatiesFactuurRoot());
	}
}

package org.rekeningsysteem.io.xml.root;

import java.util.function.Function;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.io.xml.adapter.RekeningAdapter;

@XmlRootElement(name = "bestand")
public class MutatiesFactuurRoot implements Root<MutatiesFactuur> {

	private MutatiesFactuur factuur;

	private MutatiesFactuurRoot() {
	}

	@Override
	@XmlAttribute
	public String getType() {
		return "MutatiesFactuur";
	}

	@Override
	@XmlAttribute
	public String getVersion() {
		return "4";
	}

	@Override
	@XmlElementRef
	@XmlJavaTypeAdapter(RekeningAdapter.class)
	public MutatiesFactuur getRekening() {
		return this.factuur;
	}

	@Override
	public MutatiesFactuurRoot setRekening(MutatiesFactuur factuur) {
		this.factuur = factuur;
		return this;
	}

	public static MutatiesFactuurRoot build(
			Function<MutatiesFactuurRoot, MutatiesFactuurRoot> builder) {
		return builder.apply(new MutatiesFactuurRoot());
	}
}

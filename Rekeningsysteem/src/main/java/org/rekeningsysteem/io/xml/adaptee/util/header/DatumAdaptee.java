package org.rekeningsysteem.io.xml.adaptee.util.header;

import java.util.function.Function;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "datum")
@XmlType(propOrder = { "dag", "maand", "jaar" })
public class DatumAdaptee {

	private int dag;
	private int maand;
	private int jaar;

	private DatumAdaptee() {
	}

	@XmlElement
	public int getDag() {
		return this.dag;
	}

	public DatumAdaptee setDag(int dag) {
		this.dag = dag;
		return this;
	}

	@XmlElement
	public int getMaand() {
		return this.maand;
	}

	public DatumAdaptee setMaand(int maand) {
		this.maand = maand;
		return this;
	}

	@XmlElement
	public int getJaar() {
		return this.jaar;
	}

	public DatumAdaptee setJaar(int jaar) {
		this.jaar = jaar;
		return this;
	}

	public static DatumAdaptee build(Function<DatumAdaptee, DatumAdaptee> builder) {
		return builder.apply(new DatumAdaptee());
	}
}

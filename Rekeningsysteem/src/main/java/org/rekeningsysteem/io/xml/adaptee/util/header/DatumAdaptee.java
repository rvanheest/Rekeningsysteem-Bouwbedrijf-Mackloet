package org.rekeningsysteem.io.xml.adaptee.util.header;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "datum")
@XmlType(propOrder = { "dag", "maand", "jaar" })
public class DatumAdaptee {

	private int dag;
	private int maand;
	private int jaar;

	@XmlElement
	public int getDag() {
		return this.dag;
	}

	public void setDag(int dag) {
		this.dag = dag;
	}

	@XmlElement
	public int getMaand() {
		return this.maand;
	}

	public void setMaand(int maand) {
		this.maand = maand;
	}

	@XmlElement
	public int getJaar() {
		return this.jaar;
	}

	public void setJaar(int jaar) {
		this.jaar = jaar;
	}
}

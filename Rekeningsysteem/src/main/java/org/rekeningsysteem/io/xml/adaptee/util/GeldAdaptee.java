package org.rekeningsysteem.io.xml.adaptee.util;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "bedrag" })
public class GeldAdaptee {

	private double bedrag;

	@XmlElement
	public double getBedrag() {
		return this.bedrag;
	}

	public void setBedrag(double bedrag) {
		this.bedrag = bedrag;
	}
}

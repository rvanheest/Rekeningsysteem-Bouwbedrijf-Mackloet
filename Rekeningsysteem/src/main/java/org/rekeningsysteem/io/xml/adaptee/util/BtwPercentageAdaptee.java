package org.rekeningsysteem.io.xml.adaptee.util;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "loonPercentage", "materiaalPercentage" })
public class BtwPercentageAdaptee {

	private double loonPercentage;
	private double materiaalPercentage;

	@XmlElement
	public double getLoonPercentage() {
		return this.loonPercentage;
	}

	public void setLoonPercentage(double loonPercentage) {
		this.loonPercentage = loonPercentage;
	}

	@XmlElement
	public double getMateriaalPercentage() {
		return this.materiaalPercentage;
	}

	public void setMateriaalPercentage(double materiaalPercentage) {
		this.materiaalPercentage = materiaalPercentage;
	}
}

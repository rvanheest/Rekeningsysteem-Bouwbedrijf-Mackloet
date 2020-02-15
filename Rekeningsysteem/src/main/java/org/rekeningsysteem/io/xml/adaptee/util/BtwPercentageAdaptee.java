package org.rekeningsysteem.io.xml.adaptee.util;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import java.util.function.Function;

@XmlType
public class BtwPercentageAdaptee {

  private double btwPercentage;
  private boolean verlegd;

  private BtwPercentageAdaptee() {
  }

  @XmlValue
  public double getBtwPercentage() {
    return this.btwPercentage;
  }

  public BtwPercentageAdaptee setBtwPercentage(double btwPercentage) {
    this.btwPercentage = btwPercentage;
    return this;
  }

  @XmlAttribute(name = "verlegd")
  public boolean isVerlegd() {
    return this.verlegd;
  }

  public BtwPercentageAdaptee setVerlegd(boolean verlegd) {
    this.verlegd = verlegd;
    return this;
  }

  public static BtwPercentageAdaptee build(Function<BtwPercentageAdaptee, BtwPercentageAdaptee> builder) {
    return builder.apply(new BtwPercentageAdaptee());
  }
}

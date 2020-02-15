package org.rekeningsysteem.io.xml.adapter.util;

import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.io.xml.adaptee.util.BtwPercentageAdaptee;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class BtwPercentageAdapter extends XmlAdapter<BtwPercentageAdaptee, BtwPercentage> {

  @Override
  public BtwPercentage unmarshal(BtwPercentageAdaptee adaptee) {
    return new BtwPercentage(adaptee.getBtwPercentage(), adaptee.isVerlegd());
  }

  @Override
  public BtwPercentageAdaptee marshal(BtwPercentage btwPercentage) {
    return BtwPercentageAdaptee.build(adaptee -> adaptee
        .withBtwPercentage(btwPercentage.getPercentage())
        .withVerlegd(btwPercentage.isVerlegd()));
  }
}

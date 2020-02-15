package org.rekeningsysteem.data.util;

import java.util.Comparator;
import java.util.Objects;

public class BtwPercentage implements Comparable<BtwPercentage> {

  private final double percentage;
  private final boolean verlegd;

  private static final Comparator<BtwPercentage> percentageComparator = Comparator.comparing(BtwPercentage::getPercentage);
  private static final Comparator<BtwPercentage> verlegdComparator = Comparator.comparing(BtwPercentage::isVerlegd);
  private static final Comparator<BtwPercentage> comparator = percentageComparator.thenComparing(verlegdComparator);

  public BtwPercentage(double percentage, boolean verlegd) {
    this.percentage = percentage;
    this.verlegd = verlegd;
  }

  public double getPercentage() {
    return this.percentage;
  }

  public boolean isVerlegd() {
    return this.verlegd;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof BtwPercentage) {
      BtwPercentage that = (BtwPercentage) other;
      return Objects.equals(this.percentage, that.percentage)
          && Objects.equals(this.verlegd, that.verlegd);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.percentage, this.verlegd);
  }

  @Override
  public String toString() {
    return "<BtwPercentage[" + String.valueOf(this.percentage) + ", " + String.valueOf(this.verlegd) + "]>";
  }

  @Override
  public int compareTo(BtwPercentage that) {
    return comparator.compare(this, that);
  }
}

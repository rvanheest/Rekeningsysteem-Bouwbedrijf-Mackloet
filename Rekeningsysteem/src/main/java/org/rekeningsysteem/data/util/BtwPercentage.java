package org.rekeningsysteem.data.util;

import java.util.Objects;

public class BtwPercentage implements Comparable<BtwPercentage> {

  private final double percentage;
  private final boolean verlegd;

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
  public int compareTo(BtwPercentage o) {
    int comp1 = Double.compare(this.percentage, o.percentage);
    return comp1 == 0
        ? -Boolean.compare(this.verlegd, o.verlegd)
        : comp1;
  }
}

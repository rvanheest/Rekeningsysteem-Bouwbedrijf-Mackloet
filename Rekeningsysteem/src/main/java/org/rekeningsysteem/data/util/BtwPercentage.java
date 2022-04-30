package org.rekeningsysteem.data.util;

import java.util.Comparator;

public record BtwPercentage(double percentage, boolean verlegd) implements Comparable<BtwPercentage> {

	private static final Comparator<BtwPercentage> percentageComparator = Comparator.comparing(BtwPercentage::percentage);
	private static final Comparator<BtwPercentage> verlegdComparator = Comparator.comparing(BtwPercentage::verlegd);
	private static final Comparator<BtwPercentage> comparator = percentageComparator.thenComparing(verlegdComparator);

	public String formattedString() {
		String btw = this.percentage + "%";
		return this.verlegd ? btw + ", verlegd" : btw;
	}

	@Override
	public int compareTo(BtwPercentage that) {
		return comparator.compare(this, that);
	}
}

package org.rekeningsysteem.data.util;

public record BtwPercentages(BtwPercentage loonPercentage, BtwPercentage materiaalPercentage) {

	public BtwPercentages(double loonPercentage, double materiaalPercentage) {
		this(new BtwPercentage(loonPercentage, false), new BtwPercentage(materiaalPercentage, false));
	}
}

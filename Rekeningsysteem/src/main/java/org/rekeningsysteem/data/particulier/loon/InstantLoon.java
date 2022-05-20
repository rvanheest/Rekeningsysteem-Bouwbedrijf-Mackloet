package org.rekeningsysteem.data.particulier.loon;

import org.rekeningsysteem.data.util.BtwPercentage;

import javax.money.MonetaryAmount;

public record InstantLoon(String omschrijving, MonetaryAmount loon, BtwPercentage loonBtwPercentage) implements Loon { }

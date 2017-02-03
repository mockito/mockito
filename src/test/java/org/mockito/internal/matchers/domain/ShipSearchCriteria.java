/*
 * Copyright (C) 2015 Mockito contributors.
 *
 * Licensed under the Apache License, Version 2.0.
 */
package org.mockito.internal.matchers.domain;

/**
 * Test domain class.
 */
public class ShipSearchCriteria {

    private final int minimumRange;
    private final int numberOfPhasers;

    public ShipSearchCriteria(int minimumRange, int numberOfPhasers) {
        this.minimumRange = minimumRange;
        this.numberOfPhasers = numberOfPhasers;
    }

    public int getMinimumRange() {
        return minimumRange;
    }

    public int getNumberOfPhasers() {
        return numberOfPhasers;
    }

    @Override
    public String toString() {
        return "ShipSearchCriteria{" +
                "minimumRange=" + minimumRange +
                ", numberOfPhasers=" + numberOfPhasers +
                '}';
    }
}

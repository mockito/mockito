/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.reporting;

public class Discrepancy {

    private final int wantedCount;
    private final int actualCount;

    public Discrepancy(int wantedCount, int actualCount) {
        this.wantedCount = wantedCount;
        this.actualCount = actualCount;
    }

    public int getWantedCount() {
        return wantedCount;
    }
    
    public String getPluralizedWantedCount() {
        return Pluralizer.pluralize(wantedCount);
    }

    public int getActualCount() {
        return actualCount;
    }

    public String getPluralizedActualCount() {
        return Pluralizer.pluralize(actualCount);
    }
}
package org.mockito.exceptions;

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
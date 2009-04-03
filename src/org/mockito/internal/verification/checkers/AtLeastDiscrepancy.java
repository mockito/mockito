package org.mockito.internal.verification.checkers;

import org.mockito.exceptions.Discrepancy;

public class AtLeastDiscrepancy extends Discrepancy {

    public AtLeastDiscrepancy(int wantedCount, int actualCount) {
        super(wantedCount, actualCount);
    }
    
    @Override
    public String getPluralizedWantedCount() {
        return "*at least* " + super.getPluralizedWantedCount();
    }
}
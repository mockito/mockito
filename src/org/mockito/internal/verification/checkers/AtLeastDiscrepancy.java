/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.checkers;

import org.mockito.internal.reporting.Discrepancy;

public class AtLeastDiscrepancy extends Discrepancy {

    public AtLeastDiscrepancy(int wantedCount, int actualCount) {
        super(wantedCount, actualCount);
    }
    
    @Override
    public String getPluralizedWantedCount() {
        return "*at least* " + super.getPluralizedWantedCount();
    }
}
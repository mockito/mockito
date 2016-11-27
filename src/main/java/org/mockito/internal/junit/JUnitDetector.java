/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

class JUnitDetector {

    private boolean hasJUnit;

    JUnitDetector() {
        try {
            Class.forName("junit.framework.ComparisonFailure");
            hasJUnit = true;
        } catch (Throwable t) {
            hasJUnit = false;
        }
    }

    public boolean hasJUnit() {
        return hasJUnit;
    }
}

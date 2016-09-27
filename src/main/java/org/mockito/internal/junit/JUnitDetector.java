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

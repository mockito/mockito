package org.mockito.internal.junit;

class JUnitDetecter {

    private boolean hasJUnit;

    JUnitDetecter() {
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

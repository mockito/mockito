package org.mockito.internal.util;

public class MockitoLoggerImpl implements MockitoLogger {

    /* (non-Javadoc)
     * @see org.mockito.internal.util.Logger#print(java.lang.Object)
     */
    public void println(Object what) {
        System.out.println(what.toString());
    }

    /* (non-Javadoc)
     * @see org.mockito.internal.util.MockitoLogger#println()
     */
    public void println() {
        System.out.println();
    }
}

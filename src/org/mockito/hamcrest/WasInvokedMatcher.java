/**
 * 
 */
package org.mockito.hamcrest;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class WasInvokedMatcher<T> extends BaseMatcher<T> implements MockitoMatcher<T> {
    private final T mock;
    private final int exactNumberOfInvocations;

    public WasInvokedMatcher(T mock) {
        this(mock, -1);
    }
    
    public WasInvokedMatcher(T mock, int exactNumberOfInvocations) {
        this.mock = mock;
        this.exactNumberOfInvocations = exactNumberOfInvocations;
    }

    public boolean matches(Object arg0) {
        return true;
    }

    public void describeTo(Description arg0) {
    }

    public T getMock() {
        return mock;
    }

    public int getExactNumberOfInvocations() {
        return exactNumberOfInvocations;
    }
}
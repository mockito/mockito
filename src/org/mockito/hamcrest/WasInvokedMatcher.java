/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.hamcrest;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class WasInvokedMatcher<T> extends BaseMatcher<T> implements MockitoMatcher<T> {
    private final T mock;
    private final int wantedNumberOfInvocations;

    public WasInvokedMatcher(T mock) {
        this(mock, -1);
    }
    
    public WasInvokedMatcher(T mock, int wantedNumberOfInvocations) {
        this.mock = mock;
        this.wantedNumberOfInvocations = wantedNumberOfInvocations;
    }

    public boolean matches(Object arg0) {
        return true;
    }

    public void describeTo(Description arg0) {
    }

    public T getMock() {
        return mock;
    }

    public int getWantedNumberOfInvocations() {
        return wantedNumberOfInvocations;
    }
}
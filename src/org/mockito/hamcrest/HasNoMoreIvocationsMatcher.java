/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.hamcrest;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class HasNoMoreIvocationsMatcher<T> extends BaseMatcher<T> implements MockitoMatcher<T> {
    
    private final T mock;

    public HasNoMoreIvocationsMatcher(T mock) {
        this.mock = mock;
    }
    
    public boolean matches(Object arg0) {
        return false;
    }

    public void describeTo(Description arg0) {
    };
    
    public T getMock() {
        return mock;
    }

}
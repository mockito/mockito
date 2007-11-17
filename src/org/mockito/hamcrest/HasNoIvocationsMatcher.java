/**
 * 
 */
package org.mockito.hamcrest;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class HasNoIvocationsMatcher<T> extends BaseMatcher<T> implements MockitoMatcher<T> {
    
    private final T mock;

    public HasNoIvocationsMatcher(T mock) {
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
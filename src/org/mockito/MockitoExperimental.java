/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.hamcrest.*;

/**
 * experimental assert-like syntax
 */
public class MockitoExperimental extends Mockito {

    public static <T> T assertInvoked(T mock) {
        return verify(mock);
    }

    public static <T> T assertInvoked(T mock, int wantedNumberOfInvocations) {
        return verify(mock, wantedNumberOfInvocations);
    }
    
    public static void assertNoMoreInteractions(Object ... mocks) {
        verifyNoMoreInteractions(mocks);   
    }

    public static void assertZeroInteractions(Object ... mocks) {
        verifyZeroInteractions(mocks);   
    }

    public static <T> T assertThat(MockitoMatcher<T> matcher) {
        return verify(matcher.getMock());
    }
    
    public static <T> MockitoMatcher<T> wasInvoked(T mock) {
        return new WasInvokedMatcher<T>(mock);
    }
    
    public static <T> MockitoMatcher<T> wasInvoked(T mock, int wantedNumberOfInvocations) {
        return new WasInvokedMatcher<T>(mock, wantedNumberOfInvocations);
    }
    
    public static <T> MockitoMatcher<T> noMoreInteractions(T mock) {
        return new HasNoMoreIvocationsMatcher<T>(mock);
    }
    
    public static <T> MockitoMatcher<T> zeroInteractions(T mock) {
        return new HasNoIvocationsMatcher<T>(mock);
    }
}
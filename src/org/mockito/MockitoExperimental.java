package org.mockito;

import org.mockito.hamcrest.*;

/**
 * experimental assert-like syntax
 * 
 * @author Szczepan Faber
 */
public class MockitoExperimental extends Mockito {

    public static <T> T assertInvoked(T mock) {
        return verify(mock);
    }

    public static <T> T assertInvoked(T mock, int expectedNumberOfInvocations) {
        return verify(mock, expectedNumberOfInvocations);
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
    
    public static <T> MockitoMatcher<T> wasInvoked(T mock, int expectedNumberOfInvocations) {
        return new WasInvokedMatcher<T>(mock, expectedNumberOfInvocations);
    }
    
    public static <T> MockitoMatcher<T> noMoreInteractions(T mock) {
        return new HasNoMoreIvocationsMatcher<T>(mock);
    }
    
    public static <T> MockitoMatcher<T> zeroInteractions(T mock) {
        return new HasNoIvocationsMatcher<T>(mock);
    }
}
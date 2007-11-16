package org.mockito;

import org.easymock.internal.*;
import org.mockito.exceptions.NotAMockMethodException;
import org.mockito.matchers.*;

public class Mockito {

    public static <T> T mock(Class<T> classToMock) {
        try {
            ClassProxyFactory<T> proxyFactory = new ClassProxyFactory<T>();
            return proxyFactory.createProxy(classToMock, new MockitoObjectMethodsFilter<MockitoControl>(
                    classToMock, new MockitoControl<T>(), null));
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public static <T> MockitoExpectation<T> stub(T methodCallToStub) {
        //TODO increment number of stubs
        //TODO stub has to be removed from MockitoOperations.controlToBeStubbed!!!!!!!
        MockitoExpectation controlToStub = MockitoOperations.controlToBeStubbed();
        if (controlToStub == null) {
            throw new NotAMockMethodException();
        }
        return controlToStub;
    }
    
    public static <T> T verify(T mock) {
        MockUtil.validateMock(mock);
        MockitoOperations.reportVerifyingMode(VerifyingMode.anyTimes());
        return mock;
    }
    
    public static <T> T verify(T mock, int exactNumberOfInvocations) {
        //TODO validate mock everywhere
        //TODO validate if there is unfinished stubbing
        MockitoOperations.reportVerifyingMode(VerifyingMode.times(exactNumberOfInvocations));
        return mock;
    }

	public static void verifyNoMoreInteractions(Object ... mocks) {
	    for (Object mock : mocks) {
            MockUtil.getControl(mock).verifyNoMoreInteractions();
        }
	}

    public static void verifyZeroInteractions(Object ... mocks) {
         verifyNoMoreInteractions(mocks);   
    }

    public static <T> T assertInvoked(T mock) {
        return verify(mock);
    }

    public static <T> T assertInvoked(T mock, int exactNumberOfInvocations) {
        return verify(mock, exactNumberOfInvocations);
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
    
    public static <T> MockitoMatcher<T> wasInvoked(T mock, int exactNumberOfInvocations) {
        return new WasInvokedMatcher<T>(mock, exactNumberOfInvocations);
    }
    
    public static <T> MockitoMatcher<T> noMoreInteractions(T mock) {
        return new HasNoMoreIvocationsMatcher<T>(mock);
    }
    
    public static <T> MockitoMatcher<T> zeroInteractions(T mock) {
        return new HasNoIvocationsMatcher<T>(mock);
    }

    public static <T> VoidMethodExpectation<T> stubVoid(T mock) {
        //TODO validate mock
//        MockitoOperations.reportControlForStubbing(mockitoControl)
        return MockUtil.getControl(mock);
    }
}
package org.mockito;

import org.mockito.exceptions.MissingMethodInvocationException;
import org.mockito.hamcrest.*;
import org.mockito.internal.*;

public class Mockito extends Matchers {

    public static <T> T mock(Class<T> classToMock) {
        try {
            ClassProxyFactory<T> proxyFactory = new ClassProxyFactory<T>();
            MockitoControl<T> mockitoControl = new MockitoControl<T>(MockitoState.instance(), LastArguments.instance());
            return proxyFactory.createProxy(classToMock, new MockitoObjectMethodsFilter<MockitoControl>(
                    classToMock, mockitoControl, null));
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public static <T> MockitoExpectation<T> stub(T methodCallToStub) {
//        MockitoState.instance().stubbingStarted();
        
        //TODO increment number of stubs
        //TODO stub has to be removed from MockitoState.instance().controlToBeStubbed!!!!!!!
        MockitoExpectation controlToStub = MockitoState.instance().controlToBeStubbed();
        if (controlToStub == null) {
            throw new MissingMethodInvocationException();
        }
        return controlToStub;
    }
    
    public static <T> T verify(T mock) {
        MockUtil.validateMock(mock);
        MockitoState.instance().verifyingStarted(VerifyingMode.anyTimes());
        return mock;
    }
    
    public static <T> T verify(T mock, int exactNumberOfInvocations) {
        //TODO validate mock everywhere
        //TODO validate if there is unfinished stubbing
        MockitoState.instance().verifyingStarted(VerifyingMode.times(exactNumberOfInvocations));
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
    
    public static <T> VoidMethodExpectation<T> stubVoid(T mock) {
        //TODO validate mock
//        MockitoState.instance().reportControlForStubbing(mockitoControl)
        return MockUtil.getControl(mock);
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
}
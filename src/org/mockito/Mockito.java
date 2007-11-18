package org.mockito;

import org.mockito.exceptions.MissingMethodInvocationException;
import org.mockito.internal.*;

@SuppressWarnings("unchecked")
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
        MockitoExpectation controlToStub = MockitoState.instance().removeControlToBeStubbed();
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
        MockUtil.validateMock(mock);
        MockitoState.instance().verifyingStarted(VerifyingMode.times(exactNumberOfInvocations));
        return mock;
    }

	public static void verifyNoMoreInteractions(Object ... mocks) {
	    MockitoState.instance().checkForUnfinishedVerification();
	    for (Object mock : mocks) {
            MockUtil.getControl(mock).verifyNoMoreInteractions();
        }
	}

    public static void verifyZeroInteractions(Object ... mocks) {
         verifyNoMoreInteractions(mocks);   
    }
    
    public static <T> VoidMethodExpectation<T> stubVoid(T mock) {
//        MockitoState.instance().reportControlForStubbing(mockitoControl)
        return MockUtil.getControl(mock);
    }
}
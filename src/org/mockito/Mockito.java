package org.mockito;

import org.mockito.exceptions.MissingMethodInvocationException;
import org.mockito.internal.*;

@SuppressWarnings("unchecked")
public class Mockito extends Matchers {

    public static <T> T mock(Class<T> classToMock) {
        MockFactory<T> proxyFactory = new MockFactory<T>();
        MockControl<T> mockControl = new MockControl<T>(MockitoState.instance(), LastArguments.instance());
        return proxyFactory.createMock(classToMock, new ObjectMethodsFilter<MockControl>(
                classToMock, mockControl));
    }

    public static <T> MockitoExpectation<T> stub(T methodCallToStub) {
//        MockitoState.instance().stubbingStarted();
        
        //TODO increment number of stubs
        MockitoExpectation controlToStub = MockitoState.instance().pullControlToBeStubbed();
        if (controlToStub == null) {
            throw new MissingMethodInvocationException();
        }
        return controlToStub;
    }
    
    public static <T> T verify(T mock) {
        return verify(mock, VerifyingMode.anyTimes());
    }
    
    public static <T> T verify(T mock, int exactNumberOfInvocations) {
        return verify(mock, VerifyingMode.times(exactNumberOfInvocations));
    }
    
    //TODO should not be public
    public static <T> T verify(T mock, VerifyingMode mode) {
        MockUtil.validateMock(mock);
        MockitoState.instance().verifyingStarted(mode);
        return mock;
    }

	public static void verifyNoMoreInteractions(Object ... mocks) {
	    MockitoState.instance().checkForUnfinishedVerification();
	    for (Object mock : mocks) {
            MockUtil.getControl(mock).verifyNoMoreInteractions();
        }
	}

    public static void verifyZeroInteractions(Object ... mocks) {
        MockitoState.instance().checkForUnfinishedVerification();
        for (Object mock : mocks) {
            MockUtil.getControl(mock).verifyZeroInteractions();
        }
    }
    
    public static <T> VoidMethodExpectation<T> stubVoid(T mock) {
//        MockitoState.instance().reportControlForStubbing(mockControl)
        return MockUtil.getControl(mock);
    }

    public static StrictOrderVerifier strictOrderVerifier(Object ... mocks) {
        StrictOrderVerifier strictOrderVerifier = new StrictOrderVerifier();
        for (Object mock : mocks) {
            MockUtil.validateMock(mock);
            strictOrderVerifier.addMock(mock);
        }
        return strictOrderVerifier;
    }
}
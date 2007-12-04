/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.exceptions.*;
import org.mockito.internal.*;

@SuppressWarnings("unchecked")
public class Mockito extends Matchers {

    public static VerifyingMode atLeastOnce() {
        return VerifyingMode.atLeastOnce();
    }
    
    public static <T> T mock(Class<T> classToMock) {
        MockFactory<T> proxyFactory = new MockFactory<T>();
        MockControl<T> mockControl = new MockControl<T>();
        return proxyFactory.createMock(classToMock, new ObjectMethodsFilter<MockControl>(
                classToMock, mockControl));
    }

    public static <T> MockitoExpectation<T> stub(T methodCallToStub) {
        MockitoState.instance().stubbingStarted();
        
        MockitoExpectation controlToStub = MockitoState.instance().pullControlToBeStubbed();
        if (controlToStub == null) {
            Exceptions.missingMethodInvocation();
        }
        return controlToStub;
    }
    
    public static <T> T verify(T mock) {
        return verify(mock, 1);
    }
    
    public static <T> T verify(T mock, int wantedNumberOfInvocations) {
        return verify(mock, VerifyingMode.times(wantedNumberOfInvocations));
    }
    
    public static <T> T verify(T mock, VerifyingMode mode) {
        MockUtil.validateMock(mock);
        MockitoState.instance().verifyingStarted(mode);
        return mock;
    }

	/**
	 * Throws an AssertionError if any of given mocks has any unverified interaction.
     * <p>
     * Use this method after you verified all your mocks - to make sure that nothing 
     * else was invoked on your mocks.
     * <p>
     * It's a good pattern not to use this method in every test method.
     * Test methods should focus on different behavior/interaction 
     * and it's not necessary to call verifyNoMoreInteractions() all the time
     * <p>
     * Stubbed invocations are also treated as interactions.
     * <p>
     * Example:
     * <pre>
     *     //interactions
     *     mock.doSomething();
     *     mock.doSomethingUnexpected();
     *     
     *     //verification
     *     verify(mock).doSomething();
     *     
     *     verifyNoMoreInteractions(mock);
     *     //oups: 'doSomethingUnexpected()' is unexpected
	 *</pre>
	 *
	 * @param mocks
	 */
	public static void verifyNoMoreInteractions(Object ... mocks) {
	    assertMocksNotEmpty(mocks);
	    MockitoState.instance().validateState();
	    for (Object mock : mocks) {
            MockUtil.getControl(mock).verifyNoMoreInteractions();
        }
	}

    private static void assertMocksNotEmpty(Object[] mocks) {
        if (mocks.length == 0) {
            Exceptions.mocksHaveToBePassedAsArguments();
        }
    }

    public static void verifyZeroInteractions(Object ... mocks) {
        assertMocksNotEmpty(mocks);
        MockitoState.instance().validateState();
        for (Object mock : mocks) {
            MockUtil.getControl(mock).verifyZeroInteractions();
        }
    }
    
    public static <T> VoidMethodExpectation<T> stubVoid(T mock) {
        MockitoState.instance().stubbingStarted();
        return MockUtil.getControl(mock);
    }

    public static Strictly createStrictOrderVerifier(Object ... mocks) {
        if (mocks.length == 0) {
            Exceptions.mocksHaveToBePassedWhenCreatingStrictly();
        }
        StrictOrderVerifier strictOrderVerifier = new StrictOrderVerifier();
        for (Object mock : mocks) {
            MockUtil.validateMock(mock);
            strictOrderVerifier.addMockToBeVerifiedInOrder(mock);
        }
        return strictOrderVerifier;
    }
}
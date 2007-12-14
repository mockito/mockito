/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.exceptions.*;
import org.mockito.internal.*;
import org.mockito.internal.creation.MockFactory;
import org.mockito.internal.creation.ObjectMethodsFilter;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.*;
import org.mockito.internal.stubbing.VoidMethodStubable;

@SuppressWarnings("unchecked")
public class Mockito extends Matchers {
    
    private static final Reporter reporter = new Reporter();
    
    static MockingProgress mockingProgress = new ThreadSafeMockingProgress();
    
    public static VerificationMode atLeastOnce() {
        return VerificationMode.atLeastOnce();
    }
    
    public static <T> T mock(Class<T> classToMock) {
        MockFactory<T> proxyFactory = new MockFactory<T>();
        MockControl<T> mockControl = new MockControl<T>(mockingProgress, new MatchersBinder());
        return proxyFactory.createMock(classToMock, new ObjectMethodsFilter<MockControl>(
                classToMock, mockControl));
    }

    public static <T> OngoingStubbing<T> stub(T methodCallToStub) {
        mockingProgress.stubbingStarted();
        
        OngoingStubbing stubable = mockingProgress.pullStubable();
        if (stubable == null) {
            reporter.missingMethodInvocation();
        }
        return stubable;
    }
    
    public static <T> T verify(T mock) {
        return verify(mock, 1);
    }
    
    public static <T> T verify(T mock, int wantedNumberOfInvocations) {
        return verify(mock, VerificationMode.times(wantedNumberOfInvocations));
    }
    
    public static <T> T verify(T mock, VerificationMode mode) {
        MockUtil.validateMock(mock);
        mockingProgress.verificationStarted(mode);
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
	    mockingProgress.validateState();
	    for (Object mock : mocks) {
            MockUtil.getControl(mock).verifyNoMoreInteractions();
        }
	}

    private static void assertMocksNotEmpty(Object[] mocks) {
        if (mocks.length == 0) {
            reporter.mocksHaveToBePassedAsArguments();
        }
    }

    public static void verifyZeroInteractions(Object ... mocks) {
        assertMocksNotEmpty(mocks);
        mockingProgress.validateState();
        for (Object mock : mocks) {
            MockUtil.getControl(mock).verifyZeroInteractions();
        }
    }
    
    public static <T> VoidMethodStubable<T> stubVoid(T mock) {
        MockControl<T> control = MockUtil.getControl(mock);
        mockingProgress.stubbingStarted();
        return control;
    }

    public static Strictly createStrictOrderVerifier(Object ... mocks) {
        if (mocks.length == 0) {
            reporter.mocksHaveToBePassedWhenCreatingStrictly();
        }
        StrictOrderVerifier strictOrderVerifier = new StrictOrderVerifier();
        for (Object mock : mocks) {
            MockUtil.validateMock(mock);
            strictOrderVerifier.addMockToBeVerifiedStrictly(mock);
        }
        return strictOrderVerifier;
    }
}
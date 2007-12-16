/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockUtil;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.OngoingStubbing;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.progress.VerificationMode;
import org.mockito.internal.stubbing.VoidMethodStubbable;

@SuppressWarnings("unchecked")
public class Mockito extends Matchers {

    private static final Reporter REPORTER = new Reporter();
    static final MockingProgress MOCKING_PROGRESS = new ThreadSafeMockingProgress();

    public static <T> T mock(Class<T> classToMock) {
        return MockUtil.createMock(classToMock, MOCKING_PROGRESS);
    }

    public static <T> OngoingStubbing<T> stub(T methodCallToStub) {
        MOCKING_PROGRESS.stubbingStarted();

        OngoingStubbing stubbable = MOCKING_PROGRESS.pullStubbable();
        if (stubbable == null) {
            REPORTER.missingMethodInvocation();
        }
        return stubbable;
    }

    public static <T> T verify(T mock) {
        return verify(mock, times(1));
    }

    public static <T> T verify(T mock, VerificationMode mode) {
        MockUtil.validateMock(mock);
        MOCKING_PROGRESS.verificationStarted(mode);
        return mock;
    }

    /**
     * Throws an AssertionError if any of given mocks has any unverified interaction.
     * <p>
     * Use this method after you verified your mocks - to make sure that nothing
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
        MOCKING_PROGRESS.validateState();
        for (Object mock : mocks) {
            MockUtil.getMockHandler(mock).verifyNoMoreInteractions();
        }
    }

    public static void verifyZeroInteractions(Object ... mocks) {
        verifyNoMoreInteractions(mocks);
    }

    private static void assertMocksNotEmpty(Object[] mocks) {
        if (mocks.length == 0) {
            REPORTER.mocksHaveToBePassedAsArguments();
        }
    }

    public static <T> VoidMethodStubbable<T> stubVoid(T mock) {
        MockHandler<T> handler = MockUtil.getMockHandler(mock);
        MOCKING_PROGRESS.stubbingStarted();
        return handler;
    }

    public static Strictly createStrictOrderVerifier(Object ... mocks) {
        if (mocks.length == 0) {
            REPORTER.mocksHaveToBePassedWhenCreatingStrictly();
        }
        StrictOrderVerifier strictOrderVerifier = new StrictOrderVerifier();
        for (Object mock : mocks) {
            MockUtil.validateMock(mock);
            strictOrderVerifier.addMockToBeVerifiedStrictly(mock);
        }
        return strictOrderVerifier;
    }

    public static VerificationMode atLeastOnce() {
        return VerificationMode.atLeastOnce();
    }

    public static VerificationMode times(int wantedNumberOfInvocations) {
        return VerificationMode.times(wantedNumberOfInvocations);
    }
}
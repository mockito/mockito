/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.util.Arrays;

import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.ReturnValues;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.progress.DeprecatedOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.NewOngoingStubbing;
import org.mockito.internal.progress.OngoingStubbing;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.Stubber;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.stubbing.Answer;

@SuppressWarnings("unchecked")
public class MockitoCore {

    private static final Reporter REPORTER = new Reporter();
    public static final MockingProgress MOCKING_PROGRESS = new ThreadSafeMockingProgress();
    
    public <T> T mock(Class<T> classToMock, String name, T optionalInstance, ReturnValues returnValues) {
        MOCKING_PROGRESS.validateState();
        MOCKING_PROGRESS.resetOngoingStubbing();
        return MockUtil.createMock(classToMock, MOCKING_PROGRESS, name, optionalInstance, returnValues);
    }
    
    public OngoingStubbing stub() {
        OngoingStubbing stubbing = MOCKING_PROGRESS.pullOngoingStubbing();
        if (stubbing == null) {
            MOCKING_PROGRESS.reset();
            REPORTER.missingMethodInvocation();
        }
        return stubbing;
    }

    @Deprecated
    public <T> DeprecatedOngoingStubbing<T> stub(T methodCall) {
        MOCKING_PROGRESS.stubbingStarted();
        return (DeprecatedOngoingStubbing) stub();
    }

    public <T> NewOngoingStubbing<T> when(T methodCall) {
        MOCKING_PROGRESS.stubbingStarted();
        return (NewOngoingStubbing) stub();
    }
    
    
    public <T> T verify(T mock, VerificationMode mode) {
        if (mock == null) {
            REPORTER.nullPassedToVerify();
        } else if (!MockUtil.isMock(mock)) {
            REPORTER.notAMockPassedToVerify();
        }
        MOCKING_PROGRESS.verificationStarted(mode);
        return mock;
    }
    
    public <T> void reset(T ... mocks) {
        //TODO Perhaps we should validate the state instead of resetting?
        MOCKING_PROGRESS.reset();
        MOCKING_PROGRESS.resetOngoingStubbing();
        //TODO Perhaps we should maintain previous ReturnValues?
        
        for (T m : mocks) {
            MockUtil.resetMock(m, MOCKING_PROGRESS, Mockito.RETURNS_DEFAULTS);
        }
    }
    
    public void verifyNoMoreInteractions(Object... mocks) {
        assertMocksNotEmpty(mocks);
        MOCKING_PROGRESS.validateState();
        for (Object mock : mocks) {
            try {
                if (mock == null) {
                    REPORTER.nullPassedToVerifyNoMoreInteractions();
                }
                MockUtil.getMockHandler(mock).verifyNoMoreInteractions();
            } catch (NotAMockException e) {
                REPORTER.notAMockPassedToVerifyNoMoreInteractions();
            }
        }
    }
    
    public void assertMocksNotEmpty(Object[] mocks) {
        if (mocks == null || mocks.length == 0) {
            REPORTER.mocksHaveToBePassedToVerifyNoMoreInteractions();
        }
    }
    
    public InOrder inOrder(Object... mocks) {
        if (mocks == null || mocks.length == 0) {
            REPORTER.mocksHaveToBePassedWhenCreatingInOrder();
        }
        for (Object mock : mocks) {
            if (mock == null) {
                REPORTER.nullPassedWhenCreatingInOrder();
            } else if (!MockUtil.isMock(mock)) {
                REPORTER.notAMockPassedWhenCreatingInOrder();
            }
        }
        InOrder inOrder = new InOrderImpl(Arrays.asList(mocks));
        return inOrder;
    }
    
    public Stubber doAnswer(Answer answer) {
        MOCKING_PROGRESS.stubbingStarted();
        MOCKING_PROGRESS.resetOngoingStubbing();
        return new StubberImpl().doAnswer(answer);
    }
    
    public <T> VoidMethodStubbable<T> stubVoid(T mock) {
        MockHandler<T> handler = MockUtil.getMockHandler(mock);
        MOCKING_PROGRESS.stubbingStarted();
        return handler.voidMethodStubbable(mock);
    }

}

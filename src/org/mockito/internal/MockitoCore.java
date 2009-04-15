/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.util.Arrays;

import org.mockito.InOrder;
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

    private final Reporter reporter = new Reporter();
    private final MockingProgress mockingProgress = new ThreadSafeMockingProgress();
    
    public <T> T mock(Class<T> classToMock, String name, T optionalInstance, ReturnValues returnValues) {
        mockingProgress.validateState();
        mockingProgress.resetOngoingStubbing();
        return MockUtil.createMock(classToMock, mockingProgress, name, optionalInstance, returnValues);
    }
    
    public OngoingStubbing stub() {
        OngoingStubbing stubbing = mockingProgress.pullOngoingStubbing();
        if (stubbing == null) {
            mockingProgress.reset();
            reporter.missingMethodInvocation();
        }
        return stubbing;
    }

    @Deprecated
    public <T> DeprecatedOngoingStubbing<T> stub(T methodCall) {
        mockingProgress.stubbingStarted();
        return (DeprecatedOngoingStubbing) stub();
    }

    public <T> NewOngoingStubbing<T> when(T methodCall) {
        mockingProgress.stubbingStarted();
        return (NewOngoingStubbing) stub();
    }
    
    
    public <T> T verify(T mock, VerificationMode mode) {
        if (mock == null) {
            reporter.nullPassedToVerify();
        } else if (!MockUtil.isMock(mock)) {
            reporter.notAMockPassedToVerify();
        }
        mockingProgress.verificationStarted(mode);
        return mock;
    }
    
    public <T> void reset(T ... mocks) {
        mockingProgress.validateState();
        mockingProgress.reset();
        mockingProgress.resetOngoingStubbing();
        
        for (T m : mocks) {
            MockUtil.resetMock(m, mockingProgress);
        }
    }
    
    public void verifyNoMoreInteractions(Object... mocks) {
        assertMocksNotEmpty(mocks);
        mockingProgress.validateState();
        for (Object mock : mocks) {
            try {
                if (mock == null) {
                    reporter.nullPassedToVerifyNoMoreInteractions();
                }
                MockUtil.getMockHandler(mock).verifyNoMoreInteractions();
            } catch (NotAMockException e) {
                reporter.notAMockPassedToVerifyNoMoreInteractions();
            }
        }
    }
    
    public void assertMocksNotEmpty(Object[] mocks) {
        if (mocks == null || mocks.length == 0) {
            reporter.mocksHaveToBePassedToVerifyNoMoreInteractions();
        }
    }
    
    public InOrder inOrder(Object... mocks) {
        if (mocks == null || mocks.length == 0) {
            reporter.mocksHaveToBePassedWhenCreatingInOrder();
        }
        for (Object mock : mocks) {
            if (mock == null) {
                reporter.nullPassedWhenCreatingInOrder();
            } else if (!MockUtil.isMock(mock)) {
                reporter.notAMockPassedWhenCreatingInOrder();
            }
        }
        InOrder inOrder = new InOrderImpl(Arrays.asList(mocks));
        return inOrder;
    }
    
    public Stubber doAnswer(Answer answer) {
        mockingProgress.stubbingStarted();
        mockingProgress.resetOngoingStubbing();
        return new StubberImpl().doAnswer(answer);
    }
    
    public <T> VoidMethodStubbable<T> stubVoid(T mock) {
        MockHandler<T> handler = MockUtil.getMockHandler(mock);
        mockingProgress.stubbingStarted();
        return handler.voidMethodStubbable(mock);
    }
}
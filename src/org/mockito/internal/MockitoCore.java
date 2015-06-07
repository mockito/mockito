/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.util.Arrays;
import java.util.List;

import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.MockingDetails;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.finder.VerifiableInvocationsFinder;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.util.DefaultMockingDetails;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationDataInOrderImpl;
import org.mockito.invocation.Invocation;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

@SuppressWarnings({"unchecked", "rawtypes"})
public class MockitoCore {

    private final Reporter reporter = new Reporter();
    private final MockUtil mockUtil = new MockUtil();
    private final MockingProgress mockingProgress = new ThreadSafeMockingProgress();

    public boolean isTypeMockable(final Class<?> typeToMock) {
        return mockUtil.isTypeMockable(typeToMock);
    }

    public <T> T mock(final Class<T> typeToMock, final MockSettings settings) {
        if (!MockSettingsImpl.class.isInstance(settings)) {
            throw new IllegalArgumentException(
                    "Unexpected implementation of '" + settings.getClass().getCanonicalName() + "'\n"
                    + "At the moment, you cannot provide your own implementations that class.");
        }
        final MockSettingsImpl impl = MockSettingsImpl.class.cast(settings);
        final MockCreationSettings<T> creationSettings = impl.confirm(typeToMock);
        final T mock = mockUtil.createMock(creationSettings);
        mockingProgress.mockingStarted(mock, typeToMock);
        return mock;
    }
    
    public IOngoingStubbing stub() {
        final IOngoingStubbing stubbing = mockingProgress.pullOngoingStubbing();
        if (stubbing == null) {
            mockingProgress.reset();
            reporter.missingMethodInvocation();
        }
        return stubbing;
    }

    public <T> DeprecatedOngoingStubbing<T> stub(final T methodCall) {
        mockingProgress.stubbingStarted();
        return (DeprecatedOngoingStubbing) stub();
    }

    public <T> OngoingStubbing<T> when(final T methodCall) {
        mockingProgress.stubbingStarted();
        return (OngoingStubbing) stub();
    }
    
    public <T> T verify(final T mock, final VerificationMode mode) {
        if (mock == null) {
            reporter.nullPassedToVerify();
        } else if (!mockUtil.isMock(mock)) {
            reporter.notAMockPassedToVerify(mock.getClass());
        }
        mockingProgress.verificationStarted(new MockAwareVerificationMode(mock, mode));
        return mock;
    }
    
    public <T> void reset(final T ... mocks) {
        mockingProgress.validateState();
        mockingProgress.reset();
        mockingProgress.resetOngoingStubbing();
        
        for (final T m : mocks) {
            mockUtil.resetMock(m);
        }
    }
    
    public void verifyNoMoreInteractions(final Object... mocks) {
        assertMocksNotEmpty(mocks);
        mockingProgress.validateState();
        for (final Object mock : mocks) {
            try {
                if (mock == null) {
                    reporter.nullPassedToVerifyNoMoreInteractions();
                }
                final InvocationContainer invocations = mockUtil.getMockHandler(mock).getInvocationContainer();
                final VerificationDataImpl data = new VerificationDataImpl(invocations, null);
                VerificationModeFactory.noMoreInteractions().verify(data);
            } catch (final NotAMockException e) {
                reporter.notAMockPassedToVerifyNoMoreInteractions();
            }
        }
    }

    public void verifyNoMoreInteractionsInOrder(final List<Object> mocks, final InOrderContext inOrderContext) {
        mockingProgress.validateState();
        final VerifiableInvocationsFinder finder = new VerifiableInvocationsFinder();
        final VerificationDataInOrder data = new VerificationDataInOrderImpl(inOrderContext, finder.find(mocks), null);
        VerificationModeFactory.noMoreInteractions().verifyInOrder(data);
    }    
    
    private void assertMocksNotEmpty(final Object[] mocks) {
        if (mocks == null || mocks.length == 0) {
            reporter.mocksHaveToBePassedToVerifyNoMoreInteractions();
        }
    }
    
    public InOrder inOrder(final Object... mocks) {
        if (mocks == null || mocks.length == 0) {
            reporter.mocksHaveToBePassedWhenCreatingInOrder();
        }
        for (final Object mock : mocks) {
            if (mock == null) {
                reporter.nullPassedWhenCreatingInOrder();
            } else if (!mockUtil.isMock(mock)) {
                reporter.notAMockPassedWhenCreatingInOrder();
            }
        }
        return new InOrderImpl(Arrays.asList(mocks));
    }
    
    public Stubber doAnswer(final Answer answer) {
        mockingProgress.stubbingStarted();
        mockingProgress.resetOngoingStubbing();
        return new StubberImpl().doAnswer(answer);
    }
    
    public <T> VoidMethodStubbable<T> stubVoid(final T mock) {
        final InternalMockHandler<T> handler = mockUtil.getMockHandler(mock);
        mockingProgress.stubbingStarted();
        return handler.voidMethodStubbable(mock);
    }

    public void validateMockitoUsage() {
        mockingProgress.validateState();
    }

    /**
     * For testing purposes only. Is not the part of main API.
     * @return last invocation
     */
    public Invocation getLastInvocation() {
        final OngoingStubbingImpl ongoingStubbing = ((OngoingStubbingImpl) mockingProgress.pullOngoingStubbing());
        final List<Invocation> allInvocations = ongoingStubbing.getRegisteredInvocations();
        return allInvocations.get(allInvocations.size()-1);
    }

    public Object[] ignoreStubs(final Object... mocks) {
        for (final Object m : mocks) {
            final InvocationContainer invocationContainer = new MockUtil().getMockHandler(m).getInvocationContainer();
            final List<Invocation> ins = invocationContainer.getInvocations();
            for (final Invocation in : ins) {
                if (in.stubInfo() != null) {
                    in.ignoreForVerification();
                }
            }
        }
        return mocks;
    }

    public MockingDetails mockingDetails(final Object toInspect) {
        return new DefaultMockingDetails(toInspect, new MockUtil());
    }
}
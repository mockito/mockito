/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.mockito.internal.exceptions.Reporter.*;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;
import static org.mockito.internal.util.MockUtil.createMock;
import static org.mockito.internal.util.MockUtil.createStaticMock;
import static org.mockito.internal.util.MockUtil.getInvocationContainer;
import static org.mockito.internal.util.MockUtil.getMockHandler;
import static org.mockito.internal.util.MockUtil.isMock;
import static org.mockito.internal.util.MockUtil.resetMock;
import static org.mockito.internal.util.MockUtil.typeMockabilityOf;
import static org.mockito.internal.verification.VerificationModeFactory.noInteractions;
import static org.mockito.internal.verification.VerificationModeFactory.noMoreInteractions;

import java.util.Arrays;
import java.util.List;

import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.MockedStatic;
import org.mockito.MockingDetails;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.finder.VerifiableInvocationsFinder;
import org.mockito.internal.listeners.VerificationStartedNotifier;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.stubbing.DefaultLenientStubber;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.util.DefaultMockingDetails;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationDataInOrderImpl;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.LenientStubber;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationMode;

@SuppressWarnings("unchecked")
public class MockitoCore {

    public boolean isTypeMockable(Class<?> typeToMock) {
        return typeMockabilityOf(typeToMock).mockable();
    }

    public <T> T mock(Class<T> typeToMock, MockSettings settings) {
        if (!MockSettingsImpl.class.isInstance(settings)) {
            throw new IllegalArgumentException(
                    "Unexpected implementation of '"
                            + settings.getClass().getCanonicalName()
                            + "'\n"
                            + "At the moment, you cannot provide your own implementations of that class.");
        }
        MockSettingsImpl impl = MockSettingsImpl.class.cast(settings);
        MockCreationSettings<T> creationSettings = impl.build(typeToMock);
        T mock = createMock(creationSettings);
        mockingProgress().mockingStarted(mock, creationSettings);
        return mock;
    }

    public <T> MockedStatic<T> mockStatic(Class<T> classToMock, MockSettings settings) {
        if (!MockSettingsImpl.class.isInstance(settings)) {
            throw new IllegalArgumentException(
                    "Unexpected implementation of '"
                            + settings.getClass().getCanonicalName()
                            + "'\n"
                            + "At the moment, you cannot provide your own implementations of that class.");
        }
        MockSettingsImpl impl = MockSettingsImpl.class.cast(settings);
        MockCreationSettings<T> creationSettings = impl.buildStatic(classToMock);
        MockMaker.StaticMockControl<T> control = createStaticMock(classToMock, creationSettings);
        control.enable();
        mockingProgress().mockingStarted(classToMock, creationSettings);
        return new MockedStaticImpl<>(control);
    }

    public <T> OngoingStubbing<T> when(T methodCall) {
        MockingProgress mockingProgress = mockingProgress();
        mockingProgress.stubbingStarted();
        @SuppressWarnings("unchecked")
        OngoingStubbing<T> stubbing = (OngoingStubbing<T>) mockingProgress.pullOngoingStubbing();
        if (stubbing == null) {
            mockingProgress.reset();
            throw missingMethodInvocation();
        }
        return stubbing;
    }

    public <T> T verify(T mock, VerificationMode mode) {
        if (mock == null) {
            throw nullPassedToVerify();
        }
        MockingDetails mockingDetails = mockingDetails(mock);
        if (!mockingDetails.isMock()) {
            throw notAMockPassedToVerify(mock.getClass());
        }
        assertNotStubOnlyMock(mock);
        MockHandler handler = mockingDetails.getMockHandler();
        mock =
                (T)
                        VerificationStartedNotifier.notifyVerificationStarted(
                                handler.getMockSettings().getVerificationStartedListeners(),
                                mockingDetails);

        MockingProgress mockingProgress = mockingProgress();
        VerificationMode actualMode = mockingProgress.maybeVerifyLazily(mode);
        mockingProgress.verificationStarted(
                new MockAwareVerificationMode(
                        mock, actualMode, mockingProgress.verificationListeners()));
        return mock;
    }

    public <T> void reset(T... mocks) {
        MockingProgress mockingProgress = mockingProgress();
        mockingProgress.validateState();
        mockingProgress.reset();
        mockingProgress.resetOngoingStubbing();

        for (T m : mocks) {
            resetMock(m);
        }
    }

    public <T> void clearInvocations(T... mocks) {
        MockingProgress mockingProgress = mockingProgress();
        mockingProgress.validateState();
        mockingProgress.reset();
        mockingProgress.resetOngoingStubbing();

        for (T m : mocks) {
            getInvocationContainer(m).clearInvocations();
        }
    }

    public void verifyNoMoreInteractions(Object... mocks) {
        assertMocksNotEmpty(mocks);
        mockingProgress().validateState();
        for (Object mock : mocks) {
            try {
                if (mock == null) {
                    throw nullPassedToVerifyNoMoreInteractions();
                }
                InvocationContainerImpl invocations = getInvocationContainer(mock);
                assertNotStubOnlyMock(mock);
                VerificationDataImpl data = new VerificationDataImpl(invocations, null);
                noMoreInteractions().verify(data);
            } catch (NotAMockException e) {
                throw notAMockPassedToVerifyNoMoreInteractions();
            }
        }
    }

    public void verifyNoInteractions(Object... mocks) {
        assertMocksNotEmpty(mocks);
        mockingProgress().validateState();
        for (Object mock : mocks) {
            try {
                if (mock == null) {
                    throw nullPassedToVerifyNoMoreInteractions();
                }
                InvocationContainerImpl invocations = getInvocationContainer(mock);
                assertNotStubOnlyMock(mock);
                VerificationDataImpl data = new VerificationDataImpl(invocations, null);
                noInteractions().verify(data);
            } catch (NotAMockException e) {
                throw notAMockPassedToVerifyNoMoreInteractions();
            }
        }
    }

    public void verifyNoMoreInteractionsInOrder(List<Object> mocks, InOrderContext inOrderContext) {
        mockingProgress().validateState();
        VerificationDataInOrder data =
                new VerificationDataInOrderImpl(
                        inOrderContext, VerifiableInvocationsFinder.find(mocks), null);
        VerificationModeFactory.noMoreInteractions().verifyInOrder(data);
    }

    private void assertMocksNotEmpty(Object[] mocks) {
        if (mocks == null || mocks.length == 0) {
            throw mocksHaveToBePassedToVerifyNoMoreInteractions();
        }
    }

    private void assertNotStubOnlyMock(Object mock) {
        if (getMockHandler(mock).getMockSettings().isStubOnly()) {
            throw stubPassedToVerify(mock);
        }
    }

    public InOrder inOrder(Object... mocks) {
        if (mocks == null || mocks.length == 0) {
            throw mocksHaveToBePassedWhenCreatingInOrder();
        }
        for (Object mock : mocks) {
            if (mock == null) {
                throw nullPassedWhenCreatingInOrder();
            }
            if (!isMock(mock)) {
                throw notAMockPassedWhenCreatingInOrder();
            }
            assertNotStubOnlyMock(mock);
        }
        return new InOrderImpl(Arrays.asList(mocks));
    }

    public Stubber stubber() {
        return stubber(null);
    }

    public Stubber stubber(Strictness strictness) {
        MockingProgress mockingProgress = mockingProgress();
        mockingProgress.stubbingStarted();
        mockingProgress.resetOngoingStubbing();
        return new StubberImpl(strictness);
    }

    public void validateMockitoUsage() {
        mockingProgress().validateState();
    }

    /**
     * For testing purposes only. Is not the part of main API.
     *
     * @return last invocation
     */
    public Invocation getLastInvocation() {
        OngoingStubbingImpl ongoingStubbing =
                ((OngoingStubbingImpl) mockingProgress().pullOngoingStubbing());
        List<Invocation> allInvocations = ongoingStubbing.getRegisteredInvocations();
        return allInvocations.get(allInvocations.size() - 1);
    }

    public Object[] ignoreStubs(Object... mocks) {
        for (Object m : mocks) {
            InvocationContainerImpl container = getInvocationContainer(m);
            List<Invocation> ins = container.getInvocations();
            for (Invocation in : ins) {
                if (in.stubInfo() != null) {
                    in.ignoreForVerification();
                }
            }
        }
        return mocks;
    }

    public MockingDetails mockingDetails(Object toInspect) {
        return new DefaultMockingDetails(toInspect);
    }

    public LenientStubber lenient() {
        return new DefaultLenientStubber();
    }
}

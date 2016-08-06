/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.mockito.internal.exceptions.Reporter.missingMethodInvocation;
import static org.mockito.internal.exceptions.Reporter.mocksHaveToBePassedToVerifyNoMoreInteractions;
import static org.mockito.internal.exceptions.Reporter.mocksHaveToBePassedWhenCreatingInOrder;
import static org.mockito.internal.exceptions.Reporter.notAMockPassedToVerify;
import static org.mockito.internal.exceptions.Reporter.notAMockPassedToVerifyNoMoreInteractions;
import static org.mockito.internal.exceptions.Reporter.notAMockPassedWhenCreatingInOrder;
import static org.mockito.internal.exceptions.Reporter.nullPassedToVerify;
import static org.mockito.internal.exceptions.Reporter.nullPassedToVerifyNoMoreInteractions;
import static org.mockito.internal.exceptions.Reporter.nullPassedWhenCreatingInOrder;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;
import static org.mockito.internal.util.MockUtil.createMock;
import static org.mockito.internal.util.MockUtil.getMockHandler;
import static org.mockito.internal.util.MockUtil.isMock;
import static org.mockito.internal.util.MockUtil.resetMock;
import static org.mockito.internal.util.MockUtil.typeMockabilityOf;
import static org.mockito.internal.verification.VerificationModeFactory.noMoreInteractions;

import java.util.Arrays;
import java.util.List;
import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.MockingDetails;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.finder.VerifiableInvocationsFinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
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
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationMode;

public  class MockitoCore {

    private MockitoCore(){
    }
    
    
    public  static boolean isTypeMockable(Class<?> typeToMock) {
        return typeMockabilityOf(typeToMock).mockable();
    }

    public  static <T> T mock(Class<T> typeToMock, MockSettings settings) {
        if (!MockSettingsImpl.class.isInstance(settings)) {
            throw new IllegalArgumentException("Unexpected implementation of '" + settings.getClass().getCanonicalName() + "'\n" + "At the moment, you cannot provide your own implementations of that class.");
        }
        MockSettingsImpl impl = MockSettingsImpl.class.cast(settings);
        MockCreationSettings<T> creationSettings = impl.confirm(typeToMock);
        T mock = createMock(creationSettings);
        mockingProgress().mockingStarted(mock, typeToMock);
        return mock;
    }

    public  static <T> OngoingStubbing<T> when(T methodCall) {
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

    public  static <T> T verify(T mock, VerificationMode mode) {
        if (mock == null) {
            throw nullPassedToVerify();
        }
        if (!isMock(mock)) {
            throw notAMockPassedToVerify(mock.getClass());
        }
        MockingProgress mockingProgress = mockingProgress();
        VerificationMode actualMode = mockingProgress.maybeVerifyLazily(mode);
        mockingProgress.verificationStarted(new MockAwareVerificationMode(mock, actualMode));
        return mock;
    }

    public  static <T> void reset(T... mocks) {
        MockingProgress mockingProgress = mockingProgress();
        mockingProgress.validateState();
        mockingProgress.reset();
        mockingProgress.resetOngoingStubbing();

        for (T m : mocks) {
            resetMock(m);
        }
    }

    public  static <T> void clearInvocations(T... mocks) {
        MockingProgress mockingProgress = mockingProgress();
        mockingProgress.validateState();
        mockingProgress.reset();
        mockingProgress.resetOngoingStubbing();

        for (T m : mocks) {
            getMockHandler(m).getInvocationContainer().clearInvocations();
        }
    }

    public  static void verifyNoMoreInteractions(Object... mocks) {
        assertMocksNotEmpty(mocks);
        mockingProgress().validateState();
        for (Object mock : mocks) {
            try {
                if (mock == null) {
                    throw nullPassedToVerifyNoMoreInteractions();
                }
                InvocationContainer invocations = getMockHandler(mock).getInvocationContainer();
                VerificationDataImpl data = new VerificationDataImpl(invocations, null);
                noMoreInteractions().verify(data);
            } catch (NotAMockException e) {
                throw notAMockPassedToVerifyNoMoreInteractions();
            }
        }
    }

    public  static void verifyNoMoreInteractionsInOrder(List<Object> mocks, InOrderContext inOrderContext) {
        mockingProgress().validateState();
        VerificationDataInOrder data = new VerificationDataInOrderImpl(inOrderContext, VerifiableInvocationsFinder.find(mocks), null);
        VerificationModeFactory.noMoreInteractions().verifyInOrder(data);
    }

    private static void assertMocksNotEmpty(Object[] mocks) {
        if (mocks == null || mocks.length == 0) {
            throw mocksHaveToBePassedToVerifyNoMoreInteractions();
        }
    }

    public  static InOrder inOrder(Object... mocks) {
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
        }
        return new InOrderImpl(Arrays.asList(mocks));
    }

    public  static Stubber stubber() {
        MockingProgress mockingProgress = mockingProgress();
        mockingProgress.stubbingStarted();
        mockingProgress.resetOngoingStubbing();
        return new StubberImpl();
    }

    public  static void validateMockitoUsage() {
        mockingProgress().validateState();
    }

    /**
     * For testing purposes only. Is not the part of main API.
     * 
     * @return last invocation
     */
    public  static Invocation getLastInvocation() {
        OngoingStubbingImpl ongoingStubbing = ((OngoingStubbingImpl) mockingProgress().pullOngoingStubbing());
        List<Invocation> allInvocations = ongoingStubbing.getRegisteredInvocations();
        return allInvocations.get(allInvocations.size() - 1);
    }

    public  static Object[] ignoreStubs(Object... mocks) {
        for (Object m : mocks) {
            InvocationContainer invocationContainer = getMockHandler(m).getInvocationContainer();
            List<Invocation> ins = invocationContainer.getInvocations();
            for (Invocation in : ins) {
                if (in.stubInfo() != null) {
                    in.ignoreForVerification();
                }
            }
        }
        return mocks;
    }

    public  static MockingDetails mockingDetails(Object toInspect) {
        return new DefaultMockingDetails(toInspect);
    }
}

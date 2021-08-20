/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.mockito.internal.exceptions.Reporter.inOrderRequiresFamiliarMock;

import java.util.ArrayList;
import java.util.List;

import org.mockito.InOrder;
import org.mockito.MockingDetails;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.verification.InOrderContextImpl;
import org.mockito.internal.verification.InOrderWrapper;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.VerificationWrapper;
import org.mockito.internal.verification.VerificationWrapperInOrderWrapper;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.mockingDetails;
import static org.mockito.internal.exceptions.Reporter.*;

/**
 * Allows verifying in order. This class should not be exposed, hence default access.
 */
public class InOrderImpl implements InOrder, InOrderContext {

    private final MockitoCore mockitoCore = new MockitoCore();
    private final List<Object> mocksToBeVerifiedInOrder = new ArrayList<>();
    private final InOrderContext inOrderContext = new InOrderContextImpl();

    public List<Object> getMocksToBeVerifiedInOrder() {
        return mocksToBeVerifiedInOrder;
    }

    public InOrderImpl(List<?> mocksToBeVerifiedInOrder) {
        this.mocksToBeVerifiedInOrder.addAll(mocksToBeVerifiedInOrder);
    }

    @Override
    public <T> T verify(T mock) {
        return this.verify(mock, VerificationModeFactory.times(1));
    }

    @Override
    public <T> T verify(T mock, VerificationMode mode) {
        if (mock == null) {
            throw nullPassedToVerify();
        }
        MockingDetails mockingDetails = mockingDetails(mock);
        if (!mockingDetails.isMock()) {
            throw notAMockPassedToVerify(mock.getClass());
        }
        if (!this.objectIsMockToBeVerified(mock)) {
            throw inOrderRequiresFamiliarMock();
        }
        if (mode instanceof VerificationWrapper) {
            return mockitoCore.verify(
                    mock, new VerificationWrapperInOrderWrapper((VerificationWrapper) mode, this));
        } else if (!(mode instanceof VerificationInOrderMode)) {
            throw new MockitoException(
                    mode.getClass().getSimpleName() + " is not implemented to work with InOrder");
        }
        return mockitoCore.verify(mock, new InOrderWrapper((VerificationInOrderMode) mode, this));
    }

    // We can't use `this.mocksToBeVerifiedInOrder.contains`, since that in turn calls `.equals` on
    // the mock. Since mocks can be spies and spies get their real equals method calls called, the
    // result is that Mockito incorrectly would register an invocation on a mock. This normally
    // wouldn't be a problem, unless the user explicitly verifies that no interactions are performed
    // on the mock, which would start to fail for the equals invocation.
    private boolean objectIsMockToBeVerified(Object mock) {
        for (Object inOrderMock : this.mocksToBeVerifiedInOrder) {
            if (inOrderMock == mock) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isVerified(Invocation i) {
        return inOrderContext.isVerified(i);
    }

    @Override
    public void markVerified(Invocation i) {
        inOrderContext.markVerified(i);
    }

    @Override
    public void verifyNoMoreInteractions() {
        mockitoCore.verifyNoMoreInteractionsInOrder(mocksToBeVerifiedInOrder, this);
    }
}

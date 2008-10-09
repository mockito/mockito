/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.Arrays;
import java.util.List;

/**
 */
public class InOrderVerificationMode extends VerificationModeImpl implements VerificationMode {

    protected InOrderVerificationMode(int wantedNumberOfInvocations, List<? extends Object> mocksToBeVerifiedInOrder,
            Verification verification) {
        super(wantedNumberOfInvocations, mocksToBeVerifiedInOrder, verification);
    }

    @Override
    public List<Verifier> getVerifiers() {
        return Arrays.asList(
                new MissingInvocationInOrderVerifier(),
                new NumberOfInvocationsInOrderVerifier());
    }
}
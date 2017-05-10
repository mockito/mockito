/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification;

import java.util.List;

import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;

import static org.mockito.internal.exceptions.Reporter.noInteractionsWanted;

public class NoInteractions implements VerificationMode {

    @SuppressWarnings("unchecked")
    public void verify(VerificationData data) {
        List<Invocation> invocations = data.getAllInvocations();
        if (!invocations.isEmpty()) {
            throw noInteractionsWanted(invocations.get(0).getMock(), (List) invocations);
        }
    }

    @Override
    public VerificationMode description(String description) {
        return VerificationModeFactory.description(this, description);
    }
}

/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.mockito.internal.verification.api.VerificationMode;

/**
 * Holds additional information regarding verification.
 * <p> 
 * Implements marking interface which hides details from Mockito users. 
 */
public class VerificationModeFactory {
    
    public static VerificationMode atLeastOnce() {
        return atLeast(1);
    }

    public static VerificationMode atLeast(int minNumberOfInvocations) {
        return new AtLeast(minNumberOfInvocations);
    }

    public static Times times(int wantedNumberOfInvocations) {
        return new Times(wantedNumberOfInvocations);
    }

    public static NoMoreInteractionsMode noMoreInteractions() {
        return new NoMoreInteractionsMode();
    }

    public static VerificationMode atMost(int maxNumberOfInvocations) {
        return new AtMost(maxNumberOfInvocations);
    }
}
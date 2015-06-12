/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification;

import org.mockito.verification.VerificationMode;

public class VerificationModeFactory {
    
    public static VerificationMode atLeastOnce() {
        return atLeast(1);
    }

    public static VerificationMode atLeast(int minNumberOfInvocations) {
        return new AtLeast(minNumberOfInvocations);
    }

    public static VerificationMode only() {
        return new Only(); //TODO make exception message nicer
    }

    public static Times times(int wantedNumberOfInvocations) {
        return new Times(wantedNumberOfInvocations);
    }

    public static Calls calls(int wantedNumberOfInvocations) {
        return new Calls( wantedNumberOfInvocations );
    }

    public static NoMoreInteractions noMoreInteractions() {
        return new NoMoreInteractions();
    }

    public static VerificationMode atMost(int maxNumberOfInvocations) {
        return new AtMost(maxNumberOfInvocations);
    }
    
    /**
     * Verification mode will prepend the specified failure message if verification fails with the given implementation.
     * @param mode Implementation used for verification
     * @param description The custom failure message
     * @return VerificationMode
     * @since 2.0.0
     */
    public static VerificationMode description(VerificationMode mode, String description) {
        return new Description(mode, description);
    }
}
/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.verification;

public interface VerificationModeAtLeast extends VerificationMode {

    VerificationMode andAtMost(int maxNumberOfInvocations);
}

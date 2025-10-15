/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.verification;

/**
 * Strategy to possibly lazily perform verifications.
 */
public interface VerificationStrategy {

    /**
     * Possibly wrap the given VerificationMode and return a wrapping
     * VerificationMode instead.
     *
     * @param mode The original mode.
     * @return A wrapping mode that uses the original mode.
     */
    VerificationMode maybeVerifyLazily(VerificationMode mode);
}

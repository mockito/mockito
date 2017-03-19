/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.verification;

import org.mockito.Incubating;
import org.mockito.internal.verification.api.VerificationData;

/**
 * Contains all information about a verification that has happened.
 */
@Incubating
public interface VerificationEvent {
    /**
     * @return The mock that a verification happened on.
     */
    Object getMock();

    /**
     * @return the {@link VerificationMode} that was used.
     */
    VerificationMode getMode();

    /**
     * @return the {@link VerificationData} that was verified on.
     */
    VerificationData getData();

    /**
     * A nullable Throwable if it is null, the verification succeeded,
     * otherwise the throwable contains the cause of why the verification failed.
     *
     * @return null or the error.
     */
    Throwable getVerificationError();
}

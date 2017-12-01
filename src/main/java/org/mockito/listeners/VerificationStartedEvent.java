/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.listeners;

import org.mockito.Incubating;

/**
 * The instance of this class is passed to {@link VerificationStartedListener}.
 * For all the details, including how and why to use this API, see {@link VerificationStartedListener}.
 *
 * @since 2.11.0
 */
@Incubating
public interface VerificationStartedEvent {

    /**
     * Replaces existing mock object for verification with a different one.
     * Needed for very advanced framework integrations.
     * For all the details, including how and why see {@link VerificationStartedListener}.
     * <p>
     * If this method is used to replace the mock the sibling method {@link #getMock()} will return the new value.
     * <strong>CAVEAT:</strong> if {@code setMock(Object)} is invoked multiple times from one or many listeners
     * the sibling method {@link #getMock()} will return mock that was set by most recent invocation of {@code setMock(Object)}.
     *
     * @param mock to be used for verification.
     * @since 2.11.0
     */
    @Incubating
    void setMock(Object mock);

    /**
     * The mock object that will be used during verification.
     * See sibling method {@link #setMock(Object)} for more details.
     * Please see {@link VerificationStartedListener} for all details of verification started listeners.
     *
     * @since 2.11.0
     */
    @Incubating
    Object getMock();
}

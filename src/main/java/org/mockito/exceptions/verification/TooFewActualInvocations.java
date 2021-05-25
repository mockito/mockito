/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.verification;

/**
 * @since 2.27.5
 */
public class TooFewActualInvocations extends TooLittleActualInvocations {

    private static final long serialVersionUID = 1L;

    public TooFewActualInvocations(String message) {
        super(message);
    }
}

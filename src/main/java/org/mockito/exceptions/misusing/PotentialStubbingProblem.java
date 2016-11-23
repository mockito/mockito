/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.misusing;

import org.mockito.exceptions.base.MockitoException;

/**
 * TODO strict:
 *  - blurp
 *  - rationale
 *  - negative signals
 *  - doReturn api
 *  - ways to suppress
 *  - ask for feedback
 */
public class PotentialStubbingProblem extends MockitoException {
    public PotentialStubbingProblem(String message) {
        super(message);
    }
}

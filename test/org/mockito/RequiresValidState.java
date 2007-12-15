/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.junit.Before;

/**
 * the easiest way to make sure that tests clean up invalid state is to require
 * valid state for all tests.
 */
public class RequiresValidState {

    @Before
    public void requiresValidState() {
        Mockito.MOCKING_PROGRESS.validateState();
    }
}
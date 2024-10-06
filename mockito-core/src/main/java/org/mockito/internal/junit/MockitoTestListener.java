/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import org.mockito.listeners.MockCreationListener;

/**
 * Internal test listener that helps decoupling JUnit internals from Mockito "business" logic.
 * If we ever want to expose this type publicly, it should not extend MockCreationListener
 * because we want our listeners to be single-method interfaces for easier use and evolution.
 */
public interface MockitoTestListener extends MockCreationListener {
    void testFinished(TestFinishedEvent event);
}

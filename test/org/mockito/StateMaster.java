/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.progress.ThreadSafeMockingProgress;

public class StateMaster {
    
    private static final ThreadSafeMockingProgress MOCKING_PROGRESS= new ThreadSafeMockingProgress();

    public static void reset() {
        MOCKING_PROGRESS.reset();
    }
    
    public static void validate() {
        MOCKING_PROGRESS.validateState();
    }
}
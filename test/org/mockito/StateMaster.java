/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito;

import org.mockito.internal.progress.ThreadSafeMockingProgress;

public class StateMaster {
    
    private final ThreadSafeMockingProgress mockingProgress = new ThreadSafeMockingProgress();

    public void reset() {
        mockingProgress.reset();
    }
    
    public void validate() {
        mockingProgress.validateState();
    }
}
/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

public class StateMaster {

    public void reset() {
        mockingProgress().reset();
        mockingProgress().resetOngoingStubbing();
    }

    public void validate() {
        mockingProgress().validateState();
    }
}
/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.MockitoCore;

public class StateMaster {
    
    public static void reset() {
        MockitoCore.MOCKING_PROGRESS.reset();
    }
    
    public static void validate() {
        MockitoCore.MOCKING_PROGRESS.validateState();
    }
}
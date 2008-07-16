/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

public class StateMaster {
    
    public static void reset() {
        Mockito.MOCKING_PROGRESS.reset();
    }
    
    public static void validate() {
        Mockito.MOCKING_PROGRESS.validateState();
    }
}
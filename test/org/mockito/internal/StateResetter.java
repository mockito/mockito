/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

public class StateResetter {
    
    public static void reset() {
        MockitoState.instance().reset();
    }
}

/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.debugging;

import org.mockito.Mockito;
import org.mockito.MockitoDebugger;
import org.mockito.internal.debugging.MockitoDebuggerImpl;

//TODO get rid when debug() finally is out
public class NewMockito extends Mockito {
    
    public static MockitoDebugger debug() {
        return new MockitoDebuggerImpl();
    }
}

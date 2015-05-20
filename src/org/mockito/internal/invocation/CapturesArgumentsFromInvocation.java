/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;


import org.mockito.invocation.Invocation;

public interface CapturesArgumentsFromInvocation {
    
    void captureArgumentsFrom(Invocation i);
    
}

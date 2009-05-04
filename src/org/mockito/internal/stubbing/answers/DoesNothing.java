/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class DoesNothing implements Answer<Object> {
    
    public Object answer(InvocationOnMock invocation) throws Throwable {
        return null;
    }
}
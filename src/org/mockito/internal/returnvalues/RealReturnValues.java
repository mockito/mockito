/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.returnvalues;

import org.mockito.ReturnValues;
import org.mockito.invocation.InvocationOnMock;

public class RealReturnValues implements ReturnValues {
    public Object valueFor(InvocationOnMock invocation) throws Throwable {
        return invocation.invokeSuper();
    }
}

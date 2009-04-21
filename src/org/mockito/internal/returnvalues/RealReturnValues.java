/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.returnvalues;

import org.mockito.ReturnValues;
import org.mockito.invocation.InvocationOnMock;

/**
 * Description: TODO: Enter a paragraph that summarizes what the class does and
 * why someone might want to utilize it
 * 
 * <p>
 * Copyright © 2000-2007, NetSuite, Inc.
 * </p>
 * 
 * @author amurkes
 * @version 2007.0
 * @since Apr 15, 2009
 */
public class RealReturnValues implements ReturnValues {
    public Object valueFor(InvocationOnMock invocation) throws Throwable {
        return invocation.invokeSuper();
    }
}

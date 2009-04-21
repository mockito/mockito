package org.mockito.internal.stubbing;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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
 * @since Apr 10, 2009
 */
public class CallsRealMethod implements Answer<Object> {
    public Object answer(InvocationOnMock invocation) throws Throwable {
        return invocation.invokeSuper();
    }
}

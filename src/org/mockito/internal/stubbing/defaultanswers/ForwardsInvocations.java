/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Internal answer to forward invocations on a real instance.
 *
 * @since 1.9.5
 */
public class ForwardsInvocations implements Answer<Object>, Serializable {

	private static final long serialVersionUID = -8343690268123254910L;

	private Object delegatedObject = null ;

	public ForwardsInvocations(Object delegatedObject) {
		this.delegatedObject = delegatedObject ;
	}

	public Object answer(InvocationOnMock invocation) throws Throwable {
		Method method = invocation.getMethod() ;

        try {
            return method.invoke(delegatedObject, invocation.getArguments());
        } catch (InvocationTargetException e) {
            // propagate the original exception from the delegate
            throw e.getCause();
        }
    }
}
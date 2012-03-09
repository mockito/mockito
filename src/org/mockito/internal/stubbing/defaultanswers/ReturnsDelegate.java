/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import org.mockito.internal.util.Primitives;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.lang.reflect.Method;

public class ReturnsDelegate implements Answer<Object>, Serializable {

	private static final long serialVersionUID = -8343690268123254910L;

	private Object delegatedObject = null ;

	public ReturnsDelegate(Object delegatedObject) {
		this.delegatedObject = delegatedObject ;
	}

	public Object answer(InvocationOnMock invocation) throws Throwable {
		Method method = invocation.getMethod() ;
		Object ret = method.invoke(delegatedObject, invocation.getArguments()) ;
		if (ret != null) {
			return ret;
		}

		return returnValueFor(invocation.getMethod().getReturnType());
	}

	Object returnValueFor(Class<?> type) {
		if (type.isPrimitive()) {
			return primitiveOf(type);
		} else if (Primitives.isPrimitiveWrapper(type)) {
			return Primitives.primitiveWrapperOf(type);
			//new instances are used instead of Collections.emptyList(), etc.
			//to avoid UnsupportedOperationException if code under test modifies returned collection
		}

		return null ;
	}

	private Object primitiveOf(Class<?> type) {
		if (type == Boolean.TYPE) {
			return false;
		} else if (type == Character.TYPE) {
			return (char) 0;
		} else {
			return 0;
		}
	}

}
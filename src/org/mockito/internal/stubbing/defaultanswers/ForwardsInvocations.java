/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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
        Method mockMethod = invocation.getMethod();

        try {
            Method delegateMethod = getDelegateMethod(mockMethod);
            
            if (!compatibleReturnTypes(mockMethod.getReturnType(), delegateMethod.getReturnType())) {
                throw new MockitoException("Incompatible return type on delegate method: " + delegateMethod);
            }
            
            return delegateMethod.invoke(delegatedObject, invocation.getArguments());
        } catch (NoSuchMethodException e) {
            throw new MockitoException("Method not found on delegate: " + mockMethod, e);
        } catch (InvocationTargetException e) {
            // propagate the original exception from the delegate
            throw e.getCause();
        }
    }

    private Method getDelegateMethod(Method mockMethod) throws NoSuchMethodException {
        if (mockMethod.getDeclaringClass().isAssignableFrom(delegatedObject.getClass())) {
            // Compatible class. Return original method.
            return mockMethod;
        } else {
            // Return method of delegate object with the same signature as mockMethod.
            return delegatedObject.getClass().getMethod(mockMethod.getName(), mockMethod.getParameterTypes());
        }
    }

    private static boolean compatibleReturnTypes(Class<?> superType, Class<?> subType) {
        return superType.equals(subType) || superType.isAssignableFrom(subType);
    }
}

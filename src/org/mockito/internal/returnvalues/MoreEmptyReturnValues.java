/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.returnvalues;

import org.mockito.ReturnValues;
import org.mockito.invocation.InvocationOnMock;

/**
 * Will be used by default by every Mockito 2.0 mock.
 * <ul>
 * <li>
 *  Returns appropriate primitive for primitive-returning methods
 * </li>
 * <li>
 *  Returns consistent values for primitive wrapper classes (e.g. int-returning method retuns 0 <b>and</b> Integer-returning method returns 0, too)
 * </li>
 * <li>
 *  Returns empty collection for collection-returning methods (works for most commonly used collection types)
 * </li>
 * <li>
 *  Returns empty array for array-returning methods
 * </li>
 * <li>
 *  Returns "" for String-returning method
 * </li>
 * <li>
 *  Returns description of mock for toString() method
 * </li>
 * <li>
 *  Returns null for everything else
 * </li>
 * </ul>
 */
public class MoreEmptyReturnValues implements ReturnValues {
    
    private ReturnValues delegate = new EmptyReturnValues();
    
    /* (non-Javadoc)
     * @see org.mockito.configuration.ReturnValues#valueFor(org.mockito.invocation.InvocationOnMock)
     */
    public Object valueFor(InvocationOnMock invocation) {
        Object ret = delegate.valueFor(invocation);
        if (ret != null) {
            return ret;
        }

        Class<?> returnType = invocation.getMethod().getReturnType();
        return returnValueFor(returnType);
    }
    
    Object returnValueFor(Class<?> type) {
        if (type == String.class) {
            return "";
        } else if (type == Object.class) {
            return new Object();
        } else if (type.isArray()) {
            //TODO is it possible to dynamically create array?
            return null;
//            System.out.println(type. getConstructors()[0].getParameterTypes());
//            try {
//                return type.newInstance();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
        }
        return null;
    }
}
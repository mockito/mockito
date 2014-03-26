/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.stubbing.defaultanswers;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.lang.reflect.Array;

/**
 * It's likely this implementation will be used by default by every Mockito 2.0 mock.
 * <p>
 * Currently <b>used only</b> by {@link Mockito#RETURNS_SMART_NULLS}
 * <p>
 * Current version of Mockito mocks by deafult use {@link ReturnsEmptyValues}  
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
 *  Returns non-zero for Comparable#compareTo(T other) method (see issue 184)
 * </li>
 * <li>
 *  Returns null for everything else
 * </li>
 * </ul>
 */
public class ReturnsMoreEmptyValues implements Answer<Object>, Serializable {
    
    private static final long serialVersionUID = -2816745041482698471L;
    private final Answer<Object> delegate = new ReturnsEmptyValues();
    
    /* (non-Javadoc)
     * @see org.mockito.stubbing.Answer#answer(org.mockito.invocation.InvocationOnMock)
     */
    public Object answer(InvocationOnMock invocation) throws Throwable {
        Object ret = delegate.answer(invocation);
        if (ret != null) {
            return ret;
        }

        Class<?> returnType = invocation.getMethod().getReturnType();
        return returnValueFor(returnType);
    }
    
    Object returnValueFor(Class<?> type) {
        if (type == String.class) {
            return "";
        }  else if (type.isArray()) {
            Class<?> componenetType = type.getComponentType();
            return Array.newInstance(componenetType, 0);
        }
        return null;
    }
}
/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.stubbing.defaultanswers;

import java.io.Serializable;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * It's likely this implementation will be used by default by every Mockito 3.0.0 mock.
 * <p>
 * Currently <b>used only</b> by {@link Mockito#RETURNS_SMART_NULLS}
 * <p>
 * Current version of Mockito mocks by default use {@link ReturnsEmptyValues}
 * <ul>
 * <li>Returns empty values of {@link ReturnsEmptyValues}</li>
 * <li>Returns "" for String-returning method</li>
 * <li>Returns null for everything else</li>
 * </ul>
 */
public class ReturnsMoreEmptyValues implements Answer<Object>, Serializable {

    private static final long serialVersionUID = -2816745041482698471L;
    private final Answer<Object> delegate = new ReturnsEmptyValues();

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
        }
        return null;
    }
}

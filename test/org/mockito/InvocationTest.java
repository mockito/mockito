/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.Invocation;

public class InvocationTest {

    private Invocation call;

    private Invocation equalCall;

    private Invocation nonEqualCall;

    private Method dummyMethod;

    @Before
    public void setup() throws SecurityException, NoSuchMethodException {
        Object[] arguments1 = new Object[] { "" };
        Object[] arguments2 = new Object[] { "" };
        Object[] arguments3 = new Object[] { "X" };
        dummyMethod = Object.class.getMethod("equals",
                new Class[] { Object.class });
        Object mock = new Object();
        call = new Invocation(mock, dummyMethod, arguments1, 0);
        equalCall = new Invocation(mock, dummyMethod, arguments2, 0);
        nonEqualCall = new Invocation(mock, dummyMethod, arguments3, 0);
    }

    @Test
    public void shouldKnowIfIsEqualTo() {
        assertFalse(call.equals(null));
        assertFalse(call.equals(""));
        assertTrue(call.equals(equalCall));
        assertFalse(call.equals(nonEqualCall));
    }
}
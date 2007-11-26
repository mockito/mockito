/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

public class ExpectedInvocationTest {

    @Test
    public void shouldImplementHashcodeToBeHashMapsCitizen() throws Exception {
        Object[] args = new Object[] { "" };
        Method m = Object.class.getMethod("equals", new Class[] { Object.class });
        Invocation invocation = new Invocation(null, m, args);
        assertThat(new ExpectedInvocation(invocation, null).hashCode(), equalTo(1));
    }
    
    @Test
    public void shouldNotEqualIfNumberOfArgumentsDiffer() throws SecurityException, NoSuchMethodException {
        Object mock = new Object();

        Method dummyMethod = Object.class.getMethod("equals",
                new Class[] { Object.class });

        ExpectedInvocation invocationWithOneArg = new ExpectedInvocation(
                new Invocation(mock, dummyMethod, new Object[] { "" }), null);
        ExpectedInvocation invocationWithTwoArgs = new ExpectedInvocation(
                new Invocation(mock, dummyMethod, new Object[] { "", "" }), null);

        assertFalse(invocationWithOneArg.equals(null));
        assertFalse(invocationWithOneArg.equals(invocationWithTwoArgs));
    }
}

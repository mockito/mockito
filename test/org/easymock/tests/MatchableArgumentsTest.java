/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.easymock.internal.ExpectedInvocation;
import org.easymock.internal.Invocation;
import org.junit.Before;
import org.junit.Test;

public class MatchableArgumentsTest {

    private Object[] arguments;

    private Object[] arguments2;

    @Before
    public void setup() {
        arguments = new Object[] { "" };
        arguments2 = new Object[] { "", "" };
    }

    @Test
    public void testEquals() throws SecurityException, NoSuchMethodException {
        Method toPreventNullPointerExceptionm = Object.class.getMethod(
                "toString", new Class[] {});

        Object mock = new Object();

        ExpectedInvocation matchableArguments = new ExpectedInvocation(
                new Invocation(mock, toPreventNullPointerExceptionm, arguments),
                null);
        ExpectedInvocation nonEqualMatchableArguments = new ExpectedInvocation(
                new Invocation(mock, toPreventNullPointerExceptionm, arguments2),
                null);

        assertFalse(matchableArguments.equals(null));
        assertFalse(matchableArguments.equals(nonEqualMatchableArguments));
    }
}
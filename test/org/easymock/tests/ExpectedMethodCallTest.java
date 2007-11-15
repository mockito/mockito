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

public class ExpectedMethodCallTest {

    private ExpectedInvocation call;

    @Before
    public void setup() throws SecurityException, NoSuchMethodException {
        Object[] arguments1 = new Object[] { "" };
        Method m = Object.class.getMethod("equals",
                new Class[] { Object.class });
        call = new ExpectedInvocation(new Invocation(null, m, arguments1), null);
    }

//    @Test
//    public void testHashCode() {
//        try {
//            call.hashCode();
//            fail();
//        } catch (UnsupportedOperationException expected) {
//            assertEquals("hashCode() is not implemented", expected.getMessage());
//        }
//    }
}
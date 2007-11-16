/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.easymock.internal.*;
import org.junit.Before;
import org.junit.Test;

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
        call = new Invocation(mock, dummyMethod, arguments1);
        equalCall = new Invocation(mock, dummyMethod, arguments2);
        nonEqualCall = new Invocation(mock, dummyMethod, arguments3);
    }

    @Test
    public void shouldKnowIfIsEqualTo() {
        assertFalse(call.equals(null));
        assertFalse(call.equals(""));
        assertTrue(call.equals(equalCall));
        assertFalse(call.equals(nonEqualCall));
    }
    
    @Test
    public void shouldNotImplementHashCodeBecauseItsNotUsedWithMaps() {
        try {
            call.hashCode();
            fail();
        } catch (UnsupportedOperationException expected) {
            assertEquals("hashCode() is not implemented", expected.getMessage());
        }
    }

    @Test
    public void shouldDisplayMocksToStringIfValidJavaIdentifier()
            throws SecurityException, NoSuchMethodException {
        Method method = ToString.class.getMethod("aMethod", new Class[0]);
        Invocation invocation = new Invocation(new ToString("validJavaIdentifier"),
                method, null);

        assertEquals(invocation.toString(new EqualsMatcher()),
                "validJavaIdentifier.aMethod()");

        invocation = new Invocation(new ToString("no-valid-java-identifier"),
                method, null);

        assertEquals(invocation.toString(new EqualsMatcher()), "aMethod()");

    }
    
    class ToString {
        private final String name;

        public ToString(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public void aMethod() {
        }
    }
}
/*
 * Copyright (c) 2007 Szczepan Faber 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.*;
import org.mockito.Mockito;
import org.mockito.exceptions.MockVerificationAssertionError;

public class UsingVarargsTest {

    private interface IVarArgs {
        public void withStringVarargs(int value, String... s);
        public void withObjectVarargs(int value, Object... o);
        public void withBooleanVarargs(int value, boolean... b);
    }
    
    IVarArgs mock;

    @Before
    public void setup() {
        mock = Mockito.mock(IVarArgs.class);
    }
    
    @Test
    public void shouldVerifyStringVarargs() {
        mock.withStringVarargs(1);
        mock.withStringVarargs(2, "1", "2", "3");
        mock.withStringVarargs(3, "1", "2", "3", "4");

        verify(mock).withStringVarargs(1);
        verify(mock).withStringVarargs(2, "1", "2", "3");
        try {
            verify(mock).withStringVarargs(2, "1", "2", "79", "4");
            fail();
        } catch (MockVerificationAssertionError e) {}
    }

    @Test
    public void shouldVerifyObjectVarargs() {
        mock.withObjectVarargs(1);
        mock.withObjectVarargs(2, "1", new ArrayList<Object>(), new Integer(1));
        mock.withObjectVarargs(3, new Integer(1));

        verify(mock).withObjectVarargs(1);
        verify(mock).withObjectVarargs(2, "1", new ArrayList<Object>(), new Integer(1));
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (MockVerificationAssertionError e) {}
    }

    @Test
    public void shouldVerifyBooleanVarargs() {
        mock.withBooleanVarargs(1);
        mock.withBooleanVarargs(2, true, false, true);
        mock.withBooleanVarargs(3, true, true, true);

        verify(mock).withBooleanVarargs(1);
        verify(mock).withBooleanVarargs(2, true, false, true);
        try {
            verify(mock).withBooleanVarargs(3, true, true, true, true);
            fail();
        } catch (MockVerificationAssertionError e) {}
    }
}
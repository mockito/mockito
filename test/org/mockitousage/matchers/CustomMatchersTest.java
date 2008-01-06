/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.CustomMatcher;
import org.mockito.Mockito;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.mockitousage.IMethods;

@SuppressWarnings("unchecked")
public class CustomMatchersTest extends RequiresValidState {
    private final class AnyBoolean extends CustomMatcher<Boolean> {
        public boolean matches(Boolean argument) {
            return true;
        }
    }

    private final class ZeroOrOne extends CustomMatcher<Integer> {
        public boolean matches(Integer argument) {
            if (argument == 0 || argument == 1) {
                return true;
            }
            return false;
        }
    }

    private IMethods mock;

    @Before
    public void setUp() {
        mock = Mockito.mock(IMethods.class);
    }

    @Test
    public void shouldUseCustomIntMatcher() {
        stub(mock.simpleMethod(intThatIs(new ZeroOrOne()))).toReturn("zero or one");
        
        assertEquals("zero or one", mock.simpleMethod(0));
        assertEquals("zero or one", mock.simpleMethod(1));
        assertEquals(null, mock.simpleMethod(2));
        
        try {
            verify(mock).simpleMethod(intThatIs(new ZeroOrOne()));
            fail();
        } catch (TooManyActualInvocations e) {}
    }
    
    @Test
    public void shouldUseCustomBooleanMatcher() {
        stub(mock.oneArg(booleanThatIs(new AnyBoolean()))).toReturn("any boolean");
        
        assertEquals("any boolean", mock.oneArg(true));
        assertEquals("any boolean", mock.oneArg(false));
        
        try {
            verify(mock).oneArg(booleanThatIs(new AnyBoolean()));
            fail();
        } catch (TooManyActualInvocations e) {}
    }
}
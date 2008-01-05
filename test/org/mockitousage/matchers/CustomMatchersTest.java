/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.mockito.internal.matchers.IArgumentMatcher;
import org.mockito.internal.progress.LastArguments;
import org.mockitousage.IMethods;

@SuppressWarnings("unchecked")
public class CustomMatchersTest extends RequiresValidState {
    private final class ZeroOrOne extends CustomMatcher<Integer> {
        public boolean matches(Integer argument) {
            if (argument == 0 || argument == 1) {
                return true;
            }
            
            return false;
        }
    }

    //TODO make CustomMatcher part of framework
    abstract class CustomMatcher<T> implements IArgumentMatcher<T> {
        public void appendTo(StringBuilder builder) {
            builder.append("<custom argument matcher>");
        }

        public abstract boolean matches(T argument);
    }
    
    private IMethods mock;

    @Before
    public void setUp() {
        mock = Mockito.mock(IMethods.class);
    }

    @Test
    public void shouldAllowUsingCustomMatcher() {
        stub(mock.simpleMethod(intThatIs(new ZeroOrOne()))).toReturn("zero or one");
        
        assertEquals("zero or one", mock.simpleMethod(0));
        assertEquals("zero or one", mock.simpleMethod(1));
        assertEquals(null, mock.simpleMethod(2));
        
        try {
            verify(mock).simpleMethod(intThatIs(new ZeroOrOne()));
            fail();
        } catch (TooManyActualInvocations e) {}
    }

    private int intThatIs(CustomMatcher matcher) {
        LastArguments.instance().reportMatcher(matcher);
        return 0;
    }
}
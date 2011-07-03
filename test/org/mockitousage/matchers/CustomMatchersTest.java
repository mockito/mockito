/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.matchers;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class CustomMatchersTest extends TestBase {
    
    private final class ContainsFoo extends ArgumentMatcher<String> {
        public boolean matches(Object arg) {
            return ((String) arg).contains("foo");
        }
    }

    private final class IsAnyBoolean extends ArgumentMatcher<Boolean> {
        public boolean matches(Object arg) {
            return true;
        }
    }
    
    private final class IsSorZ extends ArgumentMatcher<Character> {
        public boolean matches(Object arg) {
            Character character = (Character) arg;
            return character.equals('s') || character.equals('z');
        }
    }

    private final class IsZeroOrOne<T extends Number> extends ArgumentMatcher<T> {
        public boolean matches(Object arg) {
            Number number = (Number) arg;
            if (number.intValue() == 0 || number.intValue() == 1) {
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
    public void shouldUseCustomBooleanMatcher() {
        when(mock.oneArg(booleanThat(new IsAnyBoolean()))).thenReturn("foo");
        
        assertEquals("foo", mock.oneArg(true));
        assertEquals("foo", mock.oneArg(false));
        
        assertEquals(null, mock.oneArg("x"));
    }
    
    @Test
    public void shouldUseCustomCharMatcher() {
        when(mock.oneArg(charThat(new IsSorZ()))).thenReturn("foo");
      
        assertEquals("foo", mock.oneArg('s'));
        assertEquals("foo", mock.oneArg('z'));
        assertEquals(null, mock.oneArg('x'));
    }
    
    class Article {
        
        private int pageNumber;
        private String headline;
        
        public Article(int pageNumber, String headline) {
            super();
            this.pageNumber = pageNumber;
            this.headline = headline;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public String getHeadline() {
            return headline;
        }
    }
    
    @Test
    public void shouldUseCustomPrimitiveNumberMatchers() {
        when(mock.oneArg(byteThat(new IsZeroOrOne<Byte>()))).thenReturn("byte");
        when(mock.oneArg(shortThat(new IsZeroOrOne<Short>()))).thenReturn("short");
        when(mock.oneArg(intThat(new IsZeroOrOne<Integer>()))).thenReturn("int");
        when(mock.oneArg(longThat(new IsZeroOrOne<Long>()))).thenReturn("long");
        when(mock.oneArg(floatThat(new IsZeroOrOne<Float>()))).thenReturn("float");
        when(mock.oneArg(doubleThat(new IsZeroOrOne<Double>()))).thenReturn("double");
        
        assertEquals("byte", mock.oneArg((byte) 0));
        assertEquals("short", mock.oneArg((short) 1));
        assertEquals("int", mock.oneArg(0));
        assertEquals("long", mock.oneArg(1L));
        assertEquals("float", mock.oneArg(0F));
        assertEquals("double", mock.oneArg(1.0));
        
        assertEquals(null, mock.oneArg(2));
        assertEquals(null, mock.oneArg("foo"));
    }
         
    @Test
    public void shouldUseCustomObjectMatcher() {
        when(mock.oneArg(argThat(new ContainsFoo()))).thenReturn("foo");
        
        assertEquals("foo", mock.oneArg("foo"));
        assertEquals(null, mock.oneArg("bar"));
    }
    
    @Test
    public void shouldCustomMatcherPrintDescriptionBasedOnName() {
        mock.simpleMethod("foo");

        try {
            verify(mock).simpleMethod(containsTest());
            fail();
        } catch (AssertionError e) {
            assertContains("<String that contains xxx>", e.getMessage());
        }
    }

    private String containsTest() {
        return argThat(new StringThatContainsXxx());
    }
    
    private final class StringThatContainsXxx extends ArgumentMatcher<String> {
        public boolean matches(Object argument) {
            String arg = (String) argument;
            return arg.contains("xxx");
        }
    }
    
    @Test
    public void shouldAnonymousCustomMatcherPrintDefaultDescription() {
        mock.simpleMethod("foo");

        try {
            verify(mock).simpleMethod((String) argThat(new ArgumentMatcher<Object>() {
                @Override public boolean matches(Object argument) {
                    return false;
                }}));
            fail();
        } catch (AssertionError e) {
            assertContains("<custom argument matcher>", e.getMessage());
            assertContains("foo", e.getMessage());
        }
    }
}
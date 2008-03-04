/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static org.junit.Assert.*;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class ArgumentMatcherTest {
    
    private class StringWithDirtyWords extends ArgumentMatcher<Object> {
        public boolean matches(Object argument) {
            return false;
        }
    }
    
    private class WEIRDO1 extends ArgumentMatcher<Object> {
        public boolean matches(Object argument) {
            return false;
        }
    }
    
    private class _ extends ArgumentMatcher<Object> {
        public boolean matches(Object argument) {
            return false;
        }
    }
    
    @Test
    public void shouldDeCamelCaseTheName() throws Exception {
        Matcher matcher = new StringWithDirtyWords();
        StringDescription d = new StringDescription();
        matcher.describeTo(d);
        assertEquals("String with dirty words", d.toString());
    }
    
    @Test
    public void shouldDeCamelCaseWeirdName() throws Exception {
        Matcher matcher = new WEIRDO1();
        StringDescription d = new StringDescription();
        matcher.describeTo(d);
        assertEquals("W e i r d o1", d.toString());
    }
    
    @Test
    public void shouldNotDeCamelCaseVeryWeirdName() throws Exception {
        Matcher matcher = new _();
        StringDescription d = new StringDescription();
        matcher.describeTo(d);
        assertEquals("_", d.toString());
    }
    
    @Test
    public void shouldNotDeCamelCaseAnnonymousClass() throws Exception {
        Matcher matcher = new ArgumentMatcher() {
            @Override
            public boolean matches(Object argument) {
                return false;
            }
        };
        StringDescription d = new StringDescription();
        matcher.describeTo(d);
        assertEquals("<custom argument matcher>", d.toString());
    }
}
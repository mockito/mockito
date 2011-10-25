/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class LocalizedMatcherTest extends TestBase {
    
    @Test
    public void shouldMatchTypesWhenActualMatcherHasCorrectType() throws Exception {
        //when
        ContainsExtraTypeInformation equals10 = new Equals(10);
        LocalizedMatcher m = new LocalizedMatcher((Matcher) equals10);
        
        //then
        assertTrue(m.typeMatches(10));
        assertFalse(m.typeMatches(10L));
    }

    @Test
    public void shouldNotMatchTypesWhenActualMatcherDoesNotHaveCorrectType() throws Exception {
        //when
        LocalizedMatcher m = new LocalizedMatcher(Any.ANY);
        
        //then
        assertFalse(m.typeMatches(10));
    }
    
    @Test
    public void shouldDescribeWithTypeInfoWhenActualMatcherHasCorrectType() throws Exception {
        //when
        ContainsExtraTypeInformation equals10 = new Equals(10);
        LocalizedMatcher m = new LocalizedMatcher((Matcher) equals10);
        
        //then
        assertEquals("(Integer) 10", describe(m.withExtraTypeInfo()));
    }
    
    @Test
    public void shouldNotDescribeWithTypeInfoWhenActualMatcherDoesNotHaveCorrectType() throws Exception {
        //when
        LocalizedMatcher m = new LocalizedMatcher(Any.ANY);
        
        //then
        assertSame(m, m.withExtraTypeInfo());
    }
    
    @Test
    public void shouldDelegateToCapturingMatcher() throws Exception {
        //given
        CapturingMatcher capturingMatcher = new CapturingMatcher();
        LocalizedMatcher m = new LocalizedMatcher(capturingMatcher);
        
        //when
        m.captureFrom("boo");
        
        //then
        assertEquals("boo", capturingMatcher.getLastValue());
    }
}
/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.argumentmatching;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.Equals;
import org.mockitoutil.TestBase;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

@SuppressWarnings({"unchecked","serial"})
public class ArgumentMatchingToolTest extends TestBase {

    private ArgumentMatchingTool tool = new ArgumentMatchingTool();

    @Test
    public void shouldNotFindAnySuspiciousMatchersWhenNumberOfArgumentsDoesntMatch() {
        //given
        List<ArgumentMatcher> matchers = (List) Arrays.asList(new Equals(1));

        //when
        Integer[] suspicious = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, new Object[] {10, 20});
        
        //then
        assertEquals(0, suspicious.length);
    }

    @Test
    public void shouldNotFindAnySuspiciousMatchersWhenArgumentsMatch() {
        //given
        List<ArgumentMatcher> matchers = (List) Arrays.asList(new Equals(10), new Equals(20));
        
        //when
        Integer[] suspicious = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, new Object[] {10, 20});
        
        //then
        assertEquals(0, suspicious.length);
    }
    
    @Test
    public void shouldFindSuspiciousMatchers() {
        //given
        Equals matcherInt20 = new Equals(20);
        Long longPretendingAnInt = 20L;
        
        //when
        List<ArgumentMatcher> matchers = (List) Arrays.asList(new Equals(10), matcherInt20);
        Integer[] suspicious = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, new Object[] {10, longPretendingAnInt});
        
        //then
        assertEquals(1, suspicious.length);
        assertEquals(new Integer(1), suspicious[0]);
    }
    
    @Test
    public void shouldNotFindSuspiciousMatchersWhenTypesAreTheSame() {
        //given
        Equals matcherWithBadDescription = new Equals(20) {
            public String toString() {
                return "10";
            }
        };
        Integer argument = 10;
        
        //when
        Integer[] suspicious = tool.getSuspiciouslyNotMatchingArgsIndexes((List) Arrays.asList(matcherWithBadDescription), new Object[] {argument});
        
        //then
        assertEquals(0, suspicious.length);
    }
    
    @Test
    public void shouldWorkFineWhenGivenArgIsNull() {
        //when
        Integer[] suspicious = tool.getSuspiciouslyNotMatchingArgsIndexes((List) Arrays.asList(new Equals(20)), new Object[] {null});
        
        //then
        assertEquals(0, suspicious.length);
    }
    
    @Test
    public void shouldUseMatchersSafely() {
        //given
        List<ArgumentMatcher> matchers = (List) Arrays.asList(new ArgumentMatcher() {
            public boolean matches(Object item) {
                throw new ClassCastException("nasty matcher");
            }
        });
        
        //when
        Integer[] suspicious = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, new Object[] {10});
        
        //then
        assertEquals(0, suspicious.length);
    }
}
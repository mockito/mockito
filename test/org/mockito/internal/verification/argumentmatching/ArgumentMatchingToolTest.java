package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.matchers.Equals;

@SuppressWarnings("unchecked")
public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool tool = new ArgumentMatchingTool();

    @Test
    public void shouldNotFindAnySuspiciousMatchersWhenNumberOfArgumentsDoesntMatch() {
        //given
        List<Matcher> matchers = (List) Arrays.asList(new Equals(1));

        //when
        Matcher[] suspicious = tool.getSuspiciouslyNotMatchingArgs(matchers, new Object[] {10, 20});
        
        //then
        assertEquals(0, suspicious.length);
    }

    @Test
    public void shouldNotFindAnySuspiciousMatchersWhenArgumentsMatch() {
        //given
        List<Matcher> matchers = (List) Arrays.asList(new Equals(10), new Equals(20));
        
        //when
        Matcher[] suspicious = tool.getSuspiciouslyNotMatchingArgs(matchers, new Object[] {10, 20});
        
        //then
        assertEquals(0, suspicious.length);
    }
    
    @Test
    public void shouldFindSuspiciousMatchers() {
        //given
        Equals matcherInt20 = new Equals(20);
        Long longPretendingAnInt = new Long(20);
        
        //when
        List<Matcher> matchers = (List) Arrays.asList(new Equals(10), matcherInt20);
        Matcher[] suspicious = tool.getSuspiciouslyNotMatchingArgs(matchers, new Object[] {10, longPretendingAnInt});
        
        //then
        assertEquals(1, suspicious.length);
        assertEquals(matcherInt20, suspicious[0]);
    }
    
    @Test
    public void shouldUseMatchersSafely() {
        //given
        List<Matcher> matchers = (List) Arrays.asList(new BaseMatcher() {
            public boolean matches(Object item) {
                throw new ClassCastException("nasty matcher");
            }

            public void describeTo(Description description) {
            }});
        
        //when
        Matcher[] suspicious = tool.getSuspiciouslyNotMatchingArgs(matchers, new Object[] {10});
        
        //then
        assertEquals(0, suspicious.length);
    }
}
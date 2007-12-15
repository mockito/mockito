/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.internal.progress.VerificationMode;

public class InvocationsAnalyzerTest extends RequiresValidState {
    
    private List<Invocation> invocations = new LinkedList<Invocation>();
    private Invocation simpleMethodInvocation;
    private Invocation simpleMethodInvocationTwo;
    private Invocation differentMethodInvocation;
    private InvocationsAnalyzer analyzer;

    @Before
    public void setup() throws Exception {
        simpleMethodInvocation = new InvocationBuilder().simpleMethod().seq(1).toInvocation();
        simpleMethodInvocationTwo = new InvocationBuilder().simpleMethod().seq(2).toInvocation();
        differentMethodInvocation = new InvocationBuilder().differentMethod().seq(3).toInvocation();
        invocations.addAll(Arrays.asList(simpleMethodInvocation, simpleMethodInvocationTwo, differentMethodInvocation));
        analyzer = new InvocationsAnalyzer();
    }
    
    @Test
    public void shouldFindFirstUnverifiedInvocation() throws Exception {
        assertSame(simpleMethodInvocation, analyzer.findFirstUnverified(invocations));
        
        simpleMethodInvocationTwo.markVerified();
        simpleMethodInvocation.markVerified();
        
        assertSame(differentMethodInvocation, analyzer.findFirstUnverified(invocations));
        
        differentMethodInvocation.markVerified();
        assertNull(analyzer.findFirstUnverified(invocations));
    }
    
    @Test
    public void shouldFindFirstUndesiredWhenWantedNumberOfTimesIsZero() throws Exception {
        HasStackTrace firstUndesired = analyzer.findFirstUndesiredInvocationStackTrace(invocations, new InvocationMatcher(simpleMethodInvocation), VerificationMode.times(0));
        HasStackTrace expected = simpleMethodInvocation.getStackTrace();
        assertSame(firstUndesired, expected);
    }
    
    @Test
    public void shouldFindFirstUndesiredWhenWantedNumberOfTimesIsOne() throws Exception {
        HasStackTrace firstUndesired = analyzer.findFirstUndesiredInvocationStackTrace(invocations, new InvocationMatcher(simpleMethodInvocation), VerificationMode.times(1));
        HasStackTrace expected = simpleMethodInvocationTwo.getStackTrace();
        assertSame(firstUndesired, expected);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldBreakWhenThereAreNoUndesiredInvocations() throws Exception {
        analyzer.findFirstUndesiredInvocationStackTrace(invocations, new InvocationMatcher(simpleMethodInvocation), VerificationMode.times(2));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldBreakWhenWantedInvocationsFigureIsBigger() throws Exception {
        analyzer.findFirstUndesiredInvocationStackTrace(invocations, new InvocationMatcher(simpleMethodInvocation), VerificationMode.times(100));
    }
}

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
import org.mockito.exceptions.parents.HasStackTrace;
import org.mockito.internal.progress.VerificationMode;


public class InvocationsCalculatorTest extends RequiresValidState {
    
    private List<Invocation> invocations = new LinkedList<Invocation>();
    private Invocation simpleMethodInvocation;
    private Invocation simpleMethodInvocationTwo;
    private Invocation differentMethodInvocation;
    private InvocationsCalculator calculator;

    @Before
    public void setup() throws Exception {
        simpleMethodInvocation = new InvocationBuilder().simpleMethod().seq(1).toInvocation();
        simpleMethodInvocationTwo = new InvocationBuilder().simpleMethod().seq(2).toInvocation();
        differentMethodInvocation = new InvocationBuilder().differentMethod().seq(3).toInvocation();
        invocations.addAll(Arrays.asList(simpleMethodInvocation, simpleMethodInvocationTwo, differentMethodInvocation));
        calculator = new InvocationsCalculator();
    }
    
    @Test
    public void shouldGetFirstUnverifiedInvocation() throws Exception {
        assertSame(simpleMethodInvocation, calculator.getFirstUnverified(invocations));
        
        simpleMethodInvocationTwo.markVerified();
        simpleMethodInvocation.markVerified();
        
        assertSame(differentMethodInvocation, calculator.getFirstUnverified(invocations));
        
        differentMethodInvocation.markVerified();
        assertNull(calculator.getFirstUnverified(invocations));
    }
    
    @Test
    public void shouldGetFirstUndesiredWhenWantedNumberOfTimesIsZero() throws Exception {
        HasStackTrace firstUndesired = calculator.getFirstUndesiredInvocationStackTrace(invocations, new InvocationMatcher(simpleMethodInvocation), VerificationMode.times(0));
        HasStackTrace expected = simpleMethodInvocation.getStackTrace();
        assertSame(firstUndesired, expected);
    }
    
    @Test
    public void shouldGetFirstUndesiredWhenWantedNumberOfTimesIsOne() throws Exception {
        HasStackTrace firstUndesired = calculator.getFirstUndesiredInvocationStackTrace(invocations, new InvocationMatcher(simpleMethodInvocation), VerificationMode.times(1));
        HasStackTrace expected = simpleMethodInvocationTwo.getStackTrace();
        assertSame(firstUndesired, expected);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldBreakWhenThereAreNoUndesiredInvocations() throws Exception {
        calculator.getFirstUndesiredInvocationStackTrace(invocations, new InvocationMatcher(simpleMethodInvocation), VerificationMode.times(2));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldBreakWhenWantedInvocationsFigureIsBigger() throws Exception {
        calculator.getFirstUndesiredInvocationStackTrace(invocations, new InvocationMatcher(simpleMethodInvocation), VerificationMode.times(100));
    }
}

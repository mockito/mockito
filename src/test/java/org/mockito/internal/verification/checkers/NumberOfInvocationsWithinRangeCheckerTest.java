/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;
import org.mockitoutil.TestBase;

import java.util.LinkedList;

import static java.util.Arrays.asList;

public class NumberOfInvocationsWithinRangeCheckerTest extends TestBase {

    private NumberOfInvocationsWithinRangeChecker checker;
    private WithinRangeReporterStub reporterStub;
    private InvocationMatcher wanted;
    private LinkedList<Invocation> invocations;
    private InvocationsFinderStub finderStub;
    
    @Before
    public void setup() {
        reporterStub = new WithinRangeReporterStub();
        finderStub = new InvocationsFinderStub();
        checker = new NumberOfInvocationsWithinRangeChecker(reporterStub, finderStub);
        
        wanted = new InvocationBuilder().toInvocationMatcher();
        invocations = new LinkedList<Invocation>(asList(new InvocationBuilder().toInvocation()));
    }

    @Test
    public void shouldReportTooLittleActual() throws Exception {
        finderStub.actualToReturn.add(new InvocationBuilder().toInvocation());
        
        checker.check(invocations, wanted, 2, 3);
        
        assertEquals(1, reporterStub.actualCount);
        assertEquals(2, reporterStub.wantedCount);
        assertEquals(wanted, reporterStub.wanted);
    }

    @Test
    public void shouldReportWithLastInvocationStackTrace() throws Exception {
        Invocation first = new InvocationBuilder().toInvocation();
        Invocation second = new InvocationBuilder().toInvocation();
        
        finderStub.actualToReturn.addAll(asList(first, second));
        
        checker.check(invocations, wanted, 3, 4);
        
        assertSame(second.getLocation(), reporterStub.location);
    }
    
    @Test
    public void shouldNotReportWithLastInvocationStackTraceIfNoInvocationsFound() throws Exception {
        assertTrue(finderStub.actualToReturn.isEmpty());
        
        checker.check(invocations, wanted, 1, 2);
        
        assertNull(reporterStub.location);
    }
    
    @Test
    public void shouldReportWithFirstUndesiredInvocationStackTrace() throws Exception {
        Invocation first = new InvocationBuilder().toInvocation();
        Invocation second = new InvocationBuilder().toInvocation();
        Invocation third = new InvocationBuilder().toInvocation();
        
        finderStub.actualToReturn.addAll(asList(first, second, third));
        
        checker.check(invocations, wanted, 1, 2);
        
        assertSame(third.getLocation(), reporterStub.location);
    }
    
    @Test
    public void shouldReportTooManyActual() throws Exception {
        finderStub.actualToReturn.add(new InvocationBuilder().toInvocation());
        finderStub.actualToReturn.add(new InvocationBuilder().toInvocation());
        
        checker.check(invocations, wanted, 0, 1);
        
        assertEquals(2, reporterStub.actualCount);
        assertEquals(1, reporterStub.wantedCount);
        assertEquals(wanted, reporterStub.wanted);
    }
    
    @Test
    public void shouldReportNeverWantedButInvoked() throws Exception {
        Invocation invocation = new InvocationBuilder().toInvocation();
        finderStub.actualToReturn.add(invocation);
        
        checker.check(invocations, wanted, 0, 0);
        
        assertEquals(wanted, reporterStub.wanted);
        assertEquals(invocation.getLocation(), reporterStub.location);
    }
    
    @Test
    public void shouldMarkInvocationsAsVerifiedWhenRangeIsOneElement() throws Exception {
        Invocation invocation = new InvocationBuilder().toInvocation();
        finderStub.actualToReturn.add(invocation);
        assertFalse(invocation.isVerified());
        
        checker.check(invocations, wanted, 1, 1);
        
        assertTrue(invocation.isVerified());
    }

    @Test
    public void shouldMarkInvocationsAsVerifiedWhenAtTheBottomOfRange() throws Exception {
        Invocation invocation = new InvocationBuilder().toInvocation();
        finderStub.actualToReturn.add(invocation);
        assertFalse(invocation.isVerified());

        checker.check(invocations, wanted, 1, 3);

        assertTrue(invocation.isVerified());
    }

    @Test
    public void shouldMarkInvocationsAsVerifiedWhenInTheMiddleOfRange() throws Exception {
        Invocation invocation = new InvocationBuilder().toInvocation();
        finderStub.actualToReturn.add(invocation);
        finderStub.actualToReturn.add(invocation);
        assertFalse(invocation.isVerified());

        checker.check(invocations, wanted, 1, 3);

        assertTrue(invocation.isVerified());
    }

    @Test
    public void shouldMarkInvocationsAsVerifiedWhenAtTheTopOfRange() throws Exception {
        Invocation invocation = new InvocationBuilder().toInvocation();
        finderStub.actualToReturn.add(invocation);
        finderStub.actualToReturn.add(invocation);
        finderStub.actualToReturn.add(invocation);
        assertFalse(invocation.isVerified());

        checker.check(invocations, wanted, 1, 3);

        assertTrue(invocation.isVerified());
    }
}

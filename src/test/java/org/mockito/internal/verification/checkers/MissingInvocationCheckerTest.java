/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import static java.util.Arrays.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockitoutil.TestBase;

public class MissingInvocationCheckerTest extends TestBase {

    private MissingInvocationChecker checker;
    
    private InvocationsFinderStub finderStub;
    private ReporterStub reporterStub;
    
    private InvocationMatcher wanted;
    private List<Invocation> invocations;

    @Before
    public void setup() {
        reporterStub = new ReporterStub();
        finderStub = new InvocationsFinderStub();
        checker = new MissingInvocationChecker(finderStub, reporterStub);
        
        wanted = new InvocationBuilder().toInvocationMatcher();
        invocations = asList(new InvocationBuilder().toInvocation());
    }
    
    @Test
    public void shouldAskFinderForActualInvocations() {
        finderStub.actualToReturn.add(new InvocationBuilder().toInvocation());
        checker.check(invocations, wanted);
        
        assertSame(invocations, finderStub.invocations);
    }
    
    @Test
    public void shouldPassBecauseActualInvocationFound() {
        finderStub.actualToReturn.add(new InvocationBuilder().toInvocation());
        checker.check(invocations, wanted);
    }
    
    @Test
    public void shouldAskAnalyzerForSimilarInvocation() {
        checker.check(invocations, wanted);
        
        assertSame(invocations, finderStub.invocations);
    }
    
    @Test
    public void shouldReportWantedButNotInvoked() {
        //given          
        assertTrue(finderStub.actualToReturn.isEmpty());
        finderStub.similarToReturn = null;
        
        //when
        checker.check(invocations, wanted);
        
        //then
        assertEquals(wanted, reporterStub.wanted);
        assertNull(reporterStub.actualLocation);
    }
    
    @Test
    public void shouldReportWantedInvocationDiffersFromActual() {
        assertTrue(finderStub.actualToReturn.isEmpty());
        Invocation actualInvocation = new InvocationBuilder().toInvocation();
        finderStub.similarToReturn = actualInvocation;
        
        checker.check(invocations, wanted);
        
        assertNotNull(reporterStub.wanted);
        assertNotNull(reporterStub.actual);
        
        assertSame(actualInvocation.getLocation(), reporterStub.actualLocation);
    }
    
    class ReporterStub extends Reporter {
        private Object wanted;
        private String actual;
        private Location actualLocation;
        
        @Override
        public void wantedButNotInvoked(DescribedInvocation wanted, List<? extends DescribedInvocation> invocations) {
            this.wanted = wanted;
        }
        
        @Override public void argumentsAreDifferent(String wanted, String actual, Location actualLocation) {
                    this.wanted = wanted;
                    this.actual = actual;
                    this.actualLocation = actualLocation;
        }
    }
}

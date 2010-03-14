/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.checkers;

import static java.util.Arrays.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.VerificationModeBuilder;
import org.mockito.internal.reporting.SmartPrinter;
import org.mockito.internal.verification.InOrderContextImpl;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockitoutil.TestBase;

public class MissingInvocationInOrderCheckerTest extends TestBase {

    private MissingInvocationInOrderChecker checker;
    private ReporterStub reporterStub;
    private InvocationMatcher wanted;
    private LinkedList<Invocation> invocations;
    private InvocationsFinderStub finderStub;
    private InOrderContext context = new InOrderContextImpl();
    
    @Before
    public void setup() {
        reporterStub = new ReporterStub();
        finderStub = new InvocationsFinderStub();
        checker = new MissingInvocationInOrderChecker(finderStub, reporterStub);
        
        wanted = new InvocationBuilder().toInvocationMatcher();
        invocations = new LinkedList<Invocation>(asList(new InvocationBuilder().toInvocation()));
    }                                                                    

    @Test
    public void shouldPassWhenMatchingInteractionFound() throws Exception {
        Invocation actual = new InvocationBuilder().toInvocation();
        finderStub.allMatchingUnverifiedChunksToReturn.add(actual);
        
        checker.check(invocations, wanted, new VerificationModeBuilder().inOrder(), context);
    }
    
    @Test
    public void shouldReportWantedButNotInvoked() throws Exception {
        assertTrue(finderStub.allMatchingUnverifiedChunksToReturn.isEmpty());
        checker.check(invocations, wanted, new VerificationModeBuilder().inOrder(), context);
        
        assertEquals(wanted, reporterStub.wanted);
    }

    @Test
    public void shouldReportArgumentsAreDifferent() throws Exception {
        assertTrue(finderStub.findInvocations(invocations, wanted).isEmpty());
        finderStub.similarToReturn = new InvocationBuilder().toInvocation();
        checker.check(invocations, wanted, new VerificationModeBuilder().inOrder(), context);
        SmartPrinter printer = new SmartPrinter(wanted, finderStub.similarToReturn, 0);
        assertEquals(printer.getWanted(), reporterStub.wantedString);
        assertEquals(printer.getActual(), reporterStub.actual);
        assertEquals(finderStub.similarToReturn.getLocation(), reporterStub.actualLocation);
     }
    
    @Test
    public void shouldReportWantedDiffersFromActual() throws Exception {
        Invocation previous = new InvocationBuilder().toInvocation();
        finderStub.previousInOrderToReturn = previous;
        
        checker.check(invocations, wanted, new VerificationModeBuilder().inOrder(), context);
        
        assertEquals(wanted, reporterStub.wanted);
        assertEquals(previous, reporterStub.previous);
    }
    
    class ReporterStub extends Reporter {
        private PrintableInvocation wanted;
        private PrintableInvocation previous;
        private String wantedString;
        private String actual;
        private Location actualLocation;
        
        @Override public void wantedButNotInvokedInOrder(PrintableInvocation wanted, PrintableInvocation previous) {
            this.wanted = wanted;
            this.previous = previous;
        }
        
        @Override public void wantedButNotInvoked(PrintableInvocation wanted) {
            this.wanted = wanted;
        }

        @Override public void argumentsAreDifferent(String wanted, String actual, Location actualLocation) {
            this.wantedString = wanted;
            this.actual = actual;
            this.actualLocation = actualLocation;
        }
    }
}
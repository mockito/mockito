/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.junit.Ignore;
import org.mockitoutil.TestBase;

@Ignore
public class NoMoreInvocationsVerifierTest extends TestBase {

    //TODO change to NoMoreInteractionsModeTest
    
//    private NoMoreInteractionsMode mode;
//    private InvocationsFinderStub finder;
//    private ReporterStub reporterStub;
//
//    @Before
//    public void setup() {
//        finder = new InvocationsFinderStub();
//        reporterStub = new ReporterStub();
//        mode = new NoMoreInteractionsMode(finder, reporterStub);
//    }
    
//    @Test
//    public void shouldPassVerification() throws Exception {
//        finder.firstUnverifiedToReturn = null;
//        verifier.verify(null, null, MockitoVerificationMode.noMoreInteractions());
//    }
//    
//    @Test
//    public void shouldReportError() throws Exception {
//        Invocation firstUnverified = new InvocationBuilder().toInvocation();
//        finder.firstUnverifiedToReturn = firstUnverified;
//        List<Invocation> invocations = asList(new InvocationBuilder().toInvocation());
//        
//        verifier.verify(invocations, null, MockitoVerificationMode.noMoreInteractions());
//        
//        assertSame(invocations, finder.invocations);
//        
//        assertEquals(firstUnverified, reporterStub.undesired);
//        assertSame(firstUnverified.getStackTrace(), reporterStub.actualInvocationStackTrace);
//    }
//    
//    class ReporterStub extends Reporter {
//        private PrintableInvocation undesired;
//        private HasStackTrace actualInvocationStackTrace;
//        @Override public void noMoreInteractionsWanted(PrintableInvocation undesired, HasStackTrace actualInvocationStackTrace) {
//            this.undesired = undesired;
//            this.actualInvocationStackTrace = actualInvocationStackTrace;
//        }
//    }
}

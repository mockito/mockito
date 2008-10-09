/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static java.util.Arrays.*;
import static org.mockitoutil.ExtraMatchers.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.CanPrintInMultilines;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.VerificationMode;
import org.mockito.internal.progress.VerificationModeBuilder;
import org.mockito.internal.progress.VerificationModeImpl;
import org.mockitoutil.TestBase;

public class VerifyingRecorderTest extends TestBase {
    
    private VerifyingRecorder recorder;
    private VerifierStub verifierStub;
    
    private Invocation simpleMethod;
    private InvocationMatcher differentMethod;
    
    @Before
    public void setup() {
        verifierStub = new VerifierStub();
        recorder = new VerifyingRecorder(new AllInvocationsFinder() {
            public List<Invocation> getAllInvocations(List<? extends Object> mocks) {
                return asList(simpleMethod, differentMethod.getInvocation());
            }}, asList(verifierStub));

        simpleMethod = new InvocationBuilder().simpleMethod().toInvocation();
        differentMethod = new InvocationBuilder().differentMethod().toInvocationMatcher();
    }
    
    @Test
    public void shouldVerify() {
        recorder.recordInvocation(simpleMethod);
        
        VerificationMode mode = VerificationModeImpl.atLeastOnce();
        recorder.verify(differentMethod, mode);
        
        assertSame(verifierStub.mode, mode);
        assertSame(verifierStub.wanted, differentMethod);
        assertThat(verifierStub.invocations, hasExactlyInOrder(simpleMethod));
    }
    
    @Test
    public void shouldVerifyInOrder() {
        VerificationMode inOrderMode = new VerificationModeBuilder().inOrder();
        recorder.verify(differentMethod, inOrderMode);
        
        assertThat(verifierStub.invocations, hasExactlyInOrder(simpleMethod, differentMethod.getInvocation()));
    }
    
    @Test
    public void shouldNotReturnToStringMethod() throws Exception {
        Invocation toString = new InvocationBuilder().method("toString").toInvocation();
        Invocation simpleMethod = new InvocationBuilder().simpleMethod().toInvocation();
        
        recorder.recordInvocation(toString);
        recorder.recordInvocation(simpleMethod);
        
        assertTrue(recorder.getRegisteredInvocations().contains(simpleMethod));
        assertFalse(recorder.getRegisteredInvocations().contains(toString));
    }
    
    class VerifierStub implements Verifier {
        private List<Invocation> invocations;
        private CanPrintInMultilines wanted;
        private VerificationMode mode;
        public void verify(List<Invocation> invocations, InvocationMatcher wanted, VerificationMode mode) {
            this.invocations = invocations;
            this.wanted = wanted;
            this.mode = mode;
        }
        public boolean appliesTo(VerificationMode mode) {
            return true;
        }
    }
}
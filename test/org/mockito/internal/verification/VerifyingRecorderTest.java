/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static java.util.Arrays.*;
import static org.junit.Assert.*;
import static org.mockito.util.ExtraMatchers.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.GlobalInvocationsFinder;
import org.mockito.internal.progress.VerificationModeBuilder;
import org.mockito.internal.progress.VerificationModeImpl;

public class VerifyingRecorderTest extends RequiresValidState {
    
    private VerifyingRecorder recorder;
    private VerifierStub verifierStub;
    
    private Invocation simpleMethod;
    private InvocationMatcher differentMethod;
    
    @Before
    public void setup() {
        verifierStub = new VerifierStub();
        recorder = new VerifyingRecorder(new GlobalInvocationsFinder() {
            public List<Invocation> getAllInvocations(List<? extends Object> mocks) {
                return asList(simpleMethod, differentMethod.getInvocation());
            }}, asList(verifierStub));

        simpleMethod = new InvocationBuilder().simpleMethod().toInvocation();
        differentMethod = new InvocationBuilder().differentMethod().toInvocationMatcher();
    }
    
    @Test
    public void shouldVerify() {
        recorder.recordInvocation(simpleMethod);
        
        VerificationModeImpl mode = VerificationModeImpl.atLeastOnce();
        recorder.verify(differentMethod, mode);
        
        assertSame(verifierStub.mode, mode);
        assertSame(verifierStub.wanted, differentMethod);
        assertThat(verifierStub.invocations, collectionHasExactlyInOrder(simpleMethod));
    }
    
    @Test
    public void shouldVerifyInOrder() {
        VerificationModeImpl inOrderMode = new VerificationModeBuilder().inOrder();
        recorder.verify(differentMethod, inOrderMode);
        
        assertThat(verifierStub.invocations, collectionHasExactlyInOrder(simpleMethod, differentMethod.getInvocation()));
    }
    
    class VerifierStub implements Verifier {
        private List<Invocation> invocations;
        private InvocationMatcher wanted;
        private VerificationModeImpl mode;
        public void verify(List<Invocation> invocations, InvocationMatcher wanted, VerificationModeImpl mode) {
            this.invocations = invocations;
            this.wanted = wanted;
            this.mode = mode;
        }
    }
}
/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.util.ExtraMatchers.collectionHasExactlyInOrder;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsCalculator;
import org.mockito.internal.invocation.InvocationsChunker;
import org.mockito.internal.invocation.InvocationsMarker;
import org.mockito.internal.progress.VerificationMode;
import org.mockito.internal.progress.VerificationModeBuilder;

public class VerifyingRecorderTest extends RequiresValidState {
    
    private VerifyingRecorder recorder;
    private VerifierStub verifierStub;
    private InvocationsMarkerStub markerStub;
    private InvocationsChunkerStub chunkerStub;
    
    private Invocation simpleMethod;
    private InvocationMatcher differentMethod;
    
    @Before
    public void setup() {
        verifierStub = new VerifierStub();
        markerStub = new InvocationsMarkerStub();
        chunkerStub = new InvocationsChunkerStub();
        recorder = new VerifyingRecorder(chunkerStub, markerStub, Arrays.<Verifier>asList(verifierStub));

        simpleMethod = new InvocationBuilder().simpleMethod().toInvocation();
        differentMethod = new InvocationBuilder().differentMethod().toInvocationMatcher();
    }
    
    @Test
    public void shouldMarkInvocationsAsVerified() {
        recorder.recordInvocation(simpleMethod);
        
        VerificationMode mode = VerificationMode.atLeastOnce();
        recorder.verify(differentMethod, mode);
        
        assertThat(markerStub.invocations, collectionHasExactlyInOrder(simpleMethod));
        assertEquals(markerStub.mode, mode);
        assertEquals(markerStub.wanted, differentMethod);
    }
    
    @Test
    public void shouldVerify() {
        recorder.recordInvocation(simpleMethod);
        
        VerificationMode mode = VerificationMode.atLeastOnce();
        recorder.verify(differentMethod, mode);
        
        assertEquals(verifierStub.mode, mode);
        assertSame(verifierStub.wanted, differentMethod);
        assertThat(verifierStub.calculator.getInvocations(), collectionHasExactlyInOrder(simpleMethod));
    }
    
    @Test
    public void shouldVerifyStrictly() {
        recorder.recordInvocation(simpleMethod);
        
        VerificationMode mode = new VerificationModeBuilder().strict();
        recorder.verify(differentMethod, mode);
        
        assertEquals(verifierStub.mode, mode);
        assertEquals(verifierStub.wanted, differentMethod);
        assertThat(verifierStub.calculator.getInvocations(), collectionHasExactlyInOrder(differentMethod.getInvocation()));
    }
    
    @Test
    public void shouldNotMarkInvocationsAsVerifiedWhenModeIsNotExplicit() {
        VerificationMode mode = VerificationMode.noMoreInteractions();
        recorder.verify(mode);
        assertNull(markerStub.mode);
    }
    
    class InvocationsMarkerStub extends InvocationsMarker {
        private List<Invocation> invocations;
        private InvocationMatcher wanted;
        private VerificationMode mode;
        @Override public void markInvocationsAsVerified(List<Invocation> invocations, InvocationMatcher wanted, VerificationMode mode) {
            this.invocations = invocations;
            this.wanted = wanted;
            this.mode = mode;
            
            assertNotNull("marking should happen after verification", verifierStub.calculator);
        }
    }
    
    class VerifierStub implements Verifier {
        private InvocationsCalculator calculator;
        private InvocationMatcher wanted;
        private VerificationMode mode;
        public void verify(InvocationsCalculator calculator, InvocationMatcher wanted, VerificationMode mode) {
            this.calculator = calculator;
            this.wanted = wanted;
            this.mode = mode;
        }
    }
    
    class InvocationsChunkerStub extends InvocationsChunker {
        public InvocationsChunkerStub() {
            super(null);
        }
        @Override public List<Invocation> getFirstUnverifiedInvocationChunk(List<Object> mocks) {
            return Arrays.asList(differentMethod.getInvocation());
        }
    }
}

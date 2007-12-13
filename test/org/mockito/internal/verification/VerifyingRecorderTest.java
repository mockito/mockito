/*
 * Copyright (c) 2007, Szczepan Faber. 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.util.ExtraMatchers.collectionHasExactlyInOrder;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsCalculator;
import org.mockito.internal.invocation.InvocationsChunker;
import org.mockito.internal.invocation.InvocationsMarker;
import org.mockito.internal.progress.OngoingVerifyingMode;

public class VerifyingRecorderTest {
    
    private VerifyingRecorder recorder;
    private VerifierStub verifier;
    private InvocationsMarkerStub marker;
    private InvocationsChunkerStub chunker;
    
    private Invocation simpleMethod;
    private InvocationMatcher differentMethod;
    
    @Before
    public void setup() {
        verifier = new VerifierStub();
        marker = new InvocationsMarkerStub();
        chunker = new InvocationsChunkerStub();
        recorder = new VerifyingRecorder(chunker, marker, Arrays.<Verifier>asList(verifier));

        simpleMethod = new InvocationBuilder().simpleMethod().toInvocation();
        differentMethod = new InvocationBuilder().differentMethod().toInvocationMatcher();
    }
    
    @Test
    public void shouldMarkInvocationsAsVerified() {
        recorder.recordInvocation(simpleMethod);
        
        OngoingVerifyingMode mode = OngoingVerifyingMode.atLeastOnce();
        recorder.verify(differentMethod, mode);
        
        assertThat(marker.invocations, collectionHasExactlyInOrder(simpleMethod));
        assertEquals(marker.mode, mode);
        assertEquals(marker.wanted, differentMethod);
    }
    
    @Test
    public void shouldVerify() {
        recorder.recordInvocation(simpleMethod);
        
        OngoingVerifyingMode mode = OngoingVerifyingMode.atLeastOnce();
        recorder.verify(differentMethod, mode);
        
        assertEquals(verifier.mode, mode);
        assertSame(verifier.wanted, differentMethod);
        assertThat(verifier.calculator.getInvocations(), collectionHasExactlyInOrder(simpleMethod));
    }
    
    @Test
    public void shouldVerifyInOrder() {
        recorder.recordInvocation(simpleMethod);
        
        OngoingVerifyingMode mode = OngoingVerifyingMode.inOrder(10, Arrays.<Object>asList("mock"));
        recorder.verify(differentMethod, mode);
        
        assertEquals(verifier.mode, mode);
        assertEquals(verifier.wanted, differentMethod);
        assertThat(verifier.calculator.getInvocations(), collectionHasExactlyInOrder(differentMethod.getInvocation()));
    }
    
    class InvocationsMarkerStub extends InvocationsMarker {
        private List<Invocation> invocations;
        private InvocationMatcher wanted;
        private OngoingVerifyingMode mode;
        @Override public void markInvocationsAsVerified(List<Invocation> invocations, InvocationMatcher wanted, OngoingVerifyingMode mode) {
            this.invocations = invocations;
            this.wanted = wanted;
            this.mode = mode;
            
            assertNotNull("marking should happen after verification", verifier.calculator);
        }
    }
    
    class VerifierStub implements Verifier {
        private InvocationsCalculator calculator;
        private InvocationMatcher wanted;
        private OngoingVerifyingMode mode;
        public void verify(InvocationsCalculator calculator, InvocationMatcher wanted, OngoingVerifyingMode mode) {
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

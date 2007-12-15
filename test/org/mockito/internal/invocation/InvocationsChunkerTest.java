/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.util.ExtraMatchers.collectionHasExactlyInOrder;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;

public class InvocationsChunkerTest extends RequiresValidState {

    private InvocationsChunker chunker;
    private Invocation invocationOneChunkOne;
    private Invocation invocationTwoChunkOne;
    private Invocation differentInvocationChunkTwo;
    private Invocation invocationThreeChunkThree;

    @Before
    public void setup() throws Exception {
        invocationOneChunkOne = new InvocationBuilder().simpleMethod().seq(1).toInvocation();
        invocationTwoChunkOne = new InvocationBuilder().simpleMethod().seq(2).toInvocation();
        differentInvocationChunkTwo = new InvocationBuilder().differentMethod().seq(3).toInvocation();
        invocationThreeChunkThree = new InvocationBuilder().simpleMethod().seq(4).toInvocation();
        
        chunker = new InvocationsChunker(new InvocationsFinder() {
            public List<Invocation> allInvocationsInOrder(List<Object> mocks) {
                return Arrays.asList(invocationOneChunkOne, invocationTwoChunkOne, differentInvocationChunkTwo, invocationThreeChunkThree);
            }});
    }

    @Test
    public void shouldGetFirstUnverifiedInvocationChunk() throws Exception {
        List<Invocation> chunk = chunker.getFirstUnverifiedInvocationChunk(null);
        assertThat(chunk, collectionHasExactlyInOrder(invocationOneChunkOne, invocationTwoChunkOne));
    }
    
    @Test
    public void shouldGetSecondUnverifiedInvocationChunk() throws Exception {
        invocationOneChunkOne.markVerifiedStrictly();
        invocationTwoChunkOne.markVerifiedStrictly();
        
        List<Invocation> chunk = chunker.getFirstUnverifiedInvocationChunk(null);
        
        assertThat(chunk, collectionHasExactlyInOrder(differentInvocationChunkTwo));
    }
    
    @Test
    public void shouldGetThirdUnverifiedInvocationChunk() throws Exception {
        invocationOneChunkOne.markVerifiedStrictly();
        invocationTwoChunkOne.markVerifiedStrictly();
        differentInvocationChunkTwo.markVerifiedStrictly();
        
        List<Invocation> chunk = chunker.getFirstUnverifiedInvocationChunk(null);
        
        assertThat(chunk, collectionHasExactlyInOrder(invocationThreeChunkThree));
    }
    
    @Test
    public void shouldNotGetInvocationsChunk() throws Exception {
        invocationOneChunkOne.markVerifiedStrictly();
        invocationTwoChunkOne.markVerifiedStrictly();
        differentInvocationChunkTwo.markVerifiedStrictly();
        invocationThreeChunkThree.markVerifiedStrictly();
        
        List<Invocation> chunk = chunker.getFirstUnverifiedInvocationChunk(null);
        
        assertTrue(chunk.isEmpty());
    }
}

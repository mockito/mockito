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
    private Invocation simpleMethodInvocation;
    private Invocation simpleMethodInvocationTwo;
    private Invocation differentMethodInvocation;
    private Invocation simpleMethodInvocationThree;

    @Before
    public void setup() throws Exception {
        simpleMethodInvocation = new InvocationBuilder().simpleMethod().seq(1).toInvocation();
        simpleMethodInvocationTwo = new InvocationBuilder().simpleMethod().seq(2).toInvocation();
        differentMethodInvocation = new InvocationBuilder().differentMethod().seq(3).toInvocation();
        simpleMethodInvocationThree = new InvocationBuilder().simpleMethod().seq(4).toInvocation();
        
        chunker = new InvocationsChunker(new InvocationsFinder() {
            public List<Invocation> allInvocationsInOrder(List<Object> mocks) {
                return Arrays.asList(simpleMethodInvocation, simpleMethodInvocationTwo, differentMethodInvocation, simpleMethodInvocationThree);
            }});
    }

    @Test
    public void shouldGetFirstUnverifiedInvocationChunk() throws Exception {
        List<Invocation> chunk = chunker.getFirstUnverifiedInvocationChunk(null);
        assertThat(chunk, collectionHasExactlyInOrder(simpleMethodInvocation, simpleMethodInvocationTwo));
    }
    
    @Test
    public void shouldGetSecondUnverifiedInvocationChunk() throws Exception {
        simpleMethodInvocation.markVerifiedInOrder();
        simpleMethodInvocationTwo.markVerifiedInOrder();
        
        List<Invocation> chunk = chunker.getFirstUnverifiedInvocationChunk(null);
        
        assertThat(chunk, collectionHasExactlyInOrder(differentMethodInvocation));
    }
    
    @Test
    public void shouldGetThirdUnverifiedInvocationChunk() throws Exception {
        simpleMethodInvocation.markVerifiedInOrder();
        simpleMethodInvocationTwo.markVerifiedInOrder();
        differentMethodInvocation.markVerifiedInOrder();
        
        List<Invocation> chunk = chunker.getFirstUnverifiedInvocationChunk(null);
        
        assertThat(chunk, collectionHasExactlyInOrder(simpleMethodInvocationThree));
    }
    
    @Test
    public void shouldNotGetInvocationsChunk() throws Exception {
        simpleMethodInvocation.markVerifiedInOrder();
        simpleMethodInvocationTwo.markVerifiedInOrder();
        differentMethodInvocation.markVerifiedInOrder();
        simpleMethodInvocationThree.markVerifiedInOrder();
        
        List<Invocation> chunk = chunker.getFirstUnverifiedInvocationChunk(null);
        
        assertTrue(chunk.isEmpty());
    }
}

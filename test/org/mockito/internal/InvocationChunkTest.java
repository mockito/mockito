/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import org.junit.Test;
import static org.junit.Assert.*;

public class InvocationChunkTest {

    @Test
    public void shouldMarkAllAsVerified() throws Exception {
        Invocation i = new InvocationBuilder().toInvocation();
        Invocation i2 = new InvocationBuilder().toInvocation();
        
        InvocationChunk chunk = new InvocationChunk(i);
        chunk.add(i2);
        
        chunk.markAllInvocationsAsVerified();
        
        assertTrue(i.isVerified());
        assertTrue(i.isVerifiedInOrder());
        
        assertTrue(i2.isVerified());
        assertTrue(i2.isVerifiedInOrder());
    }
}

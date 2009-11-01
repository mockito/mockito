/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.util.Arrays;

import org.junit.Test;
import org.mockito.internal.util.ObjectBox;
import org.mockitoutil.TestBase;

@SuppressWarnings("serial")
public class InvocationMarkerTest extends TestBase {

    @Test
    public void shouldMarkInvocationAsVerified() {
        //given
        InvocationMarker marker = new InvocationMarker();
        Invocation i = new InvocationBuilder().toInvocation();
        InvocationMatcher im = new InvocationBuilder().toInvocationMatcher();
        assertFalse(i.isVerified());
        
        //when
        marker.markVerified(Arrays.asList(i), im);
        
        //then
        assertTrue(i.isVerified());
    }
    
    @Test
    public void shouldCaptureArguments() {
        //given
        InvocationMarker marker = new InvocationMarker();
        Invocation i = new InvocationBuilder().toInvocation();
        final ObjectBox box = new ObjectBox();
        CapturesArgumensFromInvocation c = new CapturesArgumensFromInvocation() {
            public void captureArgumentsFrom(Invocation i) {
                box.put(i);
            }};
        
        //when
        marker.markVerified(Arrays.asList(i), c);
        
        //then
        assertEquals(i, box.getObject());
    }

    @Test
    public void shouldMarkInvocationsAsVerifiedInOrder() {
        //given
        InvocationMarker marker = new InvocationMarker();
        Invocation i = new InvocationBuilder().toInvocation();
        InvocationMatcher im = new InvocationBuilder().toInvocationMatcher();
        assertFalse(i.isVerifiedInOrder());
        assertFalse(i.isVerified());
        
        //when
        marker.markVerifiedInOrder(Arrays.asList(i), im);
        
        //then
        assertTrue(i.isVerifiedInOrder());
        assertTrue(i.isVerified());
    }
}

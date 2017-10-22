/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import org.junit.Test;
import org.mockito.internal.verification.InOrderContextImpl;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;
import org.mockitoutil.TestBase;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class InvocationMarkerTest extends TestBase {

    @Test
    public void shouldMarkInvocationAsVerified() {
        //given
        Invocation i = new InvocationBuilder().toInvocation();
        InvocationMatcher im = new InvocationBuilder().toInvocationMatcher();
        assertFalse(i.isVerified());

        //when
        InvocationMarker.markVerified(Arrays.asList(i), im);

        //then
        assertTrue(i.isVerified());
    }

    @Test
    public void shouldCaptureArguments() {
        //given
        Invocation i = new InvocationBuilder().toInvocation();
        final AtomicReference<Invocation> box = new AtomicReference<Invocation>();
        MatchableInvocation c = new InvocationMatcher(i) {
            public void captureArgumentsFrom(Invocation i) {
                box.set(i);
            }};

        //when
        InvocationMarker.markVerified(Arrays.asList(i), c);

        //then
        assertEquals(i, box.get());
    }

    @Test
    public void shouldMarkInvocationsAsVerifiedInOrder() {
        //given
        InOrderContextImpl context = new InOrderContextImpl();

        Invocation i = new InvocationBuilder().toInvocation();
        InvocationMatcher im = new InvocationBuilder().toInvocationMatcher();
        assertFalse(context.isVerified(i));
        assertFalse(i.isVerified());

        //when
        InvocationMarker.markVerifiedInOrder(Arrays.asList(i), im, context);

        //then
        assertTrue(context.isVerified(i));
        assertTrue(i.isVerified());
    }
}

/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockitoutil.TestBase;

// see issue 188
public class CaptorAnnotationAutoboxingTest extends TestBase {

    interface Fun {
        void doFun(double prmitive);

        void moreFun(int howMuch);
    }

    @Mock Fun fun;
    @Captor ArgumentCaptor<Double> captor;

    @Test
    public void shouldAutoboxSafely() {
        // given
        fun.doFun(1.0);

        // then
        verify(fun).doFun(captor.capture());
        assertEquals(Double.valueOf(1.0), captor.getValue());
    }

    @Captor ArgumentCaptor<Integer> intCaptor;

    @Test
    public void shouldAutoboxAllPrimitives() {
        verify(fun, never()).moreFun(intCaptor.capture());
    }
}

/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.debugging;

import static org.mockito.BDDMockito.*;

import org.junit.Test;
import org.mockito.Mock;
import org.mockitoutil.TestBase;

public class PrintingInvocationsDetectsUnusedStubTest extends TestBase {

    @Mock Foo mock;
    @Mock Foo mockTwo;

    @Test
    public void shouldDetectUnusedStubbingWhenPrinting() throws Exception {
        //given
        given(mock.giveMeSomeString("different arg")).willReturn("foo");
        mock.giveMeSomeString("arg");

        //when
        String log = NewMockito.debug().printInvocations(mock, mockTwo);

        //then
        assertContainsIgnoringCase("unused", log);
    }
}
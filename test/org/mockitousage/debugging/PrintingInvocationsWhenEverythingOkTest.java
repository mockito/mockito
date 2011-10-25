/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.debugging;

import static org.mockito.BDDMockito.*;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;
import org.mockitoutil.TestBase;

public class PrintingInvocationsWhenEverythingOkTest extends TestBase {

    @Mock Foo mock;
    @Mock Foo mockTwo;

    @Test
    public void shouldPrintInvocationsWhenStubbingNotUsed() throws Exception {
        //given
        performStubbing();
        //when
        businessLogicWithAsking("arg");
        //then
        verify(mockTwo).doSomething("foo");
    }

    private void performStubbing() {
        given(mock.giveMeSomeString("arg")).willReturn("foo");
    }

    private void businessLogicWithAsking(String name) {
        String out = mock.giveMeSomeString(name);
        businessLogicWithTelling(out);
    }

    private void businessLogicWithTelling(String out) {
        mockTwo.doSomething(out);
    }

    @After
    public void printInvocations() {
        String log = NewMockito.debug().printInvocations(mock, mockTwo);
        //asking
        assertContains("giveMeSomeString(\"arg\")", log);
        assertContains(".businessLogicWithAsking(", log);
        //telling
        assertContains("doSomething(\"foo\")", log);
        assertContains(".businessLogicWithTelling(", log);
        //stubbing
        assertContains("giveMeSomeString(\"arg\")", log);
        assertContains(".performStubbing(", log);
    }
}
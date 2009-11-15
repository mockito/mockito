package org.mockitousage.debugging;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockitoutil.TestBase;

import static org.mockito.BDDMockito.*;

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
        String log = Mockito.debug().printInvocations(mock, mockTwo);
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
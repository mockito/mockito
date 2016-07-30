/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.debugging;

import org.junit.After;
import org.junit.Test;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockitoutil.TestBase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

public class PrintingInvocationsWhenStubNotUsedTest extends TestBase {

    Foo mock = mock(Foo.class);
    Foo mockTwo = mock(Foo.class);

    @Test(expected = ArgumentsAreDifferent.class)
    public void shouldPrintInvocationsWhenStubbingNotUsed() throws Exception {
        //given
        performStubbing();
        //when
        businessLogicWithAsking("arg");
        //then
        verify(mockTwo).doSomething("foo");
    }

    private void performStubbing() {
        given(mock.giveMeSomeString("different arg")).willReturn("foo");
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
        assertThat(log)
            .contains("giveMeSomeString(\"arg\")")
            .contains(".businessLogicWithAsking(");
        //telling
        assertThat(log)
            .contains("doSomething(null)")
            .contains(".businessLogicWithTelling(");
        //stubbing
        assertThat(log)
            .contains("giveMeSomeString(\"different arg\")")
            .contains(".performStubbing(");
    }
}
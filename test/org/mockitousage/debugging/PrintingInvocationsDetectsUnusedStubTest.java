package org.mockitousage.debugging;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockitoutil.TestBase;

import static org.mockito.BDDMockito.*;

public class PrintingInvocationsDetectsUnusedStubTest extends TestBase {

    @Mock Foo mock;
    @Mock Foo mockTwo;

    @Test
    public void shouldDetectUnusedStubbingWhenPrinting() throws Exception {
        //given
        given(mock.giveMeSomeString("different arg")).willReturn("foo");
        mock.giveMeSomeString("arg");

        //when
        String log = Mockito.debug().printInvocations(mock, mockTwo);

        //then
        assertContainsIgnoringCase("unused", log);
    }
}
package org.mockitousage.debugging;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockitousage.IMethods;

import static org.mockito.BDDMockito.*;

@Ignore
public class DebuggingTestCasesTest {

    IMethods mock = mock(IMethods.class);
    IMethods mockTwo = mock(IMethods.class);

    @Test
    public void shouldPointOutStubCalledWithDifferentArg() throws Exception {
        //given
        given(mock.simpleMethod("different arg")).willReturn("foo");
        //when
        businessLogic("arg");
        //then
        verify(mockTwo).oneArg("foo");
    }

    private void businessLogic(String name) {
        String out = mock.simpleMethod(name);
        mockTwo.oneArg(out);
    }

    @After
    public void checkStubs() {
        Mockito.debug().printInvocations(mock, mockTwo);
    }
}
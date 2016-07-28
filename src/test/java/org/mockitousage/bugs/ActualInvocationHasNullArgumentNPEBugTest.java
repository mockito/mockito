/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.bugs;

import org.junit.Test;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.*;

public class ActualInvocationHasNullArgumentNPEBugTest extends TestBase {
    
    public interface Fun {
        String doFun(String something);
    }

    @Test
    public void shouldAllowPassingNullArgument() {
        //given
        Fun mockFun = mock(Fun.class);
        when(mockFun.doFun((String) anyObject())).thenReturn("value");

        //when
        mockFun.doFun(null);

        //then
        try {
            verify(mockFun).doFun("hello");
            fail();
        } catch(AssertionError r) {
            //it's ok, we just want to reproduce the bug
        }
    }
}
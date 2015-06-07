/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.bugs;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockitoutil.TestBase;

public class ActualInvocationHasNullArgumentNPEBugTest extends TestBase {
    
    public interface Fun {
        String doFun(final String something);
    }

    @Test
    public void shouldAllowPassingNullArgument() {
        //given
        final Fun mockFun = mock(Fun.class);
        when(mockFun.doFun((String) anyObject())).thenReturn("value");

        //when
        mockFun.doFun(null);

        //then
        try {
            verify(mockFun).doFun("hello");
            fail();
        } catch(final AssertionError r) {
            //it's ok, we just want to reproduce the bug
        }
    }
}
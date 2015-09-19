/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.spies;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitoutil.TestBase;

@SuppressWarnings({"unchecked"})
public class SpyingOnInterfacesTest extends TestBase {

    @Test
    public void shouldFailFastWhenCallingRealMethodOnInterface() throws Exception {
        List list = mock(List.class);
        try {
            //when
            when(list.get(0)).thenCallRealMethod();
            //then
            fail();
        } catch (MockitoException e) {}
    }
    
    @Test
    public void shouldFailInRuntimeWhenCallingRealMethodOnInterface() throws Exception {
        //given
        List list = mock(List.class);
        when(list.get(0)).thenAnswer(
            new Answer() {
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    return invocation.callRealMethod();
                }
            }
        );
        try {
            //when
            list.get(0);            
            //then
            fail();
        } catch (MockitoException e) {}
    }
}
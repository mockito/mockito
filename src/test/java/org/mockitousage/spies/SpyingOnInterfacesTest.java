/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.spies;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitoutil.TestBase;

import java.util.List;

import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings({"unchecked"})
public class SpyingOnInterfacesTest extends TestBase {

    @Test
    public void shouldFailFastWhenCallingRealMethodOnInterface() throws Exception {
        List<?> list = mock(List.class);
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
        List<Object> list = mock(List.class);
        when(list.get(0)).thenAnswer(
            new Answer<Object>() {
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
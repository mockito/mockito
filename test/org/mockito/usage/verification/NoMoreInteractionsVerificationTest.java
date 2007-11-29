/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.usage.verification;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Test;
import org.mockito.exceptions.VerificationError;

@SuppressWarnings("unchecked")
public class NoMoreInteractionsVerificationTest {

    @Test
    public void shouldStubbingNotRegisterRedundantInteractions() throws Exception {
        List mock = mock(List.class);
        stub(mock.add("one")).andReturn(true);
        stub(mock.add("two")).andReturn(true);

        mock.add("one");
        
        verify(mock).add("one");
        verifyNoMoreInteractions(mock);
    }
    
    @Test
    public void shouldVerifyWhenExpectedNumberOfInvocationsUsed() throws Exception {
        List mock = mock(List.class);

        mock.add("one");
        mock.add("one");
        mock.add("one");
        
        verify(mock, 3).add("one");
        
        verifyNoMoreInteractions(mock);
    }
    
    @Test
    public void shouldVerifyNoInteractionsAsManyTimesAsYouWant() throws Exception {
        List mock = mock(List.class);

        verifyNoMoreInteractions(mock);
        verifyNoMoreInteractions(mock);
        
        verifyZeroInteractions(mock);
        verifyZeroInteractions(mock);
    }
    
    @Test
    public void shouldFailZeroInteractionsVerification() throws Exception {
        List mock = mock(List.class);

        mock.clear();
        
        try {
            verifyZeroInteractions(mock);
            fail();
        } catch (VerificationError e) {
            //cool
        }
    }
    
    @Test
    public void shouldFailNoMoreInteractionsVerification() throws Exception {
        List mock = mock(List.class);

        mock.clear();
        
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (VerificationError e) {
            //cool
        }
    }
    
    @Test
    public void shouldVerifyOneMockButFailOnOther() throws Exception {
        List list = mock(List.class);
        Map map = mock(Map.class);

        list.add("one");
        list.add("one");
        
        map.put("one", 1);
        
        verify(list, 2).add("one");
        
        verifyNoMoreInteractions(list);
        try {
            verifyZeroInteractions(map);
            fail();
        } catch (VerificationError e) {
            //cool
        }
    }
}
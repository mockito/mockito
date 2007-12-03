/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.*;
import org.mockito.exceptions.VerificationError;
import org.mockito.util.RequiresValidState;

@SuppressWarnings("unchecked")
public class NoMoreInteractionsVerificationTest extends RequiresValidState {

    private LinkedList mock;
    
    @Before
    public void setup() {
        mock = mock(LinkedList.class);
    }

    @Test
    public void shouldStubbingNotRegisterRedundantInteractions() throws Exception {
        stub(mock.add("one")).andReturn(true);
        stub(mock.add("two")).andReturn(true);

        mock.add("one");
        
        verify(mock).add("one");
        verifyNoMoreInteractions(mock);
    }
    
    @Test
    public void shouldVerifyWhenWantedNumberOfInvocationsUsed() throws Exception {
        mock.add("one");
        mock.add("one");
        mock.add("one");
        
        verify(mock, 3).add("one");
        
        verifyNoMoreInteractions(mock);
    }
    
    @Test
    public void shouldVerifyNoInteractionsAsManyTimesAsYouWant() throws Exception {
        verifyNoMoreInteractions(mock);
        verifyNoMoreInteractions(mock);
        
        verifyZeroInteractions(mock);
        verifyZeroInteractions(mock);
    }
    
    @Test
    public void shouldFailZeroInteractionsVerification() throws Exception {
        mock.clear();
        
        try {
            verifyZeroInteractions(mock);
            fail();
        } catch (VerificationError e) {}
    }
    
    @Test
    public void shouldFailNoMoreInteractionsVerification() throws Exception {
        mock.clear();
        
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (VerificationError e) {}
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
        } catch (VerificationError e) {}
    }
}
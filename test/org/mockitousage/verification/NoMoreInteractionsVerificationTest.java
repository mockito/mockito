/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class NoMoreInteractionsVerificationTest extends TestBase {

    private LinkedList mock;
    
    @Before
    public void setup() {
        mock = mock(LinkedList.class);
    }

    @Test
    public void shouldStubbingNotRegisterRedundantInteractions() throws Exception {
        when(mock.add("one")).thenReturn(true);
        when(mock.add("two")).thenReturn(true);

        mock.add("one");
        
        verify(mock).add("one");
        verifyNoMoreInteractions(mock);
    }
    
    @Test
    public void shouldVerifyWhenWantedNumberOfInvocationsUsed() throws Exception {
        mock.add("one");
        mock.add("one");
        mock.add("one");
        
        verify(mock, times(3)).add("one");
        
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
        } catch (NoInteractionsWanted e) {}
    }
    
    @Test
    public void shouldFailNoMoreInteractionsVerification() throws Exception {
        mock.clear();
        
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (NoInteractionsWanted e) {}
    }
    
    @Test
    public void shouldPrintAllInvocationsWhenVerifyingNoMoreInvocations() throws Exception {
        mock.add(1);
        mock.add(2);
        mock.clear();
        
        verify(mock).add(2);
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (NoInteractionsWanted e) {
            assertContains("list of all invocations", e.getMessage());
        }
    }
    
    @Test
    public void shouldNotContainAllInvocationsWhenSingleUnwantedFound() throws Exception {
        mock.add(1);
        
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (NoInteractionsWanted e) {
            assertNotContains("list of all invocations", e.getMessage());
        }
    }    
    
    @Test
    public void shouldVerifyOneMockButFailOnOther() throws Exception {
        List list = mock(List.class);
        Map map = mock(Map.class);

        list.add("one");
        list.add("one");
        
        map.put("one", 1);
        
        verify(list, times(2)).add("one");
        
        verifyNoMoreInteractions(list);
        try {
            verifyZeroInteractions(map);
            fail();
        } catch (NoInteractionsWanted e) {}
    }
    
    @SuppressWarnings("all")
    @Test(expected=MockitoException.class)
    public void verifyNoMoreInteractionsShouldScreamWhenNullPassed() throws Exception {
        verifyNoMoreInteractions((Object[])null);
    }
}
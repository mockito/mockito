/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.exceptions.verification.VerifcationInOrderFailure;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class AtMostXVerificationTest extends TestBase {

    private List mock;
    private List mockTwo;
    
    @Before public void setup() {
        mock = Mockito.mock(List.class);
        mockTwo = Mockito.mock(List.class);
    }

    @Test
    public void shouldVerifyAtLeastOnce() throws Exception {
        mock.clear();
        mock.clear();
        
        mockTwo.add("add");

        verify(mock, atLeastOnce()).clear();
        verify(mockTwo, atLeastOnce()).add("add");
        try {
            verify(mockTwo, atLeastOnce()).add("foo");
            fail();
        } catch (WantedButNotInvoked e) {}
    }
    
    @Test(expected=WantedButNotInvoked.class)
    public void shouldFailIfMethodWasNotCalledAtAll() throws Exception {
        verify(mock, atLeastOnce()).add("foo");
    }
    
    @Test
    public void shouldVerifyAtLeastXTimes() throws Exception {
        mock.add("foo");
        mock.add("foo");
        mock.add("foo");
        
        verify(mock, atLeast(1)).add("foo");
        verify(mock, atLeast(2)).add("foo");
        verify(mock, atLeast(3)).add("foo");
    }
    
    @Test(expected=TooLittleActualInvocations.class)
    public void shouldFailOnVerifyAtLeast10WhenMethodWasInvokedOnce() throws Exception {
        mock.add("foo");

        verify(mock, atLeast(2)).add("foo");
    }
    
    @Test
    public void shouldVerifyInOrder() throws Exception {
        mock.add("one");
        mock.add("two");
        mock.add("three");
        
        mock.clear();
  
        InOrder inOrder = inOrder(mock);
        
        inOrder.verify(mock, atLeast(2)).add(anyString());
        inOrder.verify(mock).clear();
        verifyNoMoreInteractions(mock);
    }
    
    @Test
    public void shouldFailVerificationInOrder() throws Exception {
        mock.clear();

        mock.add("one");
        mock.add("two");
        mock.add("three");
  
        InOrder inOrder = inOrder(mock);
        
        inOrder.verify(mock, atLeastOnce()).clear();

        try {
            inOrder.verify(mock, atLeast(4)).add(anyString());
            fail();
        } catch (VerifcationInOrderFailure e) {}
    }
}
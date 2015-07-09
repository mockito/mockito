/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class AtMostXVerificationTest extends TestBase {

    @Mock private List mock;
    
    @Test
    public void shouldVerifyAtMostXTimes() throws Exception {
        mock.clear();
        mock.clear();
        
        verify(mock, atMost(2)).clear();
        verify(mock, atMost(3)).clear();
        
        try {
            verify(mock, atMost(1)).clear();
            fail();
        } catch (MockitoAssertionError e) {}
    }
    
    @Test
    public void shouldWorkWithArgumentMatchers() throws Exception {
        mock.add("one");
        verify(mock, atMost(5)).add(anyString());
        
        try {
            verify(mock, atMost(0)).add(anyString());
            fail();
        } catch (MockitoAssertionError e) {}
    }
    
    @Test
    public void shouldNotAllowNegativeNumber() throws Exception {
        try {
            verify(mock, atMost(-1)).clear();
            fail();
        } catch (MockitoException e) {
            assertEquals("Negative value is not allowed here", e.getMessage());
        }
    }
    
    @Test
    public void shouldPrintDecentMessage() throws Exception {
        mock.clear();
        mock.clear();
        
        try {
            verify(mock, atMost(1)).clear();
            fail();
        } catch (MockitoAssertionError e) {
            assertEquals("\nWanted at most 1 time but was 2", e.getMessage());
        }
    }
    
    @Test
    public void shouldNotAllowInOrderMode() throws Exception {
        mock.clear();
        InOrder inOrder = inOrder(mock);
        
        try {
            inOrder.verify(mock, atMost(1)).clear();
            fail();
        } catch (MockitoException e) {
            assertEquals("AtMost is not implemented to work with InOrder", e.getMessage());
        }
    }

    @Test
    public void shouldMarkInteractionsAsVerified() throws Exception {
        mock.clear();
        mock.clear();
        
        verify(mock, atMost(3)).clear();
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void shouldDetectUnverifiedInMarkInteractionsAsVerified() throws Exception {
        mock.clear();
        mock.clear();
        undesiredInteraction();
        
        verify(mock, atMost(3)).clear();
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch(NoInteractionsWanted e) {
            assertContains("undesiredInteraction(", e.getMessage());
        }
    }

    private void undesiredInteraction() {
        mock.add("");
    }
}
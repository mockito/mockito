/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stacktrace;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Argument;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@Ignore
public class ModellingDescriptiveMessagesTest extends TestBase {
    
    @Mock private IMethods mock;
    
    @Before
    public void cleanStackTrace() {
        super.makeStackTracesClean();
    }

    @Test
    public void shouldSayWantedButNotInvoked() {
        verify(mock).otherMethod();
    }
    
    @Test
    public void shouldPointOutInteractionsOnMockWhenOrdinaryVerificationFails() {
        mock.otherMethod();
        mock.booleanObjectReturningMethod();
        
        verify(mock).simpleMethod();
    }    
    
    @Test
    public void shouldShowActualAndExpected() {
        mock.simpleMethod("blah");
        verify(mock).simpleMethod();
    }
    
    @Test
    public void shouldSayTooLittleInvocations() {
        mock.simpleMethod();
        verify(mock, times(2)).simpleMethod();
    }
    
    @Test
    public void shouldSayTooManyInvocations() {
        mock.simpleMethod();
        mock.simpleMethod();
        verify(mock, times(1)).simpleMethod();
    }
    
    @Test
    public void shouldSayWantedButNotInvokedInOrder() {
        mock.simpleMethod();
        mock.otherMethod();
        InOrder inOrder = inOrder(mock);
        inOrder.verify(mock).otherMethod();
        inOrder.verify(mock).simpleMethod();
    }
    
    @Test
    public void shouldSayTooLittleInvocationsInOrder() {
        mock.simpleMethod();
        mock.otherMethod();
        mock.otherMethod();

        InOrder inOrder = inOrder(mock);
        inOrder.verify(mock).simpleMethod();
        inOrder.verify(mock, times(3)).otherMethod();
    }
    
    @Test
    public void shouldSayTooManyInvocationsInOrder() {
        mock.otherMethod();
        mock.otherMethod();
        
        InOrder inOrder = inOrder(mock);
        inOrder.verify(mock, times(1)).otherMethod();
    }

    @Test
    public void shouldSayNeverWantedButInvokedHere() {
        mock.otherMethod();
        
        verify(mock, never()).otherMethod();
    }
    
    @Test
    public void shouldSayTooLittleInvocationsInAtLeastModeInOrder() {
        mock.simpleMethod();

        InOrder inOrder = inOrder(mock);
        inOrder.verify(mock, atLeast(2)).simpleMethod();
    }
    
    @Test
    public void shouldSayTooLittleInvocationsInAtLeastMode() {
        mock.simpleMethod();
        
        verify(mock, atLeast(2)).simpleMethod();
    }

    @Test
    public void shouldSayNoMoreInteractions() {
        mock.simpleMethod();
        
        verifyNoMoreInteractions(mock);
    }
    
    @Test
    public void shouldSayUnstubbedMethodWasInvokedHere() {
        mock = mock(IMethods.class, RETURNS_SMART_NULLS);
        
        IMethods m = mock.iMethodsReturningMethod();
        
        m.simpleMethod();
    }
    
    @Test
    public void shouldPointOutUnfinishedStubbing() {
        when(mock.simpleMethod());
        
        verify(mock).simpleMethod();
    }    

    @Test
    public void shouldMentionFinalAndObjectMethodsWhenMissingMockCall() {
        when(mock.equals(null)).thenReturn(false);
    }
    
    @Test
    public void shouldMentionFinalAndObjectMethodsWhenVerifying() {
        verify(mock).equals(null);
        verify(mock).simpleMethod();
    }
    
    @Test
    public void shouldMentionFinalAndObjectMethodsWhenMisplacedArgumentMatcher() {
        when(mock.equals(anyObject())).thenReturn(false);
    }
    
    @Test
    public void shouldShowExampleOfCorrectArgumentCapturing() {
        Argument<String> argument = new Argument<String>();
        argument.capture();
        argument.value();
    }
}
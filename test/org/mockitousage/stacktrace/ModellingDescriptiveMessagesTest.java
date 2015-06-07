/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.stacktrace;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings("rawtypes")
@Ignore
@RunWith(MockitoJUnitRunner.class)
public class ModellingDescriptiveMessagesTest extends TestBase {
    
    @Mock private IMethods mock;
    
    @Before
    public void cleanStackTrace() {
        super.makeStackTracesClean();
    }

    @Test
    public void makeSureStateIsValidatedInTheVeryFirstTestThanksToTheRunner() {
        //mess up the state:
        verify(mock);
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
        final InOrder inOrder = inOrder(mock);
        inOrder.verify(mock).otherMethod();
        inOrder.verify(mock).simpleMethod();
    }
    
    @Test
    public void shouldSayTooLittleInvocationsInOrder() {
        mock.simpleMethod();
        mock.otherMethod();
        mock.otherMethod();

        final InOrder inOrder = inOrder(mock);
        inOrder.verify(mock).simpleMethod();
        inOrder.verify(mock, times(3)).otherMethod();
    }
    
    @Test
    public void shouldSayTooManyInvocationsInOrder() {
        mock.otherMethod();
        mock.otherMethod();
        
        final InOrder inOrder = inOrder(mock);
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

        final InOrder inOrder = inOrder(mock);
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
        
        final IMethods m = mock.iMethodsReturningMethod();
        
        m.simpleMethod();
    }
    
    @Test
    public void shouldPointOutUnfinishedStubbing() {
        when(mock.simpleMethod());
        
        verify(mock).simpleMethod();
    }    

    @Test
    public void shouldMentionFinalAndObjectMethodsWhenMissingMockCall() {
        when("".equals(null)).thenReturn(false);
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
        final ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        argument.capture();
        argument.getValue();
    }
    
    @Test
    public void shouldScreamWhenNullPassedInsteadOfAnInterface() {
        mock(IMethods.class, withSettings().extraInterfaces(List.class, null));
    }
    
    @Test
    public void shouldScreamWhenNonInterfacePassed() {
        mock(IMethods.class, withSettings().extraInterfaces(LinkedList.class));
    }
    
    @Test
    public void shouldScreamWhenExtraIsTheSame() {
        mock(IMethods.class, withSettings().extraInterfaces(IMethods.class));
    }
    
    @Test
    public void shouldScreamWhenExtraInterfacesEmpty() {
        mock(IMethods.class, withSettings().extraInterfaces());
    }
    
    @Test
    public void shouldScreamWhenExtraInterfacesIsANullArray() {
        mock(IMethods.class, withSettings().extraInterfaces((Class[]) null));
    }

    @Test
    public void shouldMentionSpiesWhenVoidMethodIsToldToReturnValue() {
        final List list = mock(List.class);
        doReturn("foo").when(list).clear();
    }
}
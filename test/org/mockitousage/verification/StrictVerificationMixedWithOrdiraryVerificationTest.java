/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.Strictly;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.StrictVerificationFailure;
import org.mockitousage.IMethods;

@SuppressWarnings("unchecked")  
public class StrictVerificationMixedWithOrdiraryVerificationTest extends RequiresValidState {
    
    private IMethods mockOne;
    private IMethods mockTwo;
    private IMethods mockThree;
    private Strictly strictly;

    @Before
    public void setUp() {
        mockOne = mock(IMethods.class);
        mockTwo = mock(IMethods.class);
        mockThree = mock(IMethods.class);

        mockOne.simpleMethod(1);
        mockOne.simpleMethod(1);
        mockTwo.simpleMethod(2);
        mockThree.simpleMethod(3);
        mockThree.simpleMethod(4);

        strictly = createStrictOrderVerifier(mockOne, mockThree);
    }
    
    @Test
    public void shouldMixStrictVerificationAndNormalVerification() {
        strictly.verify(mockOne, atLeastOnce()).simpleMethod(1);
        strictly.verify(mockThree).simpleMethod(3);
        strictly.verify(mockThree).simpleMethod(4);
        verify(mockTwo).simpleMethod(2);
        
        verifyNoMoreInteractions(mockOne, mockTwo, mockThree);
    }
    
    @Test
    public void shouldAllowOrdinarilyVerifyingStrictlyControlledMock() {
        strictly.verify(mockOne, atLeastOnce()).simpleMethod(1);

        verify(mockThree).simpleMethod(3);
        verify(mockThree).simpleMethod(4);
        verify(mockTwo).simpleMethod(2);
        
        verifyNoMoreInteractions(mockOne, mockTwo, mockThree);
    }
    
    @Test
    public void shouldAllowRedundantVerifications() {
        verify(mockOne, atLeastOnce()).simpleMethod(1);
        verify(mockTwo).simpleMethod(2);
        verify(mockThree).simpleMethod(3);
        verify(mockThree).simpleMethod(4);
        
        strictly.verify(mockOne, atLeastOnce()).simpleMethod(1);
        strictly.verify(mockThree).simpleMethod(3);
        strictly.verify(mockThree).simpleMethod(4);
        
        verifyNoMoreInteractions(mockOne, mockTwo, mockThree);
    }
    
    @Test
    public void shouldFailOnNoMoreInteractions() {
        strictly.verify(mockOne, atLeastOnce()).simpleMethod(1);
        strictly.verify(mockThree).simpleMethod(3);
        strictly.verify(mockThree).simpleMethod(4);
        
        try {
            verifyNoMoreInteractions(mockOne, mockTwo, mockThree);
            fail();
        } catch (NoInteractionsWanted e) {}
    }
    
    @Test
    public void shouldFailOnNoMoreInteractionsOnStrictlyControlledMock() {
        strictly.verify(mockOne, atLeastOnce()).simpleMethod(1);
        strictly.verify(mockThree).simpleMethod(3);
        verify(mockTwo).simpleMethod(2);
        
        try {
            verifyNoMoreInteractions(mockOne, mockTwo, mockThree);
            fail();
        } catch (NoInteractionsWanted e) {}
    }
    
    @Test
    public void shouldAllowOneMethodVerifiedStrictly() {
        verify(mockTwo).simpleMethod(2);
        verify(mockOne, atLeastOnce()).simpleMethod(1);

        strictly.verify(mockOne, atLeastOnce()).simpleMethod(1);
    }
    
    @Test
    public void shouldFailOnLastInvocationTooEarly() {
        strictly.verify(mockOne, atLeastOnce()).simpleMethod(1);
        verify(mockTwo).simpleMethod(2);
        try {
            strictly.verify(mockThree).simpleMethod(4);
            fail();
        } catch (StrictVerificationFailure e) {}
    }
    
    @Test(expected=MockitoException.class)
    public void shouldScreamWhenNotStrictMockPassedToStrictly() {
        strictly.verify(mockTwo, atLeastOnce()).simpleMethod(1);
    } 
    
    @Test
    public void shouldUseEqualsToVerifyMethodArguments() {
        mockOne = mock(IMethods.class);
        
        String textOne = "test";
        String textTwo = new String(textOne);
        
        assertEquals(textOne, textTwo);
        assertNotSame(textOne, textTwo);
        
        mockOne.simpleMethod(textOne);
        mockOne.simpleMethod(textTwo);
        
        verify(mockOne, times(2)).simpleMethod(textOne);
        
        strictly = createStrictOrderVerifier(mockOne);
        strictly.verify(mockOne, times(2)).simpleMethod(textOne);
    } 
    
    @Test
    public void shouldUseEqualsToVerifyMethodVarargs() {
        mockOne = mock(IMethods.class);
        
        String textOne = "test";
        String textTwo = new String(textOne);
        
        assertEquals(textOne, textTwo);
        assertNotSame(textOne, textTwo);
        
        mockOne.varargsObject(1, textOne, textOne);
        mockOne.varargsObject(1, textTwo, textTwo);
        
        verify(mockOne, times(2)).varargsObject(1, textOne, textOne);
        
        strictly = createStrictOrderVerifier(mockOne);
        strictly.verify(mockOne, times(2)).varargsObject(1, textOne, textOne);
    } 
}
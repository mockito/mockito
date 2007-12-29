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
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.StrictVerificationFailure;
import org.mockitousage.IMethods;

@SuppressWarnings("unchecked")  
public class SelectedMocksInOrderVerificationTest extends RequiresValidState {
    
    private IMethods mockOne;
    private IMethods mockTwo;
    private IMethods mockThree;

    @Before
    public void setUp() {
        mockOne = mock(IMethods.class);
        mockTwo = mock(IMethods.class);
        mockThree = mock(IMethods.class);

        mockOne.simpleMethod(1);
        mockTwo.simpleMethod(2);
        mockTwo.simpleMethod(2);
        mockThree.simpleMethod(3);
        mockTwo.simpleMethod(2);
        mockOne.simpleMethod(4);
    }
    
    @Test
    public void shouldVerifyStrictlyAllInvocations() {
        Strictly strictly = strictly(mockOne, mockTwo, mockThree);
        strictly.verify(mockOne).simpleMethod(1);
        strictly.verify(mockTwo, times(2)).simpleMethod(2);
        strictly.verify(mockThree).simpleMethod(3);
        strictly.verify(mockTwo).simpleMethod(2);
        strictly.verify(mockOne).simpleMethod(4);
        verifyNoMoreInteractions(mockOne, mockTwo, mockThree);
    } 
    
    @Test
    public void shouldVerifyStrictlyMockTwoAndThree() {
        Strictly strictly = strictly(mockTwo, mockThree);
        
        strictly.verify(mockTwo, times(2)).simpleMethod(2);
        strictly.verify(mockThree).simpleMethod(3);
        strictly.verify(mockTwo).simpleMethod(2);
        verifyNoMoreInteractions(mockTwo, mockThree);
    }     
    
    @Test
    public void shouldVerifyStrictlyMockOneAndThree() {
        Strictly strictly = strictly(mockOne, mockThree);
        
        strictly.verify(mockOne).simpleMethod(1);
        strictly.verify(mockThree).simpleMethod(3);
        strictly.verify(mockOne).simpleMethod(4);
        verifyNoMoreInteractions(mockOne, mockThree);
    } 
    
    @Test
    public void shouldVerifyStrictlyMockOne() {
        Strictly strictly = strictly(mockOne);
        
        strictly.verify(mockOne).simpleMethod(1);
        strictly.verify(mockOne).simpleMethod(4);
        
        verifyNoMoreInteractions(mockOne);
    } 
    
    @Test
    public void shouldFailVerificationForMockOne() {
        Strictly strictly = strictly(mockOne);
        
        strictly.verify(mockOne).simpleMethod(1);
        try {
            strictly.verify(mockOne).differentMethod();
            fail();
        } catch (StrictVerificationFailure e) {}
    } 
    
    @Test
    public void shouldFailVerificationForMockOneBecauseOfWrongOrder() {
        Strictly strictly = strictly(mockOne);
        
        try {
            strictly.verify(mockOne).simpleMethod(4);
            fail();
        } catch (StrictVerificationFailure e) {}
    } 

    @Test
    public void shouldVerifyStrictlyMockTwoWhenThreeTimesUsed() {
        Strictly strictly = strictly(mockTwo);
        
        strictly.verify(mockTwo, times(3)).simpleMethod(2);
        
        verifyNoMoreInteractions(mockTwo);
    } 
    
    @Test
    public void shouldVerifyStrictlyMockTwo() {
        Strictly strictly = strictly(mockTwo);
        
        strictly.verify(mockTwo, atLeastOnce()).simpleMethod(2);
        
        verifyNoMoreInteractions(mockTwo);
    } 
    
    @Test
    public void shouldFailVerificationForMockTwo() {
        Strictly strictly = strictly(mockTwo);

        try {
            strictly.verify(mockTwo).simpleMethod(2);
            fail();
        } catch (StrictVerificationFailure e) {}
    }
    
    @Test
    public void shouldThrowNoMoreInvocationsForMockTwo() {
        Strictly strictly = strictly(mockTwo);

        try {
            strictly.verify(mockTwo, times(2)).simpleMethod(2);
            fail();
        } catch (StrictVerificationFailure e) {}
    }
    
    @Test
    public void shouldThrowTooLittleInvocationsForMockTwo() {
        Strictly strictly = strictly(mockTwo);

        try {
            strictly.verify(mockTwo, times(4)).simpleMethod(2);
            fail();
        } catch (StrictVerificationFailure e) {}
    }
    
    @Test
    public void shouldThrowTooManyInvocationsForMockTwo() {
        Strictly strictly = strictly(mockTwo);

        try {
            strictly.verify(mockTwo, times(2)).simpleMethod(2);
            fail();
        } catch (StrictVerificationFailure e) {}
    }
    
    @Test
    public void shouldAllowThreeTimesOnMockTwo() {
        Strictly strictly = strictly(mockTwo);

        strictly.verify(mockTwo, times(3)).simpleMethod(2);
        verifyNoMoreInteractions(mockTwo);
    }
    
    @Test
    public void shouldVerifyMockTwoCompletely() {
        Strictly strictly = strictly(mockTwo, mockThree);

        strictly.verify(mockTwo, times(2)).simpleMethod(2);
        strictly.verify(mockThree).simpleMethod(3);
        strictly.verify(mockTwo).simpleMethod(2);
        verifyNoMoreInteractions(mockTwo, mockThree);
    }
    
    @Test
    public void shouldAllowTwoTimesOnMockTwo() {
        Strictly strictly = strictly(mockTwo, mockThree);

        strictly.verify(mockTwo, times(2)).simpleMethod(2);
        try {
            verifyNoMoreInteractions(mockTwo);
            fail();
        } catch (NoInteractionsWanted e) {}
    }
}
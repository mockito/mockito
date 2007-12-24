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
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.mockito.exceptions.verification.InvocationDiffersFromActual;
import org.mockito.exceptions.verification.WantedButNotInvoked;
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
        Strictly strictly = createStrictOrderVerifier(mockOne, mockTwo, mockThree);
        strictly.verify(mockOne).simpleMethod(1);
        strictly.verify(mockTwo, times(2)).simpleMethod(2);
        strictly.verify(mockThree).simpleMethod(3);
        strictly.verify(mockTwo).simpleMethod(2);
        strictly.verify(mockOne).simpleMethod(4);
        verifyNoMoreInteractions(mockOne, mockTwo, mockThree);
    } 
    
    @Test
    public void shouldVerifyStrictlyMockTwoAndThree() {
        Strictly strictly = createStrictOrderVerifier(mockTwo, mockThree);
        
        strictly.verify(mockTwo, times(2)).simpleMethod(2);
        strictly.verify(mockThree).simpleMethod(3);
        strictly.verify(mockTwo).simpleMethod(2);
        verifyNoMoreInteractions(mockTwo, mockThree);
    }     
    
    @Test
    public void shouldVerifyStrictlyMockOneAndThree() {
        Strictly strictly = createStrictOrderVerifier(mockOne, mockThree);
        
        strictly.verify(mockOne).simpleMethod(1);
        strictly.verify(mockThree).simpleMethod(3);
        strictly.verify(mockOne).simpleMethod(4);
        verifyNoMoreInteractions(mockOne, mockThree);
    } 
    
    @Test
    public void shouldVerifyStrictlyMockOne() {
        Strictly strictly = createStrictOrderVerifier(mockOne);
        
        strictly.verify(mockOne).simpleMethod(1);
        strictly.verify(mockOne).simpleMethod(4);
        
        verifyNoMoreInteractions(mockOne);
    } 
    
    @Test
    public void shouldFailVerificationForMockOne() {
        Strictly strictly = createStrictOrderVerifier(mockOne);
        
        strictly.verify(mockOne).simpleMethod(1);
        try {
            strictly.verify(mockOne).differentMethod();
            fail();
        } catch (InvocationDiffersFromActual e) {}
    } 
    
    @Test
    public void shouldFailVerificationForMockOneBecauseOfWrongOrder() {
        Strictly strictly = createStrictOrderVerifier(mockOne);
        strictly.verify(mockOne).simpleMethod(4);
        
        try {
            strictly.verify(mockOne).simpleMethod(1);
            fail();
        } catch (WantedButNotInvoked e) {}
    } 

    @Test
    public void shouldVerifyStrictlyMockTwoWhenThreeTimesUsed() {
        Strictly strictly = createStrictOrderVerifier(mockTwo);
        
        strictly.verify(mockTwo, times(3)).simpleMethod(2);
        
        verifyNoMoreInteractions(mockTwo);
    } 
    
    @Test
    public void shouldVerifyStrictlyMockTwo() {
        Strictly strictly = createStrictOrderVerifier(mockTwo);
        
        strictly.verify(mockTwo, atLeastOnce()).simpleMethod(2);
        
        verifyNoMoreInteractions(mockTwo);
    } 
    
    @Test
    public void shouldFailVerificationForMockTwo() {
        Strictly strictly = createStrictOrderVerifier(mockTwo);

        try {
            strictly.verify(mockTwo).simpleMethod(2);
            fail();
        } catch (TooManyActualInvocations e) {}
    }
    
    @Test
    public void shouldThrowNoMoreInvocationsForMockTwo() {
        Strictly strictly = createStrictOrderVerifier(mockTwo);

        try {
            strictly.verify(mockTwo, times(2)).simpleMethod(2);
            fail();
        } catch (TooManyActualInvocations e) {}
    }
    
    @Test
    public void shouldThrowTooLittleInvocationsForMockTwo() {
        Strictly strictly = createStrictOrderVerifier(mockTwo);

        try {
            strictly.verify(mockTwo, times(4)).simpleMethod(2);
            fail();
        } catch (TooLittleActualInvocations e) {}
    }
    
    @Test
    public void shouldThrowTooManyInvocationsForMockTwo() {
        Strictly strictly = createStrictOrderVerifier(mockTwo);

        try {
            strictly.verify(mockTwo, times(2)).simpleMethod(2);
            fail();
        } catch (TooManyActualInvocations e) {}
    }
    
    @Test
    public void shouldAllowThreeTimesOnMockTwo() {
        Strictly strictly = createStrictOrderVerifier(mockTwo);

        strictly.verify(mockTwo, times(3)).simpleMethod(2);
        verifyNoMoreInteractions(mockTwo);
    }
    
    @Test
    public void shouldVerifyMockTwoCompletely() {
        Strictly strictly = createStrictOrderVerifier(mockTwo, mockThree);

        strictly.verify(mockTwo, times(2)).simpleMethod(2);
        strictly.verify(mockTwo, times(1)).simpleMethod(2);
        verifyNoMoreInteractions(mockTwo);
        try {
            verifyNoMoreInteractions(mockThree);
            fail();
        } catch (NoInteractionsWanted e) {}
    }
    
    @Test
    public void shouldAllowTwoTimesOnMockTwo() {
        Strictly strictly = createStrictOrderVerifier(mockTwo, mockThree);

        strictly.verify(mockTwo, times(2)).simpleMethod(2);
        try {
            verifyNoMoreInteractions(mockTwo);
            fail();
        } catch (NoInteractionsWanted e) {}
    }
}
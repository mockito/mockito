/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.util.ExtraMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.Strictly;
import org.mockito.exceptions.verification.TooLittleActualInvocationsError;
import org.mockito.exceptions.verification.VerificationError;
import org.mockitousage.IMethods;

@SuppressWarnings("unchecked")  
public class StrictVerificationTest extends RequiresValidState {
    
    private IMethods mockOne;
    private IMethods mockTwo;
    private IMethods mockThree;
    private Strictly strictly;

    @Before
    public void setUp() {
        mockOne = mock(IMethods.class);
        mockTwo = mock(IMethods.class);
        mockThree = mock(IMethods.class);
        
        strictly = createStrictOrderVerifier(mockOne, mockTwo, mockThree);
    }
    
    @Test
    public void shouldVerifySingleMockStrictlyAndNotStrictly() {
        mockOne = mock(IMethods.class);
        strictly = createStrictOrderVerifier(mockOne);
        
        mockOne.simpleMethod(1);
        mockOne.simpleMethod(2);
        
        verify(mockOne).simpleMethod(2);
        verify(mockOne).simpleMethod(1);
        
        strictly.verify(mockOne).simpleMethod(2);
        try {
            strictly.verify(mockOne).simpleMethod(1);
            fail();
        } catch (VerificationError e) {}
    } 
    
    @Test
    public void shouldCausePointToMockOne() {
        mockTwo.differentMethod();
        mockOne.simpleMethod();
        
        try {
            strictly.verify(mockOne, atLeastOnce()).differentMethod();
            fail();
        } catch (VerificationError e) {
            assertThat(e, messageContains("IMethods.differentMethod()"));
            assertThat(e, causeMessageContains("IMethods.simpleMethod()"));
        }
    }
    
    @Test
    public void shouldVerifyStrictlyWhenTwoChunksAreEqual() {
        mockOne.simpleMethod();
        mockOne.simpleMethod();
        mockTwo.differentMethod();
        mockOne.simpleMethod();
        mockOne.simpleMethod();
        
        strictly.verify(mockOne, atLeastOnce()).simpleMethod();
        strictly.verify(mockOne, times(2)).simpleMethod();
        try {
            strictly.verify(mockOne, atLeastOnce()).simpleMethod();
            fail();
        } catch (VerificationError e) {}
    }
    
    @Test
    public void shouldVerifyStrictlyUsingMatcher() {
        mockOne.simpleMethod(1);
        mockOne.simpleMethod(2);
        mockTwo.differentMethod();
        mockOne.simpleMethod(3);
        mockOne.simpleMethod(4);
        
        verify(mockOne, times(4)).simpleMethod(anyInt());
        
        strictly.verify(mockOne, times(2)).simpleMethod(anyInt());
        strictly.verify(mockOne, times(2)).simpleMethod(anyInt());
        try {
            strictly.verify(mockOne, times(3)).simpleMethod(anyInt());
            fail();
        } catch (VerificationError e) {}
    }
}
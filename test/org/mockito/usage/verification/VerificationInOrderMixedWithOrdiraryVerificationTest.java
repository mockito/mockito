/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.usage.verification;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.Strictly;
import org.mockito.exceptions.*;
import org.mockito.usage.IMethods;

@SuppressWarnings("unchecked")  
public class VerificationInOrderMixedWithOrdiraryVerificationTest {
    
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
    public void shouldMixVerifyingInOrderAndNormalVerification() {
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
        } catch (VerificationError e) {}
    }
    
    @Test
    public void shouldFailOnNoMoreInteractionsOnStrictlyControlledMock() {
        strictly.verify(mockOne, atLeastOnce()).simpleMethod(1);
        strictly.verify(mockThree).simpleMethod(3);
        verify(mockTwo).simpleMethod(2);
        
        try {
            verifyNoMoreInteractions(mockOne, mockTwo, mockThree);
            fail();
        } catch (VerificationError e) {}
    }
    
    @Test
    public void shouldFailOnWrongOrder() {
        verify(mockTwo).simpleMethod(2);
        verify(mockOne, atLeastOnce()).simpleMethod(1);

        try {
            strictly.verify(mockThree).simpleMethod(3);
            fail();
        } catch (VerificationError e) {}
    }
    
    @Test
    public void shouldFailOnWrongOrderForLastInvocationIsTooEarly() {
        strictly.verify(mockOne, atLeastOnce()).simpleMethod(1);
        verify(mockTwo).simpleMethod(2);
        
        try {
            strictly.verify(mockThree).simpleMethod(4);
            fail();
        } catch (VerificationError e) {}
    }
    
    @Test(expected=MockitoException.class)
    public void shouldScreamWhenNotStrictMockPassedToStrictly() {
        strictly.verify(mockTwo, atLeastOnce()).simpleMethod(1);
    } 
}
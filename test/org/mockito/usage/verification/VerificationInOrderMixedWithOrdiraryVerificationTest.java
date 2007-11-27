/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.usage.verification;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.mockito.Strictly;
import org.mockito.exceptions.MockitoException;
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

        strictly = strictOrderVerifier(mockOne, mockThree);
    }
    
    @Test
    public void shouldMixVerifyingInOrderAndNormalVerification() {
        verify(mockOne, atLeastOnce()).simpleMethod(1);
        verify(mockTwo).simpleMethod(2);
        verify(mockThree).simpleMethod(3);
        verify(mockThree).simpleMethod(4);
        
        strictly.verify(mockOne, atLeastOnce()).simpleMethod(1);
        strictly.verify(mockThree).simpleMethod(3);
        strictly.verify(mockThree).simpleMethod(4);
        
        verifyNoMoreInteractions(mockOne, mockTwo, mockThree);
    }
    
    @Ignore
    @Test
    public void shouldScreamWhenNotStrictMockPassedToStrictly() {
        //TODO move to some nice messages exceptions test?
        try {
            strictly.verify(mockTwo, atLeastOnce()).simpleMethod(1);
            fail();
        } catch(MockitoException e) {
            String expected = "some meaningful message";
            assertEquals(expected, e.getMessage());
        }
    } 
}
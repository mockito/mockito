/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.createStrictOrderVerifier;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.Strictly;
import org.mockito.exceptions.verification.NoInteractionsWantedError;
import org.mockito.exceptions.verification.TooLittleActualInvocationsError;
import org.mockito.exceptions.verification.TooManyActualInvocationsError;
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

        mockOne.simpleMethod(1);
        mockTwo.simpleMethod(2);
        mockTwo.simpleMethod(2);
        mockThree.simpleMethod(3);
        mockTwo.simpleMethod(2);
        mockOne.simpleMethod(4);
    }
    
    @Test
    public void shouldVerifyStrictly() {
        strictly.verify(mockOne).simpleMethod(1);
        strictly.verify(mockTwo, times(2)).simpleMethod(2);
        strictly.verify(mockThree).simpleMethod(3);
        strictly.verify(mockTwo).simpleMethod(2);
        strictly.verify(mockOne).simpleMethod(4);
        verifyNoMoreInteractions(mockOne, mockTwo, mockThree);
    } 
    
    @Test
    public void shouldVerifyStrictlyUsingAtLeastOnce() {
        strictly.verify(mockOne, atLeastOnce()).simpleMethod(1);
        strictly.verify(mockTwo, atLeastOnce()).simpleMethod(2);
        strictly.verify(mockThree).simpleMethod(3);
        strictly.verify(mockTwo).simpleMethod(2);
        strictly.verify(mockOne, atLeastOnce()).simpleMethod(4);
        verifyNoMoreInteractions(mockOne, mockTwo, mockThree);
    } 
    
    @Test
    public void shouldVerifyStrictlyWhenExpectingSomeInvocationsToBeCalledZeroTimes() {
        strictly.verify(mockOne, times(0)).oneArg(false);
        strictly.verify(mockOne).simpleMethod(1);
        strictly.verify(mockTwo, times(2)).simpleMethod(2);
        strictly.verify(mockTwo, times(0)).simpleMethod(22);
        strictly.verify(mockThree).simpleMethod(3);
        strictly.verify(mockTwo).simpleMethod(2);
        strictly.verify(mockOne).simpleMethod(4);
        strictly.verify(mockThree, times(0)).oneArg(false);
        verifyNoMoreInteractions(mockOne, mockTwo, mockThree);
    } 
    
    @Test
    public void shouldFailWhenFirstMockCalledTwice() {
        strictly.verify(mockOne).simpleMethod(1);
        try {
            strictly.verify(mockOne).simpleMethod(1);
            fail();
        } catch (VerificationError e) {}
    }
    
    @Test
    public void shouldFailWhenLastMockCalledTwice() {
        strictly.verify(mockOne).simpleMethod(1);
        strictly.verify(mockTwo, times(2)).simpleMethod(2);
        strictly.verify(mockThree).simpleMethod(3);
        strictly.verify(mockTwo).simpleMethod(2);
        strictly.verify(mockOne).simpleMethod(4);
        try {
            strictly.verify(mockOne).simpleMethod(4);
            fail();
        } catch (VerificationError e) {}
    }
    
    @Test(expected=TooManyActualInvocationsError.class)
    public void shouldFailOnFirstMethodBecauseOneInvocationWanted() {
        strictly.verify(mockOne, times(0)).simpleMethod(1);
    }
    
    @Test(expected=TooLittleActualInvocationsError.class)
    public void shouldFailOnFirstMethodBecauseOneInvocationWantedAgain() {
        strictly.verify(mockOne, times(2)).simpleMethod(1);
    }
    
    @Test
    public void shouldFailOnSecondMethodBecauseTwoInvocationsWanted() {
        strictly.verify(mockOne, times(1)).simpleMethod(1);
        try {
            strictly.verify(mockTwo, times(3)).simpleMethod(2);
            fail();
        } catch (TooLittleActualInvocationsError e) {}
    }
    
    @Test
    public void shouldFailOnSecondMethodBecauseTwoInvocationsWantedAgain() {
        strictly.verify(mockOne, times(1)).simpleMethod(1);
        try {
            strictly.verify(mockTwo, times(0)).simpleMethod(2);
            fail();
        } catch (TooManyActualInvocationsError e) {}
    }    
    
    @Test
    public void shouldFailOnLastMethodBecauseOneInvocationWanted() {
        strictly.verify(mockOne, atLeastOnce()).simpleMethod(1);
        strictly.verify(mockTwo, atLeastOnce()).simpleMethod(2);
        strictly.verify(mockThree, atLeastOnce()).simpleMethod(3);
        strictly.verify(mockTwo, atLeastOnce()).simpleMethod(2);
        try {
            strictly.verify(mockOne, times(0)).simpleMethod(4);
            fail();
        } catch (TooManyActualInvocationsError e) {}
    }
    
    @Test
    public void shouldFailOnLastMethodBecauseOneInvocationWantedAgain() {
        strictly.verify(mockOne, atLeastOnce()).simpleMethod(1);
        strictly.verify(mockTwo, atLeastOnce()).simpleMethod(2);
        strictly.verify(mockThree, atLeastOnce()).simpleMethod(3);
        strictly.verify(mockTwo, atLeastOnce()).simpleMethod(2);
        try {
            strictly.verify(mockOne, times(2)).simpleMethod(4);
            fail();
        } catch (TooLittleActualInvocationsError e) {}
    }    
    
    /* ------------- */
    
    @Test(expected=VerificationError.class)
    public void shouldFailOnFirstMethodBecauseDifferentArgsWanted() {
        strictly.verify(mockOne).simpleMethod(100);
    }
    
    @Test(expected=VerificationError.class)
    public void shouldFailOnFirstMethodBecauseDifferentMethodWanted() {
        strictly.verify(mockOne).oneArg(true);
    }
    
    @Test
    public void shouldFailOnSecondMethodBecauseDifferentArgsWanted() {
        strictly.verify(mockOne).simpleMethod(1);
        try {
            strictly.verify(mockTwo, times(2)).simpleMethod(-999);
            fail();
        } catch (TooLittleActualInvocationsError e) {}
    }
    
    @Test
    public void shouldFailOnSecondMethodBecauseDifferentMethodWanted() {
        strictly.verify(mockOne, times(1)).simpleMethod(1);
        try {
            strictly.verify(mockTwo, times(2)).oneArg(true);
            fail();
        } catch (TooLittleActualInvocationsError e) {}
    }    
    
    @Test
    public void shouldFailOnLastMethodBecauseDifferentArgsWanted() {
        strictly.verify(mockOne).simpleMethod(1);
        strictly.verify(mockTwo, atLeastOnce()).simpleMethod(2);
        strictly.verify(mockThree).simpleMethod(3);
        strictly.verify(mockTwo).simpleMethod(2);
        try {
            strictly.verify(mockOne).simpleMethod(-666);
            fail();
        } catch (VerificationError e) {}
    }
    
    @Test
    public void shouldFailOnLastMethodBecauseDifferentMethodWanted() {
        strictly.verify(mockOne).simpleMethod(1);
        strictly.verify(mockTwo, atLeastOnce()).simpleMethod(2);
        strictly.verify(mockThree).simpleMethod(3);
        strictly.verify(mockTwo).simpleMethod(2);
        try {
            strictly.verify(mockOne).oneArg(false);
            fail();
        } catch (VerificationError e) {}
    }    
    
    /* -------------- */
    
    @Test(expected = VerificationError.class)
    public void shouldFailWhenLastMethodCalledFirst() {
        strictly.verify(mockOne).simpleMethod(4);
    }
    
    @Test(expected = TooLittleActualInvocationsError.class)
    public void shouldFailWhenSecondMethodCalledFirst() {
        strictly.verify(mockTwo, times(2)).simpleMethod(2);
    }
    
    @Test
    public void shouldFailWhenLastMethodCalledToEarly() {
        strictly.verify(mockOne).simpleMethod(1);
        strictly.verify(mockTwo, times(2)).simpleMethod(2);
        try {
            strictly.verify(mockOne).simpleMethod(4);
            fail();
        } catch (VerificationError e) {}
    }
    
    @Test
    public void shouldFailWhenMockTwoIsToEarly() {
        strictly.verify(mockOne).simpleMethod(1);
        strictly.verify(mockTwo, times(2)).simpleMethod(2);
        try {
            strictly.verify(mockTwo, times(1)).simpleMethod(2);
            fail();
        } catch (VerificationError e) {}
    }
    
    @Test
    public void shouldFailWhenLastMockIsToEarly() {
        strictly.verify(mockOne).simpleMethod(1);
        try {
            strictly.verify(mockOne).simpleMethod(4);
            fail();
        } catch (VerificationError e) {}
    }
    
    @Test
    public void shouldFailOnVerifyNoMoreInteractions() {
        strictly.verify(mockOne).simpleMethod(1);
        strictly.verify(mockTwo, atLeastOnce()).simpleMethod(2);
        strictly.verify(mockThree).simpleMethod(3);
        strictly.verify(mockTwo).simpleMethod(2);
        
        try {
            verifyNoMoreInteractions(mockOne, mockTwo, mockThree);
            fail();
        } catch (NoInteractionsWantedError e) {}
    } 
    
    @Test(expected=NoInteractionsWantedError.class)
    public void shouldFailOnVerifyZeroInteractions() {
        verifyZeroInteractions(mockOne);
    }
    
    @Test
    public void shouldVerifySingleMockStrictly() {
        mockOne = mock(IMethods.class);
        strictly = createStrictOrderVerifier(mockOne);
        
        mockOne.simpleMethod(1);
        mockOne.simpleMethod(2);
        
        verify(mockOne).simpleMethod(2);
        verify(mockOne).simpleMethod(1);
        
        try {
            strictly.verify(mockOne).simpleMethod(2);
            fail();
        } catch (VerificationError e) {}
    } 
}
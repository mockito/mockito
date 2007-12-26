/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.Strictly;
import org.mockito.exceptions.verification.InvocationDiffersFromActual;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockitousage.IMethods;

/**
 * ignored since Relaxed strict verification is not implemented (too complex to bother).
 */
@Ignore
@SuppressWarnings("unchecked")  
public class RelaxedStrictVerificationTest extends RequiresValidState {
    
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
    public void shouldVerifyStrictlyAllInvocations() {
        strictly.verify(mockOne).simpleMethod(1);
        strictly.verify(mockTwo, times(2)).simpleMethod(2);
        strictly.verify(mockThree).simpleMethod(3);
        strictly.verify(mockTwo).simpleMethod(2);
        strictly.verify(mockOne).simpleMethod(4);
        verifyNoMoreInteractions(mockOne, mockTwo, mockThree);
    } 
    
    @Test
    public void shouldVerifyStrictlyMockTwoAndThree() {
        strictly.verify(mockTwo, times(2)).simpleMethod(2);
        strictly.verify(mockThree).simpleMethod(3);
        strictly.verify(mockTwo).simpleMethod(2);
        verifyNoMoreInteractions(mockTwo, mockThree);
    }     
    
    @Test
    public void shouldVerifyStrictlyMockOneAndThree() {
        strictly.verify(mockOne).simpleMethod(1);
        strictly.verify(mockThree).simpleMethod(3);
        strictly.verify(mockOne).simpleMethod(4);
        verifyNoMoreInteractions(mockOne, mockThree);
    } 
    
    @Test
    public void shouldVerifyStrictlyOnlyTwoInvocations() {
        strictly.verify(mockTwo, times(2)).simpleMethod(2);
        strictly.verify(mockOne).simpleMethod(4);
    }
    
    @Test
    public void shouldVerifyStrictlyOnlyMockTwo() {
        strictly.verify(mockTwo, times(2)).simpleMethod(2);
        strictly.verify(mockTwo).simpleMethod(2);
        verifyNoMoreInteractions(mockTwo);
    }
    
    @Test
    public void shouldVerifyMockTwoCalledOnce() {
        strictly.verify(mockTwo).simpleMethod(2);
    }

    @Test
    public void shouldVerifyMockTwoCalledTwice() {
        strictly.verify(mockTwo, times(2)).simpleMethod(2);
    }
    
    @Test
    public void shouldVerifyMockTwoCalledAtLeastOnce() {
        strictly.verify(mockTwo, atLeastOnce()).simpleMethod(2);
    }
    
    @Test(expected=InvocationDiffersFromActual.class)
    public void shouldFailOnWrongMethodCalledOnMockTwo() {
        strictly.verify(mockTwo, atLeastOnce()).differentMethod();
    }
    
    @Test
    public void shouldAllowTimesZeroButOnlyStrictly() {
        strictly.verify(mockTwo, atLeastOnce()).simpleMethod(2);
        strictly.verify(mockOne, times(0)).simpleMethod(1);
        
        try {
            verify(mockOne, times(0)).simpleMethod(1);
            fail();
        } catch (TooManyActualInvocations e) {}
    }
    
    @Test
    public void shouldFailTimesZeroStrictly() {
        strictly.verify(mockTwo, atLeastOnce()).simpleMethod(2);
        try {
            strictly.verify(mockThree, times(0)).simpleMethod(3);
            fail();
        } catch (TooManyActualInvocations e) {}
    }
    
    @Test
    public void shouldFailOnNoMoreInteractionsWantedForMockTwo() {
        strictly.verify(mockTwo, atLeastOnce()).simpleMethod(2);
        try {
            verifyNoMoreInteractions(mockTwo);
            fail();
        } catch (NoInteractionsWanted e) {}
    }
    
    @Test
    public void shouldFailWhenMockTwoWantedZeroTimes() {
        try {
            strictly.verify(mockTwo, times(0)).simpleMethod(2);
            fail();
        } catch(TooManyActualInvocations e) {}
    }
    
    @Test
    public void shouldFailWhenMockTwoWantedThreeTimes() {
        try {
            strictly.verify(mockTwo, times(3)).simpleMethod(2);
            fail();
        } catch(TooLittleActualInvocations e) {}
    }
    
    @Test
    public void shouldVerifyLastInvocation() {
        strictly.verify(mockOne).simpleMethod(4);
    }
    
    @Test
    public void shouldVerifyLastTwoInvocations() {
        strictly.verify(mockTwo).simpleMethod(2);
        strictly.verify(mockOne).simpleMethod(4);
    }
    
    @Test
    public void shouldVerifySecondAndLastInvocation() {
        strictly.verify(mockTwo, atLeastOnce()).simpleMethod(2);
        strictly.verify(mockOne).simpleMethod(4);
    }
    
    @Test
    public void shouldVerifySecondAndLastInvocationWhenAtLeastOnceUsed() {
        strictly.verify(mockTwo, atLeastOnce()).simpleMethod(2);
        strictly.verify(mockOne).simpleMethod(4);
    }
    
    @Test
    public void shouldFailOnLastTwoInvocationsInWrongOrder() {
        strictly.verify(mockOne).simpleMethod(4);
        try {
            strictly.verify(mockTwo).simpleMethod(2);
            fail();
        } catch (WantedButNotInvoked e) {}
    }
    
    @Test
    public void shouldFailOnLastAndFirstInWrongOrder() {
        strictly.verify(mockOne).simpleMethod(4);
        try {
            strictly.verify(mockOne).simpleMethod(1);
            fail();
        } catch (WantedButNotInvoked e) {}
    }
    
    @Test
    public void shouldFailOnWrongMethodAfterLastInvocation() {
        strictly.verify(mockOne).simpleMethod(4);
        try {
            strictly.verify(mockOne).simpleMethod(999);
            fail();
        } catch (WantedButNotInvoked e) {}
    }
}
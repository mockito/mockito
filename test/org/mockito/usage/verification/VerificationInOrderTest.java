/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.usage.verification;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.mockito.Strictly;
import org.mockito.exceptions.*;
import org.mockito.usage.IMethods;

@SuppressWarnings("unchecked")  
public class VerificationInOrderTest {
    
    private IMethods mockOne;
    private IMethods mockTwo;
    private IMethods mockThree;
    private Strictly strictly;

    @Before
    public void setUp() {
        mockOne = mock(IMethods.class);
        mockTwo = mock(IMethods.class);
        mockThree = mock(IMethods.class);
        
        strictly = strictOrderVerifier(mockOne, mockTwo, mockThree);

        mockOne.simpleMethod(1);
        mockTwo.simpleMethod(2);
        mockTwo.simpleMethod(2);
        mockThree.simpleMethod(3);
        mockTwo.simpleMethod(2);
        mockOne.simpleMethod(4);
    }
    
    @Ignore
    @Test
    public void shouldVerifyInOrder() {
        verify(mockOne).simpleMethod(1);
        verify(mockTwo, 2).simpleMethod(2);
        verify(mockThree).simpleMethod(3);
        verify(mockTwo).simpleMethod(2);
        verify(mockOne).simpleMethod(4);
        verifyNoMoreInteractions(mockOne, mockTwo, mockThree);
    } 
    
    @Ignore
    @Test
    public void shouldVerifyInOrderUsingAtLeastOnce() {
        verify(mockOne, atLeastOnce()).simpleMethod(1);
        verify(mockTwo, atLeastOnce()).simpleMethod(2);
        verify(mockThree).simpleMethod(3);
        verify(mockTwo).simpleMethod(2);
        verify(mockOne, atLeastOnce()).simpleMethod(4);
        verifyNoMoreInteractions(mockOne, mockTwo, mockThree);
    } 
    
    @Ignore
    @Test
    public void shouldVerifyInOrderWhenExpectedEqualsZero() {
        verify(mockOne, 0).oneArg(false);
        verify(mockOne).simpleMethod(1);
        verify(mockTwo, 2).simpleMethod(2);
        verify(mockThree).simpleMethod(3);
        verify(mockTwo).simpleMethod(2);
        verify(mockOne).simpleMethod(4);
        verify(mockThree, 0).oneArg(false);
        verifyNoMoreInteractions(mockOne, mockTwo, mockThree);
    } 
    
    @Ignore
    @Test
    public void shouldFailBecauseMethodWasInvokedTwice() {
        verify(mockOne).simpleMethod(1);
        try {
            verify(mockTwo).simpleMethod(2);
            fail();
        } catch (NumberOfInvocationsAssertionError e) {}
    }

    @Ignore
    @Test(expected = VerificationAssertionError.class)
    public void shouldFailOnWrongStartWithWrongArguments() {
        strictly.verify(mockOne).simpleMethod(100);
    }
    
    @Ignore
    @Test(expected = NumberOfInvocationsAssertionError.class)
    public void shouldFailOnStartWithWrongMethod() {
        strictly.verify(mockOne).oneArg(true);
    }
    
    @Ignore
    @Test(expected = StrictVerificationError.class)
    public void shouldFailWhenLastMethodCalledFirst() {
        strictly.verify(mockOne).simpleMethod(4);
    }
    
    @Ignore
    @Test(expected = StrictVerificationError.class)
    public void shouldFailWhenSecondMethodCalledFirst() {
        strictly.verify(mockTwo, 2).simpleMethod(2);
    }
    
//    @Test
//    public void shouldFailOnWrongOrder() {
//        strictly.verify(list, 1).add("one");
//        strictly.verify(map).put("two", "two");
//        try {
//            strictly.verify(map).put("five", "five");
//            fail();
//        } catch (StrictVerificationError e) {}
//    }
//    
//    @Test
//    public void shouldFailOnWrongOrderWhenCheckingExpectedNumberOfInvocations() {
//        strictly.verify(list, 1).add("one");
//        strictly.verify(map).put("two", "two");
//        try {
//            strictly.verify(map, 1).put("five", "five");
//            fail();
//        } catch (StrictVerificationError e) {}
//    }
//    
//    @Test
//    public void shouldFailWhenPriorVerificationCalledAgain() {
//        strictly.verify(list, 1).add("one");
//        strictly.verify(map).put("two", "two");
//        strictly.verify(list, 2).add("three and four");
//        try {
//            strictly.verify(list, 1).add("one");
//            fail();
//        } catch (StrictVerificationError e) {}
//    }
//    
//    @Test
//    public void shouldFailOnVerifyNoMoreInteractions() {
//        strictly.verify(list).add("one");
//        strictly.verify(map).put("two", "two");
//        strictly.verify(list, 2).add("three and four");
//        strictly.verify(map).put("five", "five");
//        try {
//            verifyNoMoreInteractions(list, map, set);
//            fail();
//        } catch (VerificationAssertionError e) {}
//    } 
//    
//    @Test
//    public void shouldFailWhenLastMethodDontMatch() {
//        strictly.verify(list).add("one");
//        strictly.verify(map).put("two", "two");
//        strictly.verify(list, 2).add("three and four");
//        strictly.verify(map).put("five", "five");
//        try {
//            strictly.verify(set).add("not six but something else");
//            fail();
//        } catch (VerificationAssertionError e) {}
//    }
//    
//    @Test(expected = VerificationAssertionError.class)
//    public void shouldFailOnVerifyZeroInteractions() {
//        verifyZeroInteractions(list, map, set);
//    } 
}
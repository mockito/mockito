/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.*;

public class VerificationInOrderWithCallsTest extends TestBase {

    @Mock private IMethods mockOne;
    @Mock private IMethods mockTwo;

    @Test
    public void shouldFailWhenMethodNotCalled(){
        // Given
        mockOne.oneArg( 1 );
        InOrder verifier = inOrder( mockOne );
        verifier.verify( mockOne, calls(1)).oneArg( 1 );

        // When
        try{
            verifier.verify( mockOne, calls(1)).oneArg( 2 );
            fail();
        } catch (VerificationInOrderFailure e){
            assertThat(e.getMessage(), allOf(containsString("Verification in order failure"),
                                             containsString("Wanted but not invoked"),
                                             containsString("mockOne.oneArg(2)")));
        }

        // Then - expected exception thrown
    }

    @Test
    public void shouldFailWhenMethodCalledTooFewTimes(){
        // Given
        mockOne.oneArg( 1 );
        mockOne.oneArg( 2 );

        InOrder verifier = inOrder( mockOne );
        verifier.verify( mockOne, calls(1)).oneArg( 1 );

        // When
        try{
            verifier.verify( mockOne, calls(2)).oneArg( 2 );
            fail();
        } catch(VerificationInOrderFailure e){
            assertThat(e.getMessage(), allOf(containsString("Verification in order failure"),
                                             containsString("mockOne.oneArg(2)"),
                                             containsString("Wanted 2 times"),
                                             containsString("But was 1 time")));
        }

        // Then - expected exception thrown
    }

    @Test
    public void shouldFailWhenSingleMethodCallsAreOutOfSequence(){
        // Given
        mockOne.oneArg( 1 );
        mockOne.oneArg( 2 );

        InOrder verifier = inOrder( mockOne );
        verifier.verify( mockOne, calls(1)).oneArg( 2 );

        // When
        try{
            verifier.verify( mockOne, calls(1)).oneArg( 1 );
            fail();
        } catch(VerificationInOrderFailure e){
            assertThat(e.getMessage(), allOf(containsString("Verification in order failure"),
                    containsString("Wanted but not invoked"),
                    containsString("mockOne.oneArg(1)")));
        }


        // Then - expected exception thrown
    }

    @Test
    public void shouldFailWhenDifferentMethodCallsAreOutOfSequence(){
        // Given
        mockOne.oneArg( 1 );
        mockOne.voidMethod();

        InOrder verifier = inOrder( mockOne );
        verifier.verify( mockOne, calls(1)).voidMethod();

        // When
        try{
            verifier.verify( mockOne, calls(1)).oneArg( 1 );
            fail();
        } catch(VerificationInOrderFailure e){
            assertThat(e.getMessage(), allOf(containsString("Verification in order failure"),
                                                containsString("Wanted but not invoked"),
                                                containsString("mockOne.oneArg(1)")));
        }


        // Then - expected exception thrown
    }

    @Test
    public void shouldFailWhenMethodCallsOnDifferentMocksAreOutOfSequence(){
        // Given
        mockOne.voidMethod();
        mockTwo.voidMethod();

        InOrder verifier = inOrder( mockOne, mockTwo );
        verifier.verify( mockTwo, calls(1)).voidMethod();

        // When
        try{
            verifier.verify( mockOne, calls(1)).voidMethod();
            fail();
        } catch(VerificationInOrderFailure e){
            assertThat(e.getMessage(), allOf(containsString("Verification in order failure"),
                    containsString("Wanted but not invoked"),
                    containsString("mockOne.voidMethod()")));
        }


        // Then - expected exception thrown
    }
    

    @Test
    public void shouldAllowSequentialCallsToCallsForSingleMethod(){
        // Given
        mockOne.oneArg( 1 );
        mockOne.oneArg( 2 );
        mockOne.oneArg( 2 );
        mockOne.oneArg( 1 );

        InOrder verifier = inOrder( mockOne );

        // When
        verifier.verify( mockOne, calls(1)).oneArg( 1 );
        verifier.verify( mockOne, calls(2)).oneArg( 2 );
        verifier.verify( mockOne, calls(1)).oneArg( 1 );
        verifyNoMoreInteractions(mockOne);
        verifier.verifyNoMoreInteractions();

        // Then - no exception thrown
    }

    @Test
    public void shouldAllowSequentialCallsToCallsForDifferentMethods(){
        // Given
        mockOne.oneArg( 1 );
        mockOne.voidMethod();
        mockOne.voidMethod();
        mockOne.oneArg( 1 );

        InOrder verifier = inOrder( mockOne );

        // When
        verifier.verify( mockOne, calls(1)).oneArg( 1 );
        verifier.verify( mockOne, calls(2)).voidMethod();
        verifier.verify( mockOne, calls(1)).oneArg(1);
        verifyNoMoreInteractions(mockOne);
        verifier.verifyNoMoreInteractions();

        // Then - no exception thrown
    }

    @Test
    public void shouldAllowSequentialCallsToCallsForMethodsOnDifferentMocks(){
        // Given
        mockOne.voidMethod();
        mockTwo.voidMethod();
        mockTwo.voidMethod();
        mockOne.voidMethod();

        InOrder verifier = inOrder( mockOne, mockTwo );

        // When
        verifier.verify( mockOne, calls(1)).voidMethod();
        verifier.verify( mockTwo, calls(2)).voidMethod();
        verifier.verify( mockOne, calls(1)).voidMethod();
        verifyNoMoreInteractions(mockOne);
        verifyNoMoreInteractions(mockTwo);
        verifier.verifyNoMoreInteractions();

        // Then - no exception thrown
    }
    
    
    @Test
    public void shouldAllowFewerCallsForSingleMethod(){
        // Given
        mockOne.oneArg( 1 );
        mockOne.oneArg( 2 );
        mockOne.oneArg( 2 );
        mockOne.oneArg( 1 );
        mockOne.oneArg( 2 );

        InOrder verifier = inOrder( mockOne );

        // When
        verifier.verify( mockOne, calls(1)).oneArg( 1 );
        verifier.verify( mockOne, calls(1)).oneArg( 2 );
        verifier.verify( mockOne, calls(1)).oneArg( 1 );
        verifier.verify( mockOne, calls(1)).oneArg( 2 );

        // Then - no exception thrown
    }

    @Test
    public void shouldNotVerifySkippedCallsWhenFewerCallsForSingleMethod(){
        // Given
        mockOne.oneArg( 1 );
        mockOne.oneArg( 2 );
        mockOne.oneArg( 2 );
        mockOne.oneArg( 1 );

        InOrder verifier = inOrder( mockOne );
        verifier.verify( mockOne, calls(1)).oneArg( 1 );
        verifier.verify( mockOne, calls(1)).oneArg( 2 );
        verifier.verify( mockOne, calls(1)).oneArg( 1 );

        // When
        try{
            verifyNoMoreInteractions( mockOne );
            fail();
        } catch(NoInteractionsWanted e){

        }

        // Then - expected exception thrown
    }

    @Test
    public void shouldNotVerifySkippedCallsInInOrderWhenFewerCallsForSingleMethod(){
        // Given
        mockOne.oneArg( 1 );
        mockOne.oneArg( 2 );
        mockOne.oneArg( 2 );

        InOrder verifier = inOrder( mockOne );
        verifier.verify( mockOne, calls(1)).oneArg( 1 );
        verifier.verify( mockOne, calls(1)).oneArg( 2 );

        // When
        try{
            verifier.verifyNoMoreInteractions();
            fail();
        } catch (VerificationInOrderFailure e){
            assertThat(e.getMessage(), containsString("No interactions wanted here"));
        }

        // Then - expected exception thrown
    }

    @Test
    public void shouldAllowFewerCallsForDifferentMethods(){
        // Given
        mockOne.oneArg( 1 );
        mockOne.voidMethod();
        mockOne.voidMethod();
        mockOne.oneArg( 1 );
        mockOne.voidMethod();

        InOrder verifier = inOrder( mockOne );

        // When
        verifier.verify( mockOne, calls(1)).oneArg( 1 );
        verifier.verify( mockOne, calls(1)).voidMethod();
        verifier.verify( mockOne, calls(1)).oneArg( 1 );
        verifier.verify( mockOne, calls(1)).voidMethod();

        // Then - no exception thrown
    }

    @Test
    public void shouldNotVerifySkippedCallsWhenFewerCallsForDifferentMethods(){
        // Given
        mockOne.oneArg( 1 );
        mockOne.voidMethod();
        mockOne.voidMethod();
        mockOne.oneArg( 1 );

        InOrder verifier = inOrder( mockOne );
        verifier.verify( mockOne, calls(1)).oneArg( 1 );
        verifier.verify( mockOne, calls(1)).voidMethod();
        verifier.verify( mockOne, calls(1)).oneArg( 1 );

        // When
        try{
            verifyNoMoreInteractions( mockOne );
            fail();
        }  catch(NoInteractionsWanted e){

        }

        // Then - no exception thrown
    }

    @Test
    public void shouldNotVerifySkippedCallsInInOrderWhenFewerCallsForDifferentMethods(){
        // Given
        mockOne.oneArg( 1 );
        mockOne.voidMethod();
        mockOne.voidMethod();

        InOrder verifier = inOrder( mockOne );
        verifier.verify( mockOne, calls(1)).oneArg( 1 );
        verifier.verify( mockOne, calls(1)).voidMethod();

        // When
        try{
            verifier.verifyNoMoreInteractions();
            fail();
        }  catch (VerificationInOrderFailure e){
            assertThat(e.getMessage(), containsString("No interactions wanted here"));
        }

        // Then - expected exception thrown
    }

    @Test
    public void shouldAllowFewerCallsForMethodsOnDifferentMocks(){
        // Given
        mockOne.voidMethod();
        mockTwo.voidMethod();
        mockTwo.voidMethod();
        mockOne.voidMethod();
        mockTwo.voidMethod();

        InOrder verifier = inOrder( mockOne, mockTwo );

        // When
        verifier.verify( mockOne, calls(1)).voidMethod();
        verifier.verify( mockTwo, calls(1)).voidMethod();
        verifier.verify( mockOne, calls(1)).voidMethod();
        verifier.verify( mockTwo, calls(1)).voidMethod();

        // Then - no exception thrown
    }

    @Test
    public void shouldNotVerifySkippedCallsWhenFewerCallsForMethodsOnDifferentMocks(){
        // Given
        mockOne.voidMethod();
        mockTwo.voidMethod();
        mockTwo.voidMethod();
        mockOne.voidMethod();

        InOrder verifier = inOrder( mockOne, mockTwo );
        verifier.verify( mockOne, calls(1)).voidMethod();
        verifier.verify( mockTwo, calls(1)).voidMethod();
        verifier.verify( mockOne, calls(1)).voidMethod();

        // When
        try{
            verifyNoMoreInteractions( mockTwo );
            fail();
        }  catch (NoInteractionsWanted e){
        }

        // Then - expected exception thrown
    }

    @Test
    public void shouldNotVerifySkippedCallsInInOrderWhenFewerCallsForMethodsOnDifferentMocks(){
        // Given
        mockOne.voidMethod();
        mockTwo.voidMethod();
        mockTwo.voidMethod();

        InOrder verifier = inOrder( mockOne, mockTwo );
        verifier.verify( mockOne, calls(1)).voidMethod();
        verifier.verify( mockTwo, calls(1)).voidMethod();

        // When
        try{
            verifier.verifyNoMoreInteractions();
            fail();
        }  catch (VerificationInOrderFailure e){
            assertThat(e.getMessage(), containsString("No interactions wanted here"));
        }

        // Then - expected exception thrown
    }

    @Test
    public void shouldVerifyWithCallsAfterUseOfTimes(){
        // Given
        mockOne.oneArg( 1 );
        mockOne.oneArg( 2 );
        mockOne.oneArg( 2 );
        mockOne.oneArg( 1 );

        InOrder verifier = inOrder( mockOne );

        // When
        verifier.verify( mockOne, times(1)).oneArg( 1 );
        verifier.verify( mockOne, calls(2)).oneArg( 2 );
        verifier.verify( mockOne, calls(1)).oneArg( 1 );

        // Then - no exception thrown
    }

    @Test
    public void shouldVerifyWithCallsAfterUseOfAtLeast(){
        // Given
        mockOne.oneArg( 1 );
        mockOne.oneArg( 2 );
        mockOne.oneArg( 2 );

        InOrder verifier = inOrder( mockOne );

        // When
        verifier.verify( mockOne, atLeast(1)).oneArg( 1 );
        verifier.verify( mockOne, calls(2)).oneArg( 2 );

        // Then - no exception thrown
    }

    @Test
    public void shouldVerifyWithTimesAfterUseOfCalls(){
        // Given
        mockOne.oneArg( 1 );
        mockOne.oneArg( 2 );
        mockOne.oneArg( 2 );
        mockOne.oneArg( 1 );

        InOrder verifier = inOrder( mockOne );

        // When
        verifier.verify( mockOne, calls(1)).oneArg( 1 );
        verifier.verify( mockOne, times(2)).oneArg( 2 );
        verifier.verify( mockOne, times(1)).oneArg( 1 );

        // Then - no exception thrown
    }

    @Test
    public void shouldVerifyWithAtLeastAfterUseOfCalls(){
        // Given
        mockOne.oneArg( 1 );
        mockOne.oneArg( 2 );
        mockOne.oneArg( 2 );
        mockOne.oneArg( 1 );

        InOrder verifier = inOrder( mockOne );

        // When
        verifier.verify( mockOne, calls(1)).oneArg( 1 );
        verifier.verify( mockOne, atLeast(1)).oneArg( 2 );
        verifier.verify( mockOne, atLeast(1)).oneArg( 1 );

        // Then - no exception thrown
    }

    @Test
    public void shouldVerifyWithTimesAfterCallsInSameChunk(){
        // Given
        mockOne.oneArg( 1 );
        mockOne.oneArg( 1 );
        mockOne.oneArg( 1 );

        InOrder verifier = inOrder( mockOne );

        // When
        verifier.verify( mockOne, calls(1)).oneArg( 1 );
        verifier.verify( mockOne, times(2)).oneArg( 1 );
        verifier.verifyNoMoreInteractions();

        // Then - no exception thrown
    }

    @Test
    public void shouldFailToCreateCallsWithZeroArgument(){
        // Given
        InOrder verifier = inOrder( mockOne );
        // When
        try{
            verifier.verify( mockOne, calls(0)).voidMethod();
            fail();
        } catch (MockitoException e){
            assertThat(e.getMessage(), containsString("Negative and zero values are not allowed here"));
        }

        // Then - expected exception thrown
    }

    @Test
    public void shouldFailToCreateCallsWithNegativeArgument(){
        // Given
        InOrder verifier = inOrder( mockOne );

        // When
        try{
            verifier.verify( mockOne, calls(-1)).voidMethod();
            fail();
        }  catch (MockitoException e){
            assertThat(e.getMessage(), containsString("Negative and zero values are not allowed here"));
        }

        // Then - expected exception thrown
    }

    @Test
    public void shouldFailToCreateCallsForNonInOrderVerification(){
        // Given
        mockOne.voidMethod();

        // When
        try{
            verify( mockOne, calls(1)).voidMethod();
        }  catch (MockitoException e){
            assertThat(e.getMessage(), containsString("calls is only intended to work with InOrder"));
        }

        // Then - expected exception thrown
    }
}

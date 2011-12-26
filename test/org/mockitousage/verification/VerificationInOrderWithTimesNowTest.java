/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

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

import static org.mockito.Mockito.*;

public class VerificationInOrderWithTimesNowTest extends TestBase {

    @Mock private IMethods mockOne;
    @Mock private IMethods mockTwo;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void shouldFailWhenMethodNotCalled(){
        mockOne.oneArg( 1 );

        InOrder verifier = inOrder( mockOne );
        verifier.verify( mockOne, timesNow( 1 )).oneArg( 1 );

        exceptionRule.expect( VerificationInOrderFailure.class );
        exceptionRule.expectMessage( "Verification in order failure" );
        exceptionRule.expectMessage( "Wanted but not invoked" );
        exceptionRule.expectMessage( "mockOne.oneArg(2)" );
        verifier.verify( mockOne, timesNow( 1 )).oneArg( 2 );
    }

    @Test
    public void shouldFailWhenMethodCalledTooFewTimes(){
        mockOne.oneArg( 1 );
        mockOne.oneArg( 2 );

        InOrder verifier = inOrder( mockOne );
        verifier.verify( mockOne, timesNow( 1 )).oneArg( 1 );

        exceptionRule.expect( VerificationInOrderFailure.class );
        exceptionRule.expectMessage( "Verification in order failure" );
        exceptionRule.expectMessage( "mockOne.oneArg(2)" );
        exceptionRule.expectMessage( "Wanted 2 times" );
        exceptionRule.expectMessage( "But was 1 time" );
        verifier.verify( mockOne, timesNow( 2 )).oneArg( 2 );
    }

    @Test
    public void shouldFailWhenSingleMethodCallsAreOutOfSequence(){
        mockOne.oneArg( 1 );
        mockOne.oneArg( 2 );

        InOrder verifier = inOrder( mockOne );
        verifier.verify( mockOne, timesNow( 1 )).oneArg( 2 );

        exceptionRule.expect( VerificationInOrderFailure.class );
        exceptionRule.expectMessage( "Verification in order failure" );
        exceptionRule.expectMessage( "Wanted but not invoked" );
        exceptionRule.expectMessage( "mockOne.oneArg(1)" );
        verifier.verify( mockOne, timesNow( 1 )).oneArg( 1 );
    }

    @Test
    public void shouldFailWhenDifferentMethodCallsAreOutOfSequence(){
        mockOne.oneArg( 1 );
        mockOne.voidMethod();

        InOrder verifier = inOrder( mockOne );
        verifier.verify( mockOne, timesNow( 1 )).voidMethod();

        exceptionRule.expect( VerificationInOrderFailure.class );
        exceptionRule.expectMessage( "Verification in order failure" );
        exceptionRule.expectMessage( "Wanted but not invoked" );
        exceptionRule.expectMessage( "mockOne.oneArg(1)" );
        verifier.verify( mockOne, timesNow( 1 )).oneArg( 1 );
    }

    @Test
    public void shouldFailWhenMethodCallsOnDifferentMocksAreOutOfSequence(){
        mockOne.voidMethod();
        mockTwo.voidMethod();

        InOrder verifier = inOrder( mockOne, mockTwo );
        verifier.verify( mockTwo, timesNow( 1 )).voidMethod();

        exceptionRule.expect( VerificationInOrderFailure.class );
        exceptionRule.expectMessage( "Verification in order failure" );
        exceptionRule.expectMessage( "Wanted but not invoked" );
        exceptionRule.expectMessage( "mockOne.voidMethod()" );
        verifier.verify( mockOne, timesNow( 1 )).voidMethod();
    }
    

    @Test
    public void shouldAllowSequentialCallsToTimesNowForSingleMethod(){
        mockOne.oneArg( 1 );
        mockOne.oneArg( 2 );
        mockOne.oneArg( 2 );
        mockOne.oneArg( 1 );

        InOrder verifier = inOrder( mockOne );
        verifier.verify( mockOne, timesNow( 1 )).oneArg( 1 );
        verifier.verify( mockOne, timesNow( 2 )).oneArg( 2 );
        verifier.verify( mockOne, timesNow( 1 )).oneArg( 1 );
        verifyNoMoreInteractions(mockOne);
        verifier.verifyNoMoreInteractions();
    }

    @Test
    public void shouldAllowSequentialCallsToTimesNowForDifferentMethods(){
        mockOne.oneArg( 1 );
        mockOne.voidMethod();
        mockOne.voidMethod();
        mockOne.oneArg( 1 );

        InOrder verifier = inOrder( mockOne );
        verifier.verify( mockOne, timesNow( 1 )).oneArg( 1 );
        verifier.verify( mockOne, timesNow( 2 )).voidMethod();
        verifier.verify( mockOne, timesNow( 1 )).oneArg(1);
        verifyNoMoreInteractions(mockOne);
        verifier.verifyNoMoreInteractions();
    }

    @Test
    public void shouldAllowSequentialCallsToTimesNowForMethodsOnDifferentMocks(){
        mockOne.voidMethod();
        mockTwo.voidMethod();
        mockTwo.voidMethod();
        mockOne.voidMethod();

        InOrder verifier = inOrder( mockOne, mockTwo );
        verifier.verify( mockOne, timesNow( 1 )).voidMethod();
        verifier.verify( mockTwo, timesNow( 2 )).voidMethod();
        verifier.verify( mockOne, timesNow( 1 )).voidMethod();
        verifyNoMoreInteractions(mockOne);
        verifyNoMoreInteractions(mockTwo);
        verifier.verifyNoMoreInteractions();
    }
    
    
    @Test
    public void shouldAllowFewerCallsForSingleMethod(){
        mockOne.oneArg( 1 );
        mockOne.oneArg( 2 );
        mockOne.oneArg( 2 );
        mockOne.oneArg( 1 );
        mockOne.oneArg( 2 );

        InOrder verifier = inOrder( mockOne );
        verifier.verify( mockOne, timesNow( 1 )).oneArg( 1 );
        verifier.verify( mockOne, timesNow( 1 )).oneArg( 2 );
        verifier.verify( mockOne, timesNow( 1 )).oneArg( 1 );
        verifier.verify( mockOne, timesNow( 1 )).oneArg( 2 );
    }

    @Test
    public void shouldNotVerifySkippedCallsWhenFewerCallsForSingleMethod(){
        mockOne.oneArg( 1 );
        mockOne.oneArg( 2 );
        mockOne.oneArg( 2 );
        mockOne.oneArg( 1 );

        InOrder verifier = inOrder( mockOne );
        verifier.verify( mockOne, timesNow( 1 )).oneArg( 1 );
        verifier.verify( mockOne, timesNow( 1 )).oneArg( 2 );
        verifier.verify( mockOne, timesNow( 1 )).oneArg( 1 );
        
        exceptionRule.expect( NoInteractionsWanted.class );
        verifyNoMoreInteractions( mockOne );
    }

    @Test
    public void shouldNotVerifySkippedCallsInInOrderWhenFewerCallsForSingleMethod(){
        mockOne.oneArg( 1 );
        mockOne.oneArg( 2 );
        mockOne.oneArg( 2 );

        InOrder verifier = inOrder( mockOne );
        verifier.verify( mockOne, timesNow( 1 )).oneArg( 1 );
        verifier.verify( mockOne, timesNow( 1 )).oneArg( 2 );

        exceptionRule.expect( VerificationInOrderFailure.class );
        exceptionRule.expectMessage( "No interactions wanted here" );
        verifier.verifyNoMoreInteractions();
    }

    @Test
    public void shouldAllowFewerCallsForDifferentMethods(){
        mockOne.oneArg( 1 );
        mockOne.voidMethod();
        mockOne.voidMethod();
        mockOne.oneArg( 1 );
        mockOne.voidMethod();

        InOrder verifier = inOrder( mockOne );
        verifier.verify( mockOne, timesNow( 1 )).oneArg( 1 );
        verifier.verify( mockOne, timesNow( 1 )).voidMethod();
        verifier.verify( mockOne, timesNow( 1 )).oneArg( 1 );
        verifier.verify( mockOne, timesNow( 1 )).voidMethod();
    }

    @Test
    public void shouldNotVerifySkippedCallsWhenFewerCallsForDifferentMethods(){
        mockOne.oneArg( 1 );
        mockOne.voidMethod();
        mockOne.voidMethod();
        mockOne.oneArg( 1 );

        InOrder verifier = inOrder( mockOne );
        verifier.verify( mockOne, timesNow( 1 )).oneArg( 1 );
        verifier.verify( mockOne, timesNow( 1 )).voidMethod();
        verifier.verify( mockOne, timesNow( 1 )).oneArg( 1 );

        exceptionRule.expect( NoInteractionsWanted.class );
        verifyNoMoreInteractions( mockOne );
    }

    @Test
    public void shouldNotVerifySkippedCallsInInOrderWhenFewerCallsForDifferentMethods(){
        mockOne.oneArg( 1 );
        mockOne.voidMethod();
        mockOne.voidMethod();

        InOrder verifier = inOrder( mockOne );
        verifier.verify( mockOne, timesNow( 1 )).oneArg( 1 );
        verifier.verify( mockOne, timesNow( 1 )).voidMethod();

        exceptionRule.expect( VerificationInOrderFailure.class );
        exceptionRule.expectMessage( "No interactions wanted here" );
        verifier.verifyNoMoreInteractions();
    }

    @Test
    public void shouldAllowFewerCallsForMethodsOnDifferentMocks(){
        mockOne.voidMethod();
        mockTwo.voidMethod();
        mockTwo.voidMethod();
        mockOne.voidMethod();
        mockTwo.voidMethod();

        InOrder verifier = inOrder( mockOne, mockTwo );
        verifier.verify( mockOne, timesNow( 1 )).voidMethod();
        verifier.verify( mockTwo, timesNow( 1 )).voidMethod();
        verifier.verify( mockOne, timesNow( 1 )).voidMethod();
        verifier.verify( mockTwo, timesNow( 1 )).voidMethod();
    }

    @Test
    public void shouldNotVerifySkippedCallsWhenFewerCallsForMethodsOnDifferentMocks(){
        mockOne.voidMethod();
        mockTwo.voidMethod();
        mockTwo.voidMethod();
        mockOne.voidMethod();

        InOrder verifier = inOrder( mockOne, mockTwo );
        verifier.verify( mockOne, timesNow( 1 )).voidMethod();
        verifier.verify( mockTwo, timesNow( 1 )).voidMethod();
        verifier.verify( mockOne, timesNow( 1 )).voidMethod();

        exceptionRule.expect(NoInteractionsWanted.class);
        verifyNoMoreInteractions( mockTwo );
    }

    @Test
    public void shouldNotVerifySkippedCallsInInOrderWhenFewerCallsForMethodsOnDifferentMocks(){
        mockOne.voidMethod();
        mockTwo.voidMethod();
        mockTwo.voidMethod();

        InOrder verifier = inOrder( mockOne, mockTwo );
        verifier.verify( mockOne, timesNow( 1 )).voidMethod();
        verifier.verify( mockTwo, timesNow( 1 )).voidMethod();

        exceptionRule.expect( VerificationInOrderFailure.class );
        exceptionRule.expectMessage( "No interactions wanted here" );
        verifier.verifyNoMoreInteractions();
    }
    
    @Test
    public void shouldFailToCreateTimesNowWithZeroArgument(){
        InOrder verifier = inOrder( mockOne );
        exceptionRule.expect( MockitoException.class );
        exceptionRule.expectMessage( "Negative and zero values are not allowed here" );
        verifier.verify( mockOne, timesNow( 0 )).voidMethod();
    }

    @Test
    public void shouldFailToCreateTimesNowWithNegativeArgument(){
        InOrder verifier = inOrder( mockOne );
        exceptionRule.expect( MockitoException.class );
        exceptionRule.expectMessage( "Negative and zero values are not allowed here" );
        verifier.verify( mockOne, timesNow( -1 )).voidMethod();
    }

    @Test
    public void shouldFailToCreateTimesNowForNonInOrderVerification(){
        mockOne.voidMethod();
        exceptionRule.expect( MockitoException.class );
        exceptionRule.expectMessage( "timesNow is only intended to work with InOrder" );
        verify( mockOne, timesNow( 1 )).voidMethod();
    }
}

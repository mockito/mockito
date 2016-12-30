package org.mockitousage.stubbing;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoMocking;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.quality.Strictness;
import org.mockitousage.IMethods;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockitoutil.ThrowableAssert.assertThat;

public class StrictStubbingTest {

    @Mock IMethods mock;

    MockitoMocking mocking = Mockito.startMocking(this, Strictness.STRICT_STUBS);

    @Test public void no_interactions() throws Throwable {
        //expect no exception
        mocking.finishMocking();
    }

    @Test public void few_interactions() throws Throwable {
        //when
        mock.simpleMethod(100);
        mock.otherMethod();

        //expect no exception
        mocking.finishMocking();
    }

    @Test public void few_verified_interactions() throws Throwable {
        //when
        mock.simpleMethod(100);
        mock.otherMethod();

        //and
        verify(mock).simpleMethod(100);
        verify(mock).otherMethod();
        verifyNoMoreInteractions(mock);

        //expect no exception
        mocking.finishMocking();
    }

    @Test public void stubbed_method_is_implicitly_verified() throws Throwable {
        //when
        given(mock.simpleMethod(100)).willReturn("100");
        mock.simpleMethod(100);

        //no exceptions:
        verifyNoMoreInteractions(mock);
        mocking.finishMocking();
    }

    @Test public void unused_stubbed_is_not_implicitly_verified() throws Throwable {
        //when
        given(mock.simpleMethod(100)).willReturn("100");
        mock.simpleMethod(100); // <- implicitly verified
        mock.simpleMethod(200); // <- unverified

        //expect
        assertThat(new Runnable() {
            public void run() {
                verifyNoMoreInteractions(mock);
            }
        }).throwsException(NoInteractionsWanted.class);

        //and no exception
        mocking.finishMocking();
    }

    @Test public void stubbing_argument_mismatch() throws Throwable {
        //when
        given(mock.simpleMethod(100)).willReturn("100");

        //stubbing argument mismatch is detected
        assertThat(new Runnable() {
            public void run() {
                mock.simpleMethod(200);
            }
        }).throwsException(PotentialStubbingProblem.class);
    }

    @Test public void unused_stubbing() throws Throwable {
        //when
        given(mock.simpleMethod(100)).willReturn("100");

        //unused stubbing is reported
        assertThat(new Runnable() {
            public void run() {
                mocking.finishMocking();
            }
        }).throwsException(UnnecessaryStubbingException.class);
    }
}
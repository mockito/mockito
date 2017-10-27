/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.listeners.VerificationStartedEvent;
import org.mockito.listeners.VerificationStartedListener;
import org.mockitoutil.TestBase;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * This test demonstrates how verification started listeners work.
 * The test cases are contrived but they provide necessary coverage.
 * For rationale, see https://github.com/mockito/mockito/issues/1191
 */
public class VerificationStartedListenerTest extends TestBase {

    @Test
    public void verified_mock_can_be_replaced() throws Exception {
        //given
        final List mock1 = mock(List.class);
        mock1.clear(); //register clear() on mock1

        //when
        List mock2 = mock(List.class, Mockito.withSettings().verificationStartedListeners(new VerificationStartedListener() {
            public void onVerificationStarted(VerificationStartedEvent event) {
                //this is a hack to simulate desired behavior
                event.setMock(mock1);
            }
        }));

        //then verified mock is not mock2 that was passed to 'verify' but the replacement: mock1
        List verifiedMock = verify(mock2);
        assertEquals(mock1, verifiedMock);

        //and verification is successful because mock1.clear() was called
        verifiedMock.clear();

        //this test is admittedly contrived. it's goal is to provide coverage for the key functionality of the listener
        //see the discussion at https://github.com/mockito/mockito/issues/1191
    }

    @Test
    public void verification_started_event_contains_correct_mock() throws Exception {
        //given
        final List<Object> container = new ArrayList<Object>();

        List mock = mock(List.class, Mockito.withSettings().verificationStartedListeners(new VerificationStartedListener() {
            public void onVerificationStarted(VerificationStartedEvent event) {
                //this is a hack to simulate desired behavior
                container.add(event.getMock());
            }
        }));

        //when
        verify(mock, never()).clear();

        //then
        Assertions.assertThat(container).containsExactly(mock);
    }

    @Test
    public void listeners_are_executed_in_sequence() throws Exception {
        //given
        final List<Object> container = new ArrayList<Object>();
        final List mock1 = mock(List.class);

        List mock2 = mock(List.class, Mockito.withSettings().verificationStartedListeners(new VerificationStartedListener() {
            public void onVerificationStarted(VerificationStartedEvent event) {
                //this is a hack to simulate desired behavior
                container.add(event.getMock());
                event.setMock(mock1);
            }
        }, new VerificationStartedListener() {
            @Override
            public void onVerificationStarted(VerificationStartedEvent event) {
                container.add(event.getMock());
            }
        }));

        //when
        verify(mock2, never()).clear();

        //ensure that:
        // 1. listeners were notified in sequence
        // 2. the state set by 1st listeners affects 2nd listener
        Assertions.assertThat(container).containsExactly(mock2, mock1);

        //there is no particular reason we decided on that behavior
        //we want to have a consistent and documented behavior of the verification started listener
    }

    @Test
    public void shows_clean_exception_when_null_array_passed() throws Exception {
        try {
            //when
            Mockito.withSettings().verificationStartedListeners(null);
            fail();
        } catch (MockitoException e) {
            assertEquals("verificationStartedListeners() does not accept null vararg array. See the Javadoc.", e.getMessage());
        }
    }

    @Test
    public void shows_clean_exception_when_null_listener_passed() throws Exception {
        try {
            //when
            Mockito.withSettings().verificationStartedListeners(mock(VerificationStartedListener.class), null);
            fail();
        } catch (MockitoException e) {
            assertEquals("verificationStartedListeners() does not accept null listeners. See the Javadoc.", e.getMessage());
        }
    }
}

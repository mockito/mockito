/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.mockito.Mockito.*;

import java.util.LinkedList;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class DescriptiveMessagesWhenTimesXVerificationFailsTest extends TestBase {

    @Mock private LinkedList mock;

    @Test
    public void shouldVerifyActualNumberOfInvocationsSmallerThanWanted() throws Exception {
        mock.clear();
        mock.clear();
        mock.clear();

        Mockito.verify(mock, times(3)).clear();
        try {
            Mockito.verify(mock, times(100)).clear();
            fail();
        } catch (TooLittleActualInvocations e) {
            assertThat(e, messageContains("mock.clear();"));
            assertThat(e, messageContains("Wanted 100 times"));
            assertThat(e, messageContains("was 3"));
        }
    }

    @Test
    public void shouldVerifyActualNumberOfInvocationsLargerThanWanted() throws Exception {
        mock.clear();
        mock.clear();
        mock.clear();
        mock.clear();

        Mockito.verify(mock, times(4)).clear();
        try {
            Mockito.verify(mock, times(1)).clear();
            fail();
        } catch (TooManyActualInvocations e) {
            assertThat(e, messageContains("mock.clear();"));
            assertThat(e, messageContains("Wanted 1 time"));
            assertThat(e, messageContains("was 4"));
        }
    }
}
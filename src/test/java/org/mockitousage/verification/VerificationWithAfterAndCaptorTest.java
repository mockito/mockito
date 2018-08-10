/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.verification.VerificationMode;
import org.mockitousage.IMethods;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class VerificationWithAfterAndCaptorTest {

    /**
     * Test for issue #345 and similar use cases
     */
    @Test
    public void should_capture_correct_number_of_values() {
        verifyCaptor(after(200).atMost(3));
        verifyCaptor(after(200).times(3));
        verifyCaptor(after(200).atLeast(3));

        verifyCaptor(timeout(200).atLeast(3));
        verifyCaptor(timeout(200).times(3));
    }

    private void verifyCaptor(VerificationMode mode) {
        IMethods mock = mock(IMethods.class);
        ArgumentCaptor<Character> captor = ArgumentCaptor.forClass(Character.class);

        // when
        for (int i = 0; i < 3; i++) {
            mock.oneArg((char) ('0' + i));
        }

        // then
        verify(mock, mode).oneArg((char) captor.capture());

        assertThat(captor.getAllValues()).containsExactly('0', '1', '2');
    }
}

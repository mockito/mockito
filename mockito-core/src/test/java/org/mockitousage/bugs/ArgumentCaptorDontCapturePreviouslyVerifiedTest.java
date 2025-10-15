/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockitousage.IMethods;

public class ArgumentCaptorDontCapturePreviouslyVerifiedTest {
    @Test
    public void previous_verified_invocation_should_still_capture_args() {
        IMethods mock = mock(IMethods.class);

        mock.oneArg("first");
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(mock, times(1)).oneArg(argument.capture());
        assertThat(argument.getAllValues()).hasSize(1);

        // additional interactions
        mock.oneArg("second");
        argument = ArgumentCaptor.forClass(String.class);
        verify(mock, times(2)).oneArg(argument.capture());
        assertThat(argument.getAllValues()).hasSize(2);
    }
}

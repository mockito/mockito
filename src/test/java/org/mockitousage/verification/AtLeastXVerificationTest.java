/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockitoutil.TestBase;

import java.util.List;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class AtLeastXVerificationTest extends TestBase {

    @Mock private List<String> mock;

    @Test
    public void shouldVerifyAtLeastXTimes() throws Exception {
        //when
        mock.clear();
        mock.clear();
        mock.clear();

        //then
        verify(mock, atLeast(2)).clear();
    }

    @Test
    public void shouldFailVerificationAtLeastXTimes() throws Exception {
        mock.add("one");
        verify(mock, atLeast(1)).add(anyString());

        try {
            verify(mock, atLeast(2)).add(anyString());
            fail();
        } catch (MockitoAssertionError e) {}
    }

    @Test
    public void shouldAllowAtLeastZeroForTheSakeOfVerifyNoMoreInteractionsSometimes() throws Exception {
        //when
        mock.add("one");
        mock.clear();

        //then
        verify(mock, atLeast(0)).add("one");
        verify(mock, atLeast(0)).clear();

        verifyNoMoreInteractions(mock);
    }
}

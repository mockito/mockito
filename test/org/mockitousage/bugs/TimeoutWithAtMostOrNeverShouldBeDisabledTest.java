/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.bugs;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.FriendlyReminderException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

//see issue 235
@SuppressWarnings("deprecation")
public class TimeoutWithAtMostOrNeverShouldBeDisabledTest extends TestBase {

    @Mock IMethods mock;

    @Test
    public void shouldDisableTimeoutForAtMost() {
        try {
            verify(mock, timeout(30000).atMost(1)).simpleMethod();
            fail();
        } catch (final FriendlyReminderException e) {}
    }

    @Test
    public void shouldDisableTimeoutForNever() {
        try {
            verify(mock, timeout(30000).never()).simpleMethod();
            fail();
        } catch (final FriendlyReminderException e) {}
    }
}
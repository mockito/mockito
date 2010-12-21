/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.FriendlyReminderException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.util.List;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

//see issue 235
public class TimeoutWithAtMostShouldBeDisabledTest extends TestBase {

    @Mock IMethods mock;

	@Test
	public void shouldDisableTimeout() {
        try {
		    verify(mock, timeout(30000).atMost(1)).simpleMethod();
            fail();
        } catch (FriendlyReminderException e) {}
	}
}
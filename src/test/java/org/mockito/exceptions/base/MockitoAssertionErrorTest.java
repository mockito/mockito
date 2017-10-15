/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.exceptions.base;

import org.junit.Test;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MockitoAssertionErrorTest extends TestBase {

    private void throwIt() {
        throw new MockitoAssertionError("boom");
    }

    @Test
    public void shouldKeepUnfilteredStackTrace() {
        try {
            throwIt();
            fail();
        } catch (MockitoAssertionError e) {
            assertEquals("throwIt", e.getUnfilteredStackTrace()[0].getMethodName());
        }
    }

    @Test
    public void should_prepend_message_to_original() {
        MockitoAssertionError original = new MockitoAssertionError("original message");
        MockitoAssertionError errorWithPrependedMessage = new MockitoAssertionError(original, "new message");
        assertEquals("new message\noriginal message", errorWithPrependedMessage.getMessage());
    }
}

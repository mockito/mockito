/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ChecksTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void checkNotNull_not_null() throws Exception {
        assertEquals("abc", Checks.checkNotNull("abc", "someValue"));
    }

    @Test
    public void checkNotNull_null() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("someValue should not be null");
        Checks.checkNotNull(null, "someValue");
    }
}

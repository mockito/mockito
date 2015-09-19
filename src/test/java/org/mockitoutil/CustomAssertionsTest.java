/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitoutil;

import org.junit.Test;

public class CustomAssertionsTest extends TestBase {

    @Test
    public void shouldKnowWhenStringContainsIgnoringCase() throws Exception {
        assertContainsIgnoringCase("foo", "foo");
        assertContainsIgnoringCase("fOo", "foo");
        assertContainsIgnoringCase("FoO", "foo");
        assertContainsIgnoringCase("foo", "a foo :)");
        assertContainsIgnoringCase("fOo", "a foo :)");
        assertContainsIgnoringCase("FoO", "a foo :)");
        assertContainsIgnoringCase("", "a foo :)");
        assertContainsIgnoringCase("", "");
    }

    @Test(expected = AssertionError.class)
    public void shouldKnowWhenStringDoesNotContainIgnoringCase() throws Exception {
        assertContainsIgnoringCase("fooo", "foo");
    }

    @Test(expected = AssertionError.class)
    public void shouldKnowWhenStringDoesNotContainIgnoringCase2() throws Exception {
        assertContainsIgnoringCase("fOo", "f oo");
    }
}
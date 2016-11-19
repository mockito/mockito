/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.stubbing;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.when;

public class StrictStubbingTest {

    public @Rule MockitoRule rule = MockitoJUnit.rule().strict();

    private @Mock IMethods mock;

    @Test
    public void passes_because_the_stub_is_used() throws Exception {
        given(mock.simpleMethod("hey")).willReturn("Joe");
        mock.simpleMethod("hey");
    }

    @Test
    public void fails_due_to_unused_stub() throws Exception {
        given(mock.simpleMethod("hey")).willReturn("Joe");

        mock.simpleMethod();
        mock.simpleMethod("bar");
    }
}

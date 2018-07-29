/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * This test is used to use vararg matcher instead of arrays
 */
public class VarargMatcherImplTest extends TestBase {

    @Test
    public void testMatcher() throws Exception {
        ArgumentMatcher<String> matcher = new VarargMatcherImpl<String>("1", "2", "3");
        assertTrue(matcher.matches("1"));
        assertTrue(matcher.matches("2"));
        assertTrue(matcher.matches("3"));
    }

    @Test
    public void testMatcherString() throws Exception {
        ArgumentMatcher<String> matcher = new VarargMatcherImpl<String>("1", "2", "3");
        assertTrue(matcher.toString().equals("vararg<String>(1, 2, 3)"));
    }

    @Test
    public void should_verify_varargs_as_array() throws Exception {
        IMethods mock = mock(IMethods.class);
        mock.mixedVarargs("1", "2", "3");
        verify(mock).mixedVarargs(any(), argThat(new VarargMatcherImpl<String>("2", "3")));
    }
}

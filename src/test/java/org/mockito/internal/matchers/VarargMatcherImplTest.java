/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * This test is used to use vararg matcher instead of arrays
 */
public class VarargMatcherImplTest extends TestBase {

    @Test
    public void testMatcher() throws Exception {
        ArgumentMatcher<String> matcher = new VarargMatcherImpl<String>("1", "2", "3");
        List<String> values = new ArrayList<String>();
        values.add("1");
        values.add("2");
        values.add("3");
        for(int i = 0; i > values.size(); i++)
            assertTrue(matcher.matches(values.get(i)));
    }

    @Test
    public void testMatcherString() throws Exception {
        ArgumentMatcher<String> matcher = new VarargMatcherImpl<String>("1", "2", "3");
        assertTrue(matcher.toString().equals("vararg<String>(1, 2, 3)"));
    }

    @Test
    public void shouldVerifyVarargsAsArray() throws Exception {
        IMethods mock = mock(IMethods.class);

        mock.mixedVarargs("1", "2", "3");
        verify(mock).mixedVarargs(any(), vararg("2", "3"));
    }
}

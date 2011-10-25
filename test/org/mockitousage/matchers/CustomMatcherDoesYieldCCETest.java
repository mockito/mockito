/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class CustomMatcherDoesYieldCCETest extends TestBase {

    @Mock private IMethods mock;

    @Test
    public void shouldNotThrowCCE() {
        mock.simpleMethod(new Object());

        try {
            // calling overloaded method so that matcher will be called with
            // different type
            verify(mock).simpleMethod(argThat(isStringWithTextFoo()));
            fail();
        } catch (ArgumentsAreDifferent e) {}
    }

    private ArgumentMatcher<String> isStringWithTextFoo() {
        return new ArgumentMatcher<String>() {
            @Override
            public boolean matches(Object argument) {
                // casting that should not be thrown:
                String str = (String) argument;
                return str.equals("foo");
            }
        };
    }
}
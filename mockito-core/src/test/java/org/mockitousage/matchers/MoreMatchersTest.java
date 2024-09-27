/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;

import org.assertj.core.api.ThrowableAssert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.verification.opentest4j.ArgumentsAreDifferent;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class MoreMatchersTest extends TestBase {

    @Mock private IMethods mock;

    @Test
    public void should_help_out_with_unnecessary_casting() {
        when(mock.objectArgMethod(any(String.class))).thenReturn("string");

        assertEquals("string", mock.objectArgMethod("foo"));
    }

    @Test
    public void any_class_should_be_actual_alias_to_isA() {
        mock.simpleMethod(new ArrayList());

        verify(mock).simpleMethod(isA(List.class));
        verify(mock).simpleMethod(any(List.class));

        mock.simpleMethod((String) null);

        assertThatThrownBy(
                        new ThrowableAssert.ThrowingCallable() {
                            @Override
                            public void call() {
                                verify(mock).simpleMethod(isA(String.class));
                            }
                        })
                .isInstanceOf(ArgumentsAreDifferent.class);

        assertThatThrownBy(
                        new ThrowableAssert.ThrowingCallable() {
                            @Override
                            public void call() {
                                verify(mock).simpleMethod(any(String.class));
                            }
                        })
                .isInstanceOf(ArgumentsAreDifferent.class);
    }
}

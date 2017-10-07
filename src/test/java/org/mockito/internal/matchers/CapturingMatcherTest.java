/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockitoutil.TestBase;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CapturingMatcherTest extends TestBase {

    @Test
    public void should_capture_arguments() throws Exception {
        //given
        CapturingMatcher<String> m = new CapturingMatcher<String>();

        //when
        m.captureFrom("foo");
        m.captureFrom("bar");

        //then
        assertThat(m.getAllValues()).containsSequence("foo", "bar");
    }

    @Test
    public void should_know_last_captured_value() throws Exception {
        //given
        CapturingMatcher<String> m = new CapturingMatcher<String>();

        //when
        m.captureFrom("foo");
        m.captureFrom("bar");

        //then
        assertEquals("bar", m.getLastValue());
    }

    @Test
    public void should_scream_when_nothing_yet_captured() throws Exception {
        //given
        CapturingMatcher<String> m = new CapturingMatcher<String>();

        try {
            //when
            m.getLastValue();
            //then
            fail();
        } catch (MockitoException e) {}
    }

    @Test
    public void should_not_fail_when_used_in_concurrent_tests() throws Exception {
        //given
        final CapturingMatcher<String> m = new CapturingMatcher<String>();

        //when
        m.captureFrom("concurrent access");
        Iterator<String> iterator = m.getAllValues().iterator();
        m.captureFrom("concurrent access");

        //then
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next()).isEqualTo("concurrent access"); // Potential ConcurrentModificationException
    }

}

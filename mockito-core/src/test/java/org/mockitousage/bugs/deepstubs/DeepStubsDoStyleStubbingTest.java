/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs.deepstubs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.Closeable;
import java.io.IOException;

import org.junit.Test;
import org.mockito.exceptions.misusing.MissingMethodInvocationException;
import org.mockito.exceptions.misusing.UnfinishedStubbingException;

/**
 * Regression for #1636: do* stubbing must accept a deep-stub path as the
 * target of {@code when(...)} without raising {@code UnfinishedStubbingException}.
 */
public class DeepStubsDoStyleStubbingTest {

    abstract static class Holder {
        abstract Closeable closeable();

        abstract Holder nested();
    }

    @Test
    public void do_nothing_when_deep_stub_path() throws IOException {
        Holder holder = mock(Holder.class, RETURNS_DEEP_STUBS);

        doNothing().when(holder.closeable()).close();

        holder.closeable().close();
        verify(holder.closeable()).close();
    }

    @Test
    public void do_return_when_deep_stub_path() {
        Holder holder = mock(Holder.class, RETURNS_DEEP_STUBS);

        doReturn("xxx").when(holder.closeable()).toString();

        assertThat(holder.closeable().toString()).isEqualTo("xxx");
    }

    @Test
    public void extracting_deep_stub_before_do_style_still_works() throws IOException {
        Holder holder = mock(Holder.class, RETURNS_DEEP_STUBS);
        Closeable closeable = holder.closeable();

        doNothing().when(closeable).close();

        closeable.close();
        verify(closeable).close();
    }

    @Test
    public void do_nothing_when_fresh_multi_level_deep_stub_path() throws IOException {
        Holder holder = mock(Holder.class, RETURNS_DEEP_STUBS);

        doNothing().when(holder.nested().closeable()).close();

        holder.nested().closeable().close();
        verify(holder.nested().closeable()).close();
    }

    @Test
    public void do_nothing_when_cached_multi_level_deep_stub_path() throws IOException {
        Holder holder = mock(Holder.class, RETURNS_DEEP_STUBS);
        holder.nested().closeable();

        doNothing().when(holder.nested().closeable()).close();

        holder.nested().closeable().close();
        verify(holder.nested().closeable()).close();
    }

    @Test
    public void deep_stub_invocation_during_then_return_is_still_rejected() {
        Holder holder = mock(Holder.class, RETURNS_DEEP_STUBS);
        Holder other = mock(Holder.class);

        try {
            when(other.closeable()).thenReturn(holder.closeable());
            fail("A deep-stub invocation during thenReturn must be rejected");
        } catch (UnfinishedStubbingException expected) {
        }
    }

    @Test
    public void do_style_stubbing_without_a_method_is_still_rejected() {
        Holder holder = mock(Holder.class, RETURNS_DEEP_STUBS);

        Closeable ignored = doNothing().when(holder.closeable());

        try {
            holder.closeable();
            fail("An unfinished do-style stubbing must be rejected");
        } catch (UnfinishedStubbingException expected) {
        }
    }

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test
    public void unfinished_stubbing_inside_deep_stub_answer_is_rejected() throws IOException {
        Holder holder = mock(Holder.class, RETURNS_DEEP_STUBS);
        Holder other = mock(Holder.class);
        Closeable closeable = mock(Closeable.class);
        when(holder.closeable())
                .thenAnswer(
                        invocation -> {
                            when(other.closeable());
                            return closeable;
                        });

        try {
            doNothing().when(holder.closeable()).close();
            fail("Unfinished stubbing inside the answer must be rejected");
        } catch (UnfinishedStubbingException expected) {
        }

        try {
            when(null);
            fail("A nested unfinished stubbing must not leave an ongoing stubbing");
        } catch (MissingMethodInvocationException expected) {
        }

        assertThat(other.closeable()).isNull();
    }

    @Test
    public void completed_stubbing_inside_answer_preserves_multi_level_path() throws IOException {
        Holder holder = mock(Holder.class, RETURNS_DEEP_STUBS);
        Holder nested = mock(Holder.class, RETURNS_DEEP_STUBS);
        Holder other = mock(Holder.class);
        Closeable closeable = mock(Closeable.class);
        when(holder.nested())
                .thenAnswer(
                        invocation -> {
                            when(other.closeable()).thenReturn(closeable);
                            return nested;
                        });

        doNothing().when(holder.nested().closeable()).close();

        assertThat(other.closeable()).isSameAs(closeable);
        nested.closeable().close();
        verify(nested.closeable()).close();
    }

    @Test
    public void reported_error_inside_deep_stub_answer_clears_stubbing_state() throws IOException {
        Holder holder = mock(Holder.class, RETURNS_DEEP_STUBS);
        Holder other = mock(Holder.class);
        Closeable closeable = mock(Closeable.class);
        when(holder.closeable())
                .thenAnswer(
                        invocation -> {
                            when(other.closeable()).thenReturn(other.closeable());
                            return closeable;
                        });

        try {
            doNothing().when(holder.closeable()).close();
            fail("Nested mock calls during thenReturn must be rejected");
        } catch (UnfinishedStubbingException expected) {
        }

        assertThat(other.closeable()).isNull();
    }

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test
    public void answer_failure_does_not_leave_ongoing_stubbing() throws IOException {
        Holder holder = mock(Holder.class, RETURNS_DEEP_STUBS);
        AssertionError failure = new AssertionError("answer failure");
        when(holder.closeable())
                .thenAnswer(
                        invocation -> {
                            throw failure;
                        });

        try {
            doNothing().when(holder.closeable()).close();
            fail("The answer failure must be propagated");
        } catch (AssertionError expected) {
            assertThat(expected).isSameAs(failure);
        }

        try {
            when(null);
            fail("A failed deep-stub answer must not leave an ongoing stubbing");
        } catch (MissingMethodInvocationException expected) {
        }
    }
}

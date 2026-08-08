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
}

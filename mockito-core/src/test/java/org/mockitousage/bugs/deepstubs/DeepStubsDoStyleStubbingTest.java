/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs.deepstubs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.Closeable;
import java.io.IOException;

import org.junit.Test;

/**
 * Regression for #1636: do* stubbing must accept a deep-stub path as the
 * target of {@code when(...)} without raising {@code UnfinishedStubbingException}.
 */
public class DeepStubsDoStyleStubbingTest {

    abstract static class Holder {
        abstract Closeable closeable();
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
}

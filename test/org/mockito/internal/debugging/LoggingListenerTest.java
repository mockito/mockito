/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.util.MockitoLogger;
import org.mockitoutil.TestBase;

public class LoggingListenerTest extends TestBase {

    @Mock private MockitoLogger logger;

    @Test
    public void shouldLogUnusedStub() {
        //given
        final LoggingListener listener = new LoggingListener(false, logger);

        //when
        listener.foundUnusedStub(new InvocationBuilder().toInvocation());

        //then
        verify(logger).log(notNull());
    }

    @Test
    public void shouldLogUnstubbed() {
        //given
        final LoggingListener listener = new LoggingListener(true, logger);

        //when
        listener.foundUnstubbed(new InvocationBuilder().toInvocationMatcher());

        //then
        verify(logger).log(notNull());
    }

    @Test
    public void shouldNotLogUnstubbed() {
        //given
        final LoggingListener listener = new LoggingListener(false, logger);

        //when
        listener.foundUnstubbed(new InvocationBuilder().toInvocationMatcher());

        //then
        verify(logger, never()).log(notNull());
    }

    @Test
    public void shouldLogDifferentArgs() {
        //given
        final LoggingListener listener = new LoggingListener(true, logger);

        //when
        listener.foundStubCalledWithDifferentArgs(new InvocationBuilder().toInvocation(), new InvocationBuilder().toInvocationMatcher());

        //then
        verify(logger).log(notNull());
    }
}
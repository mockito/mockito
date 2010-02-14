/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import static java.util.Arrays.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.util.MockitoLogger;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.util.MockitoLoggerStub;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class WarningsPrinterImplTest extends TestBase {

    @Mock private MockitoLogger logger;
    @Mock private WarningsFinder finder;

    @Test
    public void shouldUseFinderCorrectly() {
        // given
        WarningsPrinterImpl printer = new WarningsPrinterImpl(false, finder);

        // when
        printer.print(logger);

        // then
        ArgumentCaptor<LoggingListener> arg = ArgumentCaptor.forClass(LoggingListener.class);
        verify(finder).find(arg.capture());
        assertEquals(logger, arg.getValue().getLogger());
        assertEquals(false, arg.getValue().isWarnAboutUnstubbed());
    }

    @Test
    public void shouldPrintUnusedStub() {
        // given
        WarningsPrinterImpl printer = new WarningsPrinterImpl(true, finder);

        // when
        printer.print(logger);

        // then
        ArgumentCaptor<LoggingListener> arg = ArgumentCaptor.forClass(LoggingListener.class);
        verify(finder).find(arg.capture());
        assertEquals(true, arg.getValue().isWarnAboutUnstubbed());
    }
}
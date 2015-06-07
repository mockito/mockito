/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.internal.util.MockitoLogger;
import org.mockitoutil.TestBase;

public class WarningsPrinterImplTest extends TestBase {

    @Mock private MockitoLogger logger;
    @Mock private WarningsFinder finder;

    @Test
    public void shouldUseFinderCorrectly() {
        // given
        final WarningsPrinterImpl printer = new WarningsPrinterImpl(false, finder);

        // when
        printer.print(logger);

        // then
        final ArgumentCaptor<LoggingListener> arg = ArgumentCaptor.forClass(LoggingListener.class);
        verify(finder).find(arg.capture());
        assertEquals(logger, arg.getValue().getLogger());
        assertEquals(false, arg.getValue().isWarnAboutUnstubbed());
    }

    @Test
    public void shouldPassCorrectWarningFlag() {
        // given
        final WarningsPrinterImpl printer = new WarningsPrinterImpl(true, finder);

        // when
        printer.print(logger);

        // then
        final ArgumentCaptor<LoggingListener> arg = ArgumentCaptor.forClass(LoggingListener.class);
        verify(finder).find(arg.capture());
        assertEquals(true, arg.getValue().isWarnAboutUnstubbed());
    }

    @Test
    public void shouldPrintToString() {
        // given
        final WarningsPrinterImpl printer = spy(new WarningsPrinterImpl(true, finder));

        // when
        final String out = printer.print();

        // then
        verify(printer).print((MockitoLogger) notNull());
        assertNotNull(out);
    }
}
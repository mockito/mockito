/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.plugins.switcher;

import org.junit.Test;
import org.mockitousage.plugins.instantiator.MyInstantiatorProvider2;
import org.mockitousage.plugins.logger.MyMockitoLogger;
import org.mockitousage.plugins.stacktrace.MyStackTraceCleanerProvider;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class PluginSwitchTest {

    @SuppressWarnings("CheckReturnValue")
    @Test
    public void plugin_switcher_is_used() {
        mock(List.class);
        assertEquals(MyPluginSwitch.invokedFor, asList(MyMockMaker.class.getName(),
            MyStackTraceCleanerProvider.class.getName(),
            MyMockitoLogger.class.getName(),
            MyInstantiatorProvider2.class.getName()));
    }

    @Test
    public void uses_custom_mock_maker() {
        //when
        MyMockMaker.explosive.set(new Object());

        //when
        try {
            mock(List.class);
            fail();
        } catch (Exception e) {
            assertEquals(MyMockMaker.class.getName(), e.getMessage());
        } finally {
            MyMockMaker.explosive.remove();
        }
    }
}

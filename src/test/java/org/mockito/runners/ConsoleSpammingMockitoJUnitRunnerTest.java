/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.runners;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.notification.RunNotifier;
import org.mockito.internal.runners.InternalRunner;
import org.mockito.internal.util.ConsoleMockitoLogger;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;

public class ConsoleSpammingMockitoJUnitRunnerTest extends TestBase {

    private MockitoLoggerStub loggerStub;

    @Before
    public void setup() {
        loggerStub = new MockitoLoggerStub();
    }

    //TODO add sensible tests

    @Test
    public void shouldDelegateToGetDescription() {
        //given
        final Description expectedDescription = Description.createSuiteDescription(this.getClass());
        ConsoleSpammingMockitoJUnitRunner runner = new ConsoleSpammingMockitoJUnitRunner(loggerStub, new InternalRunnerStub() {
            public Description getDescription() {
                return expectedDescription;
            }
        });

        //when
        Description description = runner.getDescription();

        //then
        assertEquals(expectedDescription, description);
    }

    public class MockitoLoggerStub extends ConsoleMockitoLogger {

        StringBuilder loggedInfo = new StringBuilder();

        public void log(Object what) {
            super.log(what);
            loggedInfo.append(what);
        }

        public String getLoggedInfo() {
            return loggedInfo.toString();
        }
    }

    static class InternalRunnerStub implements InternalRunner {

        public Description getDescription() {
            return null;
        }

        public void run(RunNotifier notifier) {
        }

        public void filter(Filter filter) {
        }

    }
}

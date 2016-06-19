/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.runners;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.mockito.internal.runners.RunnerImpl;
import org.mockito.internal.util.ConsoleMockitoLogger;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.assertEquals;

public class ConsoleSpammingMockitoJUnitRunnerTest extends TestBase {

    private ConsoleSpammingMockitoJUnitRunner runner;

    private MockitoLoggerStub loggerStub;

    private RunNotifier notifier;

    @Before
    public void setup() throws InitializationError {
        loggerStub = new MockitoLoggerStub();
        notifier = new RunNotifier();
    }

    //TODO add sensible tests

    @Test
    public void shouldDelegateToGetDescription() throws Exception {
        //given
        final Description expectedDescription = Description.createSuiteDescription(this.getClass());
        runner = new ConsoleSpammingMockitoJUnitRunner(loggerStub, new RunnerImplStub() {
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

    static class RunnerImplStub implements RunnerImpl {

        public Description getDescription() {
            return null;
        }

        public void run(RunNotifier notifier) {
        }

        public void filter(Filter filter) throws NoTestsRemainException {
        }

    }
}
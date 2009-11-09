/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.runners;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.mockito.Mock;
import org.mockito.internal.debugging.DebuggingInfo;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.runners.RunnerImpl;
import org.mockito.internal.util.MockitoLoggerImpl;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class ConsoleSpammingMockitoJUnitRunnerTest extends TestBase {
    
    @Mock private IMethods mock;

    private ConsoleSpammingMockitoJUnitRunner runner;
    private MockitoLoggerStub loggerStub;
    private RunNotifier notifier;

    @Before
    public void setup() throws InitializationError {
        loggerStub = new MockitoLoggerStub();
        notifier = new RunNotifier();
    }
    
    @Test
    public void shouldLogUnusedStubbingWarningWhenTestFails() throws Exception {
        runner = new ConsoleSpammingMockitoJUnitRunner(loggerStub, new RunnerImplStub() {
            @Override
            public void run(RunNotifier notifier) {
                //this is what happens when the test runs:
                //first, unused stubbing:
                unusedStubbingThatQualifiesForWarning();
                //then, let's make the test fail so that warnings are printed
                notifier.fireTestFailure(null);
                //assert
                String loggedInfo = loggerStub.getLoggedInfo();
                assertContains(".unusedStubbingThatQualifiesForWarning(", loggedInfo);
            }
        });
        
        runner.run(notifier);
    }

    @Ignore
    @Test
    public void shouldLogUnstubbedMethodWarningWhenTestFails() throws Exception {
        runner = new ConsoleSpammingMockitoJUnitRunner(loggerStub, new RunnerImplStub() {
            @Override
            public void run(RunNotifier notifier) {
                callUnstubbedMethodThatQualifiesForWarning();
                notifier.fireTestFailure(null);

                String loggedInfo = loggerStub.getLoggedInfo();
                assertContains("method was not stubbed", loggedInfo);
                assertContains("mock.simpleMethod(456);", loggedInfo);
                assertContains(".callUnstubbedMethodThatQualifiesForWarning(", loggedInfo);
            }
        });
        
        runner.run(notifier);
    }
    
    @Test
    public void shouldLogStubCalledWithDifferentArgumentsWhenTestFails() throws Exception {
        runner = new ConsoleSpammingMockitoJUnitRunner(loggerStub, new RunnerImplStub() {
            @Override
            public void run(RunNotifier notifier) {
                someStubbing();
                callStubbedMethodWithDifferentArgs();
                notifier.fireTestFailure(null);
                
                String loggedInfo = loggerStub.getLoggedInfo();
                assertContains("with different arguments", loggedInfo);
                assertContains(".someStubbing(", loggedInfo);
                assertContains(".callStubbedMethodWithDifferentArgs(", loggedInfo);
            }
        });
        
        runner.run(notifier);
    }
    
    @Test
    public void shouldNotLogAnythingWhenStubCalledCorrectly() throws Exception {
        runner = new ConsoleSpammingMockitoJUnitRunner(loggerStub, new RunnerImplStub() {
            @Override
            public void run(RunNotifier notifier) {
                when(mock.simpleMethod(1)).thenReturn("foo");
                mock.simpleMethod(1);

                notifier.fireTestFailure(null);
                
                assertEquals("", loggerStub.getLoggedInfo());
            }
        });
        
        runner.run(notifier);
    }
    
    @Test
    public void shouldNotLogWhenTestPasses() throws Exception {
        runner = new ConsoleSpammingMockitoJUnitRunner(loggerStub, new RunnerImplStub() {
            @Override
            public void run(RunNotifier notifier) {
                when(mock.simpleMethod()).thenReturn("foo");
                
                notifier.fireTestFinished(null);
                
                assertEquals("", loggerStub.getLoggedInfo());
            }
        });
        
        runner.run(notifier);
    }
    
    public void shouldClearDebuggingDataAfterwards() throws Exception {
        //given
        final DebuggingInfo debuggingInfo = new ThreadSafeMockingProgress().getDebuggingInfo();

        runner = new ConsoleSpammingMockitoJUnitRunner(loggerStub, new RunnerImplStub() {
            @Override
            public void run(RunNotifier notifier) {
                unusedStubbingThatQualifiesForWarning();
                notifier.fireTestFailure(null);
                assertTrue(debuggingInfo.hasData());
            }
        });
        
        //when
        runner.run(notifier);
        
        //then
        assertFalse(debuggingInfo.hasData());
    }    
    
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

    private void unusedStubbingThatQualifiesForWarning() {
        when(mock.simpleMethod(123)).thenReturn("foo");
    }

    private void callUnstubbedMethodThatQualifiesForWarning() {
        mock.simpleMethod(456);
    }
    
    private void someStubbing() {
        when(mock.simpleMethod(789)).thenReturn("foo");
    }
    
    private void callStubbedMethodWithDifferentArgs() {
        mock.simpleMethod(10);
    }
    
    public class MockitoLoggerStub extends MockitoLoggerImpl {
        
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

        public void run(RunNotifier notifier) {}

    }
}
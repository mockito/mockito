package org.mockito.internal.experimental;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.mockito.Mock;
import org.mockito.internal.debugging.DebuggingInfo;
import org.mockito.internal.experimental.VerboseMockitoJUnitRunner.JunitTestBody;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.util.MockitoLoggerImpl;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;


public class VerboseMockitoJUnitRunnerTest extends TestBase {
    
    @Mock private IMethods mock;
    
    private VerboseMockitoJUnitRunner runner;
    private MockitoLoggerStub loggerStub;
    private RunNotifier notifier;

    @Before
    public void setup() throws InitializationError {
        loggerStub = new MockitoLoggerStub();
        notifier = new RunNotifier();
        runner = new VerboseMockitoJUnitRunner(this.getClass(), loggerStub);
    }
    
    //TODO after 1.7 refactor it to override-and-subclass
    @Test(expected=RunWasCalled.class)
    public void shouldRunTests() throws Exception {
        runner.run(notifier, new JunitTestBody() {
            public void run(RunNotifier notifier) {
                throw new RunWasCalled();
            }
        });
    }
    
    @Test
    public void shouldLogUnusedStubbingWarningWhenTestFails() throws Exception {
        runner.run(notifier, new JunitTestBody() {
            public void run(RunNotifier notifier) {
                //this is what happens when the test runs:
                //first, unused stubbing:
                unusedStubbingThatQualifiesForWarning();
                //then, let's make the test fail so that warnings are printed
                notifier.fireTestFailure(null);
                //assert
                String loggedInfo = loggerStub.getLoggedInfo();
                assertThat(loggedInfo, contains("[Mockito] Warning - this stub was not used"));
                assertThat(loggedInfo, contains("mock.simpleMethod(123);"));
                assertThat(loggedInfo, contains(".unusedStubbingThatQualifiesForWarning("));
            }
        });
    }

    @Test
    public void shouldLogUnstubbedMethodWarningWhenTestFails() throws Exception {
        runner.run(notifier, new JunitTestBody() {
            public void run(RunNotifier notifier) {
                callUnstubbedMethodThatQualifiesForWarning();
                notifier.fireTestFailure(null);

                String loggedInfo = loggerStub.getLoggedInfo();
                assertThat(loggedInfo, contains("[Mockito] Warning - this method was not stubbed"));
                assertThat(loggedInfo, contains("mock.simpleMethod(456);"));
                assertThat(loggedInfo, contains(".callUnstubbedMethodThatQualifiesForWarning("));
            }
        });
    }
    
    @Test
    public void shouldLogStubCalledWithDifferentArgumentsWhenTestFails() throws Exception {
        runner.run(notifier, new JunitTestBody() {
            public void run(RunNotifier notifier) {
                someStubbing();
                callStubbedMethodWithDifferentArgs();
                notifier.fireTestFailure(null);
                
                String loggedInfo = loggerStub.getLoggedInfo();
                assertThat(loggedInfo, contains("[Mockito] Warning - stubbed method called with different arguments"));
                assertThat(loggedInfo, contains("Stubbed this way:"));
                assertThat(loggedInfo, contains("mock.simpleMethod(789);"));
                assertThat(loggedInfo, contains(".someStubbing("));
                
                assertThat(loggedInfo, contains("But called with different arguments:"));
                assertThat(loggedInfo, contains("mock.simpleMethod(10);"));
                assertThat(loggedInfo, contains(".callStubbedMethodWithDifferentArgs("));
            }
        });
    }
    
    @Test
    public void shouldNotLogAnythingWhenStubCalledCorrectly() throws Exception {
        runner.run(notifier, new JunitTestBody() {
            public void run(RunNotifier notifier) {
                when(mock.simpleMethod(1)).thenReturn("foo");
                mock.simpleMethod(1);

                notifier.fireTestFailure(null);
                
                assertEquals("", loggerStub.getLoggedInfo());
            }
        });
    }
    
    @Test
    public void shouldNotLogWhenTestPasses() throws Exception {
        runner.run(notifier, new JunitTestBody() {
            public void run(RunNotifier notifier) {
                when(mock.simpleMethod()).thenReturn("foo");
                
                notifier.fireTestFinished(null);
                
                assertEquals("", loggerStub.getLoggedInfo());
            }
        });
    }
    
    
    public void shouldClearDebuggingDataAfterwards() throws Exception {
        final DebuggingInfo debuggingInfo = new ThreadSafeMockingProgress().getDebuggingInfo();
        
        runner.run(notifier, new JunitTestBody() {
            public void run(RunNotifier notifier) {
                unusedStubbingThatQualifiesForWarning();
                notifier.fireTestFailure(null);
                assertTrue(debuggingInfo.hasData());
            }
        });
        
        assertFalse(debuggingInfo.hasData());
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
    
    @SuppressWarnings("serial")
    private static class RunWasCalled extends RuntimeException {}; 
}
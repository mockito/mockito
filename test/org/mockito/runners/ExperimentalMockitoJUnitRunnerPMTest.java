package org.mockito.runners;

import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.mockito.Mock;
import org.mockito.internal.debugging.DebuggingInfo;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.util.MockitoLoggerImpl;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings("serial")
public class ExperimentalMockitoJUnitRunnerPMTest extends TestBase {
    
    //just to get rid of noisy constructor
    class ExperimentalMockitoJUnitRunnerPMStub extends ExperimentalMockitoJUnitRunnerPM {
        public ExperimentalMockitoJUnitRunnerPMStub() throws InitializationError {
            super(ExperimentalMockitoJUnitRunnerPMTest.class);
        }
    }
    
    @Mock private IMethods mock;
    private ExperimentalMockitoJUnitRunnerPMStub runner;
    private MockitoLoggerStub loggerStub;
    private RunNotifier notifier;

    @Before
    public void setup() throws InitializationError {
        runner = new ExperimentalMockitoJUnitRunnerPMStub();
        loggerStub = new MockitoLoggerStub();
        ExperimentalMockitoJUnitRunnerPM.logger = loggerStub;
        notifier = new RunNotifier();
    }
    
    @After
    public void restoreLogger() {
        ExperimentalMockitoJUnitRunnerPM.logger = new MockitoLoggerImpl();
    }

    @Test
    public void shouldRunTests() throws Exception {
        final StringBuilder sb = new StringBuilder();
        runner = new ExperimentalMockitoJUnitRunnerPMStub() {
            protected void runTestBody(RunNotifier notifier) {
                sb.append("ran test body");
            }
        };
        runner.run(notifier);
        assertEquals("ran test body", sb.toString());
    }
    
    class TestBodyWasRan extends RuntimeException {};
    
    @Test(expected=TestBodyWasRan.class)
    public void shouldRunTests2() throws Exception {
        runner = new ExperimentalMockitoJUnitRunnerPMStub() {
            protected void runTestBody(RunNotifier notifier) {
                throw new TestBodyWasRan();
            }
        };
        runner.run(notifier);
    }
    
    @Ignore
    @Test
    public void shouldRunTestsWithHypotheticalPartialMock() throws Exception {
        runner = spy(runner);
        
        //This even might not be needed because runOnParent() might be safe
        doNothing().when(runner).runTestBody(notifier);
        
        runner.run(notifier);
        
        verify(runner).runTestBody(notifier);
    }
    
    @Test
    public void shouldLogUnusedStubbingWarningWhenTestFails() throws Exception {
        runner = new ExperimentalMockitoJUnitRunnerPMStub() {
            protected void runTestBody(RunNotifier notifier) {
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
        };
        runner.run(notifier);
    }
    
    @Ignore
    @Test
    public void shouldLogUnusedStubbingWarningWhenTestFailsWithPartialMock() throws Exception {
        runner = spy(runner);
        
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                RunNotifier notifier = (RunNotifier) invocation.getArguments()[0];
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
                return null;
            }
        }).when(runner).runTestBody(notifier);

        runner.run(notifier);
    }

    @Test
    public void shouldLogUnstubbedMethodWarningWhenTestFails() throws Exception {
        runner = new ExperimentalMockitoJUnitRunnerPMStub() {
            protected void runTestBody(RunNotifier notifier) {
                callUnstubbedMethodThatQualifiesForWarning();
                notifier.fireTestFailure(null);

                String loggedInfo = loggerStub.getLoggedInfo();
                assertThat(loggedInfo, contains("[Mockito] Warning - this method was not stubbed"));
                assertThat(loggedInfo, contains("mock.simpleMethod(456);"));
                assertThat(loggedInfo, contains(".callUnstubbedMethodThatQualifiesForWarning("));
            }
        };
        runner.run(notifier);
    }
    
    @Test
    public void shouldLogStubCalledWithDifferentArgumentsWhenTestFails() throws Exception {
        runner = new ExperimentalMockitoJUnitRunnerPMStub() {
            protected void runTestBody(RunNotifier notifier) {
                someStubbing();
                //TODO below should be different test method
//                callStubbedMethodCorrectly();
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
                
                assertThat(loggedInfo, notContains(".callStubbedMethodCorrectly("));
            }
        };
        runner.run(notifier);
    }
    
    @Test
    public void shouldNotLogUsedStubbingWarningWhenTestFails() throws Exception {
        runner = new ExperimentalMockitoJUnitRunnerPMStub() {
            protected void runTestBody(RunNotifier notifier) {
                when(mock.simpleMethod()).thenReturn("foo");
                mock.simpleMethod();
                
                notifier.fireTestFailure(null);
                
                String loggedInfo = loggerStub.getLoggedInfo();
                assertEquals("", loggedInfo);
            }
        };
        runner.run(notifier);
    }
    
    
    public void shouldClearDebuggingDataAfterwards() throws Exception {
        final DebuggingInfo debuggingInfo = new ThreadSafeMockingProgress().getDebuggingInfo();
        
        runner = new ExperimentalMockitoJUnitRunnerPMStub() {
            protected void runTestBody(RunNotifier notifier) {
                unusedStubbingThatQualifiesForWarning();
                notifier.fireTestFailure(null);
                assertTrue(debuggingInfo.hasData());
            }
        };
        
        runner.run(notifier);
        
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
    
    private void callStubbedMethodCorrectly() {
        mock.simpleMethod(789);
    }

    private void callStubbedMethodWithDifferentArgs() {
        mock.simpleMethod(10);
    }
    
    public class MockitoLoggerStub extends MockitoLoggerImpl {
        
        StringBuilder loggedInfo = new StringBuilder();
        
        public void log(Object what) {
//            can be uncommented when debugging this test
//            super.log(what);
            loggedInfo.append(what);
        }

        public String getLoggedInfo() {
            return loggedInfo.toString();
        }
    }
}
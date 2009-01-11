package org.junit.mockito;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.mockito.Mock;
import org.mockito.internal.debugging.DebuggingInfo;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.util.MockitoLoggerStub;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings("serial")
public class ExperimentalMockitoJUnitRunnerPMTest extends TestBase {
    
    @Mock private IMethods mock;
    private ExperimentalMockitoJUnitRunnerPMStub runner;
    private MockitoLoggerStub loggerStub;
    private RunNotifier notifier;

    @Before
    public void setup() throws InitializationError {
        loggerStub = new MockitoLoggerStub();
        runner = new ExperimentalMockitoJUnitRunnerPMStub();
        notifier = new RunNotifier();
    }
    
    //just to get rid of noisy constructor
    class ExperimentalMockitoJUnitRunnerPMStub extends ExperimentalMockitoJUnitRunnerPM {
        public ExperimentalMockitoJUnitRunnerPMStub() throws InitializationError {
            super(ExperimentalMockitoJUnitRunnerPMTest.class, loggerStub);
        }
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
//        runner = spy(runner);
        
        //This even might not be needed because runOnParent() might be safe
        doNothing().when(runner).runTestBody(notifier);
        
        runner.run(notifier);
        
        verify(runner).runTestBody(notifier);
    }
    
    @Test
    public void usingAnnonymousInnerClass() throws Exception {
        //boring setup that goes to @Before
        loggerStub = new MockitoLoggerStub();
        notifier = new RunNotifier();
        
        //arrange
        runner = new ExperimentalMockitoJUnitRunnerPMStub() {
            protected void runTestBody(RunNotifier notifier) {
                someUnusedStubbingThatQualifiesForWarning();
                notifier.fireTestFailure(null);
            }
        };
        
        //act
        runner.run(notifier);
        
        //assert
        String loggedInfo = loggerStub.getLoggedInfo();
        assertThat(loggedInfo, contains("[Mockito] Warning - this stub was not used"));
    }
    
    @Ignore
    @Test
    public void usingPartialMocking() throws Exception {
        //boring setup that goes to @Before
        loggerStub = new MockitoLoggerStub();
        notifier = new RunNotifier();
        runner = spy(ExperimentalMockitoJUnitRunnerPMStub.class, guessConstructor(loggerStub));
        
        //arrange
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                RunNotifier notifier = (RunNotifier) invocation.getArguments()[0];
                someUnusedStubbingThatQualifiesForWarning();
                notifier.fireTestFailure(null);
                return null;
            }
        }).when(runner).runTestBody(notifier);
        
        //act
        runner.run(notifier);
        
        //assert
        String loggedInfo = loggerStub.getLoggedInfo();
        assertThat(loggedInfo, contains("[Mockito] Warning - this stub was not used"));
    }

    private <T> T spy(Class<T> clazz, Constructor guessConstructor) {
        return null;
    }

    class Constructor<T> {
        
    }
    
    private Constructor guessConstructor(Object ... constructorArguments) {
        return null;
    }

    private ExperimentalMockitoJUnitRunnerPMStub partialMock(Class<ExperimentalMockitoJUnitRunnerPM> class1) {
        return null;
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
                someUnusedStubbingThatQualifiesForWarning();
                notifier.fireTestFailure(null);
                assertTrue(debuggingInfo.hasData());
            }
        };
        
        runner.run(notifier);
        
        assertFalse(debuggingInfo.hasData());
    }    

    private void someUnusedStubbingThatQualifiesForWarning() {
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
}
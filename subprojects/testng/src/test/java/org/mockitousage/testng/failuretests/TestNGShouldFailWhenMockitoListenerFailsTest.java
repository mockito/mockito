package org.mockitousage.testng.failuretests;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockitousage.testng.utils.FailureRecordingListener;
import org.testng.TestNG;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertTrue;

@Test(
        singleThreaded = true,
        description = "Test that failing tests report a Mockito exception"
)
public class TestNGShouldFailWhenMockitoListenerFailsTest {

    private final FailureRecordingListener failureRecorder = new FailureRecordingListener();

    public void report_failure_on_incorrect_annotation_usage() throws Throwable {
        TestNG testNG = new_TestNG_with_failure_recorder_for(FailingOnPurposeBecauseIncorrectAnnotationUsage.class);

        testNG.run();

        assertTrue(testNG.hasFailure());
        assertThat(failureRecorder.lastThrowable()).isInstanceOf(MockitoException.class);
    }

    @Test
    public void report_failure_on_incorrect_stubbing_syntax_with_matchers_in_test_methods() throws Exception {
        TestNG testNG = new_TestNG_with_failure_recorder_for(FailingOnPurposeBecauseIncorrectStubbingSyntax.class);

        testNG.run();

        assertTrue(testNG.hasFailure());
        assertThat(failureRecorder.lastThrowable()).isInstanceOf(InvalidUseOfMatchersException.class);
    }


    @Test
    public void report_failure_on_incorrect_stubbing_syntax_with_matchers_in_configuration_methods() throws Exception {
        TestNG testNG = new_TestNG_with_failure_recorder_for(FailingOnPurposeBecauseWrongStubbingSyntaxInConfigurationMethod.class);

        testNG.run();

        assertTrue(testNG.hasFailure());
        assertThat(failureRecorder.lastThrowable()).isInstanceOf(InvalidUseOfMatchersException.class);
    }

    @AfterMethod
    public void clear_failure_recorder() throws Exception {
        failureRecorder.clear();
    }


    private TestNG new_TestNG_with_failure_recorder_for(Class<?>... testNGClasses) {
        TestNG testNG = new TestNG();
        testNG.setVerbose(0);
        testNG.setUseDefaultListeners(false);
        testNG.addListener(failureRecorder);

        testNG.setTestClasses(testNGClasses);
        return testNG;
    }
}

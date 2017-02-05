package org.mockitousage.session;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.exceptions.misusing.UnfinishedStubbingException;
import org.mockito.quality.Strictness;
import org.mockitousage.IMethods;
import org.mockitoutil.JUnitResultAssert;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class MockitoSessionTest {

    private JUnitCore junit = new JUnitCore();

    @Test public void session_without_any_configuration() {
        //when
        Result result = junit.run(MockitoSessionTest.SessionWithoutAnyConfiguration.class);

        //expect
        JUnitResultAssert.assertThat(result).succeeds(1);
    }

    @Test public void session_without_init_mocks_configured() {
        //when
        Result result = junit.run(MockitoSessionTest.SessionWithoutInitMocksConfigured.class);

        //expect
        JUnitResultAssert.assertThat(result).succeeds(1);
    }

    @Test public void session_without_strictness_configured() {
        //when
        Result result = junit.run(MockitoSessionTest.SessionWithoutStrictnessConfigured.class);

        //expect
        JUnitResultAssert.assertThat(result).succeeds(1);
    }

    @Test public void session_with_incorrect_mockito_usage() {
        //when
        Result result = junit.run(MockitoSessionTest.SessionWithIncorrectMockitoUsage.class);

        //expect
        JUnitResultAssert.assertThat(result).fails(1, UnfinishedStubbingException.class);
    }

    @Test public void reports_other_failure_and_incorrect_mockito_usage() {
        //when
        Result result = junit.run(MockitoSessionTest.SessionWithTestFailureAndIncorrectMockitoUsage.class);

        //expect
        JUnitResultAssert.assertThat(result)
                .failsExactly(AssertionError.class, UnfinishedStubbingException.class);
    }

    public static class SessionWithoutAnyConfiguration {

        @Mock IMethods mock;

        //session without initMocks is not currently supported
        MockitoSession mockito = Mockito.mockitoSession().startMocking();

        @After public void after() {
            mockito.finishMocking();
        }

        @Test public void some_test() {
            assertNull(mock); //initMocks() was not used when configuring session
        }
    }

    public static class SessionWithoutInitMocksConfigured {

        @Mock IMethods mock;

        MockitoSession mockito = Mockito.mockitoSession().strictness(Strictness.LENIENT).startMocking();

        @After public void after() {
            mockito.finishMocking();
        }

        @Test public void some_test() {
            assertNull(mock); //initMocks() was not used when configuring session
        }
    }

    public static class SessionWithoutStrictnessConfigured {
        @Mock IMethods mock;

        MockitoSession mockito = Mockito.mockitoSession().initMocks(this).startMocking();

        @After public void after() {
            mockito.finishMocking();
        }

        @Test public void some_test() {
            assertNotNull(mock);
        }
    }

    public static class SessionWithIncorrectMockitoUsage {
        @Mock IMethods mock;

        MockitoSession mockito = Mockito.mockitoSession().initMocks(this).startMocking();

        @After public void after() {
            mockito.finishMocking();
        }

        @Test public void unfinished_stubbing() {
            when(mock.simpleMethod());
        }
    }

    public static class SessionWithTestFailureAndIncorrectMockitoUsage {
        @Mock IMethods mock;

        MockitoSession mockito = Mockito.mockitoSession().initMocks(this).startMocking();

        @After public void after() {
            mockito.finishMocking();
        }

        @Test public void unfinished_stubbing_with_other_failure() {
            when(mock.simpleMethod());
            assertTrue(false);
        }
    }
}

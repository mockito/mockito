package org.mockitousage.session;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;
import org.mockitousage.IMethods;
import org.mockitoutil.JUnitResultAssert;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

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
}
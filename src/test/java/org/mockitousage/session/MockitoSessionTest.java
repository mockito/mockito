/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.session;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.exceptions.misusing.InjectMocksException;
import org.mockito.exceptions.misusing.UnfinishedStubbingException;
import org.mockito.quality.Strictness;
import org.mockitousage.IMethods;
import org.mockitoutil.JUnitResultAssert;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockitoSessionTest extends TestBase {

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

    @Test public void allows_initializing_mocks_manually() {
        //when
        Result result = junit.run(MockitoSessionTest.SessionWithManuallyInitializedMock.class);

        //expect
        JUnitResultAssert.assertThat(result).succeeds(1);
    }

    @Test public void allows_updating_strictness() {
        //when
        Result result = junit.run(MockitoSessionTest.SessionWithUpdatedStrictness.class);

        //expect
        JUnitResultAssert.assertThat(result).succeeds(1);
    }

    @Test public void allows_overriding_failure() {
        //when
        Result result = junit.run(MockitoSessionTest.SessionWithOverriddenFailure.class);

        //expect
        JUnitResultAssert.assertThat(result).isSuccessful();

        //in order to demonstrate feature, we intentionally misuse Mockito and need to clean up state
        resetState();
    }

    @Test public void cleans_up_state_when_init_fails() {
        //when
        Result result = junit.run(MockitoSessionTest.SessionWithInitMocksFailure.class);

        //expect that both failures are the same, indicating correct listener cleanup
        //incorrect cleanup causes 1 failure to be InjectMocksException
        //  but the next test method would have failed with unuseful error that session was not cleaned up
        JUnitResultAssert.assertThat(result)
            .fails(2, InjectMocksException.class);
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

        @SuppressWarnings({"MockitoUsage", "CheckReturnValue"})
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

        @SuppressWarnings({"MockitoUsage", "CheckReturnValue"})
        @Test public void unfinished_stubbing_with_other_failure() {
            when(mock.simpleMethod());
            assertTrue(false);
        }
    }

    public static class SessionWithManuallyInitializedMock {
        @Mock IMethods mock;
        IMethods mock2 = Mockito.mock(IMethods.class, "manual mock");

        MockitoSession mockito = Mockito.mockitoSession().initMocks(this).startMocking();

        @After public void after() {
            mockito.finishMocking();
        }

        @Test public void manual_mock_preserves_its_settings() {
            assertEquals("mock", mockingDetails(mock).getMockCreationSettings().getMockName().toString());
            assertEquals("manual mock", mockingDetails(mock2).getMockCreationSettings().getMockName().toString());
        }
    }

    public static class SessionWithUpdatedStrictness {
        @Mock IMethods mock;
        MockitoSession mockito = Mockito.mockitoSession().initMocks(this).strictness(Strictness.STRICT_STUBS).startMocking();

        @After public void after() {
            mockito.finishMocking();
        }

        @Test public void manual_mock_preserves_its_settings() {
            when(mock.simpleMethod(1)).thenReturn("foo");

            //when
            mockito.setStrictness(Strictness.LENIENT);

            //then no exception is thrown, even though the arg is different
            mock.simpleMethod(2);
        }
    }

    public static class SessionWithOverriddenFailure {
        @Mock IMethods mock;
        MockitoSession mockito = Mockito.mockitoSession().initMocks(this).startMocking();

        @After public void after() {
            mockito.finishMocking(new RuntimeException("Boo!"));
        }

        @SuppressWarnings({"MockitoUsage", "CheckReturnValue"})
        @Test public void invalid_mockito_usage() {
            verify(mock);
        }
    }

    public static class SessionWithInitMocksFailure {
        @InjectMocks private ConstructorFail sut;
        MockitoSession mockito;

        @Before
        public void before() {
            mockito = Mockito.mockitoSession().initMocks(this).startMocking();
        }

        @After public void after() {
            if (mockito != null) {
                //so that we reduce amount of exceptions for easier assertions
                //otherwise we would get an NPE here
                mockito.finishMocking();
            }
        }

        @Test public void test1() {
            //should fail the same way
        }

        @Test public void test2() {
            //should fail the same way
        }

        static class ConstructorFail {
            ConstructorFail() {
                throw new RuntimeException("Boo!");
            }
        }
    }
}

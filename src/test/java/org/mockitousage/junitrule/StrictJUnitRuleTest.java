package org.mockitousage.junitrule;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.junit.JUnitRule;
import org.mockito.internal.util.MockitoLogger;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class StrictJUnitRuleTest extends TestBase {

    private MockitoLogger logger = new MockitoLogger() {
        public void log(Object what) {
            throw new AssertionError("This implementation of rule should not warn about anything");
        }
    };

    private JUnitRule jUnitRule = new JUnitRule(logger, JUnitRule.Strictness.STRICT_STUBS);

    private FrameworkMethod dummy = mock(FrameworkMethod.class);

    @After public void after() {
        //so that the validate framework usage exceptions do not collide with the tests here
        resetState();
    }

    @Test public void ok_when_no_stubbings() throws Throwable {
        run(new Statement() {
            public void evaluate() throws Throwable {
                IMethods mock = mock(IMethods.class);
                mock.simpleMethod();
                Mockito.verify(mock).simpleMethod();
            }
        });
    }

    @Test public void ok_when_all_stubbings_used() throws Throwable {
        run(new Statement() {
            public void evaluate() throws Throwable {
                IMethods mock = mock(IMethods.class);
                given(mock.simpleMethod(10)).willReturn("foo");
                mock.simpleMethod(10);
            }
        });
    }

    @Test public void ok_when_used_and_mismatched_argument() throws Throwable {
        run(new Statement() {
            public void evaluate() throws Throwable {
                IMethods mock = mock(IMethods.class);
                given(mock.simpleMethod(10)).willReturn("foo");
                mock.simpleMethod(10);
                mock.simpleMethod(15);
            }
        });
    }

    @Test public void fails_when_unused_stubbings() throws Throwable {
        try {
            //when
            run(new Statement() {
                public void evaluate() throws Throwable {
                    IMethods mock = mock(IMethods.class);
                    given(mock.simpleMethod(10)).willReturn("foo");
                    mock.simpleMethod(15);
                }
            });

            //then
            fail();
        } catch (MockitoAssertionError e) {
            Assertions.assertThat(e.getMessage()).startsWith("Unused stubbings");
        }
    }

    @Test public void test_failure_trumps_unused_stubbings() throws Throwable {
        try {
            //when
            run(new Statement() {
                public void evaluate() throws Throwable {
                    IMethods mock = mock(IMethods.class);
                    given(mock.simpleMethod(10)).willReturn("foo");
                    mock.simpleMethod(15);

                    throw new AssertionError("x");
                }
            });

            //then
            fail();
        } catch (AssertionError e) {
            assertEquals("x", e.getMessage());
        }
    }

    @Ignore @Test public void fails_fast_when_mismatched_argument() throws Throwable {
        try {
            //when
            run(new Statement() {
                public void evaluate() throws Throwable {
                    IMethods mock = mock(IMethods.class);
                    given(mock.simpleMethod(10)).willReturn("foo");
                    mock.simpleMethod(15);

                    throw new AssertionError("x");
                }
            });

            //then
            fail();
        } catch (MockitoAssertionError e) {
            assertEquals("mismatched arg", e.getMessage());
        }
    }

    @Ignore @Test public void verify_no_more_interactions_ignores_stubs() throws Throwable {
        //when
        run(new Statement() {
            public void evaluate() throws Throwable {
                //in test:
                IMethods mock = mock(IMethods.class);
                given(mock.simpleMethod(10)).willReturn("foo");

                //in code:
                mock.simpleMethod(10);
                mock.otherMethod();

                //in test:
                verify(mock).otherMethod();
                verifyNoMoreInteractions(mock);
            }
        });
    }

    @Test public void unused_stubs_with_multiple_mocks() throws Throwable {
        try {
            //when
            run(new Statement() {
                public void evaluate() throws Throwable {
                    IMethods mock1 = mock(IMethods.class);
                    IMethods mock2 = mock(IMethods.class);
                    given(mock1.simpleMethod(10)).willReturn("foo");
                    given(mock2.simpleMethod(20)).willReturn("foo");

                    mock1.otherMethod();
                    mock2.booleanObjectReturningMethod();
                }
            });

            //then
            fail();
        } catch (MockitoAssertionError e) {
            assertThat(e.getMessage()).startsWith("Unused stubbings");
        }
    }

    private void run(Statement statement) throws Throwable {
        jUnitRule.apply(statement, dummy, new DummyTestCase()).evaluate();
    }

    public static class DummyTestCase {
        @Mock private IMethods mock;
    }
}